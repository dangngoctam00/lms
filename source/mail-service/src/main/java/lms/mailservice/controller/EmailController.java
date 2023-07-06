package lms.mailservice.controller;

import lms.mailservice.dto.Person;
import lms.mailservice.dto.PersonDto;
import lms.mailservice.dto.SendEmail;
import lms.mailservice.dto.SendEmailDto;
import lms.mailservice.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendEmail(@RequestBody SendEmailDto dto) {
        SendEmail sendEmail = new SendEmail();
        sendEmail.setFrom(mapPerson(dto.getFrom()));
        sendEmail.setTo(dto.getTo().stream().map(this::mapPerson).collect(Collectors.toList()));
        sendEmail.setSubject(dto.getSubject());
        sendEmail.setContent(dto.getContent());
        sendEmail.setAttachments(dto.getAttachments());
        emailService.sendEmail(sendEmail);
        log.info("Send email successfully");
        return ResponseEntity.ok().build();
    }

    private Person mapPerson(PersonDto dto) {
        Person p = new Person();
        p.setName(dto.getName());
        p.setAddress(dto.getAddress());
        return p;
    }
}
