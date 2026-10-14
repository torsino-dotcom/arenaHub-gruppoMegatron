package com.megatron.arenahub.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

// Usato da PATCH /api/utenti/me (nome, cognome, username, email, password)
// e da PATCH /api/utenti/{id_utenti} (nome, cognome, username, email, ruolo)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UtenteModificaRichiesta(String nome, String cognome, String username, String email, String password, String ruolo) {
}
