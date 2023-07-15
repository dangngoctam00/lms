package com.example.lmsbackend.controller;

import com.example.lmsbackend.dto.response.user.GetAllPermissionsResponse;
import com.example.lmsbackend.dto.response.user.PermissionDTO;
import com.example.lmsbackend.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping("/permissions")
    public ResponseEntity<GetAllPermissionsResponse> getAllPermisions() {
        List<PermissionDTO> permissions = permissionService.getAllPermissions();
        GetAllPermissionsResponse response = new GetAllPermissionsResponse();
        response.setPermissions(permissions);
        return ResponseEntity.ok(response);
    }
}
