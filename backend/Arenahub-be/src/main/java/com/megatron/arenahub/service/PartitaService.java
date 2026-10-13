package com.megatron.arenahub.service;

import com.megatron.arenahub.model.Partita;
import com.megatron.arenahub.model.Torneo;
import com.megatron.arenahub.model.Utente;
import com.megatron.arenahub.repository.PartitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartitaService {

    @Autowired
    private PartitaRepository partitaRepository;

    @Autowired
    private TorneoService torneoService;

    @Autowired
    private UtenteService utenteService;

    public Partita creaPartitaPerId(Long torneoId, Long giocatore1Id, Long giocatore2Id) {
        Torneo torneo = torneoService.cercaPerId(torneoId);
        Utente g1 = utenteService.cercaPerId(giocatore1Id);
        Utente g2 = utenteService.cercaPerId(giocatore2Id);

        if (torneo != null && g1 != null && g2 != null) {
            Partita p = new Partita();
            p.setTorneo(torneo);
            p.setGiocatore1(g1);
            p.setGiocatore2(g2);
            return partitaRepository.save(p);
        }
        return null;
    }

    public Partita salvaRisultato(Long partitaId, int puntiG1, int puntiG2) {
        if (partitaRepository.existsById(partitaId)) {
            Partita p = partitaRepository.findById(partitaId).get();
            p.setPunteggioGiocatore1(puntiG1);
            p.setPunteggioGiocatore2(puntiG2);
            p.setStato("COMPLETATA");
            return partitaRepository.save(p);
        }
        return null;
    }

    public List<Partita> cercaPartitePerTorneo(Long torneoId) {
        return partitaRepository.findByTorneoId(torneoId);
    }
}
