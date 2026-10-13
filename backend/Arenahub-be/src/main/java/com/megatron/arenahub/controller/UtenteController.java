package com.megatron.arenahub.controller;

import com.megatron.arenahub.model.Utente;
import com.megatron.arenahub.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @GetMapping
    public List<Utente> listaUtenti() {
        return utenteService.ottieniTuttiGliUtenti();
    }

    @GetMapping("/{id}")
    public Utente getUtenteById(@PathVariable Long id) {
        return utenteService.cercaPerId(id);
    }

    @PostMapping
    public Utente creaUtente(@RequestBody Utente utente) {
        return utenteService.salvaUtente(utente);
    }

    @PutMapping("/{id}/ruolo")
    public Utente cambiaRuolo(@PathVariable Long id, @RequestParam String nuovoRuolo) {
        return utenteService.aggiornaRuolo(id, nuovoRuolo);
    }
}
