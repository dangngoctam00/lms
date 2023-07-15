package com.example.lmsbackend.websocket.controller;

import com.example.lmsbackend.dto.chat.ActiveChat;
import com.example.lmsbackend.dto.chat.SendingMessageDto;
import com.example.lmsbackend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;
import static com.example.lmsbackend.websocket.utils.RequestUtils.extractTenantFromHeaderAndSetToContext;

@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat/send")
    public ResponseEntity<String> sendMessage(@RequestBody SendingMessageDto message, StompHeaderAccessor accessor) {
        extractTenantFromHeaderAndSetToContext(accessor);
        chatService.sendMessage(message);
        return ResponseEntity.ok("Oke");
    }

    @GetMapping("/chat/{chatRoomId}")
    public ResponseEntity<ActiveChat> getMessageByChatRoomId(@PathVariable("chatRoomId") Long chatRoomId,
                                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(chatService.getMessageByChatRoomId(chatRoomId, page, size));
    }

    @PostMapping("/chat/seen/{chatRoomId}")
    public ResponseEntity<String> seenMessage(@PathVariable("chatRoomId") Long chatRoomId) {
        chatService.seenMessage(chatRoomId);
        return ResponseEntity.ok("Oke");
    }

    @GetMapping("/chat/test")
    public void testCheckChatRoom(@RequestBody List<Long> users) {
        chatService.createChatRoomIfNotExist(users.get(0), users.get(1));
    }
}
