package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.domain.ChatRoomUserEntity;
import com.example.lmsbackend.domain.QChatRoomEntity;
import com.example.lmsbackend.domain.QChatRoomUserEntity;
import com.example.lmsbackend.domain.QMessageEntity;
import com.example.lmsbackend.repository.custom.ChatRoomUserRepositoryCustom;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedHashSet;
import java.util.Set;

@RequiredArgsConstructor
public class ChatRoomUserRepositoryCustomImpl implements ChatRoomUserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public Set<ChatRoomUserEntity> getRecentChatRooms(String username) {
        var chatRoomUser = QChatRoomUserEntity.chatRoomUserEntity;
        var result = new BlazeJPAQuery<ChatRoomUserEntity>(entityManager, criteriaBuilderFactory)
                .from(chatRoomUser)
                .where(chatRoomUser.user.username.eq(username))
                .select(chatRoomUser)
                .leftJoin(chatRoomUser.chatRoom.members)
                .fetchJoin()
                .orderBy(chatRoomUser.chatRoom.lastMessage.createdAt.desc(),
                        chatRoomUser.chatRoom.lastMessage.id.desc(),
                        chatRoomUser.id.chatRoomId.desc(),
                        chatRoomUser.id.userId.desc())
                .fetch();

        // ! test:
        var chatRoom = QChatRoomEntity.chatRoomEntity;
        new BlazeJPAQuery<ChatRoomUserEntity>(entityManager, criteriaBuilderFactory)
                .from(chatRoom)
                .innerJoin(chatRoom.userChatRooms, chatRoomUser).on(chatRoom.id.eq(chatRoomUser.id.chatRoomId))
                .innerJoin(chatRoom.lastMessage, QMessageEntity.messageEntity).on(chatRoom.lastMessage.id.eq(QMessageEntity.messageEntity.id))
                .where(chatRoomUser.id.userId.eq(1L))
                .orderBy(QMessageEntity.messageEntity.createdAt.desc(), QMessageEntity.messageEntity.id.desc())
                .fetch();

        return new LinkedHashSet<>(result);
    }
}
