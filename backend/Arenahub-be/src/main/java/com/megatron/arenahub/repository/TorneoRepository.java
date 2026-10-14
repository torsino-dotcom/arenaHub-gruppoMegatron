package com.megatron.arenahub.repository;

import com.megatron.arenahub.model.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {

    // Trova i tornei di un determinato gioco
    List<Torneo> findByGiocoIdOrderByDataOraAsc(Long giocoId);
}
