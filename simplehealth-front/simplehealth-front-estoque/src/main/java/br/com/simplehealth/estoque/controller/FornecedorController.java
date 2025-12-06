package br.com.simplehealth.estoque.controller;

import br.com.simplehealth.estoque.model.Fornecedor;
import br.com.simplehealth.estoque.service.FornecedorService;
import br.com.simplehealth.estoque.util.RefreshManager;
import br.com.simplehealth.estoque.util.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class FornecedorController extends AbstractCrudController<Fornecedor> {
    
    private static final Logger logger = LoggerFactory.getLogger(FornecedorController.class);
    
    @FXML private TableView<Fornecedor> tableFornecedores;
    @FXML private TableColumn<Fornecedor, UUID> colId;
    @FXML private TableColumn<Fornecedor, String> colCnpj;
    
    @FXML private TextField txtCnpj;
    
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
    
    private final FornecedorService service;
    private final ObservableList<Fornecedor> fornecedores;
    
    public FornecedorController() {
        this.service = new FornecedorService();
        this.fornecedores = FXCollections.observableArrayList();
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
        habilitarCampos(false);
    }
    
    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idFornecedor"));
        colCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        tableFornecedores.setItems(fornecedores);
    }
    
    private void setupTableSelection() {
        tableFornecedores.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    itemSelecionado = newSelection;
                    preencherFormulario(newSelection);
                    habilitarBotoesSelecao();
                }
            }
        );
    }
    
    private void setupRefreshListener() {
        RefreshManager.getInstance().addListener(source -> {
            if (!"Fornecedor".equals(source)) {
                carregarDados();
            }
        });
    }
    
    @Override
    protected void carregarDados() {
        try {
            fornecedores.clear();
            fornecedores.addAll(service.listar());
            logger.info("Fornecedores carregados: {}", fornecedores.size());
        } catch (Exception e) {
            logger.error("Erro ao carregar fornecedores", e);
            mostrarErro("Erro", "Erro ao carregar fornecedores: " + e.getMessage());
        }
    }
    
    private void preencherFormulario(Fornecedor fornecedor) {
        txtCnpj.setText(fornecedor.getCnpj());
    }
    
    @FXML
    private void handleCriar() {
        limparFormulario();
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "CRIAR";
        txtCnpj.requestFocus();
    }
    
    @FXML
    private void handleAlterar() {
        if (itemSelecionado == null) {
            mostrarErro("Erro", "Selecione um fornecedor para alterar.");
            return;
        }
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "ALTERAR";
    }
    
    @FXML
    private void handleAtualizar() {
        handleAlterar();
    }
    
    @FXML
    private void handleConfirmar() {
        try {
            if ("DELETAR".equals(modoEdicao)) {
                if (itemSelecionado == null || itemSelecionado.getIdFornecedor() == null) {
                    mostrarErro("Erro", "Item selecionado inválido.");
                    return;
                }
                service.deletar(itemSelecionado.getIdFornecedor());
                mostrarSucesso("Sucesso", "Fornecedor deletado com sucesso!");
            } else if ("CRIAR".equals(modoEdicao)) {
                if (!validarFormulario()) return;
                Fornecedor fornecedor = construirFornecedorDoFormulario();
                service.salvar(fornecedor);
                mostrarSucesso("Sucesso", "Fornecedor cadastrado com sucesso!");
            } else if ("ALTERAR".equals(modoEdicao)) {
                if (!validarFormulario()) return;
                if (itemSelecionado == null || itemSelecionado.getIdFornecedor() == null) {
                    mostrarErro("Erro", "Item selecionado inválido.");
                    return;
                }
                Fornecedor fornecedor = construirFornecedorDoFormulario();
                fornecedor.setIdFornecedor(itemSelecionado.getIdFornecedor());
                service.atualizar(itemSelecionado.getIdFornecedor(), fornecedor);
                mostrarSucesso("Sucesso", "Fornecedor atualizado com sucesso!");
            }
            
            carregarDados();
            limparFormulario();
            resetarBotoes();
            habilitarCampos(false);
            modoEdicao = null;
            RefreshManager.getInstance().notifyRefresh("Fornecedor");
            
        } catch (Exception e) {
            logger.error("Erro ao processar operação", e);
            mostrarErro("Erro", "Erro ao salvar: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleCancelar() {
        limparFormulario();
        resetarBotoes();
        habilitarCampos(false);
        modoEdicao = null;
    }
    
    @FXML
    private void handleDeletar() {
        if (itemSelecionado == null || itemSelecionado.getIdFornecedor() == null) {
            mostrarErro("Erro", "Selecione um fornecedor para excluir");
            return;
        }
        ativarModoEdicao();
        modoEdicao = "DELETAR";
    }
    
    protected boolean validarFormulario() {
        // Validar CNPJ obrigatório
        if (!ValidationUtils.validarCampoObrigatorio(txtCnpj.getText(), "CNPJ")) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("CNPJ"));
            return false;
        }
        
        return true;
    }
    
    @Override
    protected void limparFormulario() {
        itemSelecionado = null;
        txtCnpj.clear();
        tableFornecedores.getSelectionModel().clearSelection();
    }
    
    @Override
    protected void habilitarCampos(boolean habilitar) {
        txtCnpj.setDisable(!habilitar);
    }
    
    private Fornecedor construirFornecedorDoFormulario() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCnpj(txtCnpj.getText().replaceAll("[./-]", "")); // Salva sem formatação
        return fornecedor;
    }
}
