package com.example.lmsbackend.multitenancy.exception;

public class TenantNotFoundException extends RuntimeException {
    private final String tenantId;

    public TenantNotFoundException(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String getMessage() {
        return String.format("Tenant: %s not found", tenantId);
    }
}
