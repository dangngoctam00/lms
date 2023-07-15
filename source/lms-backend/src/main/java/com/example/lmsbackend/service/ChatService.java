package com.example.lmsbackend.service;

import com.example.lmsbackend.domain.ChatRoomEntity;
import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.dto.chat.ActiveChat;
import com.example.lmsbackend.dto.chat.CreateChatRoomRequest;
import com.example.lmsbackend.dto.chat.MessagesPagedDto;
import com.example.lmsbackend.dto.chat.SendingMessageDto;
import com.example.lmsbackend.enums.ChatRoomType;
import com.example.lmsbackend.exceptions.UnauthorizedException;
import com.example.lmsbackend.exceptions.UserNotFoundException;
import com.example.lmsbackend.mapper.MapperUtils;
import com.example.lmsbackend.mapper.MessageMapper;
import com.example.lmsbackend.mapper.UserMapper;
import com.example.lmsbackend.repository.ChatRoomRepository;
import com.example.lmsbackend.repository.ChatRoomUserRepository;
import com.example.lmsbackend.repository.MessageRepository;
import com.example.lmsbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageMapper messageMapper;
    private final UserMapper userMapper;
    private final SimpMessagingTemplate messageTemplate;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final MessageRepository messageRepository;

    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public void sendMessage(SendingMessageDto sendingMessageDto) {
        ChatRoomEntity chatRoom;
        if (sendingMessageDto.getChatRoomId() == null) {
            chatRoom = createChatRoomIfNotExist(sendingMessageDto.getSenderId(), sendingMessageDto.getReceiverId());
        }
        else {
            var chatRoomOpt = chatRoomRepository.findChatRoomAndMembersById(sendingMessageDto.getChatRoomId());
            if (chatRoomOpt.isEmpty()) {
                // ! TODO: thrown exception
                return;
            }
            chatRoom = chatRoomOpt.get();
        }
        var message = messageMapper.mapToMessageEntity(sendingMessageDto);
        message.setChatRoom(chatRoom);

        var senderInMembers = chatRoom.getMembers()
                .stream()
                .filter(member -> Objects.equals(member.getId(), sendingMessageDto.getSenderId()))
                .findAny();
        if (senderInMembers.isEmpty()) {
            // ! TODO: throw not authorizaion
            return;
        }
        message.setSender(senderInMembers.get());
        chatRoom.setLastMessage(message);
        messageRepository.save(message);
//        chatRoomRepository.save(chatRoom);
        var members = chatRoom.getMembers();
        var sentMessageDto = messageMapper.mapToSentMessageDto(message);
        // currently, we sent message to all user in room chat (includes member sending message)
        members.forEach(member -> messageTemplate.convertAndSend("topic.user." + member.getId(), sentMessageDto));
    }

    public ChatRoomEntity createChatRoomIfNotExist(Long senderId, Long receiverId) {
        var isExists = chatRoomRepository.isChatRoomExists(ChatRoomType.USER, List.of(senderId, receiverId));
        if (!isExists) {
            return chatRoomService.createChatRoom(new CreateChatRoomRequest(List.of(senderId, receiverId), "", ChatRoomType.USER.getValue()));
        }
        else {
            // TODO: invalid request
            throw new RuntimeException();
        }
    }

    public ActiveChat getMessageByChatRoomId(Long chatRoomId, Integer page, Integer size) {
        var messagesByChatRoomId = messageRepository.getMessagesByChatRoomId(chatRoomId, page, size);
        var chatRoom = chatRoomService.getFetchMembersById(chatRoomId);

        var messagesPagedDto = new MessagesPagedDto();
        MapperUtils.mapPagedDto(messagesPagedDto, messagesByChatRoomId);
        messagesPagedDto.setListData(
                messagesByChatRoomId
                        .stream()
                        .map(messageMapper::mapToSentMessageDto)
                        .collect(toList())
        );

        var activeChat = new ActiveChat();
        activeChat.setChatRoomId(chatRoomId);
        activeChat.setChatRoomName(chatRoom.getName());
        activeChat.setMessages(messagesPagedDto);
        if (chatRoom.getType() == ChatRoomType.USER) {
            var user = chatRoom.getMembers()
                    .stream()
                    .filter(member -> !StringUtils.equals(member.getUsername(), userService.getCurrentUsername()))
                    .findFirst()
                    .orElseThrow(UserNotFoundException::new);
            activeChat.setImage(user.getAvatar());
        }
        activeChat.setMembers(chatRoom
                .getMembers()
                .stream()
                .map(userMapper::mapToUserInformation).collect(toList()));

        return activeChat;
    }

    public void seenMessage(Long chatRoomId) {
        var currentUser = getCurrentUser();
        var chatRoom = chatRoomService.getChatRoomById(chatRoomId);

        // TODO: throw specific exception
        var chatRoomUser = chatRoomUserRepository.findByChatRoomAndUser(chatRoom, currentUser)
                .orElseThrow(RuntimeException::new);
        chatRoomUser.setNumberOfUnSeenMessages(0);
        chatRoomUserRepository.save(chatRoomUser);
    }

    public UserEntity getCurrentUser() {
        var username = userService.getCurrentUsername();
        var currentUserOpt = userRepository.findByUsername(username);
        if (currentUserOpt.isEmpty()) {
            throw new UnauthorizedException();
        }
        return currentUserOpt.get();
    }
}
