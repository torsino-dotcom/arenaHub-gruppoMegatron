package com.megatron.arenahub.service;

import com.megatron.arenahub.dto.IscrizioneRisposta;
import com.megatron.arenahub.dto.IscrizioneUtenteRisposta;
import com.megatron.arenahub.exception.ApiException;
import com.megatron.arenahub.model.Torneo;
import com.megatron.arenahub.model.Utente;
import com.megatron.arenahub.model.UtenteTorneo;
import com.megatron.arenahub.repository.UtenteTorneoRepository;
import com.megatron.arenahub.util.Paginazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IscrizioneService {

    // Stati salvati nel database
    private static final Set<String> STATI = Set.of("confermato", "in_coda");

    @Autowired
    private UtenteTorneoRepository utenteTorneoRepository;

    @Autowired
    private TorneoService torneoService;

    private String normalizza(String stato) {
        return stato == null ? null : stato.trim().toLowerCase();
    }

    // Risposta base (senza username/nome/cognome)
    private IscrizioneRisposta aRisposta(UtenteTorneo i) {
        return new IscrizioneRisposta(i.getId(), i.getUtente().getId(), i.getTorneo().getId(),
                i.getDataIscrizione(), i.getStato(), null, null, null);
    }

    // Risposta per l'elenco (solo admin): con username, nome e cognome
    private IscrizioneRisposta aRispostaElenco(UtenteTorneo i) {
        Utente u = i.getUtente();
        return new IscrizioneRisposta(i.getId(), u.getId(), i.getTorneo().getId(), i.getDataIscrizione(),
                i.getStato(), u.getUsername(), u.getNome(), u.getCognome());
    }

    public List<IscrizioneRisposta> elencoPerTorneo(Long idTornei, String stato, Integer page, Integer limit) {
        torneoService.cercaPerId(idTornei);
        String statoFiltro = normalizza(stato);
        if (statoFiltro != null && !STATI.contains(statoFiltro)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Stato non valido (confermato, in_coda)");
        }
        List<IscrizioneRisposta> risultato = utenteTorneoRepository.findByTorneoIdOrderByDataIscrizioneAscIdAsc(idTornei).stream()
                .filter(i -> statoFiltro == null || statoFiltro.equals(i.getStato()))
                .map(this::aRispostaElenco)
                .collect(Collectors.toList());
        return Paginazione.applica(risultato, page, limit);
    }

    public List<IscrizioneUtenteRisposta> elencoPerUtente(Utente utente, Integer page, Integer limit) {
        List<IscrizioneUtenteRisposta> risultato = utenteTorneoRepository.findByUtenteId(utente.getId()).stream()
                .sorted(Comparator.comparing((UtenteTorneo i) -> i.getTorneo().getDataOra(),
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .map(i -> new IscrizioneUtenteRisposta(i.getId(), i.getTorneo().getId(), i.getDataIscrizione(),
                        i.getStato(), i.getTorneo().getNomeTorneo(),
                        i.getTorneo().getGioco() != null ? i.getTorneo().getGioco().getId() : null,
                        i.getTorneo().getDataOra()))
                .collect(Collectors.toList());
        return Paginazione.applica(risultato, page, limit);
    }

    @Transactional
    public IscrizioneRisposta iscrivi(Utente utente, Long idTornei) {
        Torneo torneo = torneoService.cercaPerId(idTornei);

        LocalDate oggi = LocalDate.now();
        if (!"aperto".equals(torneo.getStato())
                || oggi.isBefore(torneo.getDataInizioIscrizioni())
                || oggi.isAfter(torneo.getDataFineIscrizioni())) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "Le iscrizioni a questo torneo non sono aperte");
        }
        if (utenteTorneoRepository.findByUtenteIdAndTorneoId(utente.getId(), idTornei) != null) {
            throw new ApiException(HttpStatus.CONFLICT, "Utente già iscritto a questo torneo");
        }

        // Se i posti sono finiti si va in coda
        long confermati = utenteTorneoRepository.countByTorneoIdAndStato(idTornei, "confermato");
        UtenteTorneo iscrizione = new UtenteTorneo(utente, torneo);
        iscrizione.setStato(confermati >= torneo.getPostiMax() ? "in_coda" : "confermato");
        iscrizione.setDataIscrizione(oggi);
        return aRisposta(utenteTorneoRepository.save(iscrizione));
    }

    // L'annullamento cancella l'iscrizione (gli stati salvati sono solo confermato e in_coda)
    @Transactional
    public void annullaPropria(Utente utente, Long idTornei) {
        UtenteTorneo iscrizione = utenteTorneoRepository.findByUtenteIdAndTorneoId(utente.getId(), idTornei);
        if (iscrizione == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Iscrizione non trovata");
        }
        eliminaEPromuovi(iscrizione);
    }

    // Admin: stato "confermato" o "in_coda"; "annullato" cancella l'iscrizione
    @Transactional
    public IscrizioneRisposta modificaStato(Long idTornei, Long idUtentiTornei, String stato) {
        String nuovoStato = normalizza(stato);
        if (nuovoStato == null || !(STATI.contains(nuovoStato) || "annullato".equals(nuovoStato))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Stato non valido (confermato, in_coda, annullato)");
        }
        UtenteTorneo iscrizione = trovaInTorneo(idTornei, idUtentiTornei);

        if ("annullato".equals(nuovoStato)) {
            IscrizioneRisposta risposta = new IscrizioneRisposta(iscrizione.getId(), iscrizione.getUtente().getId(),
                    idTornei, iscrizione.getDataIscrizione(), "annullato", null, null, null);
            eliminaEPromuovi(iscrizione);
            return risposta;
        }

        Torneo torneo = iscrizione.getTorneo();
        String vecchioStato = iscrizione.getStato();
        if ("confermato".equals(nuovoStato) && !"confermato".equals(vecchioStato)
                && utenteTorneoRepository.countByTorneoIdAndStato(idTornei, "confermato") >= torneo.getPostiMax()) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "Posti esauriti (posti_max raggiunto)");
        }

        iscrizione.setStato(nuovoStato);
        utenteTorneoRepository.save(iscrizione);
        if ("confermato".equals(vecchioStato) && !"confermato".equals(nuovoStato)) {
            promuoviPrimoInCoda(torneo);
        }
        return aRisposta(iscrizione);
    }

    @Transactional
    public void rimuovi(Long idTornei, Long idUtentiTornei) {
        eliminaEPromuovi(trovaInTorneo(idTornei, idUtentiTornei));
    }

    private UtenteTorneo trovaInTorneo(Long idTornei, Long idUtentiTornei) {
        UtenteTorneo iscrizione = utenteTorneoRepository.findById(idUtentiTornei).orElse(null);
        if (iscrizione == null || !iscrizione.getTorneo().getId().equals(idTornei)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Iscrizione non trovata");
        }
        return iscrizione;
    }

    // Cancella l'iscrizione; se era confermata e si libera un posto, avanza la coda
    private void eliminaEPromuovi(UtenteTorneo iscrizione) {
        boolean liberaPosto = "confermato".equals(iscrizione.getStato());
        Torneo torneo = iscrizione.getTorneo();
        utenteTorneoRepository.delete(iscrizione);
        if (liberaPosto) {
            promuoviPrimoInCoda(torneo);
        }
    }

    // Se c'è un posto libero, la prima iscrizione in coda (per data) passa a confermato
    private void promuoviPrimoInCoda(Torneo torneo) {
        long confermati = utenteTorneoRepository.countByTorneoIdAndStato(torneo.getId(), "confermato");
        if (confermati >= torneo.getPostiMax()) {
            return;
        }
        List<UtenteTorneo> inCoda = utenteTorneoRepository
                .findByTorneoIdAndStatoOrderByDataIscrizioneAscIdAsc(torneo.getId(), "in_coda");
        if (!inCoda.isEmpty()) {
            UtenteTorneo primo = inCoda.get(0);
            primo.setStato("confermato");
            utenteTorneoRepository.save(primo);
        }
    }
}
