package com.megatron.arenahub.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;
import java.time.LocalDateTime;

// "gioco" = nome del gioco, oppure "id_giochi" se il gioco esiste già
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TorneoRichiesta(String nomeTorneo, String descrizione, String gioco, Long idGiochi,
                              LocalDateTime dataOra, LocalDate dataInizioIscrizioni, LocalDate dataFineIscrizioni,
                              Integer postiMax, String stato, String urlImg) {
}
