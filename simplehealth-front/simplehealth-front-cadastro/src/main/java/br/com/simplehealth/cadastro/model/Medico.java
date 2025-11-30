package br.com.simplehealth.cadastro.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Modelo de dados para Médico.
 */
public class Medico {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nomeCompleto")
    private String nomeCompleto;

    @JsonProperty("crm")
    private String crm;

    @JsonProperty("especialidade")
    private String especialidade;

    @JsonProperty("telefone")
    private String telefone;

    @JsonProperty("email")
    private String email;

    public Medico() {
    }

    public Medico(Long id, String nomeCompleto, String crm, String especialidade, String telefone, String email) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.crm = crm;
        this.especialidade = especialidade;
        this.telefone = telefone;
        this.email = email;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return nomeCompleto + " - CRM: " + crm;
    }
}
