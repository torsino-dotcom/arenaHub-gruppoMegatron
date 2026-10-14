package com.megatron.arenahub.service;

import com.megatron.arenahub.exception.ApiException;
import com.megatron.arenahub.model.Gioco;
import com.megatron.arenahub.repository.GiocoRepository;
import com.megatron.arenahub.util.Validazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GiocoService {

    @Autowired
    private GiocoRepository giocoRepository;

    public Gioco cercaPerId(Long id) {
        return giocoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Gioco non trovato"));
    }

    // Se arriva l'id usa quello; se arriva il nome cerca il gioco e, se non esiste, lo crea
    public Gioco trovaOCrea(Long idGiochi, String nomeGioco) {
        if (idGiochi != null) {
            return cercaPerId(idGiochi);
        }
        if (Validazione.mancante(nomeGioco)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Indicare il gioco (gioco oppure id_giochi)");
        }
        Gioco gioco = giocoRepository.findByNomeGiochiIgnoreCase(nomeGioco.trim());
        if (gioco == null) {
            gioco = giocoRepository.save(new Gioco(nomeGioco.trim()));
        }
        return gioco;
    }
}
