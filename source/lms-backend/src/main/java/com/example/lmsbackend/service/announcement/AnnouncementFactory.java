package com.example.lmsbackend.service.announcement;

import com.example.lmsbackend.exceptions.notification.UnsupportedScopeTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementFactory {

    private final ClassAnnouncementOperation classAnnouncementOperation;
    private final CourseAnnouncementOperation courseAnnouncementOperation;
    private final CenterAnnouncementOperation centerAnnouncementOperation;

    public AnnouncementOperation getAnnouncementOperation(String scope) {
        switch (scope) {
            case "CLASS":
                return classAnnouncementOperation;
            case "COURSE":
                return courseAnnouncementOperation;
            case "CENTER":
                return centerAnnouncementOperation;
            default:
                throw new UnsupportedScopeTypeException(scope);
        }
    }
}
