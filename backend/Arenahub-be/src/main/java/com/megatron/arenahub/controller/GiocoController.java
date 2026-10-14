package com.megatron.arenahub.controller;

import com.megatron.arenahub.dto.GiocoRisposta;
import com.megatron.arenahub.service.GiocoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/giochi")
public class GiocoController {

    @Autowired
    private GiocoService giocoService;

    // 27. Elenco giochi (pubblico)
    @GetMapping
    public List<GiocoRisposta> listaGiochi(@RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer limit) {
        return giocoService.elenca(page, limit);
    }

    // 26. Dettaglio di un gioco (pubblico)
    @GetMapping("/{id_giochi}")
    public GiocoRisposta getGiocoById(@PathVariable("id_giochi") Long idGiochi) {
        return giocoService.dettaglio(idGiochi);
    }
}
