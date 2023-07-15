package com.example.lmsbackend.dto.chat;

import com.example.lmsbackend.dto.response.user.UsersPagedDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SentMessageDto {
    private Long chatRoomId;
    private UsersPagedDto.UserInformation sender;
    private String content;
    private LocalDateTime createdAt;
}
