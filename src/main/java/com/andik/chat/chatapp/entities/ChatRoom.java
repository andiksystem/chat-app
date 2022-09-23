package com.andik.chat.chatapp.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class ChatRoom implements Serializable {
    @Id
    private String id;
    private String chatId;
    private String senderId;
    private String recipientId;
}
