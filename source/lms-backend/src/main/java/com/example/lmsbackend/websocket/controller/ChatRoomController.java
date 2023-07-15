package com.example.lmsbackend.websocket.controller;

import com.example.lmsbackend.dto.chat.CreateChatRoomRequest;
import com.example.lmsbackend.dto.chat.RecentChatList;
import com.example.lmsbackend.service.ChatRoomService;
import com.example.lmsbackend.service.ChatRoomUserService;
import com.example.lmsbackend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatRoomUserService chatRoomUserService;
    private final ChatService chatService;

    @PostMapping("/chat/room")
    public ResponseEntity<String> createChatRoom(@RequestBody CreateChatRoomRequest request) {
        chatRoomService.createChatRoom(request);
        return ResponseEntity.ok("Oke");
    }

    @GetMapping("/chat")
    public ResponseEntity<RecentChatList> getChatRoom(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {

        return ResponseEntity.ok(chatRoomService.getRecentChatRoom(page, size));
    }

}
