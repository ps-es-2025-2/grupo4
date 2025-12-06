package br.com.simplehealth.estoque.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.UUID;

/**
 * Classe base abstrata para todos os tipos de itens do estoque
 * Backend: apenas 4 campos (idItem, nome, quantidadeTotal, validade)
 */
public abstract class Item {
    
    @JsonProperty("idItem")
    private UUID idItem;
    
    @JsonProperty("nome")
    private String nome;
    
    @JsonProperty("quantidadeTotal")
    private Integer quantidadeTotal;
    
    @JsonProperty("validade")
    private Date validade;
    
    // Construtores
    public Item() {
    }
    
    public Item(String nome, Integer quantidadeTotal, Date validade) {
        this.nome = nome;
        this.quantidadeTotal = quantidadeTotal;
        this.validade = validade;
    }
    
    // Getters e Setters
    public UUID getIdItem() {
        return idItem;
    }
    
    public void setIdItem(UUID idItem) {
        this.idItem = idItem;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public Integer getQuantidadeTotal() {
        return quantidadeTotal;
    }
    
    public void setQuantidadeTotal(Integer quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }
    
    public Date getValidade() {
        return validade;
    }
    
    public void setValidade(Date validade) {
        this.validade = validade;
    }
    
    @Override
    public String toString() {
        return nome;
    }
}
