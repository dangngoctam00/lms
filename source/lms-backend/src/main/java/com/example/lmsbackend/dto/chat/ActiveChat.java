package com.example.lmsbackend.dto.chat;

import com.example.lmsbackend.dto.response.user.UsersPagedDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveChat {
    private Long chatRoomId;
    private String chatRoomName;
    private String image;
    private MessagesPagedDto messages;
    private List<UsersPagedDto.UserInformation> members;
    private Long adminId;
    private Long numberOfUnseenMessages;
}
