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

@Service
public class TorneoService {

    private static final Set<String> STATI = Set.of("bozza", "aperto", "chiuso", "in_corso", "concluso");

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
                g != null ? g.getId() : null, g != null ? g.getNomeGiochi() : null, t.getUrlImg(),
                t.getDataOra(), t.getDataInizioIscrizioni(), t.getDataFineIscrizioni(),
                t.getPostiMax(), t.getStato(), postiOccupati);
    }

    public List<TorneoRisposta> elenca(String gioco, String stato, LocalDate dataDa, LocalDate dataA, String q,
                                       boolean admin, Integer page, Integer limit) {
        List<TorneoRisposta> risultato = torneoRepository.findAll(Sort.by("dataOra")).stream()
                .filter(t -> admin || !"bozza".equals(t.getStato()))
                .filter(t -> gioco == null || (t.getGioco() != null && gioco.equalsIgnoreCase(t.getGioco().getNomeGiochi())))
                .filter(t -> stato == null || stato.equals(t.getStato()))
                .filter(t -> dataDa == null || (t.getDataOra() != null && !t.getDataOra().toLocalDate().isBefore(dataDa)))
                .filter(t -> dataA == null || (t.getDataOra() != null && !t.getDataOra().toLocalDate().isAfter(dataA)))
                .filter(t -> q == null || (t.getNomeTorneo() != null && t.getNomeTorneo().toLowerCase().contains(q.toLowerCase())))
                .map(t -> aRisposta(t, null))
                .toList();
        return Paginazione.applica(risultato, page, limit);
    }

    // Tutti i tornei di un gioco (usato da GET /api/gioco/{id})
    public List<TorneoRisposta> perGioco(Long idGiochi, boolean admin) {
        return torneoRepository.findByGiocoIdOrderByDataOraAsc(idGiochi).stream()
                .filter(t -> admin || !"bozza".equals(t.getStato()))
                .map(t -> aRisposta(t, null))
                .toList();
    }

    public Torneo cercaPerId(Long id) {
        return torneoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Torneo non trovato"));
    }

    public TorneoRisposta dettaglio(Long id, boolean admin) {
        Torneo torneo = cercaPerId(id);
        if (!admin && "bozza".equals(torneo.getStato())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Torneo non trovato");
        }
        return aRisposta(torneo, (int) utenteTorneoRepository.countByTorneoIdAndStato(id, "confermato"));
    }

    public TorneoRisposta crea(TorneoRichiesta r) {
        if (Validazione.mancante(r.nomeTorneo()) || r.dataOra() == null || r.dataInizioIscrizioni() == null
                || r.dataFineIscrizioni() == null || r.postiMax() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Campi obbligatori: nome_torneo, gioco, data_ora, data_inizio_iscrizioni, data_fine_iscrizioni, posti_max");
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
        if (r.postiMax() != null && confermati > r.postiMax()) {
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
        if (r.nomeTorneo() != null) {
            if (r.nomeTorneo().isBlank()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "nome_torneo non valido");
            }
            t.setNomeTorneo(r.nomeTorneo());
        }
        if (r.descrizione() != null) {
            t.setDescrizione(r.descrizione());
        }
        if (r.idGiochi() != null || r.gioco() != null || t.getGioco() == null) {
            t.setGioco(giocoService.trovaOCrea(r.idGiochi(), r.gioco()));
        }
        if (r.dataOra() != null) {
            t.setDataOra(r.dataOra());
        }
        if (r.dataInizioIscrizioni() != null) {
            t.setDataInizioIscrizioni(r.dataInizioIscrizioni());
        }
        if (r.dataFineIscrizioni() != null) {
            t.setDataFineIscrizioni(r.dataFineIscrizioni());
        }
        if (r.postiMax() != null) {
            if (r.postiMax() <= 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "posti_max deve essere maggiore di 0");
            }
            t.setPostiMax(r.postiMax());
        }
        if (r.stato() != null) {
            if (!STATI.contains(r.stato())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Stato non valido (bozza, aperto, chiuso, in_corso, concluso)");
            }
            t.setStato(r.stato());
        }
        if (r.urlImg() != null) {
            t.setUrlImg(r.urlImg());
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
