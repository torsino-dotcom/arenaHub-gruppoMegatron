package com.megatron.arenahub.controller;

import com.megatron.arenahub.dto.PartitaRichiesta;
import com.megatron.arenahub.dto.PartitaRisposta;
import com.megatron.arenahub.service.PartitaService;
import com.megatron.arenahub.service.SessioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PartitaController {

    @Autowired
    private PartitaService partitaService;

    @Autowired
    private SessioneService sessioneService;

    // 21. Partite di un torneo (pubblico)
    @GetMapping("/tornei/{id_tornei}/partite")
    public List<PartitaRisposta> getPartitePerTorneo(@PathVariable("id_tornei") Long idTornei,
                                                     @RequestParam(required = false) String fase,
                                                     @RequestParam(required = false) String stato,
                                                     @RequestParam(required = false) Integer page,
                                                     @RequestParam(required = false) Integer limit) {
        return partitaService.elencoPerTorneo(idTornei, fase, stato, page, limit);
    }

    // 22. Dettaglio partita (pubblico)
    @GetMapping("/partite/{id_partite}")
    public PartitaRisposta getPartitaById(@PathVariable("id_partite") Long idPartite) {
        return partitaService.dettaglio(idPartite);
    }

    // 23. Creazione partita (admin)
    @PostMapping("/tornei/{id_tornei}/partite")
    @ResponseStatus(HttpStatus.CREATED)
    public PartitaRisposta creaPartita(@PathVariable("id_tornei") Long idTornei,
                                       @RequestBody PartitaRichiesta richiesta) {
        sessioneService.richiediAdmin();
        return partitaService.crea(idTornei, richiesta);
    }

    // 24. Aggiornamento risultato / stato (admin)
    @PatchMapping("/partite/{id_partite}")
    public PartitaRisposta modificaPartita(@PathVariable("id_partite") Long idPartite,
                                           @RequestBody PartitaRichiesta richiesta) {
        sessioneService.richiediAdmin();
        return partitaService.modifica(idPartite, richiesta);
    }

    // 25. Eliminazione partita (admin)
    @DeleteMapping("/partite/{id_partite}")
    public Map<String, String> eliminaPartita(@PathVariable("id_partite") Long idPartite) {
        sessioneService.richiediAdmin();
        partitaService.elimina(idPartite);
        return Map.of("messaggio", "Partita eliminata");
    }
}
