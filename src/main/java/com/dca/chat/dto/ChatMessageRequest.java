package com.dca.chat.dto;

public record ChatMessageRequest(Long channelId, Long senderId, String content) {
}