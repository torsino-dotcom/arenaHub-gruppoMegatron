package com.megatron.arenahub.controller;

import com.megatron.arenahub.dto.ImpostazioniRichiesta;
import com.megatron.arenahub.dto.ImpostazioniRisposta;
import com.megatron.arenahub.service.ImpostazioniService;
import com.megatron.arenahub.service.SessioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:8000")
@RequestMapping("/api/impostazioni")
public class ImpostazioniController {

    @Autowired
    private ImpostazioniService impostazioniService;

    @Autowired
    private SessioneService sessioneService;

    // 28. Impostazioni globali (pubblico)
    @GetMapping
    public ImpostazioniRisposta getImpostazioni() {
        return impostazioniService.leggi();
    }

    // 29. Aggiornamento impostazioni (admin)
    @PatchMapping
    public ImpostazioniRisposta aggiornaImpostazioni(@RequestBody ImpostazioniRichiesta richiesta) {
        sessioneService.richiediAdmin();
        return impostazioniService.aggiorna(richiesta);
    }
}
