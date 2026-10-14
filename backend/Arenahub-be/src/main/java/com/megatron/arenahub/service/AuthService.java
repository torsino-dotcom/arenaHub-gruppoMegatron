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
        if (Validazione.mancante(r.nome()) || Validazione.mancante(r.cognome())
                || Validazione.mancante(r.username()) || Validazione.mancante(r.email())
                || Validazione.mancante(r.password())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Campi obbligatori: nome, cognome, username, email, password");
        }
        if (!r.email().contains("@")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email non valida");
        }
        if (utenteRepository.existsByUsername(r.username())) {
            throw new ApiException(HttpStatus.CONFLICT, "Username già in uso");
        }
        if (utenteRepository.existsByEmail(r.email())) {
            throw new ApiException(HttpStatus.CONFLICT, "Email già in uso");
        }

        // Il ruolo è sempre "utente" (lo imposta il costruttore)
        Utente utente = new Utente(r.nome(), r.cognome(), r.username(), r.email(), passwordEncoder.encode(r.password()));
        return utenteService.aRisposta(utenteRepository.save(utente));
    }

    public UtenteRisposta login(LoginRichiesta r) {
        if (Validazione.mancante(r.username()) || Validazione.mancante(r.password())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Campi obbligatori: username, password");
        }
        Utente utente = utenteRepository.findByUsername(r.username());
        if (utente == null || !passwordEncoder.matches(r.password(), utente.getPassword())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Credenziali non valide");
        }
        sessioneService.accedi(utente);
        return utenteService.aRisposta(utente);
    }

    public void logout() {
        sessioneService.esci();
    }
}
