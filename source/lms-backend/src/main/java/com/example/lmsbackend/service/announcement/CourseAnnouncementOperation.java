package com.example.lmsbackend.service.announcement;

import com.example.lmsbackend.domain.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseAnnouncementOperation implements AnnouncementOperation {
    @Override
    public List<String> getReceiversEmail(Long id) {
        return null;
    }

    @Override
    public List<UserEntity> getReceivers(Long id) {
        return null;
    }
}
