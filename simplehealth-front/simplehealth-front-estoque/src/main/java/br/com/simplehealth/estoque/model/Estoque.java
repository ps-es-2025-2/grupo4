package br.com.simplehealth.estoque.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Modelo para controle de estoque
 */
public class Estoque {
    
    @JsonProperty("idEstoque")
    private Long idEstoque;
    
    @JsonProperty("local")
    private String local;
    
    @JsonProperty("item")
    private Item item;
    
    // Construtores
    public Estoque() {
    }
    
    public Estoque(String local, Item item) {
        this.local = local;
        this.item = item;
    }
    
    // Getters e Setters
    public Long getIdEstoque() {
        return idEstoque;
    }
    
    public void setIdEstoque(Long idEstoque) {
        this.idEstoque = idEstoque;
    }
    
    public String getLocal() {
        return local;
    }
    
    public void setLocal(String local) {
        this.local = local;
    }
    
    public Item getItem() {
        return item;
    }
    
    public void setItem(Item item) {
        this.item = item;
    }
    
    @Override
    public String toString() {
        return local + " - " + (item != null ? item.getNome() : "Sem item");
    }
}
