package br.com.simplehealth.estoque.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Modelo para pedidos
 */
public class Pedido {
    
    @JsonProperty("idPedido")
    private UUID idPedido;
    
    @JsonProperty("dataPedido")
    private Date dataPedido;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("itemIds")
    private List<UUID> itemIds;
    
    @JsonProperty("fornecedorId")
    private UUID fornecedorId;
    
    // Construtores
    public Pedido() {
        this.itemIds = new ArrayList<>();
    }
    
    public Pedido(Date dataPedido, String status, UUID fornecedorId) {
        this.dataPedido = dataPedido;
        this.status = status;
        this.fornecedorId = fornecedorId;
        this.itemIds = new ArrayList<>();
    }
    
    // Getters e Setters
    public UUID getIdPedido() {
        return idPedido;
    }
    
    public void setIdPedido(UUID idPedido) {
        this.idPedido = idPedido;
    }
    
    public Date getDataPedido() {
        return dataPedido;
    }
    
    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<UUID> getItemIds() {
        return itemIds;
    }
    
    public void setItemIds(List<UUID> itemIds) {
        this.itemIds = itemIds;
    }
    
    public UUID getFornecedorId() {
        return fornecedorId;
    }
    
    public void setFornecedorId(UUID fornecedorId) {
        this.fornecedorId = fornecedorId;
    }
    
    @Override
    public String toString() {
        return "Pedido #" + idPedido + " - " + status;
    }
}
