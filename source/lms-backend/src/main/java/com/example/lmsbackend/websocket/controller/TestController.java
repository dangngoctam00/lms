package com.example.lmsbackend.websocket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_PREFIX)
@Slf4j
public class TestController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/test")
    public String testMessage(@RequestBody String message) {
        log.info("receive message: {}", message);
        simpMessagingTemplate.convertAndSend("/topic/hello", "hello everybody :))");
        simpMessagingTemplate.convertAndSend("/topic.hi", "hello hmm :))");
        simpMessagingTemplate.convertAndSend("/hmm.abc.bcd", "hello hmm :))");
        return "Ok";
    }
}
