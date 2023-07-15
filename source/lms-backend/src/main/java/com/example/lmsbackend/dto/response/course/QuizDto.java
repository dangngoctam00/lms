package com.example.lmsbackend.dto.response.course;

import com.example.lmsbackend.dto.exam.QuizConfigDto;
import com.example.lmsbackend.enums.ActivityState;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class QuizDto {
    private Long id;
    @NotBlank(message = "This is a required field")
    private String title;
    private String description;
    @NotNull(message = "This is a required field")
    private String tag;
    private Long examId;
    private String examTitle;
    private Boolean isFromCourse = false;
    private Integer order;
    private String state;
    private Boolean isAllowedToInitSession;
    private Boolean isAllowedToContinueLastSession;
    private String unEndedSessionId;
    private QuizConfigDto config;
    private Integer totalGrade = 0;

    private List<TextbookRef> textbooks = new ArrayList<>();

    public QuizDto() {
    }

    public QuizDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public QuizDto(Long id, String title, Long quizCourse, ActivityState state) {
        this.id = id;
        this.title = title;
        this.state = state.getState();
        this.isFromCourse = quizCourse != null;
    }

    public QuizDto(Long id, String title, Long quizCourse, Long examId, ActivityState state) {
        this.id = id;
        this.title = title;
        this.state = state.getState();
        this.isFromCourse = quizCourse != null;
        this.examId = examId;
    }
}