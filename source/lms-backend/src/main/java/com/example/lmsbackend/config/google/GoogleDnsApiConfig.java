package com.example.lmsbackend.config.google;

import com.example.lmsbackend.constant.GoogleApiConstant;
import com.google.api.services.dns.DnsScopes;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Configuration
public class GoogleDnsApiConfig {
    private static final List<String> SCOPES = Collections.singletonList(DnsScopes.CLOUD_PLATFORM);

    public GoogleCredentials getGoogleCredential() {
        try {
            ClassPathResource jsonResource = new ClassPathResource(GoogleApiConstant.DNS_API_CREDENTIAL_FILE);
            return GoogleCredentials.fromStream(jsonResource.getInputStream())
                    .createScoped(SCOPES);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }
}