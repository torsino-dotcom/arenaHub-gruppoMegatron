package com.megatron.arenahub.service;

import com.megatron.arenahub.model.Torneo;
import com.megatron.arenahub.repository.TorneoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TorneoService {

    @Autowired
    private TorneoRepository torneoRepository;

    public Torneo creaTorneo(String nome) {
        Torneo torneo = new Torneo(nome);
        return torneoRepository.save(torneo);
    }

    public List<Torneo> ottieniTuttiITornei() {
        return torneoRepository.findAll();
    }

    public Torneo cercaPerId(Long id) {
        if (torneoRepository.existsById(id)) {
            return torneoRepository.findById(id).get();
        }
        return null;
    }

    public Torneo aggiornaStato(Long id, String nuovoStato) {
        Torneo torneo = cercaPerId(id);
        if (torneo != null) {
            torneo.setStato(nuovoStato);
            return torneoRepository.save(torneo);
        }
        return null;
    }
}
