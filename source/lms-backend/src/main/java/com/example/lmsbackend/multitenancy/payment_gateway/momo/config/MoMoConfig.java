package com.example.lmsbackend.multitenancy.payment_gateway.momo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "payment.momo")
@Data
public class MoMoConfig {
    private String apiEndpoint;
    private String partnerCode;
    private String accessKey;
    private String secretKey;
}
