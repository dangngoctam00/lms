package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.MessageEntity;
import com.example.lmsbackend.repository.custom.MessageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long>, QuerydslPredicateExecutor<MessageEntity>,
        MessageRepositoryCustom {
}
