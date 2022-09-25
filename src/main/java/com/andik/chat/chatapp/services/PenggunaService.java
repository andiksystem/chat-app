package com.andik.chat.chatapp.services;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.andik.chat.chatapp.entities.Pengguna;
import com.andik.chat.chatapp.exceptions.BadRequestException;
import com.andik.chat.chatapp.exceptions.ResourceNotFoundException;
import com.andik.chat.chatapp.repositories.PenggunaRepository;

@Service
public class PenggunaService {

    @Autowired
    private PenggunaRepository penggunaRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public Pengguna findByUsername(String username) {
        Optional<Pengguna> optional = penggunaRepository.findByUsername(username);
        if (optional.isPresent())
            return optional.get();

        return null;
    }

    public Pengguna create(Pengguna pengguna) {
        pengguna.setId(UUID.randomUUID().toString());
        return penggunaRepository.save(pengguna);
    }

    public Pengguna findByEmail(String email) {
        Optional<Pengguna> optional = penggunaRepository.findByEmail(email);
        if (optional.isPresent()) {
            return optional.get();
        }

        return null;
    }

    public void sendEmail(String email, String token) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Permintaan Reset Password");
        msg.setText("Untuk mereset password, silakan klik link berikut\n" +
                "http://localhost:3000/resetPassword?token=" + token);
        javaMailSender.send(msg);
    }

    public Pengguna resetPassword(String email) {
        Pengguna pengguna = findByEmail(email);
        if (pengguna == null) {
            throw new ResourceNotFoundException("Pengguna tidak ditemukan");
        }

        final String token = UUID.randomUUID().toString();
        sendEmail(email, token);
        pengguna.setChangePasswordToken(token);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        pengguna.setChangePasswordExpriredAt(calendar.getTime());
        pengguna.setTokenUsed(false);
        return penggunaRepository.save(pengguna);
    }

    public Pengguna changePassword(String username, String token, String newEncodedPassword) {
        Pengguna pengguna = findByUsername(username);
        if (pengguna == null) {
            throw new ResourceNotFoundException("Pengguna tidak ditemukan");
        }

        if (pengguna.getChangePasswordToken() == null) {
            throw new BadRequestException("Pengguna tidak sedang request reset password");
        }

        if (!pengguna.getChangePasswordToken().equals(token)) {
            throw new BadRequestException("Token tidak sesuai");
        }

        Date sekarang = new Date();
        if (pengguna.getChangePasswordExpriredAt().equals(sekarang)
                || pengguna.getChangePasswordExpriredAt().before(sekarang)) {
            throw new BadRequestException("Token sudah expired");
        }

        if (Objects.equals(pengguna.getTokenUsed(), true)) {
            throw new BadRequestException("Token sudah digunakan");
        }

        pengguna.setPassword(newEncodedPassword);
        pengguna.setTokenUsed(true);
        return penggunaRepository.save(pengguna);

    }

}
