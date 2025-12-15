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

import java.util.UUID;

public class EstoqueController extends AbstractCrudController<Estoque> {
    
    private static final Logger logger = LoggerFactory.getLogger(EstoqueController.class);
    
    @FXML private TableView<Estoque> tableEstoques;
    @FXML private TableColumn<Estoque, UUID> colId;
    @FXML private TableColumn<Estoque, String> colNome;
    @FXML private TableColumn<Estoque, String> colLocalizacao;
    @FXML private TableColumn<Estoque, String> colSetor;
    
    @FXML private TextField txtNome;
    @FXML private TextField txtLocalizacao;
    @FXML private TextField txtSetor;
    
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
    
    private final EstoqueService service;
    private final ObservableList<Estoque> estoques;
    
    public EstoqueController() {
        this.service = new EstoqueService();
        this.estoques = FXCollections.observableArrayList();
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
        colId.setCellValueFactory(new PropertyValueFactory<>("idEstoque"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colLocalizacao.setCellValueFactory(new PropertyValueFactory<>("localizacao"));
        colSetor.setCellValueFactory(new PropertyValueFactory<>("setor"));
        tableEstoques.setItems(estoques);
    }
    
    private void setupTableSelection() {
        tableEstoques.getSelectionModel().selectedItemProperty().addListener(
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
            tableEstoques.refresh(); // Forçar atualização visual
            logger.info("Estoques carregados: {}", estoques.size());
        } catch (Exception e) {
            logger.error("Erro ao carregar estoques", e);
            mostrarErro("Erro", "Erro ao carregar estoques: " + extrairMensagemErro(e));
        }
    }
    
    private void preencherFormulario(Estoque estoque) {
        txtNome.setText(estoque.getNome());
        txtLocalizacao.setText(estoque.getLocalizacao());
        txtSetor.setText(estoque.getSetor());
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
            mostrarErro("Erro", "Selecione um estoque para alterar.");
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
                if (itemSelecionado == null || itemSelecionado.getIdEstoque() == null) {
                    mostrarErro("Erro", "Item selecionado inválido.");
                    return;
                }
                service.deletar(itemSelecionado.getIdEstoque());
                mostrarSucesso("Sucesso", "Estoque deletado com sucesso!");
            } else if ("CRIAR".equals(modoEdicao)) {
                if (!validarFormulario()) return;
                Estoque estoque = construirEstoqueDoFormulario();
                service.salvar(estoque);
                mostrarSucesso("Sucesso", "Estoque cadastrado com sucesso!");
            } else if ("ALTERAR".equals(modoEdicao)) {
                if (!validarFormulario()) return;
                if (itemSelecionado == null || itemSelecionado.getIdEstoque() == null) {
                    mostrarErro("Erro", "Item selecionado inválido.");
                    return;
                }
                Estoque estoque = construirEstoqueDoFormulario();
                estoque.setIdEstoque(itemSelecionado.getIdEstoque());
                service.atualizar(itemSelecionado.getIdEstoque(), estoque);
                mostrarSucesso("Sucesso", "Estoque atualizado com sucesso!");
            }
            
            // Atualizar tabela imediatamente
            carregarDados();
            limparFormulario();
            resetarBotoes();
            habilitarCampos(false);
            modoEdicao = null;
            
            // Notificar outros módulos
            RefreshManager.getInstance().notifyRefresh("Estoque");
            
        } catch (Exception e) {
            logger.error("Erro ao processar operação", e);
            mostrarErro("Erro", "Erro ao salvar: " + extrairMensagemErro(e));
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
        if (itemSelecionado == null || itemSelecionado.getIdEstoque() == null) {
            mostrarErro("Erro", "Selecione um estoque para excluir");
            return;
        }
        ativarModoEdicao();
        modoEdicao = "DELETAR";
    }
    
    protected boolean validarFormulario() {
        if (!ValidationUtils.validarCampoObrigatorio(txtNome.getText(), "Nome")) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("Nome"));
            return false;
        }
        
        if (!ValidationUtils.validarCampoObrigatorio(txtLocalizacao.getText(), "Localização")) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("Localização"));
            return false;
        }
        
        return true;
    }
    
    @Override
    protected void limparFormulario() {
        itemSelecionado = null;
        txtNome.clear();
        txtLocalizacao.clear();
        txtSetor.clear();
        tableEstoques.getSelectionModel().clearSelection();
    }
    
    @Override
    protected void habilitarCampos(boolean habilitar) {
        txtNome.setDisable(!habilitar);
        txtLocalizacao.setDisable(!habilitar);
        txtSetor.setDisable(!habilitar);
    }
    
    private Estoque construirEstoqueDoFormulario() {
        Estoque estoque = new Estoque();
        estoque.setNome(txtNome.getText());
        estoque.setLocalizacao(txtLocalizacao.getText());
        estoque.setSetor(txtSetor.getText());
        return estoque;
    }
}
