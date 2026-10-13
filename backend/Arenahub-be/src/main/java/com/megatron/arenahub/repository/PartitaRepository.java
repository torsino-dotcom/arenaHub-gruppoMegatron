package com.megatron.arenahub.repository;

import com.megatron.arenahub.model.Partita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PartitaRepository extends JpaRepository<Partita, Long> {
    
    // Trova le partite collegate a un determinato torneo
    List<Partita> findByTorneoId(Long torneoId);
}
