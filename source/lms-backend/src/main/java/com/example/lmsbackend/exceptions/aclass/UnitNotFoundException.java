package com.example.lmsbackend.exceptions.aclass;

public class UnitNotFoundException extends RuntimeException {
    private Long unitId;

    public UnitNotFoundException(Long unitId) {
        this.unitId = unitId;
    }

    @Override
    public String getMessage() {
        return String.format("Unit %s not found", unitId);
    }
}
