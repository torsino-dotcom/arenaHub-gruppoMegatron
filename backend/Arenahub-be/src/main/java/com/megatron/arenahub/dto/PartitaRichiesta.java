package com.megatron.arenahub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class PartitaRichiesta {

    @JsonProperty("id_utenti1")
    private Long idUtenti1;

    @JsonProperty("id_utenti2")
    private Long idUtenti2;

    @JsonProperty("orario_inizio")
    private LocalDateTime orarioInizio;

    @JsonProperty("punteggio_1")
    private Integer punteggio1;

    @JsonProperty("punteggio_2")
    private Integer punteggio2;

    @JsonProperty("id_persona_vincitrice")
    private Long idPersonaVincitrice;

    @JsonProperty("formato_match")
    private String formatoMatch;

    @JsonProperty("stato")
    private String stato;

    @JsonProperty("fase")
    private String fase;

    public PartitaRichiesta() {
    }

    public Long getIdUtenti1() {
        return idUtenti1;
    }

    public void setIdUtenti1(Long idUtenti1) {
        this.idUtenti1 = idUtenti1;
    }

    public Long getIdUtenti2() {
        return idUtenti2;
    }

    public void setIdUtenti2(Long idUtenti2) {
        this.idUtenti2 = idUtenti2;
    }

    public LocalDateTime getOrarioInizio() {
        return orarioInizio;
    }

    public void setOrarioInizio(LocalDateTime orarioInizio) {
        this.orarioInizio = orarioInizio;
    }

    public Integer getPunteggio1() {
        return punteggio1;
    }

    public void setPunteggio1(Integer punteggio1) {
        this.punteggio1 = punteggio1;
    }

    public Integer getPunteggio2() {
        return punteggio2;
    }

    public void setPunteggio2(Integer punteggio2) {
        this.punteggio2 = punteggio2;
    }

    public Long getIdPersonaVincitrice() {
        return idPersonaVincitrice;
    }

    public void setIdPersonaVincitrice(Long idPersonaVincitrice) {
        this.idPersonaVincitrice = idPersonaVincitrice;
    }

    public String getFormatoMatch() {
        return formatoMatch;
    }

    public void setFormatoMatch(String formatoMatch) {
        this.formatoMatch = formatoMatch;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }
}
