package br.com.simplehealth.estoque.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

/**
 * Response da baixa de insumos
 */
public class BaixaInsumoResponse {
    
    @JsonProperty("itemId")
    private UUID itemId;
    
    @JsonProperty("nome")
    private String nome;
    
    @JsonProperty("quantidadeBaixada")
    private Integer quantidadeBaixada;
    
    @JsonProperty("saldoAnterior")
    private Integer saldoAnterior;
    
    @JsonProperty("saldoAtual")
    private Integer saldoAtual;
    
    @JsonProperty("lote")
    private String lote;
    
    @JsonProperty("estoqueCritico")
    private Boolean estoqueCritico;
    
    // Construtores
    public BaixaInsumoResponse() {
    }
    
    // Getters e Setters
    public UUID getItemId() {
        return itemId;
    }
    
    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public Integer getQuantidadeBaixada() {
        return quantidadeBaixada;
    }
    
    public void setQuantidadeBaixada(Integer quantidadeBaixada) {
        this.quantidadeBaixada = quantidadeBaixada;
    }
    
    public Integer getSaldoAnterior() {
        return saldoAnterior;
    }
    
    public void setSaldoAnterior(Integer saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }
    
    public Integer getSaldoAtual() {
        return saldoAtual;
    }
    
    public void setSaldoAtual(Integer saldoAtual) {
        this.saldoAtual = saldoAtual;
    }
    
    public String getLote() {
        return lote;
    }
    
    public void setLote(String lote) {
        this.lote = lote;
    }
    
    public Boolean getEstoqueCritico() {
        return estoqueCritico;
    }
    
    public void setEstoqueCritico(Boolean estoqueCritico) {
        this.estoqueCritico = estoqueCritico;
    }
}
