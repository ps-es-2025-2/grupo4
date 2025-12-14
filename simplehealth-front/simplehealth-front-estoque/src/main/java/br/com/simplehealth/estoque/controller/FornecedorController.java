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
    @FXML private TableColumn<Fornecedor, String> colNome;
    @FXML private TableColumn<Fornecedor, String> colTelefone;
    @FXML private TableColumn<Fornecedor, String> colEmail;
    
    @FXML private TextField txtCnpj;
    @FXML private TextField txtNome;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtEndereco;
    
    // Campos de busca
    @FXML private TextField txtBuscarNome;
    @FXML private Button btnBuscar;
    @FXML private Button btnLimparBusca;
    
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
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
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
            // Sempre recarrega quando houver notificação de outros módulos
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
            tableFornecedores.refresh(); // Forçar atualização visual
            logger.info("Fornecedores carregados: {}", fornecedores.size());
        } catch (Exception e) {
            logger.error("Erro ao carregar fornecedores", e);
            mostrarErro("Erro", "Erro ao carregar fornecedores: " + e.getMessage());
        }
    }
    
    private void preencherFormulario(Fornecedor fornecedor) {
        txtCnpj.setText(fornecedor.getCnpj());
        txtNome.setText(fornecedor.getNome());
        txtTelefone.setText(fornecedor.getTelefone());
        txtEmail.setText(fornecedor.getEmail());
        txtEndereco.setText(fornecedor.getEndereco());
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
            
            // Atualizar tabela imediatamente
            carregarDados();
            limparFormulario();
            resetarBotoes();
            habilitarCampos(false);
            modoEdicao = null;
            
            // Notificar outros módulos
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
        
        // Validar formato do CNPJ
        if (!ValidationUtils.validarCNPJ(txtCnpj.getText())) {
            mostrarErro("Validação", "CNPJ inválido. Digite um CNPJ válido (14 dígitos).");
            return false;
        }
        
        // Validar Nome obrigatório
        if (!ValidationUtils.validarCampoObrigatorio(txtNome.getText(), "Nome")) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("Nome"));
            return false;
        }
        
        // Validar Email (se preenchido)
        if (txtEmail.getText() != null && !txtEmail.getText().trim().isEmpty()) {
            if (!ValidationUtils.validarEmail(txtEmail.getText())) {
                mostrarErro("Validação", "E-mail inválido. Digite um e-mail válido.");
                return false;
            }
        }
        
        // Validar Telefone (se preenchido)
        if (txtTelefone.getText() != null && !txtTelefone.getText().trim().isEmpty()) {
            if (!ValidationUtils.validarTelefone(txtTelefone.getText())) {
                mostrarErro("Validação", "Telefone inválido. Digite um telefone válido (10 ou 11 dígitos).");
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    protected void limparFormulario() {
        itemSelecionado = null;
        txtCnpj.clear();
        txtNome.clear();
        txtTelefone.clear();
        txtEmail.clear();
        txtEndereco.clear();
        tableFornecedores.getSelectionModel().clearSelection();
    }
    
    @Override
    protected void habilitarCampos(boolean habilitar) {
        txtCnpj.setDisable(!habilitar);
        txtNome.setDisable(!habilitar);
        txtTelefone.setDisable(!habilitar);
        txtEmail.setDisable(!habilitar);
        txtEndereco.setDisable(!habilitar);
    }
    
    private Fornecedor construirFornecedorDoFormulario() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCnpj(txtCnpj.getText().replaceAll("[./-]", "")); // Salva sem formatação
        fornecedor.setNome(txtNome.getText());
        fornecedor.setTelefone(txtTelefone.getText());
        fornecedor.setEmail(txtEmail.getText());
        fornecedor.setEndereco(txtEndereco.getText());
        return fornecedor;
    }
    
    /**
     * Busca fornecedores por nome usando a API
     */
    @FXML
    public void handleBuscar() {
        String nome = txtBuscarNome.getText();
        
        if (nome == null || nome.trim().isEmpty()) {
            mostrarErro("Busca", "Digite um nome para buscar.");
            return;
        }
        
        try {
            logger.info("Buscando fornecedores por nome: {}", nome);
            var resultados = service.buscarPorNome(nome.trim());
            
            fornecedores.clear();
            fornecedores.addAll(resultados);
            tableFornecedores.refresh();
            
            logger.info("Busca concluída. {} fornecedor(es) encontrado(s)", resultados.size());
            
            if (resultados.isEmpty()) {
                mostrarErro("Busca", "Nenhum fornecedor encontrado com o nome: " + nome);
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar fornecedores", e);
            mostrarErro("Erro", "Erro ao buscar fornecedores: " + e.getMessage());
        }
    }
    
    /**
     * Limpa a busca e recarrega todos os fornecedores
     */
    @FXML
    public void handleLimparBusca() {
        txtBuscarNome.clear();
        carregarDados();
    }
}
