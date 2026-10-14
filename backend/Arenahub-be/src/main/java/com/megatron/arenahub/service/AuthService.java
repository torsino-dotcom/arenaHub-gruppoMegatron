package com.megatron.arenahub.service;

import com.megatron.arenahub.dto.LoginRichiesta;
import com.megatron.arenahub.dto.RegistrazioneRichiesta;
import com.megatron.arenahub.dto.UtenteRisposta;
import com.megatron.arenahub.exception.ApiException;
import com.megatron.arenahub.model.Utente;
import com.megatron.arenahub.repository.UtenteRepository;
import com.megatron.arenahub.util.Validazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private SessioneService sessioneService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UtenteRisposta registra(RegistrazioneRichiesta r) {
        if (Validazione.mancante(r.getNome()) || Validazione.mancante(r.getCognome())
                || Validazione.mancante(r.getUsername()) || Validazione.mancante(r.getEmail())
                || Validazione.mancante(r.getPassword())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Campi obbligatori: nome, cognome, username, email, password");
        }
        if (!r.getEmail().contains("@")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email non valida");
        }
        if (utenteRepository.existsByUsername(r.getUsername())) {
            throw new ApiException(HttpStatus.CONFLICT, "Username già in uso");
        }
        if (utenteRepository.existsByEmail(r.getEmail())) {
            throw new ApiException(HttpStatus.CONFLICT, "Email già in uso");
        }

        // Il ruolo è sempre "utente" (lo imposta il costruttore)
        Utente utente = new Utente(r.getNome(), r.getCognome(), r.getUsername(), r.getEmail(),
                passwordEncoder.encode(r.getPassword()));
        return utenteService.aRisposta(utenteRepository.save(utente));
    }

    public UtenteRisposta login(LoginRichiesta r) {
        if (Validazione.mancante(r.getEmail()) || Validazione.mancante(r.getPassword())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Campi obbligatori: email, password");
        }
        Utente utente = utenteRepository.findByEmail(r.getEmail());
        if (utente == null || !passwordEncoder.matches(r.getPassword(), utente.getPassword())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Credenziali non valide");
        }
        sessioneService.accedi(utente);
        return utenteService.aRisposta(utente);
    }

    public void logout() {
        sessioneService.esci();
    }
}
