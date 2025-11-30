package br.com.simplehealth.estoque.controller;

import br.com.simplehealth.estoque.model.Item;
import br.com.simplehealth.estoque.service.ItemService;
import br.com.simplehealth.estoque.util.RefreshManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

public class ItemController extends AbstractCrudController {
    
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    
    @FXML private TableView<Item> tableItens;
    @FXML private TableColumn<Item, Long> colId;
    @FXML private TableColumn<Item, String> colNome;
    @FXML private TableColumn<Item, String> colTipo;
    @FXML private TableColumn<Item, Integer> colQuantidade;
    @FXML private TableColumn<Item, LocalDateTime> colValidade;
    @FXML private TableColumn<Item, String> colLote;
    
    @FXML private TextField txtBusca;
    
    private final ItemService service;
    private final ObservableList<Item> itens;
    private Item itemSelecionado;
    
    public ItemController() {
        this.service = new ItemService();
        this.itens = FXCollections.observableArrayList();
    }
    
    @FXML
    public void initialize() {
        setupTableColumns();
        carregarDados();
        setupTableSelection();
        setupRefreshListener();
    }
    
    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidadeTotal"));
        colValidade.setCellValueFactory(new PropertyValueFactory<>("validade"));
        colLote.setCellValueFactory(new PropertyValueFactory<>("lote"));
        
        tableItens.setItems(itens);
    }
    
    private void setupTableSelection() {
        tableItens.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                itemSelecionado = newSelection;
            }
        );
    }
    
    private void setupRefreshListener() {
        RefreshManager.getInstance().addListener(source -> {
            carregarDados();
        });
    }
    
    @Override
    protected void carregarDados() {
        try {
            itens.clear();
            itens.addAll(service.listar());
            logger.info("Itens carregados: {}", itens.size());
        } catch (Exception e) {
            logger.error("Erro ao carregar itens", e);
            mostrarErro("Erro", "Erro ao carregar itens: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleBuscar() {
        String busca = txtBusca.getText().trim().toLowerCase();
        
        if (busca.isEmpty()) {
            carregarDados();
            return;
        }
        
        try {
            ObservableList<Item> todoItens = FXCollections.observableArrayList(service.listar());
            ObservableList<Item> filtrados = todoItens.filtered(item -> 
                item.getNome().toLowerCase().contains(busca) ||
                (item.getTipo() != null && item.getTipo().toLowerCase().contains(busca)) ||
                (item.getLote() != null && item.getLote().toLowerCase().contains(busca))
            );
            
            itens.clear();
            itens.addAll(filtrados);
            logger.info("Itens filtrados: {}", itens.size());
            
        } catch (Exception e) {
            logger.error("Erro ao buscar itens", e);
            mostrarErro("Erro", "Erro ao buscar: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeletar() {
        if (itemSelecionado == null) {
            mostrarErro("Atenção", "Selecione um item para deletar");
            return;
        }
        
        if (mostrarConfirmacao("Confirmar Exclusão", 
            "Deseja realmente excluir o item: " + itemSelecionado.getNome() + "?")) {
            try {
                service.deletar(itemSelecionado.getIdItem());
                mostrarSucesso("Sucesso", "Item deletado com sucesso!");
                carregarDados();
                itemSelecionado = null;
                RefreshManager.getInstance().notifyRefresh("Item");
            } catch (Exception e) {
                logger.error("Erro ao deletar item", e);
                mostrarErro("Erro", "Erro ao deletar: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleAtualizar() {
        carregarDados();
    }
    
    @Override
    protected void limparFormulario() {
        txtBusca.clear();
        itemSelecionado = null;
        tableItens.getSelectionModel().clearSelection();
    }
}
