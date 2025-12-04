package br.com.simplehealth.estoque.controller;

import br.com.simplehealth.estoque.model.Alimento;
import br.com.simplehealth.estoque.service.AlimentoService;
import br.com.simplehealth.estoque.util.RefreshManager;
import br.com.simplehealth.estoque.util.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class AlimentoController extends AbstractCrudController<Alimento> {
    
    private static final Logger logger = LoggerFactory.getLogger(AlimentoController.class);
    
    @FXML private TableView<Alimento> tableAlimentos;
    @FXML private TableColumn<Alimento, Long> colId;
    @FXML private TableColumn<Alimento, String> colNome;
    @FXML private TableColumn<Alimento, String> colTipo;
    @FXML private TableColumn<Alimento, Integer> colQuantidade;
    @FXML private TableColumn<Alimento, String> colArmazenamento;
    
    @FXML private TextField txtNome;
    @FXML private TextArea txtDescricao;
    @FXML private TextField txtTipo;
    @FXML private TextField txtUnidadeMedida;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtLote;
    @FXML private TextField txtNf;
    @FXML private TextArea txtAlergenicos;
    @FXML private ComboBox<String> cbTipoArmazenamento;
    
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
    
    private final AlimentoService service;
    private final ObservableList<Alimento> alimentos;
    
    public AlimentoController() {
        this.service = new AlimentoService();
        this.alimentos = FXCollections.observableArrayList();
    }
    
    @FXML
    public void initialize() {
        super.btnCriar = this.btnCriar;
        super.btnAlterar = this.btnAlterar;
        super.btnDeletar = this.btnDeletar;
        super.btnConfirmar = this.btnConfirmar;
        super.btnCancelar = this.btnCancelar;

        setupTableColumns();
        setupComboBox();
        carregarDados();
        setupTableSelection();
        setupRefreshListener();
        configurarEstadoInicialBotoes();
        habilitarCampos(false);
    }
    
    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidadeTotal"));
        colArmazenamento.setCellValueFactory(new PropertyValueFactory<>("tipoArmazenamento"));
        
        tableAlimentos.setItems(alimentos);
    }
    
    private void setupComboBox() {
        cbTipoArmazenamento.setItems(FXCollections.observableArrayList(
            "Ambiente",
            "Refrigerado",
            "Congelado",
            "Seco",
            "Climatizado"
        ));
    }
    
    private void setupTableSelection() {
        tableAlimentos.getSelectionModel().selectedItemProperty().addListener(
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
            if (!"Alimento".equals(source)) {
                carregarDados();
            }
        });
    }
    
    @Override
    protected void carregarDados() {
        try {
            alimentos.clear();
            alimentos.addAll(service.listar());
            logger.info("Alimentos carregados: {}", alimentos.size());
        } catch (Exception e) {
            logger.error("Erro ao carregar alimentos", e);
            mostrarErro("Erro", "Erro ao carregar alimentos: " + e.getMessage());
        }
    }
    
    private void preencherFormulario(Alimento alimento) {
        txtNome.setText(alimento.getNome());
        txtDescricao.setText(alimento.getDescricao());
        txtTipo.setText(alimento.getTipo());
        txtUnidadeMedida.setText(alimento.getUnidadeMedida());
        txtQuantidade.setText(String.valueOf(alimento.getQuantidadeTotal()));
        txtLote.setText(alimento.getLote());
        txtNf.setText(alimento.getNf());
        txtAlergenicos.setText(alimento.getAlergenicos());
        cbTipoArmazenamento.setValue(alimento.getTipoArmazenamento());
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
            mostrarErro("Erro", "Selecione um alimento para alterar.");
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
                if (itemSelecionado == null || itemSelecionado.getIdItem() == null) {
                    mostrarErro("Erro", "Item selecionado inválido.");
                    return;
                }
                service.deletar(itemSelecionado.getIdItem());
                mostrarSucesso("Sucesso", "Alimento deletado com sucesso!");
            } else if ("CRIAR".equals(modoEdicao)) {
                if (!validarFormulario()) return;
                Alimento alimento = construirAlimentoDoFormulario();
                service.salvar(alimento);
                mostrarSucesso("Sucesso", "Alimento cadastrado com sucesso!");
            } else if ("ALTERAR".equals(modoEdicao)) {
                if (!validarFormulario()) return;
                if (itemSelecionado == null || itemSelecionado.getIdItem() == null) {
                    mostrarErro("Erro", "Item selecionado inválido.");
                    return;
                }
                Alimento alimento = construirAlimentoDoFormulario();
                alimento.setIdItem(itemSelecionado.getIdItem());
                service.atualizar(itemSelecionado.getIdItem(), alimento);
                mostrarSucesso("Sucesso", "Alimento atualizado com sucesso!");
            }
            
            carregarDados();
            limparFormulario();
            resetarBotoes();
            habilitarCampos(false);
            modoEdicao = null;
            RefreshManager.getInstance().notifyRefresh("Alimento");
            
        } catch (Exception e) {
            logger.error("Erro ao salvar alimento", e);
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
        if (itemSelecionado == null || itemSelecionado.getIdItem() == null) {
            mostrarErro("Erro", "Selecione um alimento para excluir");
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
        
        // Validar quantidade obrigatória e positiva
        if (!ValidationUtils.validarQuantidade(txtQuantidade.getText())) {
            mostrarErro("Validação", "A quantidade deve ser um número inteiro positivo.");
            return false;
        }
        
        // Validar tipo de armazenamento obrigatório
        if (cbTipoArmazenamento.getValue() == null) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("Tipo de Armazenamento"));
            return false;
        }
        
        // Validar lote (se preenchido)
        if (!txtLote.getText().trim().isEmpty() && 
            !ValidationUtils.validarLote(txtLote.getText())) {
            mostrarErro("Validação", ValidationUtils.mensagemFormatoInvalido("Lote", "3-20 caracteres alfanuméricos"));
            return false;
        }
        
        // Validar nota fiscal (se preenchida)
        if (!txtNf.getText().trim().isEmpty() && 
            !ValidationUtils.validarNotaFiscal(txtNf.getText())) {
            mostrarErro("Validação", ValidationUtils.mensagemFormatoInvalido("Nota Fiscal", "6-44 caracteres"));
            return false;
        }
        
        return true;
    }
    
    @Override
    protected void limparFormulario() {
        itemSelecionado = null;
        txtNome.clear();
        txtDescricao.clear();
        txtTipo.clear();
        txtUnidadeMedida.clear();
        txtQuantidade.clear();
        txtLote.clear();
        txtNf.clear();
        txtAlergenicos.clear();
        cbTipoArmazenamento.setValue(null);
        tableAlimentos.getSelectionModel().clearSelection();
    }
    
    @Override
    protected void habilitarCampos(boolean habilitar) {
        txtNome.setDisable(!habilitar);
        txtDescricao.setDisable(!habilitar);
        txtTipo.setDisable(!habilitar);
        txtUnidadeMedida.setDisable(!habilitar);
        txtQuantidade.setDisable(!habilitar);
        txtLote.setDisable(!habilitar);
        txtNf.setDisable(!habilitar);
        txtAlergenicos.setDisable(!habilitar);
        cbTipoArmazenamento.setDisable(!habilitar);
    }
    
    private Alimento construirAlimentoDoFormulario() {
        Alimento alimento = new Alimento();
        alimento.setNome(txtNome.getText());
        alimento.setDescricao(txtDescricao.getText());
        alimento.setTipo(txtTipo.getText());
        alimento.setUnidadeMedida(txtUnidadeMedida.getText());
        alimento.setQuantidadeTotal(Integer.parseInt(txtQuantidade.getText()));
        alimento.setValidade(LocalDateTime.now().plusMonths(6)); // Default 6 meses
        alimento.setLote(txtLote.getText());
        alimento.setNf(txtNf.getText());
        alimento.setAlergenicos(txtAlergenicos.getText());
        alimento.setTipoArmazenamento(cbTipoArmazenamento.getValue());
        return alimento;
    }
}
