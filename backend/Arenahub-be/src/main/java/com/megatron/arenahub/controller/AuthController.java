package com.megatron.arenahub.controller;

import com.megatron.arenahub.dto.LoginRichiesta;
import com.megatron.arenahub.dto.RegistrazioneRichiesta;
import com.megatron.arenahub.dto.UtenteRisposta;
import com.megatron.arenahub.service.AuthService;
import com.megatron.arenahub.service.SessioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private SessioneService sessioneService;

    // 1. Registrazione (pubblico)
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UtenteRisposta registraUtente(@RequestBody RegistrazioneRichiesta richiesta) {
        return authService.registra(richiesta);
    }

    // 2. Login (pubblico)
    @PostMapping("/login")
    public UtenteRisposta login(@RequestBody LoginRichiesta richiesta) {
        return authService.login(richiesta);
    }

    // 3. Logout (autenticato)
    @PostMapping("/logout")
    public Map<String, String> logout() {
        sessioneService.richiediLogin();
        authService.logout();
        return Map.of("messaggio", "Logout effettuato");
    }
}
