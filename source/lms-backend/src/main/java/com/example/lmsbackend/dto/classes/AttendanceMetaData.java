package com.example.lmsbackend.dto.classes;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttendanceMetaData {
    private Long id;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private String note;
    private String room;
    private String strategy;
}
