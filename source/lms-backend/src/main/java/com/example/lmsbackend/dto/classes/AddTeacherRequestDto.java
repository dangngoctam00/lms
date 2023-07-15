package com.example.lmsbackend.dto.classes;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class AddTeacherRequestDto {

    @NotNull(message = "This is required field")
    @Valid
    private List<TeacherDto> idList;

    @Getter
    @Setter
    public static class TeacherDto {
        private Long id;
        @Pattern(regexp = "^(TEACHER|TEACHER_ASSISTANT)$",
                message = "For the Status when updating class, only values TEACHER, TEACHER_ASSISTANT are accepted")
        private String role;
    }
}
