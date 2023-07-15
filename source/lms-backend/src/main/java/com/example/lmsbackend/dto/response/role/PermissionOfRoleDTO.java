package com.example.lmsbackend.dto.response.role;

import lombok.Data;

@Data
public class PermissionOfRoleDTO {
    private Integer id;
    private String code;
    private String title;
    private String description;
    private Integer isLimitByBranch;
    private Integer isLimitByTeaching;
    private Integer isLimitByDean;
    private Integer isLimitByManager;
    private Integer isLimitByLearn;
    private Integer isLimitByOwner;
}
