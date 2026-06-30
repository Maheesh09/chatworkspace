package com.dca.chat.messaging.publisher;

import com.dca.chat.dto.ChatMessageEvent;

public interface MessagePublisher {

    void publish(ChatMessageEvent event);
}