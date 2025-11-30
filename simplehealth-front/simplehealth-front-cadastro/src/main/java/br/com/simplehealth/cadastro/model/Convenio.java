package br.com.simplehealth.cadastro.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Modelo de dados para Convênio.
 */
public class Convenio {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("plano")
    private String plano;

    @JsonProperty("ativo")
    private Boolean ativo;

    public Convenio() {
    }

    public Convenio(Long id, String nome, String plano, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.plano = plano;
        this.ativo = ativo;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPlano() {
        return plano;
    }

    public void setPlano(String plano) {
        this.plano = plano;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return nome + " - " + plano + (ativo ? " (Ativo)" : " (Inativo)");
    }
}
