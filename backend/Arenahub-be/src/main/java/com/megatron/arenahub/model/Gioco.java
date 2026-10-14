package com.megatron.arenahub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "giochi")
public class Gioco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_giochi")
    private Long id;

    @Column(name = "nome_giochi")
    private String nomeGiochi;

    @Column(name = "url_img")
    private String urlImg;

    public Gioco() {
    }

    public Gioco(String nomeGiochi) {
        this.nomeGiochi = nomeGiochi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
