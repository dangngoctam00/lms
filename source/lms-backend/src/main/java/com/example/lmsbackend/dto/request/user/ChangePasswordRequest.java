package com.example.lmsbackend.dto.request.user;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String password;
    private String newPassword;
    private String confirmPassword;
}
