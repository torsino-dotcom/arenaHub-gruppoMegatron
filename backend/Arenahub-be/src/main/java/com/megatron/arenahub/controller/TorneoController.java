package com.megatron.arenahub.controller;

import com.megatron.arenahub.dto.TorneoRichiesta;
import com.megatron.arenahub.dto.TorneoRisposta;
import com.megatron.arenahub.service.SessioneService;
import com.megatron.arenahub.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tornei")
public class TorneoController {

    @Autowired
    private TorneoService torneoService;

    @Autowired
    private SessioneService sessioneService;

    // 16. Elenco e filtri (pubblico). "gioco" può essere l'id o il nome del gioco
    @GetMapping
    public List<TorneoRisposta> listaTornei(
            @RequestParam(required = false) String gioco,
            @RequestParam(required = false) String stato,
            @RequestParam(name = "data_da", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataDa,
            @RequestParam(name = "data_a", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataA,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit) {
        return torneoService.elenca(gioco, stato, dataDa, dataA, q, page, limit);
    }

    // 17. Dettaglio (pubblico)
    @GetMapping("/{id_tornei}")
    public TorneoRisposta getTorneoById(@PathVariable("id_tornei") Long idTornei) {
        return torneoService.dettaglio(idTornei);
    }

    // 18. Creazione (admin)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TorneoRisposta creaTorneo(@RequestBody TorneoRichiesta richiesta) {
        sessioneService.richiediAdmin();
        return torneoService.crea(richiesta);
    }

    // 19. Modifica (admin)
    @PatchMapping("/{id_tornei}")
    public TorneoRisposta modificaTorneo(@PathVariable("id_tornei") Long idTornei,
                                         @RequestBody TorneoRichiesta richiesta) {
        sessioneService.richiediAdmin();
        return torneoService.modifica(idTornei, richiesta);
    }

    // 20. Eliminazione (admin)
    @DeleteMapping("/{id_tornei}")
    public Map<String, String> eliminaTorneo(@PathVariable("id_tornei") Long idTornei) {
        sessioneService.richiediAdmin();
        torneoService.elimina(idTornei);
        return Map.of("messaggio", "Torneo eliminato");
    }
}
