package com.example.lmsbackend.dto.exam;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateExamInstanceRequestDto {

    @NotNull(message = "This is a required field")
    private Long testContentId;
}
