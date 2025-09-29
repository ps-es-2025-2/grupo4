package br.com.simplehealth.armazenamento.view;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * ViewModel para a entidade Item, usado para exibição na TableView do JavaFX.
 * 
 * @version 1.0
 */
public class Item {
    private final SimpleLongProperty idItem;
    private final SimpleStringProperty nome;
    private final SimpleStringProperty tipo;
    private final SimpleStringProperty unidadeMedida;
    private final SimpleIntegerProperty quantidadeTotal;
    private final SimpleStringProperty validade;
    private final SimpleStringProperty lote;

    /**
     * Construtor da classe Item.
     */
    public Item(Long idItem, String nome, String tipo, String unidadeMedida, 
                Integer quantidadeTotal, String validade, String lote) {
        this.idItem = new SimpleLongProperty(idItem != null ? idItem : 0L);
        this.nome = new SimpleStringProperty(nome != null ? nome : "");
        this.tipo = new SimpleStringProperty(tipo != null ? tipo : "");
        this.unidadeMedida = new SimpleStringProperty(unidadeMedida != null ? unidadeMedida : "");
        this.quantidadeTotal = new SimpleIntegerProperty(quantidadeTotal != null ? quantidadeTotal : 0);
        this.validade = new SimpleStringProperty(validade != null ? validade : "");
        this.lote = new SimpleStringProperty(lote != null ? lote : "");
    }

    // Getters para as propriedades do JavaFX
    public Long getIdItem() { return idItem.get(); }
    public SimpleLongProperty idItemProperty() { return idItem; }

    public String getNome() { return nome.get(); }
    public SimpleStringProperty nomeProperty() { return nome; }

    public String getTipo() { return tipo.get(); }
    public SimpleStringProperty tipoProperty() { return tipo; }

    public String getUnidadeMedida() { return unidadeMedida.get(); }
    public SimpleStringProperty unidadeMedidaProperty() { return unidadeMedida; }

    public Integer getQuantidadeTotal() { return quantidadeTotal.get(); }
    public SimpleIntegerProperty quantidadeTotalProperty() { return quantidadeTotal; }

    public String getValidade() { return validade.get(); }
    public SimpleStringProperty validadeProperty() { return validade; }

    public String getLote() { return lote.get(); }
    public SimpleStringProperty loteProperty() { return lote; }
}