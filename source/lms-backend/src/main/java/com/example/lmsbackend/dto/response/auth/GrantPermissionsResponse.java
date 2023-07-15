package com.example.lmsbackend.dto.response.auth;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.Data;

@Data
public class GrantPermissionsResponse extends BaseResponse {
    private Long userId;
}
