package com.megatron.arenahub.service;

import com.megatron.arenahub.model.Utente;
import com.megatron.arenahub.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    public Utente salvaUtente(Utente utente) {
        return utenteRepository.save(utente);
    }

    public List<Utente> ottieniTuttiGliUtenti() {
        return utenteRepository.findAll();
    }

    public Utente cercaPerId(Long id) {
        if (utenteRepository.existsById(id)) {
            return utenteRepository.findById(id).get();
        }
        return null;
    }

    public Utente cercaPerEmailEPassword(String email, String password) {
        return utenteRepository.findByEmailAndPassword(email, password);
    }

    public Utente aggiornaRuolo(Long id, String nuovoRuolo) {
        Utente u = cercaPerId(id);
        if (u != null) {
            u.setRuolo(nuovoRuolo);
            return utenteRepository.save(u);
        }
        return null;
    }
}
