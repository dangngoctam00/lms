package com.example.lmsbackend.dto.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddStudentRequestDto {

    @NotNull(message = "This is required field")
    private List<Long> studentIds;
}
