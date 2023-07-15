package com.example.lmsbackend.client.google;

import com.example.lmsbackend.config.google.GoogleApiConfig;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.auth.http.HttpCredentialsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleCalendarClient {

    private final GoogleApiConfig config;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Bean
    public Calendar getUpStreamCalendarService() {
        // Build a new authorized API client service.
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(config.getGoogleCredential());
            // * Set up the calendar service
            return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
