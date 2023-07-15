package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.ChatRoomEntity;
import com.example.lmsbackend.domain.ChatRoomUserEntity;
import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.repository.custom.ChatRoomUserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUserEntity, Long>, QuerydslPredicateExecutor<ChatRoomUserEntity>,
        ChatRoomUserRepositoryCustom {
    Optional<ChatRoomUserEntity> findByChatRoomAndUser(ChatRoomEntity chatRoom, UserEntity user);
}
