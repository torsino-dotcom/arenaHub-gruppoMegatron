package com.megatron.arenahub.repository;

import com.megatron.arenahub.model.Gioco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiocoRepository extends JpaRepository<Gioco, Long> {

    // Cerca un gioco per nome (senza distinguere maiuscole/minuscole)
    Gioco findByNomeGiochiIgnoreCase(String nomeGiochi);
}
