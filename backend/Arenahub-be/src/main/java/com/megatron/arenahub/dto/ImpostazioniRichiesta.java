package com.megatron.arenahub.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ImpostazioniRichiesta {

    @JsonProperty("max_tornei_attivi_utente")
    @JsonAlias("limite_iscrizioni_default")
    private Integer maxTorneiAttiviUtente;

    public ImpostazioniRichiesta() {
    }

    public Integer getMaxTorneiAttiviUtente() {
        return maxTorneiAttiviUtente;
    }

    public void setMaxTorneiAttiviUtente(Integer maxTorneiAttiviUtente) {
        this.maxTorneiAttiviUtente = maxTorneiAttiviUtente;
    }
}
