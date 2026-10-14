package com.megatron.arenahub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class IscrizioneUtenteRisposta {

    @JsonProperty("id_utenti_tornei")
    private Long idUtentiTornei;

    @JsonProperty("id_tornei")
    private Long idTornei;

    @JsonProperty("data_iscrizione")
    private LocalDate dataIscrizione;

    @JsonProperty("stato")
    private String stato;

    @JsonProperty("nome_torneo")
    private String nomeTorneo;

    @JsonProperty("id_giochi")
    private Long idGiochi;

    @JsonProperty("data_ora")
    private LocalDateTime dataOra;

    public IscrizioneUtenteRisposta(Long idUtentiTornei, Long idTornei, LocalDate dataIscrizione, String stato, String nomeTorneo, Long idGiochi, LocalDateTime dataOra) {
        this.idUtentiTornei = idUtentiTornei;
        this.idTornei = idTornei;
        this.dataIscrizione = dataIscrizione;
        this.stato = stato;
        this.nomeTorneo = nomeTorneo;
        this.idGiochi = idGiochi;
        this.dataOra = dataOra;
    }

    public Long getIdUtentiTornei() {
        return idUtentiTornei;
    }

    public void setIdUtentiTornei(Long idUtentiTornei) {
        this.idUtentiTornei = idUtentiTornei;
    }

    public Long getIdTornei() {
        return idTornei;
    }

    public void setIdTornei(Long idTornei) {
        this.idTornei = idTornei;
    }

    public LocalDate getDataIscrizione() {
        return dataIscrizione;
    }

    public void setDataIscrizione(LocalDate dataIscrizione) {
        this.dataIscrizione = dataIscrizione;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getNomeTorneo() {
        return nomeTorneo;
    }

    public void setNomeTorneo(String nomeTorneo) {
        this.nomeTorneo = nomeTorneo;
    }

    public Long getIdGiochi() {
        return idGiochi;
    }

    public void setIdGiochi(Long idGiochi) {
        this.idGiochi = idGiochi;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }
}
