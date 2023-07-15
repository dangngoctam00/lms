package com.example.lmsbackend.dto.chat;

import com.example.lmsbackend.dto.response.AbstractPagedDto;
import com.example.lmsbackend.dto.response.user.UsersPagedDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecentChatList extends AbstractPagedDto {

    private List<ChatItem> listData;

    @Data
    public static class ChatItem {
        private Long chatRoomId;
        private String chatRoomName;
        private String image;
        private String type;
        private String lastMessage;
        private String lastMessageSender;
        private Integer numOfUnseenMessages;
        List<UsersPagedDto.UserInformation> members;
    }
}

//
//id: ID,
//        name: string,
//        image?: string,
//        lastMessage: Message,
//        numOfUnseenMessages: number,
//        state: "hidden" | "minimized" | "active"