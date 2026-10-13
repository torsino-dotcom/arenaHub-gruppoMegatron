package com.megatron.arenahub.repository;

import com.megatron.arenahub.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    
    // Utile per il Login e la registrazione
    Utente findByEmailAndPassword(String email, String password);
    Utente findByEmail(String email);
}
