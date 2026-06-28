package com.dca.chat.stomp;

import com.dca.chat.dto.ChatMessageRequest;
import com.dca.chat.dto.ErrorResponse;
import com.dca.chat.exception.ChannelNotFoundException;
import com.dca.chat.exception.UserNotFoundException;
import com.dca.chat.service.ChatService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.stereotype.Controller;

import java.util.stream.Collectors;

@Controller
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("chat.send")
    public void handleIncomingMessage(@Valid ChatMessageRequest request) {
        chatService.processIncomingMessage(request);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public ErrorResponse handleValidationFailure(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Rejected invalid chat.send payload: {}", message);
        return new ErrorResponse(message);
    }

    @MessageExceptionHandler({ChannelNotFoundException.class, UserNotFoundException.class})
    @SendToUser("/queue/errors")
    public ErrorResponse handleLookupFailure(RuntimeException ex) {
        log.warn("Rejected chat.send: {}", ex.getMessage());
        return new ErrorResponse(ex.getMessage());
    }
}