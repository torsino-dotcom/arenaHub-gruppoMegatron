package com.megatron.arenahub.service;

import com.megatron.arenahub.dto.PartitaRichiesta;
import com.megatron.arenahub.dto.PartitaRisposta;
import com.megatron.arenahub.exception.ApiException;
import com.megatron.arenahub.model.Partita;
import com.megatron.arenahub.model.Torneo;
import com.megatron.arenahub.repository.PartitaRepository;
import com.megatron.arenahub.util.Paginazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PartitaService {

    private static final Set<String> STATI = Set.of("programmata", "in_corso", "conclusa");

    @Autowired
    private PartitaRepository partitaRepository;

    @Autowired
    private TorneoService torneoService;

    @Autowired
    private UtenteService utenteService;

    public PartitaRisposta aRisposta(Partita p) {
        return new PartitaRisposta(p.getId(), p.getTorneo().getId(),
                p.getGiocatore1() != null ? p.getGiocatore1().getId() : null,
                p.getGiocatore2() != null ? p.getGiocatore2().getId() : null,
                p.getOrarioInizio(), p.getPunteggioGiocatore1(), p.getPunteggioGiocatore2(),
                p.getIdPersonaVincitrice(), p.getFormatoMatch(), p.getStato(), p.getFase());
    }

    public Partita cercaPerId(Long id) {
        return partitaRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Partita non trovata"));
    }

    public List<PartitaRisposta> elencoPerTorneo(Long idTornei, String fase, String stato, Integer page, Integer limit) {
        torneoService.cercaPerId(idTornei);
        String statoFiltro = stato == null ? null : stato.trim().toLowerCase();
        if (statoFiltro != null && !STATI.contains(statoFiltro)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Stato non valido (programmata, in_corso, conclusa)");
        }
        List<PartitaRisposta> risultato = partitaRepository.findByTorneoId(idTornei).stream()
                .filter(p -> fase == null || fase.equalsIgnoreCase(p.getFase()))
                .filter(p -> statoFiltro == null || statoFiltro.equals(p.getStato()))
                .sorted(Comparator.comparing(Partita::getOrarioInizio, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(this::aRisposta)
                .collect(Collectors.toList());
        return Paginazione.applica(risultato, page, limit);
    }

    public PartitaRisposta dettaglio(Long id) {
        return aRisposta(cercaPerId(id));
    }

    public PartitaRisposta crea(Long idTornei, PartitaRichiesta r) {
        Torneo torneo = torneoService.cercaPerId(idTornei);
        if (r.getIdUtenti1() == null || r.getIdUtenti2() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Campi obbligatori: id_utenti1, id_utenti2");
        }
        Partita partita = new Partita();
        partita.setTorneo(torneo);
        partita.setGiocatore1(utenteService.cercaPerId(r.getIdUtenti1()));
        partita.setGiocatore2(utenteService.cercaPerId(r.getIdUtenti2()));
        applicaCampi(partita, r);
        validaPartita(partita);
        return aRisposta(partitaRepository.save(partita));
    }

    public PartitaRisposta modifica(Long id, PartitaRichiesta r) {
        Partita partita = cercaPerId(id);
        if (r.getIdUtenti1() != null) {
            partita.setGiocatore1(utenteService.cercaPerId(r.getIdUtenti1()));
        }
        if (r.getIdUtenti2() != null) {
            partita.setGiocatore2(utenteService.cercaPerId(r.getIdUtenti2()));
        }
        applicaCampi(partita, r);
        validaPartita(partita);
        return aRisposta(partitaRepository.save(partita));
    }

    public void elimina(Long id) {
        partitaRepository.delete(cercaPerId(id));
    }

    // Copia sulla partita i soli campi presenti nella richiesta
    private void applicaCampi(Partita p, PartitaRichiesta r) {
        if (r.getOrarioInizio() != null) {
            p.setOrarioInizio(r.getOrarioInizio());
        }
        if (r.getPunteggio1() != null) {
            if (r.getPunteggio1() < 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "punteggio_1 non può essere negativo");
            }
            p.setPunteggioGiocatore1(r.getPunteggio1());
        }
        if (r.getPunteggio2() != null) {
            if (r.getPunteggio2() < 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "punteggio_2 non può essere negativo");
            }
            p.setPunteggioGiocatore2(r.getPunteggio2());
        }
        if (r.getIdPersonaVincitrice() != null) {
            p.setIdPersonaVincitrice(r.getIdPersonaVincitrice());
        }
        if (r.getFormatoMatch() != null) {
            p.setFormatoMatch(r.getFormatoMatch());
        }
        if (r.getStato() != null) {
            String stato = r.getStato().trim().toLowerCase();
            if (!STATI.contains(stato)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Stato non valido (programmata, in_corso, conclusa)");
            }
            p.setStato(stato);
        }
        if (r.getFase() != null) {
            p.setFase(r.getFase());
        }
    }

    // Regole di business sullo stato finale della partita
    private void validaPartita(Partita p) {
        Long id1 = p.getGiocatore1().getId();
        Long id2 = p.getGiocatore2().getId();
        if (id1.equals(id2)) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "I due giocatori devono essere diversi");
        }
        Long vincitore = p.getIdPersonaVincitrice();
        if (vincitore != null && !vincitore.equals(id1) && !vincitore.equals(id2)) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "id_persona_vincitrice deve coincidere con id_utenti1 o id_utenti2");
        }
    }
}
