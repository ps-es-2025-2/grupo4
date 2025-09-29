package br.com.simplehealth.armazenamento.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe que representa um pedido.
 * 
 * @version 1.0
 */
public class Pedido {
    
    @JsonProperty("idPedido")
    private Long idPedido;
    
    @JsonProperty("dataPedido")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDate dataPedido;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("nf")
    private String nf; // Nota Fiscal
    
    @JsonProperty("fornecedor")
    private Fornecedor fornecedor;
    
    @JsonProperty("itens")
    private List<Item> itens;

    // Construtores
    public Pedido() {}

    public Pedido(LocalDate dataPedido, String status, String nf, Fornecedor fornecedor) {
        this.dataPedido = dataPedido;
        this.status = status;
        this.nf = nf;
        this.fornecedor = fornecedor;
    }

    // Getters e Setters
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

    public LocalDate getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDate dataPedido) { this.dataPedido = dataPedido; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNf() { return nf; }
    public void setNf(String nf) { this.nf = nf; }

    public Fornecedor getFornecedor() { return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

    public List<Item> getItens() { return itens; }
    public void setItens(List<Item> itens) { this.itens = itens; }

    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", dataPedido=" + dataPedido +
                ", status='" + status + '\'' +
                ", nf='" + nf + '\'' +
                ", fornecedor=" + (fornecedor != null ? fornecedor.getNome() : "null") +
                '}';
    }
}