package com.megatron.arenahub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GiocoRisposta {

    @JsonProperty("id_giochi")
    private Long idGiochi;

    @JsonProperty("nome_giochi")
    private String nomeGiochi;

    @JsonProperty("url_img")
    private String urlImg;

    public GiocoRisposta(Long idGiochi, String nomeGiochi, String urlImg) {
        this.idGiochi = idGiochi;
        this.nomeGiochi = nomeGiochi;
        this.urlImg = urlImg;
    }

    public Long getIdGiochi() {
        return idGiochi;
    }

    public void setIdGiochi(Long idGiochi) {
        this.idGiochi = idGiochi;
    }

    public String getNomeGiochi() {
        return nomeGiochi;
    }

    public void setNomeGiochi(String nomeGiochi) {
        this.nomeGiochi = nomeGiochi;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }
}
