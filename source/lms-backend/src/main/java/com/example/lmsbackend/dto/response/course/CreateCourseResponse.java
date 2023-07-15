package com.example.lmsbackend.dto.response.course;

import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.enums.StatusCode;

public class CreateCourseResponse extends BaseResponse {

    public CreateCourseResponse() {
    }

    public CreateCourseResponse(String message) {
        super(StatusCode.SUCCESS.name(), message);
    }
}
