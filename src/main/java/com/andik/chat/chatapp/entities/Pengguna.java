package com.andik.chat.chatapp.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Pengguna implements Serializable {

    @Id
    private String id;
    private String username;
    private String password;
    private String authority;
    private String namaLengkap;

    private String email;
    private String changePasswordToken;
    private Date changePasswordExpriredAt;
    private Boolean tokenUsed;
}
