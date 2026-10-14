package com.megatron.arenahub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "impostazioni_sito")
public class ImpostazioniSito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_impostazioni")
    private Long id;

    @Column(name = "max_tornei_attivi_utente")
    private Integer maxTorneiAttiviUtente;

    @Column(name = "aggiornato_il")
    private LocalDateTime aggiornatoIl;

    public ImpostazioniSito() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMaxTorneiAttiviUtente() {
        return maxTorneiAttiviUtente;
    }

    public void setMaxTorneiAttiviUtente(Integer maxTorneiAttiviUtente) {
        this.maxTorneiAttiviUtente = maxTorneiAttiviUtente;
    }

    public LocalDateTime getAggiornatoIl() {
        return aggiornatoIl;
    }

    public void setAggiornatoIl(LocalDateTime aggiornatoIl) {
        this.aggiornatoIl = aggiornatoIl;
    }
}
