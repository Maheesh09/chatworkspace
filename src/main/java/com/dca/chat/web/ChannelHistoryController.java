package com.dca.chat.web;

import com.dca.chat.dto.MessageResponse;
import com.dca.chat.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
public class ChannelHistoryController {

    private static final Logger log = LoggerFactory.getLogger(ChannelHistoryController.class);
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private final ChatService chatService;

    public ChannelHistoryController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/{channelId}/messages")
    public Page<MessageResponse> getChannelHistory(
            @PathVariable Long channelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        int clampedSize = Math.min(size, MAX_PAGE_SIZE);

        PageRequest pageable = PageRequest.of(
                page,
                clampedSize,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        log.info("History request for channel {} page {} size {}", channelId, page, clampedSize);
        return chatService.getChannelHistory(channelId, pageable);
    }
}
