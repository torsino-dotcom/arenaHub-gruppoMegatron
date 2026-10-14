package com.megatron.arenahub.service;

import com.megatron.arenahub.dto.GiocoRisposta;
import com.megatron.arenahub.exception.ApiException;
import com.megatron.arenahub.model.Gioco;
import com.megatron.arenahub.repository.GiocoRepository;
import com.megatron.arenahub.util.Paginazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiocoService {

    @Autowired
    private GiocoRepository giocoRepository;

    public GiocoRisposta aRisposta(Gioco g) {
        return new GiocoRisposta(g.getId(), g.getNomeGiochi(), g.getUrlImg());
    }

    public Gioco cercaPerId(Long id) {
        return giocoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Gioco non trovato"));
    }

    public GiocoRisposta dettaglio(Long id) {
        return aRisposta(cercaPerId(id));
    }

    public List<GiocoRisposta> elenca(Integer page, Integer limit) {
        List<GiocoRisposta> risultato = giocoRepository.findAll(Sort.by("nomeGiochi")).stream()
                .map(this::aRisposta)
                .collect(Collectors.toList());
        return Paginazione.applica(risultato, page, limit);
    }
}
