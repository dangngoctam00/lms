package com.example.lmsbackend.dto.request.google.calendar;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateNewCalendarDto {
    @NotBlank(message = "Tên calendar không được để trống")
    private String name;
}
