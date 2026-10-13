package com.megatron.arenahub.controller;

import com.megatron.arenahub.model.Partita;
import com.megatron.arenahub.service.PartitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partite")
public class PartitaController {

    @Autowired
    private PartitaService partitaService;

    @GetMapping("/torneo/{torneoId}")
    public List<Partita> getPartitePerTorneo(@PathVariable Long torneoId) {
        return partitaService.cercaPartitePerTorneo(torneoId);
    }

    @PostMapping("/crea")
    public Partita creaPartita(@RequestParam Long torneoId, 
                               @RequestParam Long giocatore1Id, 
                               @RequestParam Long giocatore2Id) {
        return partitaService.creaPartitaPerId(torneoId, giocatore1Id, giocatore2Id);
    }

    @PutMapping("/{id}/risultato")
    public Partita inserisciRisultato(@PathVariable Long id, 
                                      @RequestParam int puntiG1, 
                                      @RequestParam int puntiG2) {
        return partitaService.salvaRisultato(id, puntiG1, puntiG2);
    }
}
