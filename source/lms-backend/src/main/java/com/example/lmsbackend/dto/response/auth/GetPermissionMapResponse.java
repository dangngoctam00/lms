package com.example.lmsbackend.dto.response.auth;

import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.enums.PermissionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetPermissionMapResponse extends BaseResponse {
    private Map<PermissionEnum, Boolean> permissionMap;
}
