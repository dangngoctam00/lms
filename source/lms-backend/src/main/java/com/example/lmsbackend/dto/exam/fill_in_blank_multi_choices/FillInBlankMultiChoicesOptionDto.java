package com.example.lmsbackend.dto.exam.fill_in_blank_multi_choices;

import lombok.Data;

@Data
public class FillInBlankMultiChoicesOptionDto {
    private Long id;
    private Integer answerKey;
    private String content;
    private Integer order;
}
