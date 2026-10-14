package com.megatron.arenahub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UtenteRisposta {

    @JsonProperty("id_utenti")
    private Long idUtenti;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("cognome")
    private String cognome;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("ruolo")
    private String ruolo;

    public UtenteRisposta(Long idUtenti, String nome, String cognome, String username, String email, String ruolo) {
        this.idUtenti = idUtenti;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.email = email;
        this.ruolo = ruolo;
    }

    public Long getIdUtenti() {
        return idUtenti;
    }

    public void setIdUtenti(Long idUtenti) {
        this.idUtenti = idUtenti;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
}
