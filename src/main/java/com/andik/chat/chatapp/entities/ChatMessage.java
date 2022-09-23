package com.andik.chat.chatapp.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.andik.chat.chatapp.models.MessageStatus;

import lombok.Data;

@Data
@Entity
public class ChatMessage implements Serializable {
    @Id
    private String id;
    private String chatId;
    private String recipientId;
    private String recipientName;
    private String senderId;
    private String senderName;
    private String content;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

}
