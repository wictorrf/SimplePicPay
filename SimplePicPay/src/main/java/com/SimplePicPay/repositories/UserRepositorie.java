package com.SimplePicPay.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SimplePicPay.domain.user.User;

public interface UserRepositorie extends JpaRepository<User, Long>{
    Optional<User> findUserByDocument(String document);
    Optional<User> findUserById(Long id);
}
