package br.com.simplehealth.estoque.controller;

import br.com.simplehealth.estoque.model.Estoque;
import br.com.simplehealth.estoque.service.EstoqueService;
import br.com.simplehealth.estoque.util.RefreshManager;
import br.com.simplehealth.estoque.util.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EstoqueController extends AbstractCrudController {
    
    private static final Logger logger = LoggerFactory.getLogger(EstoqueController.class);
    
    @FXML private TableView<Estoque> tableEstoques;
    @FXML private TableColumn<Estoque, Long> colId;
    @FXML private TableColumn<Estoque, String> colLocal;
    @FXML private TableColumn<Estoque, String> colItem;
    
    @FXML private TextField txtLocal;
    @FXML private TextField txtIdItem;
    
    private final EstoqueService service;
    private final ObservableList<Estoque> estoques;
    private Estoque estoqueSelecionado;
    
    public EstoqueController() {
        this.service = new EstoqueService();
        this.estoques = FXCollections.observableArrayList();
    }
    
    @FXML
    public void initialize() {
        setupTableColumns();
        carregarDados();
        setupTableSelection();
        setupRefreshListener();
    }
    
    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEstoque"));
        colLocal.setCellValueFactory(new PropertyValueFactory<>("local"));
        colItem.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getItem() != null ? 
                cellData.getValue().getItem().getNome() : "N/A"
            )
        );
        tableEstoques.setItems(estoques);
    }
    
    private void setupTableSelection() {
        tableEstoques.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    preencherFormulario(newSelection);
                    estoqueSelecionado = newSelection;
                }
            }
        );
    }
    
    private void setupRefreshListener() {
        RefreshManager.getInstance().addListener(source -> {
            if (!"Estoque".equals(source)) {
                carregarDados();
            }
        });
    }
    
    @Override
    protected void carregarDados() {
        try {
            estoques.clear();
            estoques.addAll(service.listar());
            logger.info("Estoques carregados: {}", estoques.size());
        } catch (Exception e) {
            logger.error("Erro ao carregar estoques", e);
            mostrarErro("Erro", "Erro ao carregar estoques: " + e.getMessage());
        }
    }
    
    private void preencherFormulario(Estoque estoque) {
        txtLocal.setText(estoque.getLocal());
        if (estoque.getItem() != null && estoque.getItem().getIdItem() != null) {
            txtIdItem.setText(String.valueOf(estoque.getItem().getIdItem()));
        }
    }
    
    @FXML
    private void handleNovo() {
        limparFormulario();
        estoqueSelecionado = null;
    }
    
    @FXML
    private void handleSalvar() {
        try {
            if (!validarCampos()) return;
            
            Estoque estoque = estoqueSelecionado != null ? 
                estoqueSelecionado : new Estoque();
            
            estoque.setLocal(txtLocal.getText());
            // Note: Item deve ser configurado via relacionamento no backend
            
            if (estoqueSelecionado != null && estoqueSelecionado.getIdEstoque() != null) {
                service.atualizar(estoqueSelecionado.getIdEstoque(), estoque);
                mostrarSucesso("Sucesso", "Estoque atualizado com sucesso!");
            } else {
                service.salvar(estoque);
                mostrarSucesso("Sucesso", "Estoque cadastrado com sucesso!");
            }
            
            carregarDados();
            limparFormulario();
            RefreshManager.getInstance().notifyRefresh("Estoque");
            
        } catch (Exception e) {
            logger.error("Erro ao salvar estoque", e);
            mostrarErro("Erro", "Erro ao salvar: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeletar() {
        if (estoqueSelecionado == null) {
            mostrarErro("Atenção", "Selecione um estoque para deletar");
            return;
        }
        
        if (mostrarConfirmacao("Confirmar Exclusão", 
            "Deseja realmente excluir o estoque no local: " + estoqueSelecionado.getLocal() + "?")) {
            try {
                service.deletar(estoqueSelecionado.getIdEstoque());
                mostrarSucesso("Sucesso", "Estoque deletado com sucesso!");
                carregarDados();
                limparFormulario();
                RefreshManager.getInstance().notifyRefresh("Estoque");
            } catch (Exception e) {
                logger.error("Erro ao deletar estoque", e);
                mostrarErro("Erro", "Erro ao deletar: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleAtualizar() {
        carregarDados();
    }
    
    private boolean validarCampos() {
        if (!ValidationUtils.validarCampoObrigatorio(txtLocal.getText(), "Local")) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("Local"));
            return false;
        }
        
        return true;
    }
    
    @Override
    protected void limparFormulario() {
        txtLocal.clear();
        txtIdItem.clear();
        estoqueSelecionado = null;
        tableEstoques.getSelectionModel().clearSelection();
    }
}
