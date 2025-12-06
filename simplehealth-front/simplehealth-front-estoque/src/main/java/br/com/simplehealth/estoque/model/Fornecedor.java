package br.com.simplehealth.estoque.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

/**
 * Modelo para fornecedores
 */
public class Fornecedor {
    
    @JsonProperty("idFornecedor")
    private UUID idFornecedor;
    
    @JsonProperty("cnpj")
    private String cnpj;
    
    // Construtores
    public Fornecedor() {
    }
    
    public Fornecedor(String cnpj) {
        this.cnpj = cnpj;
    }
    
    // Getters e Setters
    public UUID getIdFornecedor() {
        return idFornecedor;
    }
    
    public void setIdFornecedor(UUID idFornecedor) {
        this.idFornecedor = idFornecedor;
    }
    
    public String getCnpj() {
        return cnpj;
    }
    
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    @Override
    public String toString() {
        return "Fornecedor: " + cnpj;
    }
}
