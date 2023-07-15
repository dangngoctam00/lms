package com.example.lmsbackend.dto.response.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PermissionOfUserDTO {
    private Integer id;
    private String title;
    private String description;
    private Integer isLimitByBranch;
    private Integer isLimitByTeaching;
    private Integer isLimitByDean;
    private Integer isLimitByManager;
    private Integer isLimitByLearn;
    private LocalDateTime validFrom;
    private LocalDateTime expiresAt;
}
