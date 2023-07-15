package com.example.lmsbackend.dto.classes;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class PostReactionDto {

    private Long id;

    @NotNull(message = "This is a required field")
    @Pattern(regexp = "^(UP_VOTE|DOWN_VOTE)$", message = "For the Type, only values UP_VOTE, DOWN_VOTE are accepted")
    private String type;
}
