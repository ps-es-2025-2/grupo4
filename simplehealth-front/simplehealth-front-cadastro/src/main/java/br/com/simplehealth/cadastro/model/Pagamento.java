package br.com.simplehealth.cadastro.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * Modelo de dados para Pagamento.
 */
public class Pagamento {

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("valor")
    private BigDecimal valor;

    public Pagamento() {
    }

    public Pagamento(String descricao, BigDecimal valor) {
        this.descricao = descricao;
        this.valor = valor;
    }

    // Getters e Setters
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return descricao + " - R$ " + valor;
    }
}
