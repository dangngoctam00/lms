package com.example.lmsbackend.multitenancy.controller;

import com.example.lmsbackend.config.AppConfig;
import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.multitenancy.dto.*;
import com.example.lmsbackend.multitenancy.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TenantController {

    private final TenantService tenantService;
    private final AppConfig appConfig;

    @GetMapping(API_PREFIX + "/tenant/{verifyCode}")
    public ResponseEntity<Void> verifyEmail(@PathVariable(name = "verifyCode") String verifyCode) {
        String domain = tenantService.createTenant(verifyCode);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://" + domain + "." + appConfig.getDomain())).build();
    }

    @PostMapping(API_PREFIX + "/tenant/register")
    public ResponseEntity<CreateTenantResponse> registerDomain(@RequestBody TenantDto dto) {
        String domain = tenantService.createTenantCache(dto);
        return ResponseEntity.ok(new CreateTenantResponse(domain));
    }

    @PostMapping(API_PREFIX + "/tenant/login")
    public ResponseEntity<TenantLoginResponse> login(@RequestBody LoginRequest request) {
        TenantLoginResponse response = tenantService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(API_PREFIX + "/tenant/expire")
    public ResponseEntity<GetExpireTimeResponse> getExpireTime(){
        LocalDateTime expireTime = tenantService.getExpireTime();
        GetExpireTimeResponse response = new GetExpireTimeResponse();
        response.setExpireTime(expireTime);
        return ResponseEntity.ok(response);
    }

    @PostMapping(API_PREFIX + "/tenant/customize")
    public ResponseEntity<BaseResponse> customTenant(@RequestBody CustomTenantInfo customTenantRequest){
        tenantService.customTenant(customTenantRequest);
        return ResponseEntity.ok(new BaseResponse());
    }

    @GetMapping(API_PREFIX + "/tenant/customizeInfo")
    public ResponseEntity<CustomTenantInfo> getCustomTenantInfo(@RequestParam String tenantId) {
        CustomTenantInfo customTenantInfo = tenantService.getCustomTenantInfo(tenantId);
        return ResponseEntity.ok(customTenantInfo);
    }

}
