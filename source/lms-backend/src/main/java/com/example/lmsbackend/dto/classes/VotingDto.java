package com.example.lmsbackend.dto.classes;

import com.example.lmsbackend.dto.response.user.UsersPagedDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingDto {
    private Long id;
    @NotBlank(message = "This is a required field")
    private String title;
    private String description;
    private Boolean isAllowedToAddChoice;
    private Integer order;
    private List<VotingChoiceDto> choices = new ArrayList<>();
    private LocalDateTime createdAt;
    private UsersPagedDto.UserInformation createdBy;
    private LocalDateTime updatedAt;
    private UsersPagedDto.UserInformation updatedBy;

    public VotingDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
