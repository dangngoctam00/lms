package com.example.lmsbackend.dto.exam.writing;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdatePointDto {
    @NotNull
    private Double point;
    private String type;
}
