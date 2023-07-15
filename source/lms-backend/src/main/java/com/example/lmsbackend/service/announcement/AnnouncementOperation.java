package com.example.lmsbackend.service.announcement;

import com.example.lmsbackend.domain.UserEntity;

import java.util.List;

public interface AnnouncementOperation {
    List<String> getReceiversEmail(Long id);

    List<UserEntity> getReceivers(Long id);
}
