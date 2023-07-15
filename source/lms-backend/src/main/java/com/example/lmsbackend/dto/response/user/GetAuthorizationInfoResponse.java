package com.example.lmsbackend.dto.response.user;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetAuthorizationInfoResponse extends BaseResponse {
    private Long userId;
    private List<RoleOfUserDTO> roles = new ArrayList<>();
    private List<PermissionOfUserDTO> permissions = new ArrayList<>();
}
