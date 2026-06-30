package com.dca.chat.service;

import com.dca.chat.domain.entity.Channel;
import com.dca.chat.domain.entity.Message;
import com.dca.chat.domain.entity.User;
import com.dca.chat.dto.ChatMessageEvent;
import com.dca.chat.dto.ChatMessageRequest;
import com.dca.chat.exception.ChannelNotFoundException;
import com.dca.chat.exception.UserNotFoundException;
import com.dca.chat.repository.ChannelRepository;
import com.dca.chat.repository.MessageRepository;
import com.dca.chat.repository.UserRepository;
import com.dca.chat.security.StompPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);
    private static final String CHANNEL_TOPIC_TEMPLATE = "/topic/channels/%d/messages";

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(ChannelRepository channelRepository,
                       UserRepository userRepository,
                       MessageRepository messageRepository,
                       SimpMessagingTemplate messagingTemplate) {
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void processIncomingMessage(ChatMessageRequest request, Principal principal) {
        Long senderId = ((StompPrincipal) principal).getUserId();
        Message savedMessage = saveMessage(request, senderId);
        broadcastMessage(savedMessage);
    }

    private Message saveMessage(ChatMessageRequest request, Long senderId) {
        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(request.channelId()));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException(senderId));

        Message message = new Message(request.content(), channel, sender);
        return messageRepository.save(message);
    }

    private void broadcastMessage(Message message) {
        ChatMessageEvent event = new ChatMessageEvent(
                message.getId(),
                message.getChannel().getId(),
                message.getSender().getId(),
                message.getContent(),
                message.getCreatedAt()
        );

        String destination = String.format(CHANNEL_TOPIC_TEMPLATE, event.channelId());
        messagingTemplate.convertAndSend(destination, event);

        log.info("Message {} saved and broadcast to channel {}", event.id(), event.channelId());
    }
}