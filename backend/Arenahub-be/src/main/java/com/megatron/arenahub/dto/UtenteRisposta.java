package com.megatron.arenahub.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming
(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UtenteRisposta(Long idUtenti, String nome, String cognome, String username, String email, String ruolo) {
}
