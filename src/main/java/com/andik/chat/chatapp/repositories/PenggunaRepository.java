package com.andik.chat.chatapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andik.chat.chatapp.entities.Pengguna;

public interface PenggunaRepository extends JpaRepository<Pengguna, String> {

    Optional<Pengguna> findByUsername(String username);

}
