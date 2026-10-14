package com.megatron.arenahub.service;

import com.megatron.arenahub.dto.ImpostazioniRichiesta;
import com.megatron.arenahub.dto.ImpostazioniRisposta;
import com.megatron.arenahub.exception.ApiException;
import com.megatron.arenahub.model.ImpostazioniSito;
import com.megatron.arenahub.repository.ImpostazioniSitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ImpostazioniService {

    private static final int MAX_TORNEI_DEFAULT = 3;

    @Autowired
    private ImpostazioniSitoRepository impostazioniRepository;

    private ImpostazioniRisposta aRisposta(ImpostazioniSito i) {
        return new ImpostazioniRisposta(i.getId(), i.getMaxTorneiAttiviUtente(), i.getAggiornatoIl());
    }

    // Le impostazioni sono una sola riga; se la tabella è vuota la creo con i valori di default
    private ImpostazioniSito corrente() {
        ImpostazioniSito impostazioni = impostazioniRepository.findFirstByOrderByIdAsc();
        if (impostazioni == null) {
            impostazioni = new ImpostazioniSito();
            impostazioni.setMaxTorneiAttiviUtente(MAX_TORNEI_DEFAULT);
            impostazioni.setAggiornatoIl(LocalDateTime.now());
            impostazioni = impostazioniRepository.save(impostazioni);
        }
        return impostazioni;
    }

    public ImpostazioniRisposta leggi() {
        return aRisposta(corrente());
    }

    public ImpostazioniRisposta aggiorna(ImpostazioniRichiesta r) {
        if (r.getMaxTorneiAttiviUtente() == null || r.getMaxTorneiAttiviUtente() < 1) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "max_tornei_attivi_utente deve essere un intero maggiore di 0");
        }
        ImpostazioniSito impostazioni = corrente();
        impostazioni.setMaxTorneiAttiviUtente(r.getMaxTorneiAttiviUtente());
        impostazioni.setAggiornatoIl(LocalDateTime.now());
        return aRisposta(impostazioniRepository.save(impostazioni));
    }
}
