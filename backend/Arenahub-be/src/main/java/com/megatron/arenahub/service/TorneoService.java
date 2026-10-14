package com.megatron.arenahub.service;

import com.megatron.arenahub.dto.TorneoRichiesta;
import com.megatron.arenahub.dto.TorneoRisposta;
import com.megatron.arenahub.exception.ApiException;
import com.megatron.arenahub.model.Gioco;
import com.megatron.arenahub.model.Torneo;
import com.megatron.arenahub.repository.PartitaRepository;
import com.megatron.arenahub.repository.TorneoRepository;
import com.megatron.arenahub.repository.UtenteTorneoRepository;
import com.megatron.arenahub.util.Paginazione;
import com.megatron.arenahub.util.Validazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TorneoService {

    private static final Set<String> STATI = Set.of("aperto", "chiuso", "concluso");

    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private GiocoService giocoService;

    @Autowired
    private UtenteTorneoRepository utenteTorneoRepository;

    @Autowired
    private PartitaRepository partitaRepository;

    public TorneoRisposta aRisposta(Torneo t, Integer postiOccupati) {
        Gioco g = t.getGioco();
        return new TorneoRisposta(t.getId(), t.getNomeTorneo(), t.getDescrizione(),
                g != null ? g.getId() : null, t.getDataOra(), t.getDataInizioIscrizioni(),
                t.getDataFineIscrizioni(), t.getPostiMax(), t.getStato(), t.getUrlImg(), postiOccupati);
    }

    // Il filtro "gioco" accetta l'id del gioco oppure il suo nome
    private boolean corrispondeGioco(Torneo t, String gioco) {
        Gioco g = t.getGioco();
        if (g == null) {
            return false;
        }
        if (gioco.matches("\\d{1,18}")) {
            return g.getId().equals(Long.parseLong(gioco));
        }
        return gioco.equalsIgnoreCase(g.getNomeGiochi());
    }

    public List<TorneoRisposta> elenca(String gioco, String stato, LocalDate dataDa, LocalDate dataA, String q,
                                       Integer page, Integer limit) {
        List<TorneoRisposta> risultato = torneoRepository.findAll(Sort.by("dataOra")).stream()
                .filter(t -> gioco == null || corrispondeGioco(t, gioco))
                .filter(t -> stato == null || stato.equalsIgnoreCase(t.getStato()))
                .filter(t -> dataDa == null || (t.getDataOra() != null && !t.getDataOra().toLocalDate().isBefore(dataDa)))
                .filter(t -> dataA == null || (t.getDataOra() != null && !t.getDataOra().toLocalDate().isAfter(dataA)))
                .filter(t -> q == null || (t.getNomeTorneo() != null && t.getNomeTorneo().toLowerCase().contains(q.toLowerCase())))
                .map(t -> aRisposta(t, null))
                .collect(Collectors.toList());
        return Paginazione.applica(risultato, page, limit);
    }

    public Torneo cercaPerId(Long id) {
        return torneoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Torneo non trovato"));
    }

    public TorneoRisposta dettaglio(Long id) {
        Torneo torneo = cercaPerId(id);
        return aRisposta(torneo, (int) utenteTorneoRepository.countByTorneoIdAndStato(id, "confermato"));
    }

    public TorneoRisposta crea(TorneoRichiesta r) {
        if (Validazione.mancante(r.getNomeTorneo()) || r.getIdGiochi() == null || r.getDataOra() == null
                || r.getDataInizioIscrizioni() == null || r.getDataFineIscrizioni() == null || r.getPostiMax() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Campi obbligatori: nome_torneo, id_giochi, data_ora, data_inizio_iscrizioni, data_fine_iscrizioni, posti_max");
        }
        Torneo torneo = new Torneo();
        applicaCampi(torneo, r);
        validaDate(torneo);
        return aRisposta(torneoRepository.save(torneo), 0);
    }

    public TorneoRisposta modifica(Long id, TorneoRichiesta r) {
        Torneo torneo = cercaPerId(id);
        applicaCampi(torneo, r);
        validaDate(torneo);

        long confermati = utenteTorneoRepository.countByTorneoIdAndStato(id, "confermato");
        if (r.getPostiMax() != null && confermati > r.getPostiMax()) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "posti_max non può essere inferiore agli iscritti confermati (" + confermati + ")");
        }
        return aRisposta(torneoRepository.save(torneo), (int) confermati);
    }

    @Transactional
    public void elimina(Long id) {
        Torneo torneo = cercaPerId(id);
        // Cancellazione a cascata: iscrizioni e partite collegate
        utenteTorneoRepository.deleteByTorneoId(id);
        partitaRepository.deleteByTorneoId(id);
        torneoRepository.delete(torneo);
    }

    // Copia sul torneo i soli campi presenti nella richiesta (vale sia per POST che per PATCH)
    private void applicaCampi(Torneo t, TorneoRichiesta r) {
        if (r.getNomeTorneo() != null) {
            if (r.getNomeTorneo().isBlank()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "nome_torneo non valido");
            }
            t.setNomeTorneo(r.getNomeTorneo());
        }
        if (r.getDescrizione() != null) {
            t.setDescrizione(r.getDescrizione());
        }
        if (r.getIdGiochi() != null) {
            t.setGioco(giocoService.cercaPerId(r.getIdGiochi()));
        }
        if (r.getDataOra() != null) {
            t.setDataOra(r.getDataOra());
        }
        if (r.getDataInizioIscrizioni() != null) {
            t.setDataInizioIscrizioni(r.getDataInizioIscrizioni());
        }
        if (r.getDataFineIscrizioni() != null) {
            t.setDataFineIscrizioni(r.getDataFineIscrizioni());
        }
        if (r.getPostiMax() != null) {
            if (r.getPostiMax() <= 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "posti_max deve essere maggiore di 0");
            }
            t.setPostiMax(r.getPostiMax());
        }
        if (r.getStato() != null) {
            String stato = r.getStato().trim().toLowerCase();
            if (!STATI.contains(stato)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Stato non valido (aperto, chiuso, concluso)");
            }
            t.setStato(stato);
        }
        if (r.getUrlImg() != null) {
            t.setUrlImg(r.getUrlImg());
        }
    }

    // data_inizio_iscrizioni <= data_fine_iscrizioni <= data_ora
    private void validaDate(Torneo t) {
        if (t.getDataInizioIscrizioni().isAfter(t.getDataFineIscrizioni())
                || t.getDataFineIscrizioni().isAfter(t.getDataOra().toLocalDate())) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Date non valide: inizio iscrizioni <= fine iscrizioni <= data_ora");
        }
    }
}
