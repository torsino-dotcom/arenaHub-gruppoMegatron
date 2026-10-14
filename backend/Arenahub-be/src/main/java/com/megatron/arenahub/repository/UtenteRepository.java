package com.megatron.arenahub.repository;

import com.megatron.arenahub.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    // Utili per login e controllo duplicati in registrazione
    Utente findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
