package br.com.simplehealth.cadastro.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Modelo de dados para Item de Estoque.
 */
public class ItemEstoque {

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("quantidade")
    private Integer quantidade;

    public ItemEstoque() {
    }

    public ItemEstoque(String nome, Integer quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return nome + " (Qtd: " + quantidade + ")";
    }
}
