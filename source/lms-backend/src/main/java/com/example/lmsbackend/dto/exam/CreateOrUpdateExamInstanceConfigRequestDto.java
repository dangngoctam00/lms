package com.example.lmsbackend.dto.exam;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateOrUpdateExamInstanceConfigRequestDto {
    private Long id;
    private LocalDateTime startedAt;
    private LocalDateTime validBefore;
    private Boolean haveTimeLimit;
    private Long timeLimit;
    private Boolean canNavigateBackward;
    private Boolean showResultImmediately;
    private Boolean allowDisconnect;
}
