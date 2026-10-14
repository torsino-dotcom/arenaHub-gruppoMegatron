package com.megatron.arenahub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;

// I campi null (username, nome, cognome) non vengono mostrati
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record IscrizioneRisposta(Long idUtentiTornei, Long idUtenti, Long idTornei, LocalDate dataIscrizione,
                                 String stato, String username, String nome, String cognome) {
}
