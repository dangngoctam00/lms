package com.example.lmsbackend.dto.request.staff;

import lombok.Data;

@Data
public class AllocationAccountRequest {
    private Long staffId;
    private String username;
    private String password;
}
