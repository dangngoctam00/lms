package com.example.lmsbackend.dto.classes;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttendanceTimeMetaData {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
