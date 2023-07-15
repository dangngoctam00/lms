package com.example.lmsbackend.repository.custom;

import com.blazebit.persistence.PagedList;
import com.example.lmsbackend.domain.ChatRoomEntity;
import com.example.lmsbackend.enums.ChatRoomType;
import com.querydsl.core.Tuple;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChatRoomRepositoryCustom {

    Optional<ChatRoomEntity> findChatRoomAndMembersById(Long id);

    Set<ChatRoomEntity> findFetchMembersByIdIn(List<Long> idList);

    boolean isChatRoomExists(ChatRoomType type, List<Long> usernames);

    PagedList<Tuple> getRecentChatRoomByUser(Long id, Integer page, Integer size);
}
