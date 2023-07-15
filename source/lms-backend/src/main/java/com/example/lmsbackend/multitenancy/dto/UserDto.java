package com.example.lmsbackend.multitenancy.dto;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
