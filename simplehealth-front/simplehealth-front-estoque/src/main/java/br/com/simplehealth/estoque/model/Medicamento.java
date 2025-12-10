package br.com.simplehealth.estoque.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Modelo para medicamentos (extends Item)
 * Backend possui apenas: prescricao e targa
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Medicamento extends Item {
    
    @JsonProperty("prescricao")
    private String prescricao;
    
    @JsonProperty("targa")
    private String targa;
    
    // Construtores
    public Medicamento() {
        super();
    }
    
    public Medicamento(String nome, Integer quantidadeTotal, Date validade,
                      String prescricao, String targa) {
        super(nome, quantidadeTotal, validade);
        this.prescricao = prescricao;
        this.targa = targa;
    }
    
    // Getters e Setters
    public String getPrescricao() {
        return prescricao;
    }
    
    public void setPrescricao(String prescricao) {
        this.prescricao = prescricao;
    }
    
    public String getTarga() {
        return targa;
    }
    
    public void setTarga(String targa) {
        this.targa = targa;
    }
}
