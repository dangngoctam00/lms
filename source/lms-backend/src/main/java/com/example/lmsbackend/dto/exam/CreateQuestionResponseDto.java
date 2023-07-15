package com.example.lmsbackend.dto.exam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionResponseDto {
    private Long id;
    private String type;
}
