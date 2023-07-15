package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.compositekey.UserAnnouncementKey;
import com.example.lmsbackend.domain.notification.UserAnnouncementEntity;
import com.example.lmsbackend.repository.custom.UserAnnouncementRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface UserAnnouncementRepository extends JpaRepository<UserAnnouncementEntity, UserAnnouncementKey>,
        QuerydslPredicateExecutor<UserAnnouncementEntity>, UserAnnouncementRepositoryCustom {
    Optional<UserAnnouncementEntity> findById(UserAnnouncementKey id);

}
