package com.megatron.arenahub.service;

import com.megatron.arenahub.model.Torneo;
import com.megatron.arenahub.model.Utente;
import com.megatron.arenahub.model.UtenteTorneo;
import com.megatron.arenahub.repository.UtenteTorneoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IscrizioneService {

    @Autowired
    private UtenteTorneoRepository utenteTorneoRepository;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private TorneoService torneoService;

    public UtenteTorneo iscriviUtenteATorneo(Long utenteId, Long torneoId) {
        Utente utente = utenteService.cercaPerId(utenteId);
        Torneo torneo = torneoService.cercaPerId(torneoId);

        if (utente != null && torneo != null) {
            // Controlla se l'iscrizione esiste già
            UtenteTorneo giaIscritto = utenteTorneoRepository.findByUtenteIdAndTorneoId(utenteId, torneoId);
            if (giaIscritto == null) {
                UtenteTorneo nuovaIscrizione = new UtenteTorneo(utente, torneo);
                return utenteTorneoRepository.save(nuovaIscrizione);
            }
            return giaIscritto;
        }
        return null;
    }

    public List<UtenteTorneo> ottieniIscrittiPerTorneo(Long torneoId) {
        return utenteTorneoRepository.findByTorneoId(torneoId);
    }

    public boolean rimuoviIscrizione(Long utenteId, Long torneoId) {
        UtenteTorneo iscrizione = utenteTorneoRepository.findByUtenteIdAndTorneoId(utenteId, torneoId);
        if (iscrizione != null) {
            utenteTorneoRepository.delete(iscrizione);
            return true;
        }
        return false;
    }
}
