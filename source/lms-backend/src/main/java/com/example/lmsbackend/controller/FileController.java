package com.example.lmsbackend.controller;

import com.example.lmsbackend.dto.request.file.CreateShareFileTokenRequest;
import com.example.lmsbackend.dto.request.file.DecodeShareFileTokenRequest;
import com.example.lmsbackend.dto.response.file.CreateShareFileTokenResponse;
import com.example.lmsbackend.dto.response.file.DecodeShareFileTokenResponse;
import com.example.lmsbackend.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final FileService fileService;

    @PostMapping("files/createShareFileToken")
    public ResponseEntity<CreateShareFileTokenResponse> createShareFileToken(@RequestBody CreateShareFileTokenRequest request){
        String token = fileService.createShareFileToken(request);
        CreateShareFileTokenResponse response = new CreateShareFileTokenResponse();
        response.setToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("files/decodeShareFileToken")
    public ResponseEntity<DecodeShareFileTokenResponse> decodeShareFileToken(@RequestBody DecodeShareFileTokenRequest request){
        List<String> filePaths = fileService.decodeShareFileToken(request);
        DecodeShareFileTokenResponse response = new DecodeShareFileTokenResponse();
        response.setFullpaths(filePaths);
        return ResponseEntity.ok(response);
    }
}
