package com.example.lmsbackend.controller;

import com.example.lmsbackend.dto.request.role.CreateRoleRequest;
import com.example.lmsbackend.dto.request.role.UpdateRoleRequest;
import com.example.lmsbackend.dto.response.role.*;
import com.example.lmsbackend.dto.response.user.GetRolesOfUserResponse;
import com.example.lmsbackend.dto.response.user.RoleOfUserDTO;
import com.example.lmsbackend.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@Slf4j
@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("myRoles")
    public ResponseEntity<GetRolesOfUserResponse> getMyRole(){
        List<RoleOfUserDTO> roles = roleService.getMyRoles();
        GetRolesOfUserResponse response = new GetRolesOfUserResponse();
        response.setRoles(roles);
        return ResponseEntity.ok(response);
    }

    @GetMapping("roles/{roleId}")
    public ResponseEntity<GetRoleResponse> getRole(@PathVariable(name = "roleId") Long roleId){
        RolePermissionDTO role = roleService.getRole(roleId);
        GetRoleResponse response = new GetRoleResponse();
        response.setRole(role);
        return ResponseEntity.ok(response);
    }

    @GetMapping("roles")
    public ResponseEntity<GetAllRolesResponse> getAllRoles(){
        List<RoleDTO> roles = roleService.getAllRoles();
        GetAllRolesResponse response = new GetAllRolesResponse();
        response.setRoles(roles);
        return ResponseEntity.ok(response);
    }

    @PostMapping("roles")
    public ResponseEntity<CreateRoleResponse> createRole(@RequestBody CreateRoleRequest request){
        Long roleId = roleService.createRole(request);
        CreateRoleResponse response = new CreateRoleResponse();
        response.setRoleId(roleId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("roles")
    public ResponseEntity<UpdateRoleResponse> updateRole(@RequestBody UpdateRoleRequest request){
        Long roleId = roleService.updateRole(request, request.getRoleId());
        UpdateRoleResponse response = new UpdateRoleResponse();
        response.setRoleId(roleId);
        return  ResponseEntity.ok(response);
    }

    @DeleteMapping("roles/{roleId}")
    public ResponseEntity<UpdateRoleResponse> deleteRole(@PathVariable(name = "roleId") Long roleId){
        Long id = roleService.deleteRole(roleId);
        UpdateRoleResponse response = new UpdateRoleResponse();
        response.setRoleId(id);
        return  ResponseEntity.ok(response);
    }
}
