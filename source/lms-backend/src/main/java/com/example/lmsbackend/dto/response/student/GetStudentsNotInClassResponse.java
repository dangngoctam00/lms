package com.example.lmsbackend.dto.response.student;

import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.dto.students.StudentInfo;
import lombok.Data;

import java.util.List;

@Data
public class GetStudentsNotInClassResponse extends BaseResponse {
    private List<StudentInfo> studentInfos;
}
