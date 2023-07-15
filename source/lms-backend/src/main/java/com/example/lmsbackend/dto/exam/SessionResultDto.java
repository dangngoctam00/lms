package com.example.lmsbackend.dto.exam;

import com.example.lmsbackend.dto.response.user.UsersPagedDto;
import com.example.lmsbackend.enums.FinalVerdict;
import com.example.lmsbackend.enums.GradedState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SessionResultDto {
    private String sessionId;
    private UsersPagedDto.UserInformation user;
    private Integer totalScore;
    private Double score;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private Long timeTaken; // seconds unit
    private Integer attempt;
    private FinalVerdict finalVerdict;
    private GradedState gradedState;
    private QuestionAnswerPagedResponseDto questions;
    private Boolean isLastSession;
}
