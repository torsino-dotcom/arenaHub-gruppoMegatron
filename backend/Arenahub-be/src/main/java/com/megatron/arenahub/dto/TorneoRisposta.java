package com.megatron.arenahub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TorneoRisposta {

    @JsonProperty("id_tornei")
    private Long idTornei;

    @JsonProperty("nome_torneo")
    private String nomeTorneo;

    @JsonProperty("descrizione")
    private String descrizione;

    @JsonProperty("id_giochi")
    private Long idGiochi;

    @JsonProperty("data_ora")
    private LocalDateTime dataOra;

    @JsonProperty("data_inizio_iscrizioni")
    private LocalDate dataInizioIscrizioni;

    @JsonProperty("data_fine_iscrizioni")
    private LocalDate dataFineIscrizioni;

    @JsonProperty("posti_max")
    private Integer postiMax;

    @JsonProperty("stato")
    private String stato;

    @JsonProperty("url_img")
    private String urlImg;

    @JsonProperty("posti_occupati")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer postiOccupati;

    public TorneoRisposta(Long idTornei, String nomeTorneo, String descrizione, Long idGiochi, LocalDateTime dataOra, LocalDate dataInizioIscrizioni, LocalDate dataFineIscrizioni, Integer postiMax, String stato, String urlImg, Integer postiOccupati) {
        this.idTornei = idTornei;
        this.nomeTorneo = nomeTorneo;
        this.descrizione = descrizione;
        this.idGiochi = idGiochi;
        this.dataOra = dataOra;
        this.dataInizioIscrizioni = dataInizioIscrizioni;
        this.dataFineIscrizioni = dataFineIscrizioni;
        this.postiMax = postiMax;
        this.stato = stato;
        this.urlImg = urlImg;
        this.postiOccupati = postiOccupati;
    }

    public Long getIdTornei() {
        return idTornei;
    }

    public void setIdTornei(Long idTornei) {
        this.idTornei = idTornei;
    }

    public String getNomeTorneo() {
        return nomeTorneo;
    }

    public void setNomeTorneo(String nomeTorneo) {
        this.nomeTorneo = nomeTorneo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
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

    public LocalDate getDataInizioIscrizioni() {
        return dataInizioIscrizioni;
    }

    public void setDataInizioIscrizioni(LocalDate dataInizioIscrizioni) {
        this.dataInizioIscrizioni = dataInizioIscrizioni;
    }

    public LocalDate getDataFineIscrizioni() {
        return dataFineIscrizioni;
    }

    public void setDataFineIscrizioni(LocalDate dataFineIscrizioni) {
        this.dataFineIscrizioni = dataFineIscrizioni;
    }

    public Integer getPostiMax() {
        return postiMax;
    }

    public void setPostiMax(Integer postiMax) {
        this.postiMax = postiMax;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public Integer getPostiOccupati() {
        return postiOccupati;
    }

    public void setPostiOccupati(Integer postiOccupati) {
        this.postiOccupati = postiOccupati;
    }
}
