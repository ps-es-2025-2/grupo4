package br.com.simplehealth.cadastro.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Modelo de dados para Usuário.
 */
public class Usuario {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nomeCompleto")
    private String nomeCompleto;

    @JsonProperty("login")
    private String login;

    @JsonProperty("senha")
    private String senha;

    @JsonProperty("perfil")
    private String perfil; // MEDICO, SECRETARIA, ADMINISTRADOR

    @JsonProperty("telefone")
    private String telefone;

    @JsonProperty("email")
    private String email;

    public Usuario() {
    }

    public Usuario(Long id, String nomeCompleto, String login, String senha, String perfil, String telefone, String email) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
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
        return nomeCompleto + " - " + login + " (" + perfil + ")";
    }
}
