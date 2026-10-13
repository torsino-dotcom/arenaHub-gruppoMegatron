package com.megatron.arenahub.service;

import com.megatron.arenahub.model.Utente;
import com.megatron.arenahub.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UtenteRepository utenteRepository;

    public Utente registra(Utente utente) {
        // Evita due utenti con la stessa email
        if (utenteRepository.findByEmail(utente.getEmail()) != null) {
            return null;
        }
        utente.setRuolo("UTENTE");
        return utenteRepository.save(utente);
    }

    public Utente login(String email, String password) {
        return utenteRepository.findByEmailAndPassword(email, password);
    }
}
