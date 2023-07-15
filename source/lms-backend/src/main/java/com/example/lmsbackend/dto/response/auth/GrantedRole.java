package com.example.lmsbackend.dto.response.auth;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GrantedRole {
    private Long id;
    private LocalDateTime validFrom;
    private LocalDateTime expiresAt;
}
