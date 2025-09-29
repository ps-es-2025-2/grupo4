package br.com.simplehealth.armazenamento.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa um fornecedor.
 * 
 * @version 1.0
 */
public class Fornecedor {
    
    @JsonProperty("idFornecedor")
    private Long idFornecedor;
    
    @JsonProperty("nome")
    private String nome;
    
    @JsonProperty("cnpj")
    private String cnpj;
    
    @JsonProperty("contato")
    private String contato;
    
    @JsonProperty("endereco")
    private String endereco;

    // Construtores
    public Fornecedor() {}

    public Fornecedor(String nome, String cnpj, String contato, String endereco) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.contato = contato;
        this.endereco = endereco;
    }

    // Getters e Setters
    public Long getIdFornecedor() { return idFornecedor; }
    public void setIdFornecedor(Long idFornecedor) { this.idFornecedor = idFornecedor; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    @Override
    public String toString() {
        return "Fornecedor{" +
                "idFornecedor=" + idFornecedor +
                ", nome='" + nome + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", contato='" + contato + '\'' +
                '}';
    }
}