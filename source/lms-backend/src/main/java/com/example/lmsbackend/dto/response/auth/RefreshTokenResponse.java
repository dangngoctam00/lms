package com.example.lmsbackend.dto.response.auth;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RefreshTokenResponse extends BaseResponse {
    private String token;
    private String refreshToken;
}
