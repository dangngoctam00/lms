package com.example.lmsbackend.service;

import com.example.lmsbackend.config.security.aop.Auth;
import com.example.lmsbackend.enums.PermissionEnum;
import org.springframework.stereotype.Service;

@Service
public class TestAuthService {

    @Auth(permission = PermissionEnum.VIEW_DETAIL_COURSE)
    public String testAuth(int resourceId) {
        return "Hello";
    }

    @Auth(permission = PermissionEnum.VIEW_DETAIL_COURSE)
    public String testAuth() {
        return "Hello";
    }
}
