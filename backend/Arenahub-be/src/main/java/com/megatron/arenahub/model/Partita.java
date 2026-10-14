package com.megatron.arenahub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "partite")
public class Partita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partite")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_tornei")
    private Torneo torneo;

    @ManyToOne
    @JoinColumn(name = "id_utenti1")
    private Utente giocatore1;

    @ManyToOne
    @JoinColumn(name = "id_utenti2")
    private Utente giocatore2;

    @Column(name = "orario_inizio")
    private LocalDateTime orarioInizio;

    @Column(name = "punteggio_1")
    private int punteggioGiocatore1;

    @Column(name = "punteggio_2")
    private int punteggioGiocatore2;

    @Column(name = "id_persona_vincitrice")
    private Long idPersonaVincitrice;

    @Column(name = "formato_match")
    private String formatoMatch;

    private String stato;
    private String fase;

    public Partita() {
        this.punteggioGiocatore1 = 0;
        this.punteggioGiocatore2 = 0;
        this.stato = "programmata";
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

    public LocalDateTime getOrarioInizio() {
        return orarioInizio;
    }

    public void setOrarioInizio(LocalDateTime orarioInizio) {
        this.orarioInizio = orarioInizio;
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
