package br.com.simplehealth.estoque.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

/**
 * DTO para baixa de insumos
 */
public class BaixaInsumoDTO {
    
    @JsonProperty("itemId")
    private UUID itemId;
    
    @JsonProperty("quantidadeNecessaria")
    private Integer quantidadeNecessaria;
    
    @JsonProperty("destinoConsumo")
    private String destinoConsumo;
    
    @JsonProperty("lote")
    private String lote;
    
    // Construtores
    public BaixaInsumoDTO() {
    }
    
    public BaixaInsumoDTO(UUID itemId, Integer quantidadeNecessaria, String destinoConsumo, String lote) {
        this.itemId = itemId;
        this.quantidadeNecessaria = quantidadeNecessaria;
        this.destinoConsumo = destinoConsumo;
        this.lote = lote;
    }
    
    // Getters e Setters
    public UUID getItemId() {
        return itemId;
    }
    
    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }
    
    public Integer getQuantidadeNecessaria() {
        return quantidadeNecessaria;
    }
    
    public void setQuantidadeNecessaria(Integer quantidadeNecessaria) {
        this.quantidadeNecessaria = quantidadeNecessaria;
    }
    
    public String getDestinoConsumo() {
        return destinoConsumo;
    }
    
    public void setDestinoConsumo(String destinoConsumo) {
        this.destinoConsumo = destinoConsumo;
    }
    
    public String getLote() {
        return lote;
    }
    
    public void setLote(String lote) {
        this.lote = lote;
    }
}
