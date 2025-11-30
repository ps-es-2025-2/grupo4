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

public class FornecedorController extends AbstractCrudController {
    
    private static final Logger logger = LoggerFactory.getLogger(FornecedorController.class);
    
    @FXML private TableView<Fornecedor> tableFornecedores;
    @FXML private TableColumn<Fornecedor, Long> colId;
    @FXML private TableColumn<Fornecedor, String> colNome;
    @FXML private TableColumn<Fornecedor, String> colCnpj;
    @FXML private TableColumn<Fornecedor, String> colContato;
    
    @FXML private TextField txtNome;
    @FXML private TextField txtCnpj;
    @FXML private TextField txtContato;
    @FXML private TextArea txtEndereco;
    
    private final FornecedorService service;
    private final ObservableList<Fornecedor> fornecedores;
    private Fornecedor fornecedorSelecionado;
    
    public FornecedorController() {
        this.service = new FornecedorService();
        this.fornecedores = FXCollections.observableArrayList();
    }
    
    @FXML
    public void initialize() {
        setupTableColumns();
        carregarDados();
        setupTableSelection();
        setupRefreshListener();
        setupFormatters();
    }
    
    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idFornecedor"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        colContato.setCellValueFactory(new PropertyValueFactory<>("contato"));
        tableFornecedores.setItems(fornecedores);
    }
    
    private void setupTableSelection() {
        tableFornecedores.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    preencherFormulario(newSelection);
                    fornecedorSelecionado = newSelection;
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
    
    private void setupFormatters() {
        // Formatar CNPJ ao sair do campo
        txtCnpj.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && txtCnpj.getText() != null && !txtCnpj.getText().isEmpty()) {
                txtCnpj.setText(ValidationUtils.formatarCNPJ(txtCnpj.getText()));
            }
        });
        
        // Formatar telefone ao sair do campo
        txtContato.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && txtContato.getText() != null && !txtContato.getText().isEmpty()) {
                txtContato.setText(ValidationUtils.formatarTelefone(txtContato.getText()));
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
        txtNome.setText(fornecedor.getNome());
        txtCnpj.setText(fornecedor.getCnpj());
        txtContato.setText(fornecedor.getContato());
        txtEndereco.setText(fornecedor.getEndereco());
    }
    
    @FXML
    private void handleNovo() {
        limparFormulario();
        fornecedorSelecionado = null;
    }
    
    @FXML
    private void handleSalvar() {
        try {
            if (!validarCampos()) return;
            
            Fornecedor fornecedor = fornecedorSelecionado != null ? 
                fornecedorSelecionado : new Fornecedor();
            
            fornecedor.setNome(txtNome.getText());
            fornecedor.setCnpj(txtCnpj.getText().replaceAll("[./-]", "")); // Salva sem formatação
            fornecedor.setContato(txtContato.getText());
            fornecedor.setEndereco(txtEndereco.getText());
            
            if (fornecedorSelecionado != null && fornecedorSelecionado.getIdFornecedor() != null) {
                service.atualizar(fornecedorSelecionado.getIdFornecedor(), fornecedor);
                mostrarSucesso("Sucesso", "Fornecedor atualizado com sucesso!");
            } else {
                service.salvar(fornecedor);
                mostrarSucesso("Sucesso", "Fornecedor cadastrado com sucesso!");
            }
            
            carregarDados();
            limparFormulario();
            RefreshManager.getInstance().notifyRefresh("Fornecedor");
            
        } catch (Exception e) {
            logger.error("Erro ao salvar fornecedor", e);
            mostrarErro("Erro", "Erro ao salvar: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeletar() {
        if (fornecedorSelecionado == null) {
            mostrarErro("Atenção", "Selecione um fornecedor para deletar");
            return;
        }
        
        if (mostrarConfirmacao("Confirmar Exclusão", 
            "Deseja realmente excluir o fornecedor: " + fornecedorSelecionado.getNome() + "?")) {
            try {
                service.deletar(fornecedorSelecionado.getIdFornecedor());
                mostrarSucesso("Sucesso", "Fornecedor deletado com sucesso!");
                carregarDados();
                limparFormulario();
                RefreshManager.getInstance().notifyRefresh("Fornecedor");
            } catch (Exception e) {
                logger.error("Erro ao deletar fornecedor", e);
                mostrarErro("Erro", "Erro ao deletar: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleAtualizar() {
        carregarDados();
    }
    
    private boolean validarCampos() {
        // Validar nome obrigatório
        if (!ValidationUtils.validarCampoObrigatorio(txtNome.getText(), "Nome")) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("Nome"));
            return false;
        }
        
        // Validar CNPJ obrigatório e formato
        if (!ValidationUtils.validarCampoObrigatorio(txtCnpj.getText(), "CNPJ")) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("CNPJ"));
            return false;
        }
        
        if (!ValidationUtils.validarCNPJ(txtCnpj.getText())) {
            mostrarErro("Validação", ValidationUtils.mensagemFormatoInvalido("CNPJ", "00.000.000/0000-00"));
            return false;
        }
        
        // Validar contato (se preenchido)
        if (!txtContato.getText().trim().isEmpty() && 
            !ValidationUtils.validarTelefone(txtContato.getText())) {
            mostrarErro("Validação", ValidationUtils.mensagemFormatoInvalido("Contato", "(00) 0000-0000 ou (00) 00000-0000"));
            return false;
        }
        
        return true;
    }
    
    @Override
    protected void limparFormulario() {
        txtNome.clear();
        txtCnpj.clear();
        txtContato.clear();
        txtEndereco.clear();
        fornecedorSelecionado = null;
        tableFornecedores.getSelectionModel().clearSelection();
    }
}
