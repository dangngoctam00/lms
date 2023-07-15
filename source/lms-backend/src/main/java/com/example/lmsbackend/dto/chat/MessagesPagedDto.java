package com.example.lmsbackend.dto.chat;

import com.example.lmsbackend.dto.response.AbstractPagedDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class MessagesPagedDto extends AbstractPagedDto {
    private List<SentMessageDto> listData;
}
