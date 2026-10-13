package com.megatron.arenahub.controller;

import com.megatron.arenahub.model.UtenteTorneo;
import com.megatron.arenahub.service.IscrizioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/iscrizioni")
public class IscrizioneController {

    @Autowired
    private IscrizioneService iscrizioneService;

    @PostMapping
    public UtenteTorneo iscriviUtente(@RequestParam Long utenteId, @RequestParam Long torneoId) {
        return iscrizioneService.iscriviUtenteATorneo(utenteId, torneoId);
    }

    @GetMapping("/torneo/{torneoId}")
    public List<UtenteTorneo> getIscrittiTorneo(@PathVariable Long torneoId) {
        return iscrizioneService.ottieniIscrittiPerTorneo(torneoId);
    }

    @DeleteMapping
    public String cancellaIscrizione(@RequestParam Long utenteId, @RequestParam Long torneoId) {
        boolean cancellato = iscrizioneService.rimuoviIscrizione(utenteId, torneoId);
        if (cancellato) {
            return "Iscrizione cancellata con successo";
        }
        return "Impossibile cancellare l'iscrizione";
    }
}
