package com.example.lmsbackend.repository.custom;

import com.blazebit.persistence.PagedList;
import com.example.lmsbackend.domain.MessageEntity;

public interface MessageRepositoryCustom {
    PagedList<MessageEntity> getMessagesByChatRoomId(Long chatRoomId, Integer page, Integer size);
}
