package com.megatron.arenahub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TorneoRisposta(Long idTornei, String nomeTorneo, String descrizione, Long idGiochi, String gioco,
                             String urlImg, LocalDateTime dataOra, LocalDate dataInizioIscrizioni,
                             LocalDate dataFineIscrizioni, int postiMax, String stato,
                             @JsonInclude(JsonInclude.Include.NON_NULL) Integer postiOccupati) {
}
