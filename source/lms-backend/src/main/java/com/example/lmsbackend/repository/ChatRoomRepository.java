package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.ChatRoomEntity;
import com.example.lmsbackend.repository.custom.ChatRoomRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long>, QuerydslPredicateExecutor<ChatRoomEntity>,
        ChatRoomRepositoryCustom {
}
