package com.megatron.arenahub.controller;

import com.megatron.arenahub.dto.IscrizioneUtenteRisposta;
import com.megatron.arenahub.dto.UtenteModificaRichiesta;
import com.megatron.arenahub.dto.UtenteRisposta;
import com.megatron.arenahub.model.Utente;
import com.megatron.arenahub.service.IscrizioneService;
import com.megatron.arenahub.service.SessioneService;
import com.megatron.arenahub.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private IscrizioneService iscrizioneService;

    @Autowired
    private SessioneService sessioneService;

    // 4. Il proprio profilo
    @GetMapping("/me")
    public UtenteRisposta profilo() {
        return utenteService.aRisposta(sessioneService.richiediLogin());
    }

    // 5. Modifica del proprio profilo
    @PatchMapping("/me")
    public UtenteRisposta modificaProfilo(@RequestBody UtenteModificaRichiesta richiesta) {
        Utente utente = sessioneService.richiediLogin();
        return utenteService.modificaProfilo(utente, richiesta);
    }

    // 6. Le proprie iscrizioni
    @GetMapping("/me/iscrizioni")
    public List<IscrizioneUtenteRisposta> mieIscrizioni(@RequestParam(required = false) Integer page,
                                                        @RequestParam(required = false) Integer limit) {
        Utente utente = sessioneService.richiediLogin();
        return iscrizioneService.elencoPerUtente(utente, page, limit);
    }

    // 7. Elenco utenti (admin)
    @GetMapping
    public List<UtenteRisposta> listaUtenti(@RequestParam(required = false) String ruolo,
                                            @RequestParam(required = false) String username,
                                            @RequestParam(required = false) Integer page,
                                            @RequestParam(required = false) Integer limit) {
        sessioneService.richiediAdmin();
        return utenteService.elenca(ruolo, username, page, limit);
    }

    // 8. Dettaglio utente (admin)
    @GetMapping("/{id_utenti}")
    public UtenteRisposta getUtenteById(@PathVariable("id_utenti") Long idUtenti) {
        sessioneService.richiediAdmin();
        return utenteService.aRisposta(utenteService.cercaPerId(idUtenti));
    }

    // 9. Modifica utente / ruolo (admin)
    @PatchMapping("/{id_utenti}")
    public UtenteRisposta modificaUtente(@PathVariable("id_utenti") Long idUtenti,
                                         @RequestBody UtenteModificaRichiesta richiesta) {
        sessioneService.richiediAdmin();
        return utenteService.modificaComeAdmin(idUtenti, richiesta);
    }

    // 10. Elimina utente (admin)
    @DeleteMapping("/{id_utenti}")
    public Map<String, String> eliminaUtente(@PathVariable("id_utenti") Long idUtenti) {
        sessioneService.richiediAdmin();
        utenteService.elimina(idUtenti);
        return Map.of("messaggio", "Utente eliminato");
    }
}
