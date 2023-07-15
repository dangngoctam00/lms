package com.example.lmsbackend.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionDTO {
    private long id;
    private String title;
    private String code;
    private String description;
    private boolean hasLimitByBranch;
    private boolean hasLimitByTeaching;
    private boolean hasLimitByDean;
    private boolean hasLimitByManager;
    private boolean hasLimitByLearn;
}

