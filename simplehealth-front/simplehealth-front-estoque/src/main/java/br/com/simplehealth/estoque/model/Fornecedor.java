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
    
    @JsonProperty("nome")
    private String nome;
    
    @JsonProperty("telefone")
    private String telefone;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("endereco")
    private String endereco;
    
    // Construtores
    public Fornecedor() {
    }
    
    public Fornecedor(String cnpj, String nome) {
        this.cnpj = cnpj;
        this.nome = nome;
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
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getEndereco() {
        return endereco;
    }
    
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    @Override
    public String toString() {
        return "Fornecedor: " + nome + " (" + cnpj + ")";
    }
}
