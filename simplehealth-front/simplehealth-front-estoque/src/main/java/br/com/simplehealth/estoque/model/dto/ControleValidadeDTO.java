package br.com.simplehealth.estoque.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para controle de validade
 */
public class ControleValidadeDTO {
    
    @JsonProperty("diasAntecedencia")
    private Integer diasAntecedencia;
    
    @JsonProperty("incluirVencidos")
    private Boolean incluirVencidos;
    
    @JsonProperty("descartarItens")
    private Boolean descartarItens;
    
    @JsonProperty("codigoCusto")
    private String codigoCusto;
    
    // Construtores
    public ControleValidadeDTO() {
    }
    
    public ControleValidadeDTO(Integer diasAntecedencia, Boolean incluirVencidos, 
                                Boolean descartarItens, String codigoCusto) {
        this.diasAntecedencia = diasAntecedencia;
        this.incluirVencidos = incluirVencidos;
        this.descartarItens = descartarItens;
        this.codigoCusto = codigoCusto;
    }
    
    // Getters e Setters
    public Integer getDiasAntecedencia() {
        return diasAntecedencia;
    }
    
    public void setDiasAntecedencia(Integer diasAntecedencia) {
        this.diasAntecedencia = diasAntecedencia;
    }
    
    public Boolean getIncluirVencidos() {
        return incluirVencidos;
    }
    
    public void setIncluirVencidos(Boolean incluirVencidos) {
        this.incluirVencidos = incluirVencidos;
    }
    
    public Boolean getDescartarItens() {
        return descartarItens;
    }
    
    public void setDescartarItens(Boolean descartarItens) {
        this.descartarItens = descartarItens;
    }
    
    public String getCodigoCusto() {
        return codigoCusto;
    }
    
    public void setCodigoCusto(String codigoCusto) {
        this.codigoCusto = codigoCusto;
    }
}
