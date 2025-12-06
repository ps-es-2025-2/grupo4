package br.com.simplehealth.estoque.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Modelo para alimentos (extends Item)
 */
public class Alimento extends Item {
    
    @JsonProperty("alergenicos")
    private String alergenicos;
    
    @JsonProperty("tipoArmazenamento")
    private String tipoArmazenamento;
    
    // Construtores
    public Alimento() {
        super();
    }
    
    public Alimento(String nome, Integer quantidadeTotal, Date validade,
                   String alergenicos, String tipoArmazenamento) {
        super(nome, quantidadeTotal, validade);
        this.alergenicos = alergenicos;
        this.tipoArmazenamento = tipoArmazenamento;
    }
    
    // Getters e Setters
    public String getAlergenicos() {
        return alergenicos;
    }
    
    public void setAlergenicos(String alergenicos) {
        this.alergenicos = alergenicos;
    }
    
    public String getTipoArmazenamento() {
        return tipoArmazenamento;
    }
    
    public void setTipoArmazenamento(String tipoArmazenamento) {
        this.tipoArmazenamento = tipoArmazenamento;
    }
}
