package com.example.lmsbackend.controller;

import com.example.lmsbackend.config.security.aop.RuleFactory;
import com.example.lmsbackend.dto.request.auth.*;
import com.example.lmsbackend.dto.response.auth.*;
import com.example.lmsbackend.enums.StatusCode;
import com.example.lmsbackend.service.AuthService;
import com.example.lmsbackend.service.UserAuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@Slf4j
@RestController
@RequestMapping(API_PREFIX)
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserAuthService userAuthService;
    private final RuleFactory ruleFactory;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        loginResponse.setStatusCode(StatusCode.SUCCESS);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        RegisterResponse response = new RegisterResponse();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RequestTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("grantPermissions")
    public ResponseEntity<GrantPermissionsResponse> grantPermissions(@RequestBody GrantPermissionsRequest request) {
        Long userId = authService.grantPermissions(request);
        GrantPermissionsResponse response = new GrantPermissionsResponse();
        response.setUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkPermission")
    public ResponseEntity<CheckPermissionResponse> checkPermission(CheckPermissionRequest request){
        boolean hasPermission = authService.checkPermission(request);
        CheckPermissionResponse response = new CheckPermissionResponse();
        response.setHasPermission(hasPermission);
        return ResponseEntity.ok(response);
    }
}
