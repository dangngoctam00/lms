package com.example.lmsbackend.dto.exam.fill_in_blank;

import com.example.lmsbackend.dto.exam.BlankDto;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class FillInBlankOptionDto extends BlankDto {
    private String expectedAnswer;
    @Pattern(regexp = "^(EXACT|CONTAIN|REGEX)$",
            message = "For the Match Strategy, only values EXACT, CONTAIN, REGEX are accepted")
    private String matchStrategy;
    private String studentAnswer = "";
    private Integer order;
}
