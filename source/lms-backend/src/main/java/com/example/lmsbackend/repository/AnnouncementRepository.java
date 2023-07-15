package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.notification.AnnouncementEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {

    @EntityGraph("notification-user-tags")
    Optional<AnnouncementEntity> findById(Long id);
}
