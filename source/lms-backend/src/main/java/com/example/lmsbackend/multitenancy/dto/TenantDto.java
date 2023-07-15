package com.example.lmsbackend.multitenancy.dto;

import lombok.Data;

@Data
public class TenantDto {
    private String firstname;
    private String lastname;
    private String domain;
    private String email;
    private String phone;
    private String username;
    private String password;
}
