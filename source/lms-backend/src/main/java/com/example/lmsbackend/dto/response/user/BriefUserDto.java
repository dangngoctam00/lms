package com.example.lmsbackend.dto.response.user;

import lombok.Data;

@Data
public class BriefUserDto {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String avatar;
}
