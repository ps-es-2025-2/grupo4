package br.com.simplehealth.armazenamento.model;

import br.com.simplehealth.armazenamento.util.FlexibleLocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;
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
    
    @JsonProperty("timestamp")
    @JsonDeserialize(using = FlexibleLocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate timestamp;

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

    public LocalDate getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDate timestamp) { this.timestamp = timestamp; }

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