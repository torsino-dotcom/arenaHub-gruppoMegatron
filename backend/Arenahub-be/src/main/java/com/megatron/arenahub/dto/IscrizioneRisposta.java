package com.megatron.arenahub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IscrizioneRisposta {

    @JsonProperty("id_utenti_tornei")
    private Long idUtentiTornei;

    @JsonProperty("id_utenti")
    private Long idUtenti;

    @JsonProperty("id_tornei")
    private Long idTornei;

    @JsonProperty("data_iscrizione")
    private LocalDate dataIscrizione;

    @JsonProperty("stato")
    private String stato;

    @JsonProperty("username")
    private String username;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("cognome")
    private String cognome;

    public IscrizioneRisposta(Long idUtentiTornei, Long idUtenti, Long idTornei, LocalDate dataIscrizione, String stato, String username, String nome, String cognome) {
        this.idUtentiTornei = idUtentiTornei;
        this.idUtenti = idUtenti;
        this.idTornei = idTornei;
        this.dataIscrizione = dataIscrizione;
        this.stato = stato;
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
    }

    public Long getIdUtentiTornei() {
        return idUtentiTornei;
    }

    public void setIdUtentiTornei(Long idUtentiTornei) {
        this.idUtentiTornei = idUtentiTornei;
    }

    public Long getIdUtenti() {
        return idUtenti;
    }

    public void setIdUtenti(Long idUtenti) {
        this.idUtenti = idUtenti;
    }

    public Long getIdTornei() {
        return idTornei;
    }

    public void setIdTornei(Long idTornei) {
        this.idTornei = idTornei;
    }

    public LocalDate getDataIscrizione() {
        return dataIscrizione;
    }

    public void setDataIscrizione(LocalDate dataIscrizione) {
        this.dataIscrizione = dataIscrizione;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
