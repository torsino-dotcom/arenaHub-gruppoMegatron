package com.megatron.arenahub.repository;

import com.megatron.arenahub.model.ImpostazioniSito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImpostazioniSitoRepository extends JpaRepository<ImpostazioniSito, Long> {

    // Le impostazioni sono una sola riga: prendo la prima
    ImpostazioniSito findFirstByOrderByIdAsc();
}
