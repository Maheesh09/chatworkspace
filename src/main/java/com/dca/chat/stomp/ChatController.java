package com.dca.chat.stomp;

import com.dca.chat.dto.ChatMessageRequest;
import com.dca.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("chat.send")
    public void handleIncomingMessage(ChatMessageRequest request) {
        chatService.processIncomingMessage(request);
    }
}