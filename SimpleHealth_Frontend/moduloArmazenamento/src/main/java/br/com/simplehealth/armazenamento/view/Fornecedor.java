package br.com.simplehealth.armazenamento.view;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * ViewModel para a entidade Fornecedor, usado para exibição na TableView do JavaFX.
 * 
 * @version 1.0
 */
public class Fornecedor {
    private final SimpleLongProperty idFornecedor;
    private final SimpleStringProperty nome;
    private final SimpleStringProperty cnpj;
    private final SimpleStringProperty contato;
    private final SimpleStringProperty endereco;

    /**
     * Construtor da classe Fornecedor.
     */
    public Fornecedor(Long idFornecedor, String nome, String cnpj, String contato, String endereco) {
        this.idFornecedor = new SimpleLongProperty(idFornecedor != null ? idFornecedor : 0L);
        this.nome = new SimpleStringProperty(nome != null ? nome : "");
        this.cnpj = new SimpleStringProperty(cnpj != null ? cnpj : "");
        this.contato = new SimpleStringProperty(contato != null ? contato : "");
        this.endereco = new SimpleStringProperty(endereco != null ? endereco : "");
    }

    // Getters para as propriedades do JavaFX
    public Long getIdFornecedor() { return idFornecedor.get(); }
    public SimpleLongProperty idFornecedorProperty() { return idFornecedor; }

    public String getNome() { return nome.get(); }
    public SimpleStringProperty nomeProperty() { return nome; }

    public String getCnpj() { return cnpj.get(); }
    public SimpleStringProperty cnpjProperty() { return cnpj; }

    public String getContato() { return contato.get(); }
    public SimpleStringProperty contatoProperty() { return contato; }

    public String getEndereco() { return endereco.get(); }
    public SimpleStringProperty enderecoProperty() { return endereco; }
}