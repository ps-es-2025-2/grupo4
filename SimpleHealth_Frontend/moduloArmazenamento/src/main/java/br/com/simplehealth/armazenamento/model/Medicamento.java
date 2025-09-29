package br.com.simplehealth.armazenamento.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa um medicamento, que é um tipo específico de Item.
 * Herda todas as propriedades de Item e adiciona propriedades específicas de medicamentos.
 * 
 * @version 1.0
 */
public class Medicamento extends Item {
    
    @JsonProperty("prescricao")
    private String prescricao;
    
    @JsonProperty("composicao")
    private String composicao;
    
    @JsonProperty("bula")
    private String bula;
    
    @JsonProperty("tarja")
    private String tarja;
    
    @JsonProperty("modoConsumo")
    private String modoConsumo; // Ex: Injetável, pastilha, via oral

    // Construtores
    public Medicamento() {
        super();
        setTipo("MEDICAMENTO");
    }

    public Medicamento(String nome, String descricao, String unidadeMedida, 
                      Integer quantidadeTotal, java.time.LocalDate validade, String lote,
                      String prescricao, String composicao, String bula, String tarja, String modoConsumo) {
        super(nome, descricao, "MEDICAMENTO", unidadeMedida, quantidadeTotal, validade, lote);
        this.prescricao = prescricao;
        this.composicao = composicao;
        this.bula = bula;
        this.tarja = tarja;
        this.modoConsumo = modoConsumo;
    }

    // Getters e Setters
    public String getPrescricao() { return prescricao; }
    public void setPrescricao(String prescricao) { this.prescricao = prescricao; }

    public String getComposicao() { return composicao; }
    public void setComposicao(String composicao) { this.composicao = composicao; }

    public String getBula() { return bula; }
    public void setBula(String bula) { this.bula = bula; }

    public String getTarja() { return tarja; }
    public void setTarja(String tarja) { this.tarja = tarja; }

    public String getModoConsumo() { return modoConsumo; }
    public void setModoConsumo(String modoConsumo) { this.modoConsumo = modoConsumo; }

    @Override
    public String toString() {
        return "Medicamento{" +
                "idItem=" + getIdItem() +
                ", nome='" + getNome() + '\'' +
                ", tarja='" + tarja + '\'' +
                ", modoConsumo='" + modoConsumo + '\'' +
                ", quantidadeTotal=" + getQuantidadeTotal() +
                '}';
    }
}