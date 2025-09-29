package br.com.simplehealth.armazenamento.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Classe que representa um estoque.
 * 
 * @version 1.0
 */
public class Estoque {
    
    @JsonProperty("idEstoque")
    private Long idEstoque;
    
    @JsonProperty("local")
    private String local;
    
    @JsonProperty("item")
    private Item item;

    // Construtores
    public Estoque() {}

    public Estoque(String local) {
        this.local = local;
    }

    // Getters e Setters
    public Long getIdEstoque() { return idEstoque; }
    public void setIdEstoque(Long idEstoque) { this.idEstoque = idEstoque; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    /**
     * Método para obter o ID do item associado.
     * @return O ID do item ou null se não existir
     */
    public Long getIdItem() {
        return item != null ? item.getIdItem() : null;
    }

    @Override
    public String toString() {
        return "Estoque{" +
                "idEstoque=" + idEstoque +
                ", local='" + local + '\'' +
                ", item=" + (item != null ? item.getNome() : "null") +
                '}';
    }
}