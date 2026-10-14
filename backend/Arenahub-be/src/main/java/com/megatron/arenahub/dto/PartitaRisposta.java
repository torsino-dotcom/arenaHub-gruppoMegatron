package com.megatron.arenahub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PartitaRisposta(Long idPartite, Long idTornei, Long idUtenti1, Long idUtenti2,
                              String nomePersona1, String nomePersona2, LocalDateTime orarioInizio,
                              @JsonProperty("punteggio_1") int punteggio1,
                              @JsonProperty("punteggio_2") int punteggio2,
                              String personaVincitrice, String formatoMatch, String stato, String fase) {
}
