package com.example.lmsbackend.dto.response.student;

import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.dto.students.StudentPagedDTO;
import lombok.Data;

@Data
public class GetStudentsResponse extends BaseResponse {
    private StudentPagedDTO data;
}
