package com.megatron.arenahub.repository;

import com.megatron.arenahub.model.Partita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PartitaRepository extends JpaRepository<Partita, Long> {

    // Trova le partite collegate a un determinato torneo
    List<Partita> findByTorneoId(Long torneoId);

    void deleteByTorneoId(Long torneoId);

    // Quante partite ha un utente (come giocatore 1 o 2)
    @Query("select count(p) from Partita p where p.giocatore1.id = :id or p.giocatore2.id = :id")
    long contaPartiteDiUtente(@Param("id") Long id);
}
