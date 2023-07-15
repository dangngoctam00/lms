package com.example.lmsbackend.dto.response.role;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.Data;

@Data
public class CreateRoleResponse extends BaseResponse {
    private Long roleId;
}
