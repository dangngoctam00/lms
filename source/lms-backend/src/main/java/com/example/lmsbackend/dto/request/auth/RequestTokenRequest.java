package com.example.lmsbackend.dto.request.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RequestTokenRequest {
    @NotBlank(message = "Token được để trống")
    private String token;
    @NotNull(message = "Refresh Token ko được để trống")
    private String refreshToken;
}
