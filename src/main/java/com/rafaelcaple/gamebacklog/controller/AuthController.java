package com.rafaelcaple.gamebacklog.controller;

import com.rafaelcaple.gamebacklog.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    public record AuthRequest (String username,String password){}

    @PostMapping("/register")
    public String register (@RequestBody AuthRequest request) {
        return authService.register(request.username(), request.password());
    }

    @PostMapping("/login")
    public String login (@RequestBody AuthRequest request) {
        return authService.login(request.username(), request.password());
    }

}
