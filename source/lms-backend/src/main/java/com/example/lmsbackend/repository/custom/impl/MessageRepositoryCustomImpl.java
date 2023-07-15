package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.domain.MessageEntity;
import com.example.lmsbackend.domain.QMessageEntity;
import com.example.lmsbackend.domain.coursemodel.CourseEntity;
import com.example.lmsbackend.repository.custom.MessageRepositoryCustom;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequiredArgsConstructor
public class MessageRepositoryCustomImpl implements MessageRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public PagedList<MessageEntity> getMessagesByChatRoomId(Long chatRoomId, Integer page, Integer size) {
        var messageEntity = QMessageEntity.messageEntity;
        return new BlazeJPAQuery<CourseEntity>(entityManager, criteriaBuilderFactory)
                .from(messageEntity)
                .leftJoin(messageEntity.sender)
                .fetchJoin()
                .where(messageEntity.chatRoom.id.eq(chatRoomId))
                .orderBy(messageEntity.createdAt.desc(), messageEntity.id.desc())
                .select(messageEntity)
                .fetchPage((page - 1) * size, size);
    }
}
