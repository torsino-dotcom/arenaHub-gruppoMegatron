package com.megatron.arenahub.controller;

import com.megatron.arenahub.model.Utente;
import com.megatron.arenahub.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public Utente registraUtente(@RequestBody Utente utente) {
        return authService.registra(utente);
    }

    @PostMapping("/login")
    public Utente login(@RequestParam String email, @RequestParam String password) {
        return authService.login(email, password);
    }
}
