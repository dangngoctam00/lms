package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.domain.ChatRoomEntity;
import com.example.lmsbackend.domain.QChatRoomEntity;
import com.example.lmsbackend.domain.QChatRoomUserEntity;
import com.example.lmsbackend.domain.QMessageEntity;
import com.example.lmsbackend.enums.ChatRoomType;
import com.example.lmsbackend.repository.custom.ChatRoomRepositoryCustom;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public Optional<ChatRoomEntity> findChatRoomAndMembersById(Long id) {
        var chatRoom = QChatRoomEntity.chatRoomEntity;
        var chatRoomEntity = new BlazeJPAQuery<ChatRoomEntity>(entityManager, criteriaBuilderFactory)
                .from(chatRoom)
                .leftJoin(chatRoom.members)
                .fetchJoin()
                .where(chatRoom.id.eq(id))
                .select(chatRoom)
                .distinct()
                .fetchFirst();
        return Optional.ofNullable(chatRoomEntity);
    }

    @Override
    public boolean isChatRoomExists(ChatRoomType type, List<Long> userIdList) {
        var chatRoom = QChatRoomEntity.chatRoomEntity;
        var chatRoomUser = QChatRoomUserEntity.chatRoomUserEntity;
        List<Tuple> fetch = new BlazeJPAQuery<ChatRoomEntity>(entityManager, criteriaBuilderFactory)
                .from(chatRoom)
                .where(chatRoom.type.eq(type))
                .innerJoin(chatRoom.userChatRooms, QChatRoomUserEntity.chatRoomUserEntity).on(chatRoom.id.eq(chatRoomUser.id.chatRoomId))
                .where(chatRoomUser.id.userId.in(userIdList))
                .fetchJoin()
                .groupBy(chatRoom.id)
                .having(chatRoom.count().eq(2L))
                .select(chatRoom.id, chatRoom.count())
                .fetch();
        return !CollectionUtils.isEmpty(fetch);
    }

    @Override
    public PagedList<Tuple> getRecentChatRoomByUser(Long id, Integer page, Integer size) {
        var chatRoom = QChatRoomEntity.chatRoomEntity;
        var chatRoomUser = QChatRoomUserEntity.chatRoomUserEntity;
        var message = QMessageEntity.messageEntity;
        return new BlazeJPAQuery<ChatRoomEntity>(entityManager, criteriaBuilderFactory)
                .from(chatRoom)
                .innerJoin(chatRoom.userChatRooms, chatRoomUser).on(chatRoom.id.eq(chatRoomUser.id.chatRoomId))
                .innerJoin(chatRoom.lastMessage, message).on(chatRoom.lastMessage.id.eq(message.id))
                .where(chatRoomUser.id.userId.eq(id))
                .orderBy(message.createdAt.desc(), QMessageEntity.messageEntity.id.desc())
                .select(chatRoom.id, chatRoom.name, chatRoom.type, chatRoom.lastMessage, chatRoomUser.numberOfUnSeenMessages)
                .fetchPage((page - 1) * size, size);
    }

    @Override
    public Set<ChatRoomEntity> findFetchMembersByIdIn(List<Long> idList) {
        var chatRoom = QChatRoomEntity.chatRoomEntity;
        var result = new BlazeJPAQuery<ChatRoomEntity>(entityManager, criteriaBuilderFactory)
                .from(chatRoom)
                .leftJoin(chatRoom.members)
                .fetchJoin()
                .where(chatRoom.id.in(idList))
                .select(chatRoom)
                .fetch();
        return new HashSet<>(result);
    }
}
