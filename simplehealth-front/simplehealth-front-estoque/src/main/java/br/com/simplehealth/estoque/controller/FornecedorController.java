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

public class FornecedorController extends AbstractCrudController<Fornecedor> {
    
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
        setupFormatters();
        configurarEstadoInicialBotoes();
        habilitarCampos(false);
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
    private void handleCriar() {
        limparFormulario();
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "CRIAR";
        txtNome.requestFocus();
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
        itemSelecionado = null;
        txtNome.clear();
        txtCnpj.clear();
        txtContato.clear();
        txtEndereco.clear();
        tableFornecedores.getSelectionModel().clearSelection();
    }
    
    @Override
    protected void habilitarCampos(boolean habilitar) {
        txtNome.setDisable(!habilitar);
        txtCnpj.setDisable(!habilitar);
        txtContato.setDisable(!habilitar);
        txtEndereco.setDisable(!habilitar);
    }
    
    private Fornecedor construirFornecedorDoFormulario() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(txtNome.getText());
        fornecedor.setCnpj(txtCnpj.getText().replaceAll("[./-]", "")); // Salva sem formatação
        fornecedor.setContato(txtContato.getText());
        fornecedor.setEndereco(txtEndereco.getText());
        return fornecedor;
    }
}
