package com.megatron.arenahub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tornei")
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tornei")
    private Long id;

    @Column(name = "nome")
    private String nomeTorneo;

    @Column(length = 1000)
    private String descrizione;

    @ManyToOne
    @JoinColumn(name = "id_giochi")
    private Gioco gioco;

    @Column(name = "data_ora")
    private LocalDateTime dataOra;

    @Column(name = "data_inizio_iscrizioni")
    private LocalDate dataInizioIscrizioni;

    @Column(name = "data_fine_iscrizioni")
    private LocalDate dataFineIscrizioni;

    @Column(name = "posti_max")
    private int postiMax;

    private String stato;

    @Column(name = "url_img")
    private String urlImg;

    public Torneo() {
        this.stato = "aperto";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Gioco getGioco() {
        return gioco;
    }

    public void setGioco(Gioco gioco) {
        this.gioco = gioco;
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

    public int getPostiMax() {
        return postiMax;
    }

    public void setPostiMax(int postiMax) {
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
