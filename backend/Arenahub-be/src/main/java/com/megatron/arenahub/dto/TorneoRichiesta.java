package com.megatron.arenahub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TorneoRichiesta {

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

    public TorneoRichiesta() {
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
}
