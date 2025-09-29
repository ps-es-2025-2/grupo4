package br.com.simplehealth.armazenamento.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa um alimento, que é um tipo específico de Item.
 * Herda todas as propriedades de Item e adiciona propriedades específicas de alimentos.
 * 
 * @version 1.0
 */
public class Alimento extends Item {
    
    @JsonProperty("alergenicos")
    private String alergenicos;
    
    @JsonProperty("tipoArmazenamento")
    private String tipoArmazenamento; // Ex: Perecível, não perecível, refrigerado

    // Construtores
    public Alimento() {
        super();
        setTipo("ALIMENTO");
    }

    public Alimento(String nome, String descricao, String unidadeMedida, 
                   Integer quantidadeTotal, java.time.LocalDate validade, String lote,
                   String alergenicos, String tipoArmazenamento) {
        super(nome, descricao, "ALIMENTO", unidadeMedida, quantidadeTotal, validade, lote);
        this.alergenicos = alergenicos;
        this.tipoArmazenamento = tipoArmazenamento;
    }

    // Getters e Setters
    public String getAlergenicos() { return alergenicos; }
    public void setAlergenicos(String alergenicos) { this.alergenicos = alergenicos; }

    public String getTipoArmazenamento() { return tipoArmazenamento; }
    public void setTipoArmazenamento(String tipoArmazenamento) { this.tipoArmazenamento = tipoArmazenamento; }

    @Override
    public String toString() {
        return "Alimento{" +
                "idItem=" + getIdItem() +
                ", nome='" + getNome() + '\'' +
                ", tipoArmazenamento='" + tipoArmazenamento + '\'' +
                ", alergenicos='" + alergenicos + '\'' +
                ", quantidadeTotal=" + getQuantidadeTotal() +
                '}';
    }
}