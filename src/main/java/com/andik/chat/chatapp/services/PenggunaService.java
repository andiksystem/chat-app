package com.andik.chat.chatapp.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andik.chat.chatapp.entities.Pengguna;
import com.andik.chat.chatapp.repositories.PenggunaRepository;

@Service
public class PenggunaService {

    @Autowired
    private PenggunaRepository penggunaRepository;

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

}
