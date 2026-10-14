package com.megatron.arenahub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ImpostazioniRisposta {

    @JsonProperty("id_impostazioni")
    private Long idImpostazioni;

    @JsonProperty("max_tornei_attivi_utente")
    private Integer maxTorneiAttiviUtente;

    @JsonProperty("aggiornato_il")
    private LocalDateTime aggiornatoIl;

    public ImpostazioniRisposta(Long idImpostazioni, Integer maxTorneiAttiviUtente, LocalDateTime aggiornatoIl) {
        this.idImpostazioni = idImpostazioni;
        this.maxTorneiAttiviUtente = maxTorneiAttiviUtente;
        this.aggiornatoIl = aggiornatoIl;
    }

    public Long getIdImpostazioni() {
        return idImpostazioni;
    }

    public void setIdImpostazioni(Long idImpostazioni) {
        this.idImpostazioni = idImpostazioni;
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
