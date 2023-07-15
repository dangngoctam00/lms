package com.example.lmsbackend.dto.response;

import com.example.lmsbackend.enums.StatusCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse implements Serializable {
    private String code;
    private String message;

    public BaseResponse(){
        this.code = StatusCode.SUCCESS.name();
        this.message = StatusCode.SUCCESS.getMessage();
    }

    public void setStatusCode(StatusCode statusCode){
        code = statusCode.name();
        message = statusCode.getMessage();
    }

    public BaseResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
