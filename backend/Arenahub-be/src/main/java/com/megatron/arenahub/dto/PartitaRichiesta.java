package com.megatron.arenahub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PartitaRichiesta(Long idUtenti1, Long idUtenti2, LocalDateTime orarioInizio,
                               @JsonProperty("punteggio_1") Integer punteggio1,
                               @JsonProperty("punteggio_2") Integer punteggio2,
                               String personaVincitrice, String formatoMatch, String stato, String fase) {
}
