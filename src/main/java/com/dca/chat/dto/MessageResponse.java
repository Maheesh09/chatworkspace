package com.dca.chat.dto;

import java.time.Instant;

public record MessageResponse(
        Long id,
        Long channelId,
        Long senderId,
        String content,
        Instant createdAt
) {
}