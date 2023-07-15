package com.example.lmsbackend.config.google;

import com.example.lmsbackend.constant.GoogleApiConstant;
import com.google.api.services.calendar.CalendarScopes;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Configuration
public class GoogleApiConfig {
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    public GoogleCredentials getGoogleCredential() {
        try {
            ClassPathResource jsonResource = new ClassPathResource(GoogleApiConstant.API_CREDENTIAL_FILE);
            return GoogleCredentials.fromStream(jsonResource.getInputStream())
                    .createScoped(SCOPES);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    public GoogleCredentials getFirebaseCredential() {
        try {
            ClassPathResource jsonResource = new ClassPathResource(GoogleApiConstant.FIREBASE_CREDENTIAL_FILE);
            return GoogleCredentials.fromStream(jsonResource.getInputStream());
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }
}


