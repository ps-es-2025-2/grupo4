package br.com.simplehealth.estoque.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Modelo para itens hospitalares (extends Item)
 */
public class Hospitalar extends Item {
    
    @JsonProperty("descartabilidade")
    private Boolean descartabilidade;
    
    @JsonProperty("uso")
    private String uso;
    
    // Construtores
    public Hospitalar() {
        super();
    }
    
    public Hospitalar(String nome, String descricao, String tipo, String unidadeMedida,
                     Integer quantidadeTotal, LocalDateTime validade, String lote, String nf,
                     Boolean descartabilidade, String uso) {
        super(nome, descricao, tipo, unidadeMedida, quantidadeTotal, validade, lote, nf);
        this.descartabilidade = descartabilidade;
        this.uso = uso;
    }
    
    // Getters e Setters
    public Boolean getDescartabilidade() {
        return descartabilidade;
    }
    
    public void setDescartabilidade(Boolean descartabilidade) {
        this.descartabilidade = descartabilidade;
    }
    
    public String getUso() {
        return uso;
    }
    
    public void setUso(String uso) {
        this.uso = uso;
    }
}
