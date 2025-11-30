package br.com.simplehealth.estoque.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo para pedidos
 */
public class Pedido {
    
    @JsonProperty("idPedido")
    private Long idPedido;
    
    @JsonProperty("dataPedido")
    private LocalDateTime dataPedido;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("fornecedor")
    private Fornecedor fornecedor;
    
    @JsonProperty("itens")
    private List<Item> itens;
    
    // Construtores
    public Pedido() {
        this.itens = new ArrayList<>();
    }
    
    public Pedido(LocalDateTime dataPedido, String status, Fornecedor fornecedor) {
        this.dataPedido = dataPedido;
        this.status = status;
        this.fornecedor = fornecedor;
        this.itens = new ArrayList<>();
    }
    
    // Getters e Setters
    public Long getIdPedido() {
        return idPedido;
    }
    
    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }
    
    public LocalDateTime getDataPedido() {
        return dataPedido;
    }
    
    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Fornecedor getFornecedor() {
        return fornecedor;
    }
    
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
    
    public List<Item> getItens() {
        return itens;
    }
    
    public void setItens(List<Item> itens) {
        this.itens = itens;
    }
    
    @Override
    public String toString() {
        return "Pedido #" + idPedido + " - " + status + " - " + 
               (fornecedor != null ? fornecedor.getNome() : "Sem fornecedor");
    }
}
