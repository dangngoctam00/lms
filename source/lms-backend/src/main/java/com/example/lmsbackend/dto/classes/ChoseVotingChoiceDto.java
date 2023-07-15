package com.example.lmsbackend.dto.classes;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChoseVotingChoiceDto {

    @NotNull(message = "This is a required field")
    private Long choiceId;
}
