package com.andik.chat.chatapp.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.andik.chat.chatapp.entities.ChatMessage;
import com.andik.chat.chatapp.models.ChatNotification;
import com.andik.chat.chatapp.services.ChatMessageService;
import com.andik.chat.chatapp.services.ChatRoomService;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private ChatRoomService chatRoomService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        Optional<String> chatId = chatRoomService.getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(),
                true);
        chatMessage.setChatId(chatId.get());
        chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),
                "/queue/messages",
                new ChatNotification(
                        chatMessage.getChatId(),
                        chatMessage.getSenderId(),
                        chatMessage.getSenderName()));
    }

}
