package com.example.lmsbackend.dto.response.auth;

import com.example.lmsbackend.dto.request.auth.LimitPermission;
import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.enums.PermissionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginResponse extends BaseResponse {
    private String token;
    private String refreshToken;
    private UserDto user;
    private String tenant;
    private Map<PermissionEnum, LimitPermission> permissions;
}
