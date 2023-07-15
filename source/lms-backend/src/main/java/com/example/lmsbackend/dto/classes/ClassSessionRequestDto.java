package com.example.lmsbackend.dto.classes;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class ClassSessionRequestDto {
    private Long id;
    private Long teacherId;
    private Long unitId;
    @NotNull(message = "This is a required field")
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private String room;
    private String note;
    private Boolean notifyWithEmail;
    private Boolean applyTeacherToAll;
    private Boolean applyRoomToAll;
}
