package com.example.lmsbackend.multitenancy.controller;

import com.example.lmsbackend.multitenancy.dto.GetPackagesResponse;
import com.example.lmsbackend.multitenancy.dto.PackageDTO;
import com.example.lmsbackend.multitenancy.service.PackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PackageController {
    private final PackageService packageService;

    @GetMapping(API_PREFIX + "/packages")
    public ResponseEntity<GetPackagesResponse> getAllPackages(){
        List<PackageDTO> packages = packageService.getAllPackages();
        GetPackagesResponse response = new GetPackagesResponse();
        response.setPackages(packages);
        return ResponseEntity.ok(response);
    }
}
