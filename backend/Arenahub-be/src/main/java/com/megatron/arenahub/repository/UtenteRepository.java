package com.megatron.arenahub.repository;

import com.megatron.arenahub.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    // Utili per il login (con email) e per il controllo duplicati
    Utente findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
