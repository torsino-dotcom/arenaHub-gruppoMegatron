package com.megatron.arenahub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "partite")
public class Partita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Torneo torneo;

    @ManyToOne
    private Utente giocatore1;

    @ManyToOne
    private Utente giocatore2;

    private int punteggioGiocatore1;
    private int punteggioGiocatore2;
    private String stato;

    public Partita() {
        this.punteggioGiocatore1 = 0;
        this.punteggioGiocatore2 = 0;
        this.stato = "DA_GIOCARE";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public Utente getGiocatore1() {
        return giocatore1;
    }

    public void setGiocatore1(Utente giocatore1) {
        this.giocatore1 = giocatore1;
    }

    public Utente getGiocatore2() {
        return giocatore2;
    }

    public void setGiocatore2(Utente giocatore2) {
        this.giocatore2 = giocatore2;
    }

    public int getPunteggioGiocatore1() {
        return punteggioGiocatore1;
    }

    public void setPunteggioGiocatore1(int punteggioGiocatore1) {
        this.punteggioGiocatore1 = punteggioGiocatore1;
    }

    public int getPunteggioGiocatore2() {
        return punteggioGiocatore2;
    }

    public void setPunteggioGiocatore2(int punteggioGiocatore2) {
        this.punteggioGiocatore2 = punteggioGiocatore2;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}
