package com.example.lmsbackend.service;

import com.example.lmsbackend.domain.ChatRoomEntity;
import com.example.lmsbackend.domain.MessageEntity;
import com.example.lmsbackend.dto.chat.CreateChatRoomRequest;
import com.example.lmsbackend.dto.chat.RecentChatList;
import com.example.lmsbackend.enums.ChatRoomType;
import com.example.lmsbackend.exceptions.UserNotFoundException;
import com.example.lmsbackend.exceptions.chat.ChatRoomAlreadyExistException;
import com.example.lmsbackend.exceptions.chat.ChatRoomNotFoundException;
import com.example.lmsbackend.mapper.ChatRoomMapper;
import com.example.lmsbackend.mapper.MapperUtils;
import com.example.lmsbackend.mapper.UserMapper;
import com.example.lmsbackend.repository.ChatRoomRepository;
import com.example.lmsbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public ChatRoomEntity createChatRoom(CreateChatRoomRequest request) {
        var newChatRoom = chatRoomMapper.mapToChatRoomEntity(request);
        newChatRoom.setType(ChatRoomType.valueOf(request.getType()));

        var isChatRoomExisted = chatRoomRepository.isChatRoomExists(ChatRoomType.valueOf(request.getType()), request.getUsersId());
        if (isChatRoomExisted) {
            throw new ChatRoomAlreadyExistException();
        }

        var members = userRepository.findUsersAndChatRoomByIds(request.getUsersId());

        request.getUsersId()
                .forEach(userId -> members
                        .stream()
                        .filter(member -> member.getId().equals(userId))
                        .findAny()
                        .orElseThrow(UserNotFoundException::new));

        newChatRoom.setMembers(members);
        members
                .forEach(member -> member.getChatRooms().add(newChatRoom));
        chatRoomRepository.save(newChatRoom);
        log.info("Create chat room {} successfully", newChatRoom.getName());
        return newChatRoom;
    }

    public ChatRoomEntity getChatRoomById(Long id) {
        // TODO: throw specific exception
        return chatRoomRepository.findById(id).orElseThrow(ChatRoomNotFoundException::new);
    }

    public ChatRoomEntity getFetchMembersById(Long id) {
        return chatRoomRepository.findChatRoomAndMembersById(id).orElseThrow(ChatRoomNotFoundException::new);
    }

    public RecentChatList getRecentChatRoom(Integer page, Integer size) {
        var username = userService.getCurrentUsername();
        var user = userService.findByUsername(username);

        // .select(chatRoom.id, chatRoom.name, chatRoom.type, chatRoom.lastMessage, chatRoomUser.numberOfUnSeenMessages)
        var recentChatRoomByUser = chatRoomRepository.getRecentChatRoomByUser(user.getId(), page, size);
        var chatRoomsId = recentChatRoomByUser
                .stream()
                .map(room -> room.get(0, Long.class))
                .collect(toList());
        var chatRooms = chatRoomRepository.findFetchMembersByIdIn(chatRoomsId);
        var recentChats = new RecentChatList();
        MapperUtils.mapPagedDto(recentChats, recentChatRoomByUser);
        var chatItems = recentChatRoomByUser
                .stream()
                .map(chatRoomInfo -> {
                    var chatItem = new RecentChatList.ChatItem();
                    var chatRoomId = chatRoomInfo.get(0, Long.class);
                    var chatRoom = chatRooms
                            .stream()
                            .filter(c -> Objects.equals(c.getId(), chatRoomId))
                            .findAny()
                            .orElseThrow();
                    chatItem.setChatRoomId(chatRoomId);
                    chatItem.setType(chatRoomInfo.get(2, ChatRoomType.class).getValue());
                    if (StringUtils.equals(chatItem.getType(), ChatRoomType.USER.getValue())) {
                        var oppositeUser = chatRoom.getMembers()
                                .stream()
                                .filter(c -> !StringUtils.equals(username, c.getUsername()))
                                .findAny()
                                .orElseThrow();
                        chatItem.setChatRoomName(String.format("%s %s", oppositeUser.getLastName(), oppositeUser.getFirstName()));
                        chatItem.setImage(oppositeUser.getAvatar());
                    } else {
                        chatItem.setChatRoomName(chatRoom.getName());
                        chatItem.setImage(chatRoom.getUrl());
                    }
                    var lastMessage = chatRoomInfo.get(3, MessageEntity.class);
                    chatItem.setLastMessage(lastMessage.getContent());
                    chatItem.setLastMessageSender(String.format("%s %s", lastMessage.getSender().getLastName(), lastMessage.getSender().getFirstName()));
                    chatItem.setNumOfUnseenMessages(chatRoomInfo.get(4, Integer.class));
                    chatItem.setMembers(chatRoom.getMembers()
                            .stream()
                            .map(userMapper::mapToUserInformation)
                            .collect(toList()));
                    return chatItem;
                })
                .collect(toList());
        recentChats.setListData(chatItems);
        return recentChats;
    }
}
