package br.com.simplehealth.estoque.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

/**
 * Modelo para controle de estoque
 */
public class Estoque {
    
    @JsonProperty("idEstoque")
    private UUID idEstoque;
    
    @JsonProperty("local")
    private String local;
    
    // Construtores
    public Estoque() {
    }
    
    public Estoque(String local) {
        this.local = local;
    }
    
    // Getters e Setters
    public UUID getIdEstoque() {
        return idEstoque;
    }
    
    public void setIdEstoque(UUID idEstoque) {
        this.idEstoque = idEstoque;
    }
    
    public String getLocal() {
        return local;
    }
    
    public void setLocal(String local) {
        this.local = local;
    }
    
    @Override
    public String toString() {
        return "Estoque: " + local;
    }
}
