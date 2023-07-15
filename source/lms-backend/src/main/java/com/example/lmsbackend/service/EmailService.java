package com.example.lmsbackend.service;

import com.example.lmsbackend.dto.email.SendEmailDto;
import com.example.lmsbackend.utils.FileUtils;
import com.example.lmsbackend.utils.MailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final MailUtils mailUtils;
    private final UtilsService utilsService;
    private final FileUtils fileUtils;

    public void sendEmailToUser(String emailAddress, SendEmailDto dto) {
        var tenantName = utilsService.getTenantName();
        var file = fileUtils.getFileFromFirebase(dto.getAttachment());
        mailUtils.sendMail(tenantName, emailAddress, dto.getSubject(), dto.getContent(), file);
    }
}
