package br.com.simplehealth.estoque.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Classe base abstrata para todos os tipos de itens do estoque
 */
public abstract class Item {
    
    @JsonProperty("idItem")
    private Long idItem;
    
    @JsonProperty("nome")
    private String nome;
    
    @JsonProperty("descricao")
    private String descricao;
    
    @JsonProperty("tipo")
    private String tipo;
    
    @JsonProperty("unidadeMedida")
    private String unidadeMedida;
    
    @JsonProperty("quantidadeTotal")
    private Integer quantidadeTotal;
    
    @JsonProperty("validade")
    private LocalDateTime validade;
    
    @JsonProperty("lote")
    private String lote;
    
    @JsonProperty("nf")
    private String nf;
    
    // Construtores
    public Item() {
    }
    
    public Item(String nome, String descricao, String tipo, String unidadeMedida, 
                Integer quantidadeTotal, LocalDateTime validade, String lote, String nf) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.unidadeMedida = unidadeMedida;
        this.quantidadeTotal = quantidadeTotal;
        this.validade = validade;
        this.lote = lote;
        this.nf = nf;
    }
    
    // Getters e Setters
    public Long getIdItem() {
        return idItem;
    }
    
    public void setIdItem(Long idItem) {
        this.idItem = idItem;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getUnidadeMedida() {
        return unidadeMedida;
    }
    
    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
    
    public Integer getQuantidadeTotal() {
        return quantidadeTotal;
    }
    
    public void setQuantidadeTotal(Integer quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }
    
    public LocalDateTime getValidade() {
        return validade;
    }
    
    public void setValidade(LocalDateTime validade) {
        this.validade = validade;
    }
    
    public String getLote() {
        return lote;
    }
    
    public void setLote(String lote) {
        this.lote = lote;
    }
    
    public String getNf() {
        return nf;
    }
    
    public void setNf(String nf) {
        this.nf = nf;
    }
    
    @Override
    public String toString() {
        return nome + " - " + tipo;
    }
}
