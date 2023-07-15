package com.example.lmsbackend.dto.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SendingMessageDto {
    private Long chatRoomId;
    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime createdAt;
}
