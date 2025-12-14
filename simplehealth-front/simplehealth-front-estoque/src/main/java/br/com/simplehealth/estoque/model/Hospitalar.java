package br.com.simplehealth.estoque.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.UUID;

/**
 * Modelo para itens hospitalares (extends Item)
 * Backend possui apenas: descartabilidade
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hospitalar extends Item {
    
    @JsonProperty("descartabilidade")
    private Boolean descartabilidade;
    
    // Construtores
    public Hospitalar() {
        super();
    }
    
    public Hospitalar(String nome, Integer quantidadeTotal, Date validade,
                     UUID estoqueId, Boolean descartabilidade) {
        super(nome, quantidadeTotal, validade, estoqueId);
        this.descartabilidade = descartabilidade;
    }
    
    // Getters e Setters
    public Boolean getDescartabilidade() {
        return descartabilidade;
    }
    
    public void setDescartabilidade(Boolean descartabilidade) {
        this.descartabilidade = descartabilidade;
    }
}
