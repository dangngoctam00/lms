package com.example.lmsbackend.dto.exam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class QuestionStatistic {

    @JsonIgnore
    private Long id;
    private String courseName;
    private String courseCode;
    private String description;
    private String type;
    private Double averageScore;
    private Double standardDeviation;
    private Double averageAnswerAttempt;
    private Integer notAnswered;
    private Boolean needGrading;
}
