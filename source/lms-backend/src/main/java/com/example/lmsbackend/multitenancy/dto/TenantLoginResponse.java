package com.example.lmsbackend.multitenancy.dto;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TenantLoginResponse extends BaseResponse {
    private String token;
    private String refreshToken;
    private UserDto user;
    private String tenant;
}
