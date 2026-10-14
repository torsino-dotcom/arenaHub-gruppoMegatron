package com.megatron.arenahub.repository;

import com.megatron.arenahub.model.UtenteTorneo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UtenteTorneoRepository extends JpaRepository<UtenteTorneo, Long> {

    // Tutte le iscrizioni di un torneo, in ordine di iscrizione
    List<UtenteTorneo> findByTorneoIdOrderByDataIscrizioneAscIdAsc(Long torneoId);

    // Iscrizioni di un torneo con un certo stato (es. coda di attesa)
    List<UtenteTorneo> findByTorneoIdAndStatoOrderByDataIscrizioneAscIdAsc(Long torneoId, String stato);

    // Tutte le iscrizioni di un utente
    List<UtenteTorneo> findByUtenteId(Long utenteId);

    // Cerca l'iscrizione singola di un utente a un determinato torneo
    UtenteTorneo findByUtenteIdAndTorneoId(Long utenteId, Long torneoId);

    // Conta le iscrizioni di un torneo con un certo stato
    long countByTorneoIdAndStato(Long torneoId, String stato);

    void deleteByTorneoId(Long torneoId);
    void deleteByUtenteId(Long utenteId);
}
