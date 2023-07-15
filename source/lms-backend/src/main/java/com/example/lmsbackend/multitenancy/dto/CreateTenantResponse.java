package com.example.lmsbackend.multitenancy.dto;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateTenantResponse extends BaseResponse {
    private String domain;
}
