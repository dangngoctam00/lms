package com.example.lmsbackend.dto.response.auth;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetAllPermissionIdsResponse extends BaseResponse {
    private List<Integer> permissions;
}
