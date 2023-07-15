package com.example.lmsbackend.dto.response.user;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class GetLimitPermissionOfUserResponse extends BaseResponse {
    private List<LimitPermissionOfUserDTO> permissions;
}
