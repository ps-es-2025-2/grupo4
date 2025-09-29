package br.com.simplehealth.armazenamento.view;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * ViewModel para a entidade Estoque, usado para exibição na TableView do JavaFX.
 * 
 * @version 1.0
 */
public class Estoque {
    private final SimpleLongProperty idEstoque;
    private final SimpleStringProperty local;
    private final SimpleStringProperty nomeItem;

    /**
     * Construtor da classe Estoque.
     */
    public Estoque(Long idEstoque, String local, String nomeItem) {
        this.idEstoque = new SimpleLongProperty(idEstoque != null ? idEstoque : 0L);
        this.local = new SimpleStringProperty(local != null ? local : "");
        this.nomeItem = new SimpleStringProperty(nomeItem != null ? nomeItem : "");
    }

    // Getters para as propriedades do JavaFX
    public Long getIdEstoque() { return idEstoque.get(); }
    public SimpleLongProperty idEstoqueProperty() { return idEstoque; }

    public String getLocal() { return local.get(); }
    public SimpleStringProperty localProperty() { return local; }

    public String getNomeItem() { return nomeItem.get(); }
    public SimpleStringProperty nomeItemProperty() { return nomeItem; }
}