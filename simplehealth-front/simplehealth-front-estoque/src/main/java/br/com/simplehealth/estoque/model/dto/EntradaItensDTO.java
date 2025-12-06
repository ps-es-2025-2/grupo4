package br.com.simplehealth.estoque.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO para entrada de itens
 */
public class EntradaItensDTO {
    
    @JsonProperty("nfNumero")
    private String nfNumero;
    
    @JsonProperty("fornecedorId")
    private UUID fornecedorId;
    
    @JsonProperty("pedidoId")
    private UUID pedidoId;
    
    @JsonProperty("itens")
    private List<ItemDTO> itens;
    
    // Construtores
    public EntradaItensDTO() {
        this.itens = new ArrayList<>();
    }
    
    public EntradaItensDTO(String nfNumero, UUID fornecedorId, UUID pedidoId, List<ItemDTO> itens) {
        this.nfNumero = nfNumero;
        this.fornecedorId = fornecedorId;
        this.pedidoId = pedidoId;
        this.itens = itens;
    }
    
    // Getters e Setters
    public String getNfNumero() {
        return nfNumero;
    }
    
    public void setNfNumero(String nfNumero) {
        this.nfNumero = nfNumero;
    }
    
    public UUID getFornecedorId() {
        return fornecedorId;
    }
    
    public void setFornecedorId(UUID fornecedorId) {
        this.fornecedorId = fornecedorId;
    }
    
    public UUID getPedidoId() {
        return pedidoId;
    }
    
    public void setPedidoId(UUID pedidoId) {
        this.pedidoId = pedidoId;
    }
    
    public List<ItemDTO> getItens() {
        return itens;
    }
    
    public void setItens(List<ItemDTO> itens) {
        this.itens = itens;
    }
}
