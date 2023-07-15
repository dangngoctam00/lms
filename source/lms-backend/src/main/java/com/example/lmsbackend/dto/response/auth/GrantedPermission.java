package com.example.lmsbackend.dto.response.auth;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GrantedPermission {
    private Integer id;
    private int isLimitByBranch;
    private int isLimitByTeaching;
    private int isLimitByDean;
    private int isLimitByManager;
    private int isLimitByLearn;
    private LocalDateTime validFrom;
    private LocalDateTime expiresAt;
}
