package com.example.lmsbackend.dto.classes;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AttendanceSessionNoteDto {

    @NotNull(message = "This is a required field")
    private String note;
}
