package com.megatron.arenahub.repository;

import com.megatron.arenahub.model.UtenteTorneo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UtenteTorneoRepository extends JpaRepository<UtenteTorneo, Long> {
    
    // Trova tutte le iscrizioni di un torneo
    List<UtenteTorneo> findByTorneoId(Long torneoId);
    
    // Cerca l'iscrizione singola di un utente a un determinato torneo
    UtenteTorneo findByUtenteIdAndTorneoId(Long utenteId, Long torneoId);
}
