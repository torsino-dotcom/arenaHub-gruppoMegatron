package com.megatron.arenahub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IscrizioneModificaRichiesta {

    @JsonProperty("stato")
    private String stato;

    public IscrizioneModificaRichiesta() {
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}
