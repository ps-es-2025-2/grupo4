package br.com.simplehealth.estoque.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

/**
 * Modelo para controle de estoque
 */
public class Estoque {
    
    @JsonProperty("idEstoque")
    private UUID idEstoque;
    
    @JsonProperty("localizacao")
    private String localizacao;
    
    @JsonProperty("nome")
    private String nome;
    
    @JsonProperty("setor")
    private String setor;
    
    // Construtores
    public Estoque() {
    }
    
    public Estoque(String localizacao, String nome) {
        this.localizacao = localizacao;
        this.nome = nome;
    }
    
    // Getters e Setters
    public UUID getIdEstoque() {
        return idEstoque;
    }
    
    public void setIdEstoque(UUID idEstoque) {
        this.idEstoque = idEstoque;
    }
    
    public String getLocalizacao() {
        return localizacao;
    }
    
    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getSetor() {
        return setor;
    }
    
    public void setSetor(String setor) {
        this.setor = setor;
    }
    
    @Override
    public String toString() {
        return nome != null ? nome + " (" + localizacao + ")" : "Estoque: " + localizacao;
    }
}
