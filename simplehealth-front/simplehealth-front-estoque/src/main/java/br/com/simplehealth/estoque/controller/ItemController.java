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
import java.util.UUID;

/**
 * @deprecated Este controller usa ItemService que está deprecado.
 * Considere refatorar para usar EntradaItensService.
 */
@Deprecated
public class ItemController extends AbstractCrudController<Item> {
    
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    
    @FXML private TableView<Item> tableItens;
    @FXML private TableColumn<Item, UUID> colId;
    @FXML private TableColumn<Item, String> colNome;
    @FXML private TableColumn<Item, String> colTipo;
    @FXML private TableColumn<Item, Integer> colQuantidade;
    @FXML private TableColumn<Item, LocalDateTime> colValidade;
    @FXML private TableColumn<Item, String> colLote;
    
    @FXML private TextField txtBusca;
    
    @FXML
    private Button btnCriar;
    @FXML
    private Button btnAlterar;
    @FXML
    private Button btnDeletar;
    @FXML
    private Button btnConfirmar;
    @FXML
    private Button btnCancelar;
    
    private final ItemService service;
    private final ObservableList<Item> itens;
    
    public ItemController() {
        this.service = new ItemService();
        this.itens = FXCollections.observableArrayList();
    }
    
    @FXML
    public void initialize() {
        super.btnCriar = this.btnCriar;
        super.btnAlterar = this.btnAlterar;
        super.btnDeletar = this.btnDeletar;
        super.btnConfirmar = this.btnConfirmar;
        super.btnCancelar = this.btnCancelar;

        setupTableColumns();
        carregarDados();
        setupTableSelection();
        setupRefreshListener();
        configurarEstadoInicialBotoes();
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
                if (newSelection != null) {
                    itemSelecionado = newSelection;
                    if (modoEdicao == null) {
                        btnDeletar.setDisable(false);
                    }
                }
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
                item.getNome().toLowerCase().contains(busca)
                // getTipo() e getLote() removidos - não existem mais no modelo
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
        if (itemSelecionado == null || itemSelecionado.getIdItem() == null) {
            mostrarErro("Erro", "Selecione um item para excluir");
            return;
        }
        
        if (mostrarConfirmacao("Confirmar Exclusão", 
            "Deseja realmente excluir o item: " + itemSelecionado.getNome() + "?")) {
            try {
                service.deletar(itemSelecionado.getIdItem());
                mostrarSucesso("Sucesso", "Item deletado com sucesso!");
                carregarDados();
                limparFormulario();
                resetarBotoes();
                RefreshManager.getInstance().notifyRefresh("Item");
            } catch (Exception e) {
                logger.error("Erro ao deletar item", e);
                mostrarErro("Erro", "Erro ao deletar: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleAtualizar() {
        mostrarErro("Não Implementado", "Use os CRUDs específicos (Medicamento, Alimento, etc.) para editar itens.");
    }
    
    @Override
    protected void limparFormulario() {
        itemSelecionado = null;
        txtBusca.clear();
        tableItens.getSelectionModel().clearSelection();
    }
    
    @Override
    protected void habilitarCampos(boolean habilitar) {
        // ItemController não tem campos de edição, apenas busca
        txtBusca.setDisable(!habilitar);
    }
    
    @Override
    protected boolean validarFormulario() {
        // ItemController não tem formulário de edição
        return true;
    }
}
