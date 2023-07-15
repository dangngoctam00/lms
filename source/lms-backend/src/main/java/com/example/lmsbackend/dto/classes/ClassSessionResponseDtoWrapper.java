package com.example.lmsbackend.dto.classes;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassSessionResponseDtoWrapper {
    private List<ClassSessionResponseDto> sessions = new ArrayList<>();

    private Boolean hasAfter = false;
    private Boolean hasPrevious = false;
    private String daysOfWeek;
}
