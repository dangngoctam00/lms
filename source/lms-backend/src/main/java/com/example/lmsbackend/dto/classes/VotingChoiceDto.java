package com.example.lmsbackend.dto.classes;

import com.example.lmsbackend.dto.response.user.UsersPagedDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class VotingChoiceDto {
    private Long id;
    @NotBlank(message = "This is a required field")
    private String content;
    private Double percent;
    private Integer numbersOfChosen;

    private LocalDateTime createdAt;
    private UsersPagedDto.UserInformation createdBy;
    private LocalDateTime updatedAt;
    private UsersPagedDto.UserInformation updatedBy;
}
