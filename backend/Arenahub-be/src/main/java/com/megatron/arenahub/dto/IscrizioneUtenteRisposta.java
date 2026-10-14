package com.megatron.arenahub.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record IscrizioneUtenteRisposta(Long idUtentiTornei, Long idTornei, LocalDate dataIscrizione, String stato,
                                       String nomeTorneo, String gioco, LocalDateTime dataOra) {
}
