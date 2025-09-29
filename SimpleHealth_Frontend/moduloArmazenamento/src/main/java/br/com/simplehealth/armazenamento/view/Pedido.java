package br.com.simplehealth.armazenamento.view;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * ViewModel para a entidade Pedido, usado para exibição na TableView do JavaFX.
 * 
 * @version 1.0
 */
public class Pedido {
    private final SimpleLongProperty idPedido;
    private final SimpleStringProperty dataPedido;
    private final SimpleStringProperty status;
    private final SimpleStringProperty nf;
    private final SimpleStringProperty fornecedorNome;
    private final SimpleIntegerProperty quantidadeItens;

    /**
     * Construtor da classe Pedido.
     */
    public Pedido(Long idPedido, String dataPedido, String status, String nf, String fornecedorNome, Integer quantidadeItens) {
        this.idPedido = new SimpleLongProperty(idPedido != null ? idPedido : 0L);
        this.dataPedido = new SimpleStringProperty(dataPedido != null ? dataPedido : "");
        this.status = new SimpleStringProperty(status != null ? status : "");
        this.nf = new SimpleStringProperty(nf != null ? nf : "");
        this.fornecedorNome = new SimpleStringProperty(fornecedorNome != null ? fornecedorNome : "");
        this.quantidadeItens = new SimpleIntegerProperty(quantidadeItens != null ? quantidadeItens : 0);
    }

    // Getters para as propriedades do JavaFX
    public Long getIdPedido() { return idPedido.get(); }
    public SimpleLongProperty idPedidoProperty() { return idPedido; }

    public String getDataPedido() { return dataPedido.get(); }
    public SimpleStringProperty dataPedidoProperty() { return dataPedido; }

    public String getStatus() { return status.get(); }
    public SimpleStringProperty statusProperty() { return status; }

    public String getNf() { return nf.get(); }
    public SimpleStringProperty nfProperty() { return nf; }

    public String getFornecedorNome() { return fornecedorNome.get(); }
    public SimpleStringProperty fornecedorNomeProperty() { return fornecedorNome; }

    public Integer getQuantidadeItens() { return quantidadeItens.get(); }
    public SimpleIntegerProperty quantidadeItensProperty() { return quantidadeItens; }
}