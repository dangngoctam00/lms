package com.example.lmsbackend.mapper;


import com.example.lmsbackend.domain.ChatRoomEntity;
import com.example.lmsbackend.dto.chat.CreateChatRoomRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "url", expression = "java(randomUUID())")
    ChatRoomEntity mapToChatRoomEntity(CreateChatRoomRequest dto);

    default String randomUUID() {
        return UUID.randomUUID().toString();
    }
}
