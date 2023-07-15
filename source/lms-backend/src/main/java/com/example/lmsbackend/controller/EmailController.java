package com.example.lmsbackend.controller;

import com.example.lmsbackend.dto.email.SendEmailDto;
import com.example.lmsbackend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("email/{emailAddress}")
    public ResponseEntity<?> sendEmail(@PathVariable String emailAddress, @RequestBody SendEmailDto dto) {
        emailService.sendEmailToUser(emailAddress, dto);
        return ResponseEntity.ok().build();
    }
}
