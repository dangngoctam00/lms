package com.example.lmsbackend.dto.request.role;

import lombok.Data;

@Data
public class PermissionForRole {
    private Integer id;
    private Integer isLimitByBranch;
    private Integer isLimitByTeaching;
    private Integer isLimitByDean;
    private Integer isLimitByManager;
    private Integer isLimitByLearn;
}
