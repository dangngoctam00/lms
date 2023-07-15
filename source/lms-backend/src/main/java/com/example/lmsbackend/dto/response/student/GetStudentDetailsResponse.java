package com.example.lmsbackend.dto.response.student;

import com.example.lmsbackend.dto.students.StudentDTO;
import lombok.Data;

@Data
public class GetStudentDetailsResponse {
    private StudentDTO student;
}
