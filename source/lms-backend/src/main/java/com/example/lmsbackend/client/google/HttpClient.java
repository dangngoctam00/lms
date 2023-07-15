//package com.example.lmsbackend.client.google;
//
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import org.springframework.http.HttpHeaders;
//
//
///*
//    Create calendar : https://www.googleapis.com/calendar/v3/calendars
// */
//
//@Component
//public class HttpClient {
//    private final WebClient client = WebClient.builder().baseUrl("https://www.googleapis.com/calendar/v3")
//            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//            .build();
//
//    public WebClient getWebClient() {
//        return client;
//    }
//}
