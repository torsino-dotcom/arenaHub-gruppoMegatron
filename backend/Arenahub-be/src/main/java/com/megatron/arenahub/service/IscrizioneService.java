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

@Service
public class IscrizioneService {

    private static final Set<String> STATI = Set.of("confermato", "in_attesa", "annullato");

    @Autowired
    private UtenteTorneoRepository utenteTorneoRepository;

    @Autowired
    private TorneoService torneoService;

    // Risposta base (senza username/nome/cognome)
    private IscrizioneRisposta aRisposta(UtenteTorneo i) {
        return new IscrizioneRisposta(i.getId(), i.getUtente().getId(), i.getTorneo().getId(),
                i.getDataIscrizione(), i.getStato(), null, null, null);
    }

    // Risposta per l'elenco: il pubblico vede solo lo username, l'admin anche nome e cognome
    private IscrizioneRisposta aRispostaElenco(UtenteTorneo i, boolean admin) {
        Utente u = i.getUtente();
        return new IscrizioneRisposta(i.getId(), u.getId(), i.getTorneo().getId(), i.getDataIscrizione(),
                i.getStato(), u.getUsername(), admin ? u.getNome() : null, admin ? u.getCognome() : null);
    }

    public List<IscrizioneRisposta> elencoPerTorneo(Long idTornei, String stato, boolean admin, Integer page, Integer limit) {
        torneoService.cercaPerId(idTornei);
        if (stato != null && !STATI.contains(stato)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Stato non valido (confermato, in_attesa, annullato)");
        }
        List<IscrizioneRisposta> risultato = utenteTorneoRepository.findByTorneoIdOrderByDataIscrizioneAscIdAsc(idTornei).stream()
                // senza filtro non mostro le iscrizioni annullate
                .filter(i -> stato == null ? !"annullato".equals(i.getStato()) : stato.equals(i.getStato()))
                .map(i -> aRispostaElenco(i, admin))
                .toList();
        return Paginazione.applica(risultato, page, limit);
    }

    public List<IscrizioneUtenteRisposta> elencoPerUtente(Utente utente, Integer page, Integer limit) {
        List<IscrizioneUtenteRisposta> risultato = utenteTorneoRepository.findByUtenteId(utente.getId()).stream()
                .filter(i -> !"annullato".equals(i.getStato()))
                .sorted(Comparator.comparing((UtenteTorneo i) -> i.getTorneo().getDataOra(),
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .map(i -> new IscrizioneUtenteRisposta(i.getId(), i.getTorneo().getId(), i.getDataIscrizione(),
                        i.getStato(), i.getTorneo().getNomeTorneo(),
                        i.getTorneo().getGioco() != null ? i.getTorneo().getGioco().getNomeGiochi() : null,
                        i.getTorneo().getDataOra()))
                .toList();
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

        UtenteTorneo iscrizione = utenteTorneoRepository.findByUtenteIdAndTorneoId(utente.getId(), idTornei);
        if (iscrizione != null && !"annullato".equals(iscrizione.getStato())) {
            throw new ApiException(HttpStatus.CONFLICT, "Utente già iscritto a questo torneo");
        }
        if (iscrizione == null) {
            iscrizione = new UtenteTorneo(utente, torneo);
        }

        // Se i posti sono finiti si va in coda
        long confermati = utenteTorneoRepository.countByTorneoIdAndStato(idTornei, "confermato");
        iscrizione.setStato(confermati >= torneo.getPostiMax() ? "in_attesa" : "confermato");
        iscrizione.setDataIscrizione(LocalDate.now());
        return aRisposta(utenteTorneoRepository.save(iscrizione));
    }

    @Transactional
    public void annullaPropria(Utente utente, Long idTornei) {
        UtenteTorneo iscrizione = utenteTorneoRepository.findByUtenteIdAndTorneoId(utente.getId(), idTornei);
        if (iscrizione == null || "annullato".equals(iscrizione.getStato())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Iscrizione non trovata");
        }
        boolean liberaPosto = "confermato".equals(iscrizione.getStato());
        iscrizione.setStato("annullato");
        utenteTorneoRepository.save(iscrizione);
        if (liberaPosto) {
            promuoviPrimaInAttesa(iscrizione.getTorneo());
        }
    }

    @Transactional
    public IscrizioneRisposta modificaStato(Long idTornei, Long idUtentiTornei, String nuovoStato) {
        if (nuovoStato == null || !STATI.contains(nuovoStato)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Stato non valido (confermato, in_attesa, annullato)");
        }
        UtenteTorneo iscrizione = trovaInTorneo(idTornei, idUtentiTornei);
        Torneo torneo = iscrizione.getTorneo();
        String vecchioStato = iscrizione.getStato();

        if ("confermato".equals(nuovoStato) && !"confermato".equals(vecchioStato)
                && utenteTorneoRepository.countByTorneoIdAndStato(idTornei, "confermato") >= torneo.getPostiMax()) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "Posti esauriti (posti_max raggiunto)");
        }

        iscrizione.setStato(nuovoStato);
        utenteTorneoRepository.save(iscrizione);
        if ("confermato".equals(vecchioStato) && !"confermato".equals(nuovoStato)) {
            promuoviPrimaInAttesa(torneo);
        }
        return aRisposta(iscrizione);
    }

    @Transactional
    public void rimuovi(Long idTornei, Long idUtentiTornei) {
        UtenteTorneo iscrizione = trovaInTorneo(idTornei, idUtentiTornei);
        boolean liberaPosto = "confermato".equals(iscrizione.getStato());
        Torneo torneo = iscrizione.getTorneo();
        utenteTorneoRepository.delete(iscrizione);
        if (liberaPosto) {
            promuoviPrimaInAttesa(torneo);
        }
    }

    private UtenteTorneo trovaInTorneo(Long idTornei, Long idUtentiTornei) {
        UtenteTorneo iscrizione = utenteTorneoRepository.findById(idUtentiTornei).orElse(null);
        if (iscrizione == null || !iscrizione.getTorneo().getId().equals(idTornei)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Iscrizione non trovata");
        }
        return iscrizione;
    }

    // Se c'è un posto libero, la prima iscrizione in attesa (per data) passa a confermato
    private void promuoviPrimaInAttesa(Torneo torneo) {
        long confermati = utenteTorneoRepository.countByTorneoIdAndStato(torneo.getId(), "confermato");
        if (confermati >= torneo.getPostiMax()) {
            return;
        }
        List<UtenteTorneo> inAttesa = utenteTorneoRepository
                .findByTorneoIdAndStatoOrderByDataIscrizioneAscIdAsc(torneo.getId(), "in_attesa");
        if (!inAttesa.isEmpty()) {
            UtenteTorneo primo = inAttesa.get(0);
            primo.setStato("confermato");
            utenteTorneoRepository.save(primo);
        }
    }
}
