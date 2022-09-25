package com.andik.chat.chatapp.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResetPasswordRequest implements Serializable {
    private String email;
}
