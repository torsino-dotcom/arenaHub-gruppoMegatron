package com.megatron.arenahub.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GiocoRisposta(Long idGiochi, String nomeGiochi, String urlImg, List<TorneoRisposta> tornei) {
}
