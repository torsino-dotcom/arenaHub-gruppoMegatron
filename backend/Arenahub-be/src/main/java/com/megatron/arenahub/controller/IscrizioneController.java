package com.megatron.arenahub.controller;

import com.megatron.arenahub.dto.IscrizioneModificaRichiesta;
import com.megatron.arenahub.dto.IscrizioneRisposta;
import com.megatron.arenahub.model.Utente;
import com.megatron.arenahub.service.IscrizioneService;
import com.megatron.arenahub.service.SessioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:8000")
@RequestMapping("/api/tornei/{id_tornei}/iscrizioni")
public class IscrizioneController {

    @Autowired
    private IscrizioneService iscrizioneService;

    @Autowired
    private SessioneService sessioneService;

    // 11. Iscrizioni di un torneo (admin, come nel riepilogo di api2.md)
    @GetMapping
    public Map<String, List<IscrizioneRisposta>> getIscrittiTorneo(@PathVariable("id_tornei") Long idTornei,
                                                                   @RequestParam(required = false) String stato,
                                                                   @RequestParam(required = false) Integer page,
                                                                   @RequestParam(required = false) Integer limit) {
        sessioneService.richiediAdmin();
        return Map.of("iscrizioni", iscrizioneService.elencoPerTorneo(idTornei, stato, page, limit));
    }

    // 12. Iscriversi a un torneo (autenticato)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IscrizioneRisposta iscriviUtente(@PathVariable("id_tornei") Long idTornei) {
        Utente utente = sessioneService.richiediLogin();
        return iscrizioneService.iscrivi(utente, idTornei);
    }

    // 13. Annullare la propria iscrizione (autenticato)
    @DeleteMapping
    public Map<String, String> annullaIscrizione(@PathVariable("id_tornei") Long idTornei) {
        Utente utente = sessioneService.richiediLogin();
        iscrizioneService.annullaPropria(utente, idTornei);
        return Map.of("messaggio", "Iscrizione annullata");
    }

    // 14. Modifica stato di un'iscrizione (admin)
    @PatchMapping("/{id_utenti_tornei}")
    public IscrizioneRisposta modificaStato(@PathVariable("id_tornei") Long idTornei,
                                            @PathVariable("id_utenti_tornei") Long idUtentiTornei,
                                            @RequestBody IscrizioneModificaRichiesta richiesta) {
        sessioneService.richiediAdmin();
        return iscrizioneService.modificaStato(idTornei, idUtentiTornei, richiesta.getStato());
    }

    // 15. Rimuovere un'iscrizione (admin)
    @DeleteMapping("/{id_utenti_tornei}")
    public Map<String, String> rimuoviIscrizione(@PathVariable("id_tornei") Long idTornei,
                                                 @PathVariable("id_utenti_tornei") Long idUtentiTornei) {
        sessioneService.richiediAdmin();
        iscrizioneService.rimuovi(idTornei, idUtentiTornei);
        return Map.of("messaggio", "Iscrizione rimossa");
    }
}
