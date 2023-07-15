package com.example.lmsbackend.service;

import com.example.lmsbackend.domain.ChatRoomEntity;
import com.example.lmsbackend.domain.ChatRoomUserEntity;
import com.example.lmsbackend.dto.chat.RecentChatList;
import com.example.lmsbackend.enums.ChatRoomType;
import com.example.lmsbackend.exceptions.UserNotFoundException;
import com.example.lmsbackend.repository.ChatRoomUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class ChatRoomUserService {

    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatService chatService;
    private final UserService userService;

    public RecentChatList getRecentChatRoom(Integer page, Integer size) {
        var username = userService.getCurrentUsername();
        var recentUserChatRooms = chatRoomUserRepository.getRecentChatRooms(username);
        var chatItems = recentUserChatRooms
                .stream()
                .map(recentUserChatRoom -> {
                    var chatRoom = recentUserChatRoom.getChatRoom();
                    return mapToChatItem(chatRoom, recentUserChatRoom, username);
                }).collect(toList());
        var recentChats = new RecentChatList();
        recentChats.setListData(chatItems);
        return recentChats;
    }

    private RecentChatList.ChatItem mapToChatItem(ChatRoomEntity chatRoom, ChatRoomUserEntity chatRoomUser, String currentUserName) {
        var chatItem = new RecentChatList.ChatItem();
        chatItem.setChatRoomId(chatRoomUser.getId().getChatRoomId());
        chatItem.setNumOfUnseenMessages(chatRoomUser.getNumberOfUnSeenMessages());
        chatItem.setChatRoomName(chatRoom.getName());
        chatItem.setLastMessage(chatRoom.getLastMessage().getContent());
        chatItem.setType(chatRoom.getType().getValue());
        if (chatRoom.getType() == ChatRoomType.USER) {
            var user = chatRoom.getMembers()
                    .stream()
                    .filter(member -> !StringUtils.equals(member.getUsername(), currentUserName))
                    .findFirst()
                    .orElseThrow(UserNotFoundException::new);
            chatItem.setImage(user.getAvatar());
        } else {
            // TODO avatar of group chat left null now
        }
        return chatItem;
    }
}
