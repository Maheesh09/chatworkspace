package com.dca.chat.stomp;

import com.dca.chat.dto.ChatMessageRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("chat.send")
    public void handleIncomingMessage(ChatMessageRequest request) {
        messagingTemplate.convertAndSend("/topic/messages", request);
    }
}