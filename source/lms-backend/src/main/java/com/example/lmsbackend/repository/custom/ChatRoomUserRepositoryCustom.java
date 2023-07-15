package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.domain.ChatRoomUserEntity;

import java.util.Set;

public interface ChatRoomUserRepositoryCustom {
    Set<ChatRoomUserEntity> getRecentChatRooms(String username);
}
