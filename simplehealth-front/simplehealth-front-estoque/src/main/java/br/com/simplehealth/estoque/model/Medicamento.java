package br.com.simplehealth.estoque.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Modelo para medicamentos (extends Item)
 */
public class Medicamento extends Item {
    
    @JsonProperty("prescricao")
    private String prescricao;
    
    @JsonProperty("composicao")
    private String composicao;
    
    @JsonProperty("bula")
    private String bula;
    
    @JsonProperty("targa")
    private String targa;
    
    @JsonProperty("modoConsumo")
    private String modoConsumo;
    
    // Construtores
    public Medicamento() {
        super();
    }
    
    public Medicamento(String nome, String descricao, String tipo, String unidadeMedida,
                      Integer quantidadeTotal, LocalDateTime validade, String lote, String nf,
                      String prescricao, String composicao, String bula, String targa, String modoConsumo) {
        super(nome, descricao, tipo, unidadeMedida, quantidadeTotal, validade, lote, nf);
        this.prescricao = prescricao;
        this.composicao = composicao;
        this.bula = bula;
        this.targa = targa;
        this.modoConsumo = modoConsumo;
    }
    
    // Getters e Setters
    public String getPrescricao() {
        return prescricao;
    }
    
    public void setPrescricao(String prescricao) {
        this.prescricao = prescricao;
    }
    
    public String getComposicao() {
        return composicao;
    }
    
    public void setComposicao(String composicao) {
        this.composicao = composicao;
    }
    
    public String getBula() {
        return bula;
    }
    
    public void setBula(String bula) {
        this.bula = bula;
    }
    
    public String getTarga() {
        return targa;
    }
    
    public void setTarga(String targa) {
        this.targa = targa;
    }
    
    public String getModoConsumo() {
        return modoConsumo;
    }
    
    public void setModoConsumo(String modoConsumo) {
        this.modoConsumo = modoConsumo;
    }
}
