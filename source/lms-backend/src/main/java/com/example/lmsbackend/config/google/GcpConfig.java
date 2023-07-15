package com.example.lmsbackend.config.google;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "gcp")
public class GcpConfig {
    private String projectId;
    private DnsConfig dns;
}
