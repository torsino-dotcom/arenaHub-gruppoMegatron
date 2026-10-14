package com.megatron.arenahub.service;

import com.megatron.arenahub.dto.PartitaRichiesta;
import com.megatron.arenahub.dto.PartitaRisposta;
import com.megatron.arenahub.exception.ApiException;
import com.megatron.arenahub.model.Partita;
import com.megatron.arenahub.model.Torneo;
import com.megatron.arenahub.model.Utente;
import com.megatron.arenahub.repository.PartitaRepository;
import com.megatron.arenahub.util.Paginazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class PartitaService {

    private static final Set<String> STATI = Set.of("programmata", "in_corso", "conclusa");

    @Autowired
    private PartitaRepository partitaRepository;

    @Autowired
    private TorneoService torneoService;

    @Autowired
    private UtenteService utenteService;

    private String nomeCompleto(Utente u) {
        return u == null ? null : u.getNome() + " " + u.getCognome();
    }

    public PartitaRisposta aRisposta(Partita p) {
        return new PartitaRisposta(p.getId(), p.getTorneo().getId(),
                p.getGiocatore1() != null ? p.getGiocatore1().getId() : null,
                p.getGiocatore2() != null ? p.getGiocatore2().getId() : null,
                nomeCompleto(p.getGiocatore1()), nomeCompleto(p.getGiocatore2()),
                p.getOrarioInizio(), p.getPunteggioGiocatore1(), p.getPunteggioGiocatore2(),
                p.getPersonaVincitrice(), p.getFormatoMatch(), p.getStato(), p.getFase());
    }

    public Partita cercaPerId(Long id) {
        return partitaRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Partita non trovata"));
    }

    public List<PartitaRisposta> elencoPerTorneo(Long idTornei, String fase, String stato, Integer page, Integer limit) {
        torneoService.cercaPerId(idTornei);
        if (stato != null && !STATI.contains(stato)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Stato non valido (programmata, in_corso, conclusa)");
        }
        List<PartitaRisposta> risultato = partitaRepository.findByTorneoId(idTornei).stream()
                .filter(p -> fase == null || fase.equalsIgnoreCase(p.getFase()))
                .filter(p -> stato == null || stato.equals(p.getStato()))
                .sorted(Comparator.comparing(Partita::getOrarioInizio, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(this::aRisposta)
                .toList();
        return Paginazione.applica(risultato, page, limit);
    }

    public PartitaRisposta dettaglio(Long id) {
        return aRisposta(cercaPerId(id));
    }

    public PartitaRisposta crea(Long idTornei, PartitaRichiesta r) {
        Torneo torneo = torneoService.cercaPerId(idTornei);
        if (r.idUtenti1() == null || r.idUtenti2() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Campi obbligatori: id_utenti1, id_utenti2");
        }
        Partita partita = new Partita();
        partita.setTorneo(torneo);
        partita.setGiocatore1(utenteService.cercaPerId(r.idUtenti1()));
        partita.setGiocatore2(utenteService.cercaPerId(r.idUtenti2()));
        applicaCampi(partita, r);
        validaPartita(partita);
        return aRisposta(partitaRepository.save(partita));
    }

    public PartitaRisposta modifica(Long id, PartitaRichiesta r) {
        Partita partita = cercaPerId(id);
        if (r.idUtenti1() != null) {
            partita.setGiocatore1(utenteService.cercaPerId(r.idUtenti1()));
        }
        if (r.idUtenti2() != null) {
            partita.setGiocatore2(utenteService.cercaPerId(r.idUtenti2()));
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
        if (r.orarioInizio() != null) {
            p.setOrarioInizio(r.orarioInizio());
        }
        if (r.punteggio1() != null) {
            if (r.punteggio1() < 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "punteggio_1 non può essere negativo");
            }
            p.setPunteggioGiocatore1(r.punteggio1());
        }
        if (r.punteggio2() != null) {
            if (r.punteggio2() < 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "punteggio_2 non può essere negativo");
            }
            p.setPunteggioGiocatore2(r.punteggio2());
        }
        if (r.personaVincitrice() != null) {
            p.setPersonaVincitrice(r.personaVincitrice());
        }
        if (r.formatoMatch() != null) {
            p.setFormatoMatch(r.formatoMatch());
        }
        if (r.stato() != null) {
            if (!STATI.contains(r.stato())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Stato non valido (programmata, in_corso, conclusa)");
            }
            p.setStato(r.stato());
        }
        if (r.fase() != null) {
            p.setFase(r.fase());
        }
    }

    // Regole di business sullo stato finale della partita
    private void validaPartita(Partita p) {
        if (p.getGiocatore1().getId().equals(p.getGiocatore2().getId())) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "I due giocatori devono essere diversi");
        }
        String vincitrice = p.getPersonaVincitrice();
        if (vincitrice != null && !vincitrice.equals(nomeCompleto(p.getGiocatore1()))
                && !vincitrice.equals(nomeCompleto(p.getGiocatore2()))) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "persona_vincitrice deve coincidere con uno dei due giocatori");
        }
    }
}
