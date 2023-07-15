package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.criteria.AnnouncementCriteria;
import com.example.lmsbackend.domain.notification.AnnouncementEntity;
import com.example.lmsbackend.domain.notification.UserAnnouncementEntity;

import java.util.Optional;

public interface UserAnnouncementRepositoryCustom {

    PagedList<AnnouncementEntity> getAnnouncementByClass(Long classId, Long receiverId, AnnouncementCriteria criteria);

    PagedList<UserAnnouncementEntity> getReceivedAnnouncementByClass(Long classId, Long receiverId, AnnouncementCriteria criteria);

    com.blazebit.persistence.PagedList<AnnouncementEntity> getSentAnnouncementByClass(Long classId, Long senderId, AnnouncementCriteria criteria);

    Optional<UserAnnouncementEntity> findByAnnouncementAndUser(Long announcementId, Long userId);
}
