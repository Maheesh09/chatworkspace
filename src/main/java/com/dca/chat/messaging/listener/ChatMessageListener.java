package com.dca.chat.messaging.listener;

import com.dca.chat.dto.ChatMessageEvent;
import com.dca.chat.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageListener {

    private static final Logger log = LoggerFactory.getLogger(ChatMessageListener.class);

    private final ChatService chatService;

    public ChatMessageListener(ChatService chatService) {
        this.chatService = chatService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.chat-messages}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeChatMessage(ChatMessageEvent event) {
        log.info("Consumed message {} from Kafka for channel {}", event.id(), event.channelId());
        chatService.broadcastToSubscribers(event);
    }
}