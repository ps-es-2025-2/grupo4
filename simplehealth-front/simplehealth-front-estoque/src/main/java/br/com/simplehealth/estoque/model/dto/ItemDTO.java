package br.com.simplehealth.estoque.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.UUID;

/**
 * DTO para itens
 */
public class ItemDTO {
    
    @JsonProperty("itemId")
    private UUID itemId;
    
    @JsonProperty("nome")
    private String nome;
    
    @JsonProperty("quantidade")
    private Integer quantidade;
    
    @JsonProperty("validade")
    private Date validade;
    
    @JsonProperty("tipo")
    private String tipo; // ALIMENTO, MEDICAMENTO, HOSPITALAR
    
    @JsonProperty("lote")
    private String lote;
    
    @JsonProperty("confirmacaoGestor")
    private Boolean confirmacaoGestor;
    
    // Construtores
    public ItemDTO() {
    }
    
    public ItemDTO(UUID itemId, String nome, Integer quantidade, Date validade, 
                   String tipo, String lote, Boolean confirmacaoGestor) {
        this.itemId = itemId;
        this.nome = nome;
        this.quantidade = quantidade;
        this.validade = validade;
        this.tipo = tipo;
        this.lote = lote;
        this.confirmacaoGestor = confirmacaoGestor;
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
    
    public Integer getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
    
    public Date getValidade() {
        return validade;
    }
    
    public void setValidade(Date validade) {
        this.validade = validade;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getLote() {
        return lote;
    }
    
    public void setLote(String lote) {
        this.lote = lote;
    }
    
    public Boolean getConfirmacaoGestor() {
        return confirmacaoGestor;
    }
    
    public void setConfirmacaoGestor(Boolean confirmacaoGestor) {
        this.confirmacaoGestor = confirmacaoGestor;
    }
}
