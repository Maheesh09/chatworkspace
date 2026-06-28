package com.dca.chat.dto;

import java.time.Instant;

public record ChatMessageEvent(Long id, Long channelId, Long senderId, String content, Instant createdAt) {
}