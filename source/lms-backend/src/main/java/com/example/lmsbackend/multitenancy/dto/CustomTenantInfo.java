package com.example.lmsbackend.multitenancy.dto;

import lombok.Data;

@Data
public class CustomTenantInfo {
    private String tenantId;
    private String name;
    private String logo;
    private String banner;
}
