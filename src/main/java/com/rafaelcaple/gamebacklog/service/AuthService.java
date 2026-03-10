package com.rafaelcaple.gamebacklog.service;

import com.rafaelcaple.gamebacklog.entity.User;
import com.rafaelcaple.gamebacklog.repository.UserRepository;
import com.rafaelcaple.gamebacklog.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public String register (String username, String password) {
        if (userRepository.existsByUsername(username.toLowerCase().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username.toLowerCase().trim());
        user.setDisplayName(username.trim());
        user.setPassword(hashedPassword);
        user = userRepository.save(user);
        return jwtService.generateToken(user);
    }

    public String login (String username, String password) {
        User user = userRepository.findByUsername(username.toLowerCase().trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User or password invalid!"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User or password invalid!");
        }
        return jwtService.generateToken(user);
    }

}
