package com.megatron.arenahub.service;

import com.megatron.arenahub.exception.ApiException;
import com.megatron.arenahub.model.Utente;
import com.megatron.arenahub.repository.UtenteRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

// Gestisce la sessione (cookie) creata dal login.
// L'utente autenticato viene sempre riletto dal database, così ruolo e dati sono aggiornati.
@Service
public class SessioneService {

    private static final String CHIAVE_UTENTE = "utenteId";

    @Autowired
    private UtenteRepository utenteRepository;

    private HttpServletRequest richiesta() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    public void accedi(Utente utente) {
        HttpServletRequest richiesta = richiesta();
        HttpSession vecchia = richiesta.getSession(false);
        if (vecchia != null) {
            vecchia.invalidate();
        }
        richiesta.getSession(true).setAttribute(CHIAVE_UTENTE, utente.getId());
    }

    public void esci() {
        HttpSession sessione = richiesta().getSession(false);
        if (sessione != null) {
            sessione.invalidate();
        }
    }

    // Restituisce l'utente loggato, oppure null
    public Utente utenteCorrente() {
        HttpSession sessione = richiesta().getSession(false);
        if (sessione == null) {
            return null;
        }
        Object id = sessione.getAttribute(CHIAVE_UTENTE);
        if (id == null) {
            return null;
        }
        return utenteRepository.findById((Long) id).orElse(null);
    }

    public Utente richiediLogin() {
        Utente utente = utenteCorrente();
        if (utente == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Autenticazione richiesta");
        }
        return utente;
    }

    public Utente richiediAdmin() {
        Utente utente = richiediLogin();
        if (!"admin".equals(utente.getRuolo())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Permessi insufficienti");
        }
        return utente;
    }

    public boolean isAdmin() {
        Utente utente = utenteCorrente();
        return utente != null && "admin".equals(utente.getRuolo());
    }
}
