package br.com.simplehealth.estoque.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Modelo para alimentos (extends Item)
 * Backend possui apenas: alergenicos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Alimento extends Item {
    
    @JsonProperty("alergenicos")
    private String alergenicos;
    
    // Construtores
    public Alimento() {
        super();
    }
    
    public Alimento(String nome, Integer quantidadeTotal, Date validade,
                   String alergenicos) {
        super(nome, quantidadeTotal, validade);
        this.alergenicos = alergenicos;
    }
    
    // Getters e Setters
    public String getAlergenicos() {
        return alergenicos;
    }
    
    public void setAlergenicos(String alergenicos) {
        this.alergenicos = alergenicos;
    }
}
