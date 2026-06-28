package com.dca.chat.exception;

public class ChannelNotFoundException extends RuntimeException {
    public ChannelNotFoundException(Long channelId) {
        super("Channel not found: " + channelId);
    }
}