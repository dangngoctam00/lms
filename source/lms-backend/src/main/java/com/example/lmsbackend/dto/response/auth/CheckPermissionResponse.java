package com.example.lmsbackend.dto.response.auth;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.Data;

@Data
public class CheckPermissionResponse extends BaseResponse {
    private boolean hasPermission;
}
