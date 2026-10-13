package com.megatron.arenahub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "utenti_tornei")
public class UtenteTorneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Utente utente;

    @ManyToOne
    private Torneo torneo;

    private String statoIscrizione;

    public UtenteTorneo() {
        this.statoIscrizione = "ISCRITTO";
    }

    public UtenteTorneo(Utente utente, Torneo torneo) {
        this.utente = utente;
        this.torneo = torneo;
        this.statoIscrizione = "ISCRITTO";
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

    public String getStatoIscrizione() {
        return statoIscrizione;
    }

    public void setStatoIscrizione(String statoIscrizione) {
        this.statoIscrizione = statoIscrizione;
    }
}
