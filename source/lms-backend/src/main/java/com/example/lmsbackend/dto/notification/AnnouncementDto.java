package com.example.lmsbackend.dto.notification;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AnnouncementDto {
    private Long id;
    private Long senderId;
    private String senderName;
    private String image;
    private String subject;
    private String content;
    private String attachment;
    private LocalDateTime sentAt;
    private List<String> tags;
    private Boolean seen = true;
    private Boolean readOnly;
    private Long classId;
    private String className;
}
