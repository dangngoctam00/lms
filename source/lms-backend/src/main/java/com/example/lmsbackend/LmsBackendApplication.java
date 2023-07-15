package com.example.lmsbackend;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = ActiveMQAutoConfiguration.class)
@EnableAsync
@EnableEmailTools
public class LmsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmsBackendApplication.class, args);
    }

}
