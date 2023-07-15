package com.example.lmsbackend.dto.response.course;

import lombok.Data;

import java.util.List;

@Data
public class UnitByTextbookDto {

    private Long textbookId;
    private String name;
    private List<UnitDto> units;
}
