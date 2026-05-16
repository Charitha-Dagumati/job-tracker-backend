package com.jobtracker.backend.controller;

import com.jobtracker.backend.entity.User;
import com.jobtracker.backend.repository.UserRepository;
import com.jobtracker.backend.util.JwtUtil;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {

        Optional<User> existingUser =
                userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            return "User already exists";
        }

        userRepository.save(user);
        return "Registration Successful";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        Optional<User> dbUser =
                userRepository.findByUsername(user.getUsername());

        if (dbUser.isPresent() &&
                dbUser.get().getPassword().equals(user.getPassword())) {

            return JwtUtil.generateToken(user.getUsername());
        }

        return "Invalid Credentials";
    }
}