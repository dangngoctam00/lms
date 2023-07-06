package lms.mailservice;

import lms.commonlib.KafkaClient;
import lms.mailservice.dto.SendEmail;
import lms.mailservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("kafka")
@RequiredArgsConstructor
@Slf4j
public class MessageListener implements InitializingBean {

    private final KafkaClient kafkaClient;
    private final EmailService emailService;

    @Override
    public void afterPropertiesSet() {
        kafkaClient.subscribe("lms", SendEmail.class, specificRecordBase -> {
            emailService.sendEmail((SendEmail) specificRecordBase);
            log.info("Email sent");
        });
    }

    @PostMapping("test")
    public void test() {
        kafkaClient.subscribe("lms", SendEmail.class, specificRecordBase -> {
            log.info("Consume event");
            emailService.sendEmail((SendEmail) specificRecordBase);
        });
    }
}
