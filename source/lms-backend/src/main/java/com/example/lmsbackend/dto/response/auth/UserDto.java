package com.example.lmsbackend.dto.response.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String avatar;
    private String calendarId;
    private String accountType;
}
