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

@Entity
@Table(name = "utenti_tornei")
public class UtenteTorneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utenti_tornei")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_utenti")
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "id_tornei")
    private Torneo torneo;

    @Column(name = "data_iscrizione")
    private LocalDate dataIscrizione;

    private String stato;

    public UtenteTorneo() {
        this.stato = "confermato";
        this.dataIscrizione = LocalDate.now();
    }

    public UtenteTorneo(Utente utente, Torneo torneo) {
        this.utente = utente;
        this.torneo = torneo;
        this.stato = "confermato";
        this.dataIscrizione = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
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
}
