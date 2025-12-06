package br.com.simplehealth.cadastro.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

/**
 * Modelo de dados para Pessoa.
 */
public class Pessoa {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("cpf")
    private String cpf;

    @JsonProperty("email")
    private String email;

    @JsonProperty("dataNascimento")
    private LocalDate dataNascimento;

    @JsonProperty("tipo")
    private String tipo;

    public Pessoa() {
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
