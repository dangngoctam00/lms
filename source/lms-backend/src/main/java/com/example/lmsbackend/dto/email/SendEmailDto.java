package com.example.lmsbackend.dto.email;

import lombok.Data;

@Data
public class SendEmailDto {

    private String subject;
    private String content;
    private String attachment;
}
