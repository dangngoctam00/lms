package lms.mailservice.service;

import lms.mailservice.dto.AttachmentDto;
import lms.mailservice.dto.Person;
import lms.mailservice.dto.SendEmail;
import lms.mailservice.exception.SendEmailError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(SendEmail sendEmail) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.displayName());
            List<String> attachments = sendEmail.getAttachments();
            if (CollectionUtils.isNotEmpty(attachments)) {
                helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.displayName());
            }
            helper.setSubject(sendEmail.getSubject());
            prepareBody(helper, sendEmail.getContent());
            helper.setFrom(toInterNetAddressForEmail(sendEmail.getFrom()));
            helper.setTo(sendEmail.getTo()
                    .stream()
                    .map(EmailService::toInterNetAddressForEmail)
                    .toArray(InternetAddress[]::new));
            addAttachments(sendEmail, helper);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new SendEmailError(e);
        }
    }

    private void addAttachments(SendEmail email, MimeMessageHelper helper)
            throws MessagingException {
        if (CollectionUtils.isNotEmpty(email.getAttachments())) {
            // TODO: implement get attachments from repository
            List<AttachmentDto> attachments = List.of();
            for (AttachmentDto attachment : attachments) {
                helper.addAttachment(attachment.getName(),
                        new ByteArrayResource(attachment.getContent().array()), attachment.getContentType());
            }
        }
    }

    private static InternetAddress toInterNetAddressForEmail(Person contact) {
        try {
            if (StringUtils.isNotBlank(contact.getName())) {
                return new InternetAddress(contact.getAddress(), contact.getName(), StandardCharsets.UTF_8.displayName());
            } else {
                return new InternetAddress(contact.getAddress());
            }
        } catch (UnsupportedEncodingException | AddressException e) {
            throw new SendEmailError(e);
        }
    }

    private void prepareBody(MimeMessageHelper helper, String body) throws MessagingException {
        String messageType = "text/html; charset=utf-8";
        if (helper.isMultipart()) {
            var mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, messageType);
            helper.getMimeMultipart().addBodyPart(mimeBodyPart);
        } else {
            helper.getMimeMessage().setContent(body, messageType);
        }
    }
}
