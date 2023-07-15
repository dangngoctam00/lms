package com.example.lmsbackend.dto.response.course;

import com.example.lmsbackend.dto.classes.VotingDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChapterDto {
    private Long id;

    @NotBlank(message = "This is a required field")
    private String title;
    private Long order;
    private List<UnitDto> units = new ArrayList<>();
    private List<QuizDto> quizzes = new ArrayList<>();
    private List<VotingDto> votes = new ArrayList<>();
}
