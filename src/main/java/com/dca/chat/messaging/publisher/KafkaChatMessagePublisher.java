package com.dca.chat.messaging.publisher;

import com.dca.chat.dto.ChatMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaChatMessagePublisher implements MessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaChatMessagePublisher.class);

    private final KafkaTemplate<String, ChatMessageEvent> kafkaTemplate;
    private final String topic;

    public KafkaChatMessagePublisher(
            KafkaTemplate<String, ChatMessageEvent> kafkaTemplate,
            @Value("${app.kafka.topics.chat-messages}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(ChatMessageEvent event) {
        kafkaTemplate.send(topic, String.valueOf(event.channelId()), event);
        log.info("Published message {} to topic {} (channel {})",
                event.id(), topic, event.channelId());
    }
}