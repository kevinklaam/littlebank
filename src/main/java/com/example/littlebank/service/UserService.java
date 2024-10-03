package com.example.littlebank.service;

import com.example.littlebank.model.User;
import com.example.littlebank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String password) {
        System.out.println("Checking if user exists: " + username);
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setBalance(BigDecimal.ZERO);
        User savedUser = userRepository.save(newUser);
        System.out.println("New user added: " + savedUser.getUsername());
        System.out.println("New user added: " + savedUser.getPassword());
        return savedUser;
    }
}