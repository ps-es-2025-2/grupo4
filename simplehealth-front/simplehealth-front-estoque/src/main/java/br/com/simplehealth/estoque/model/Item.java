package br.com.simplehealth.estoque.model;

import br.com.simplehealth.estoque.util.DateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.UUID;

/**
 * Classe base abstrata para todos os tipos de itens do estoque
 * Backend: apenas 4 campos (idItem, nome, quantidadeTotal, validade)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Item {
    
    @JsonProperty("idItem")
    private UUID idItem;
    
    @JsonProperty("nome")
    private String nome;
    
    @JsonProperty("quantidadeTotal")
    private Integer quantidadeTotal;
    
    @JsonProperty("validade")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date validade;
    
    @JsonProperty("estoqueId")
    private UUID estoqueId;
    
    // Construtores
    public Item() {
    }
    
    public Item(String nome, Integer quantidadeTotal, Date validade, UUID estoqueId) {
        this.nome = nome;
        this.quantidadeTotal = quantidadeTotal;
        this.validade = validade;
        this.estoqueId = estoqueId;
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
    
    public UUID getEstoqueId() {
        return estoqueId;
    }
    
    public void setEstoqueId(UUID estoqueId) {
        this.estoqueId = estoqueId;
    }
    
    @Override
    public String toString() {
        return nome;
    }
}
