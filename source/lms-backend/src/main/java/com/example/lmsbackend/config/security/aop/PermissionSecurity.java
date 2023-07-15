package com.example.lmsbackend.config.security.aop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionSecurity {
    private Integer id;
    private int isLimitByBranch;
    private int isLimitByTeaching;
    private int isLimitByDean;
    private int isLimitByManager;
    private int isLimitByLearn;
    private int isLimitByOwner;
}
