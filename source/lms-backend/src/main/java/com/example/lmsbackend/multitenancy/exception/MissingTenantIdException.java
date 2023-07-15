package com.example.lmsbackend.multitenancy.exception;

public class MissingTenantIdException extends RuntimeException {

    @Override
    public String getMessage() {
        return  "Missing X-TENANT-ID property in header";
    }
}
