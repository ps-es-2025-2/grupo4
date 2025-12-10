package br.com.simplehealth.estoque.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

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
                     Boolean descartabilidade) {
        super(nome, quantidadeTotal, validade);
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
