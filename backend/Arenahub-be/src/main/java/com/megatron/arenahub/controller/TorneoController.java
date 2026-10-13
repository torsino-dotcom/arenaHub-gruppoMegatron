package com.megatron.arenahub.controller;

import com.megatron.arenahub.model.Torneo;
import com.megatron.arenahub.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tornei")
public class TorneoController {

    @Autowired
    private TorneoService torneoService;

    @GetMapping
    public List<Torneo> listaTornei() {
        return torneoService.ottieniTuttiITornei();
    }

    @GetMapping("/{id}")
    public Torneo getTorneoById(@PathVariable Long id) {
        return torneoService.cercaPerId(id);
    }

    @PostMapping
    public Torneo creaTorneo(@RequestParam String nome) {
        return torneoService.creaTorneo(nome);
    }

    @PutMapping("/{id}/stato")
    public Torneo cambiaStatoTorneo(@PathVariable Long id, @RequestParam String nuovoStato) {
        return torneoService.aggiornaStato(id, nuovoStato);
    }
}
