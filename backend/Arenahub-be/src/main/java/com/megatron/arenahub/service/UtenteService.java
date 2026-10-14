package com.megatron.arenahub.service;

import com.megatron.arenahub.dto.UtenteModificaRichiesta;
import com.megatron.arenahub.dto.UtenteRisposta;
import com.megatron.arenahub.exception.ApiException;
import com.megatron.arenahub.model.Utente;
import com.megatron.arenahub.repository.PartitaRepository;
import com.megatron.arenahub.repository.UtenteRepository;
import com.megatron.arenahub.repository.UtenteTorneoRepository;
import com.megatron.arenahub.util.Paginazione;
import com.megatron.arenahub.util.Validazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UtenteService {

    private static final Set<String> RUOLI = Set.of("utente", "admin");

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private UtenteTorneoRepository utenteTorneoRepository;

    @Autowired
    private PartitaRepository partitaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UtenteRisposta aRisposta(Utente u) {
        return new UtenteRisposta(u.getId(), u.getNome(), u.getCognome(), u.getUsername(), u.getEmail(), u.getRuolo());
    }

    public List<UtenteRisposta> elenca(String ruolo, String username, Integer page, Integer limit) {
        List<UtenteRisposta> risultato = utenteRepository.findAll().stream()
                .filter(u -> ruolo == null || ruolo.equalsIgnoreCase(u.getRuolo()))
                .filter(u -> username == null
                        || (u.getUsername() != null && u.getUsername().toLowerCase().contains(username.toLowerCase())))
                .map(this::aRisposta)
                .toList();
        return Paginazione.applica(risultato, page, limit);
    }

    public Utente cercaPerId(Long id) {
        return utenteRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Utente non trovato"));
    }

    // PATCH /api/utenti/me: nome, cognome, username, email, password (il ruolo viene ignorato)
    public UtenteRisposta modificaProfilo(Utente utente, UtenteModificaRichiesta r) {
        applicaModifiche(utente, r, false);
        return aRisposta(utenteRepository.save(utente));
    }

    // PATCH /api/utenti/{id}: nome, cognome, username, email, ruolo (la password viene ignorata)
    public UtenteRisposta modificaComeAdmin(Long id, UtenteModificaRichiesta r) {
        Utente utente = cercaPerId(id);
        applicaModifiche(utente, r, true);
        return aRisposta(utenteRepository.save(utente));
    }

    private void applicaModifiche(Utente u, UtenteModificaRichiesta r, boolean admin) {
        if (r.nome() != null) {
            if (r.nome().isBlank()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Nome non valido");
            }
            u.setNome(r.nome());
        }
        if (r.cognome() != null) {
            if (r.cognome().isBlank()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Cognome non valido");
            }
            u.setCognome(r.cognome());
        }
        if (r.username() != null) {
            if (r.username().isBlank()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Username non valido");
            }
            if (!r.username().equals(u.getUsername()) && utenteRepository.existsByUsername(r.username())) {
                throw new ApiException(HttpStatus.CONFLICT, "Username già in uso");
            }
            u.setUsername(r.username());
        }
        if (r.email() != null) {
            if (r.email().isBlank() || !r.email().contains("@")) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Email non valida");
            }
            if (!r.email().equals(u.getEmail()) && utenteRepository.existsByEmail(r.email())) {
                throw new ApiException(HttpStatus.CONFLICT, "Email già in uso");
            }
            u.setEmail(r.email());
        }
        if (!admin && r.password() != null) {
            if (Validazione.mancante(r.password())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Password non valida");
            }
            u.setPassword(passwordEncoder.encode(r.password()));
        }
        if (admin && r.ruolo() != null) {
            if (!RUOLI.contains(r.ruolo())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Ruolo non valido (utente, admin)");
            }
            u.setRuolo(r.ruolo());
        }
    }

    @Transactional
    public void elimina(Long id) {
        Utente utente = cercaPerId(id);
        if (partitaRepository.contaPartiteDiUtente(id) > 0) {
            throw new ApiException(HttpStatus.CONFLICT, "L'utente partecipa a delle partite: impossibile eliminarlo");
        }
        utenteTorneoRepository.deleteByUtenteId(id);
        utenteRepository.delete(utente);
    }
}
