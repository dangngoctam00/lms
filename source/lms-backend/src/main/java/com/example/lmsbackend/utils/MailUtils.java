package com.example.lmsbackend.utils;

import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailUtils {

    private final JavaMailSender javaMailSender;
    private final FileUtils fileUtils;
    private final EmailService emailService;

    @Async
    public void sendMail(String from, String to, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setText(content, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(new InternetAddress("bklms.education@gmail.com",from));
            javaMailSender.send(message);
        } catch (MessagingException e) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("bklms.education@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            javaMailSender.send(message);
        } catch (UnsupportedEncodingException e) {
            log.warn("Mail is sent failed {}", e.getMessage());
        }
    }

    @Async
    public void sendMail(String from, String to, String subject, String content, FileDto file) {
        try {
            MimeMessagePreparator preparator = (mimeMessage) -> {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.addAttachment(file.getName(), file.getBytes(), fileUtils.getContentType(file.getExtension()));
                helper.setFrom(new InternetAddress("bklms.education@gmail.com", from));
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(content, true);
            };
            javaMailSender.send(preparator);
            log.info("Send mail to {} successfully", to);
        } catch (MailException exception) {
            log.warn("Mail is sent failed {}", exception.getMessage());
        }
    }

    @Async
    public void sendMailWithTemplate(String from, String to, String subject, Map<String, Object> content, String template) {
        try {
            var email = DefaultEmail.builder()
                    .from(new InternetAddress("bklms.education@gmail.com", from))
                    .to(List.of(new InternetAddress(to, to)))
                    .subject(subject)
                    .body("")
                    .encoding("UTF-8")
                    .build();
            emailService.send(email, template, content);
        } catch (UnsupportedEncodingException | CannotSendEmailException e) {
            e.printStackTrace();
        }
    }
}
