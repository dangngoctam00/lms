package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.MessageEntity;
import com.example.lmsbackend.dto.chat.SendingMessageDto;
import com.example.lmsbackend.dto.chat.SentMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        uses = CommonMapper.class)
public interface MessageMapper {

    @Mapping(target = "createdAt", expression = ("java(defaultDateTime())"))
    MessageEntity mapToMessageEntity(SendingMessageDto dto);

    @Mapping(target = "chatRoomId", expression = ("java(entity.getChatRoom().getId())"))
    SentMessageDto mapToSentMessageDto(MessageEntity entity);

    default LocalDateTime defaultDateTime() {
        return LocalDateTime.now();
    }
}
