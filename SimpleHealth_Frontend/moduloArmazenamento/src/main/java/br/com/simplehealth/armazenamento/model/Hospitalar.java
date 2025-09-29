package br.com.simplehealth.armazenamento.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa um item hospitalar, que é um tipo específico de Item.
 * Herda todas as propriedades de Item e adiciona propriedades específicas de itens hospitalares.
 * 
 * @version 1.0
 */
public class Hospitalar extends Item {
    
    @JsonProperty("descartabilidade")
    private Boolean descartavel;
    
    @JsonProperty("uso")
    private String uso; // Ex: Geral, cirúrgico, curativo

    // Construtores
    public Hospitalar() {
        super();
        setTipo("HOSPITALAR");
    }

    public Hospitalar(String nome, String descricao, String unidadeMedida, 
                     Integer quantidadeTotal, java.time.LocalDate validade, String lote,
                     Boolean descartavel, String uso) {
        super(nome, descricao, "HOSPITALAR", unidadeMedida, quantidadeTotal, validade, lote);
        this.descartavel = descartavel;
        this.uso = uso;
    }

    // Getters e Setters
    public Boolean getDescartavel() { return descartavel; }
    public void setDescartavel(Boolean descartavel) { this.descartavel = descartavel; }

    public String getUso() { return uso; }
    public void setUso(String uso) { this.uso = uso; }

    @Override
    public String toString() {
        return "Hospitalar{" +
                "idItem=" + getIdItem() +
                ", nome='" + getNome() + '\'' +
                ", descartavel=" + descartavel +
                ", uso='" + uso + '\'' +
                ", quantidadeTotal=" + getQuantidadeTotal() +
                '}';
    }
}