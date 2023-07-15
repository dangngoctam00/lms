package com.example.lmsbackend.dto.response.role;

import lombok.Data;

import java.util.List;

@Data
public class GetAllRolesResponse {
    private List<RoleDTO> roles;

}
