package com.example.lmsbackend.controller;

import com.example.lmsbackend.dto.request.breadcrumb.GetBreadcrumbTitleRequest;
import com.example.lmsbackend.dto.response.breadcrumb.GetBreadcrumbTitleResponse;
import com.example.lmsbackend.service.BreadcrumbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class BreadcrumbController {
    private final BreadcrumbService breadcrumbService;

    @PostMapping("/breadcrumb")
    public ResponseEntity<GetBreadcrumbTitleResponse> getBreadcrumbTitle(@RequestBody GetBreadcrumbTitleRequest request){
        String title = breadcrumbService.getBreadcrumbTitle(request);
        GetBreadcrumbTitleResponse response = new GetBreadcrumbTitleResponse();
        response.setTitle(title);
        return ResponseEntity.ok(response);
    }
}
