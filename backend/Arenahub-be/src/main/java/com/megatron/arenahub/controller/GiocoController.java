package com.megatron.arenahub.controller;

import com.megatron.arenahub.dto.GiocoRisposta;
import com.megatron.arenahub.dto.TorneoRisposta;
import com.megatron.arenahub.model.Gioco;
import com.megatron.arenahub.service.GiocoService;
import com.megatron.arenahub.service.SessioneService;
import com.megatron.arenahub.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gioco")
public class GiocoController {

    @Autowired
    private GiocoService giocoService;

    @Autowired
    private TorneoService torneoService;

    @Autowired
    private SessioneService sessioneService;

    // 26. Un gioco con i suoi tornei (pubblico)
    @GetMapping("/{id_gioco}")
    public GiocoRisposta getGiocoById(@PathVariable("id_gioco") Long idGioco) {
        Gioco gioco = giocoService.cercaPerId(idGioco);
        List<TorneoRisposta> tornei = torneoService.perGioco(idGioco, sessioneService.isAdmin());
        return new GiocoRisposta(gioco.getId(), gioco.getNomeGiochi(), gioco.getUrlImg(), tornei);
    }
}
