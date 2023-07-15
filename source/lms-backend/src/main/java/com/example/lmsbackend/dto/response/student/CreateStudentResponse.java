package com.example.lmsbackend.dto.response.student;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.Data;

@Data
public class CreateStudentResponse extends BaseResponse {
    private Long studentId;
}
