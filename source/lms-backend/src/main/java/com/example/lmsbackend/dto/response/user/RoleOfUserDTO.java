package com.example.lmsbackend.dto.response.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoleOfUserDTO {
    private Long id;
    private String title;
    private LocalDateTime validFrom;
    private LocalDateTime expiresAt;
}
