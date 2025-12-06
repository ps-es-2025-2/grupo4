package br.com.simplehealth.estoque.controller;

import br.com.simplehealth.estoque.model.Medicamento;
import br.com.simplehealth.estoque.service.MedicamentoService;
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
 * @deprecated Este controller usa MedicamentoService que está deprecado.
 * Considere refatorar para usar EntradaItensService com tipo=MEDICAMENTO.
 */
@Deprecated
public class MedicamentoController extends AbstractCrudController<Medicamento> {
    
    private static final Logger logger = LoggerFactory.getLogger(MedicamentoController.class);
    
    @FXML private TableView<Medicamento> tableMedicamentos;
    @FXML private TableColumn<Medicamento, UUID> colId;
    @FXML private TableColumn<Medicamento, String> colNome;
    @FXML private TableColumn<Medicamento, String> colTipo;
    @FXML private TableColumn<Medicamento, Integer> colQuantidade;
    @FXML private TableColumn<Medicamento, String> colPrescricao;
    
    @FXML private TextField txtNome;
    @FXML private TextArea txtDescricao;
    @FXML private TextField txtTipo;
    @FXML private TextField txtUnidadeMedida;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtLote;
    @FXML private TextField txtNf;
    @FXML private TextField txtPrescricao;
    @FXML private TextArea txtComposicao;
    @FXML private TextArea txtBula;
    @FXML private TextField txtTarga;
    @FXML private TextArea txtModoConsumo;
    
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
    
    private final MedicamentoService service;
    private final ObservableList<Medicamento> medicamentos;
    
    public MedicamentoController() {
        this.service = new MedicamentoService();
        this.medicamentos = FXCollections.observableArrayList();
    }
    
    @FXML
    public void initialize() {
        super.btnCriar = this.btnCriar;
        super.btnAlterar = this.btnAlterar;
        super.btnDeletar = this.btnDeletar;
        super.btnConfirmar = this.btnConfirmar;
        super.btnCancelar = this.btnCancelar;

        configurarTabela();
        carregarDados();
        
        tableMedicamentos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    itemSelecionado = newSelection;
                    preencherFormulario(newSelection);
                    habilitarBotoesSelecao();
                }
            }
        );
        
        RefreshManager.getInstance().addListener(source -> {
            if (!"Medicamento".equals(source)) {
                carregarDados();
            }
        });
        
        configurarEstadoInicialBotoes();
        habilitarCampos(false);
    }
    
    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidadeTotal"));
        colPrescricao.setCellValueFactory(new PropertyValueFactory<>("prescricao"));
        
        tableMedicamentos.setItems(medicamentos);
    }
    
    @Override
    protected void carregarDados() {
        try {
            medicamentos.clear();
            medicamentos.addAll(service.listar());
            logger.info("Medicamentos carregados: {}", medicamentos.size());
        } catch (Exception e) {
            logger.error("Erro ao carregar medicamentos", e);
            mostrarErro("Erro", "Erro ao carregar medicamentos: " + e.getMessage());
        }
    }
    
    private void preencherFormulario(Medicamento medicamento) {
        txtNome.setText(medicamento.getNome());
        txtQuantidade.setText(String.valueOf(medicamento.getQuantidadeTotal()));
        // txtDescricao, txtTipo, txtUnidadeMedida, txtLote, txtNf removidos - não existem mais no modelo
        txtPrescricao.setText(medicamento.getPrescricao());
        txtComposicao.setText(medicamento.getComposicao());
        txtBula.setText(medicamento.getBula());
        txtTarga.setText(medicamento.getTarga());
        txtModoConsumo.setText(medicamento.getModoConsumo());
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
            mostrarErro("Erro", "Selecione um medicamento para alterar.");
            return;
        }
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "ALTERAR";
    }
    
    @FXML
    private void handleAtualizar() {
        handleAlterar(); // Alias para compatibilidade com FXML
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
                mostrarSucesso("Sucesso", "Medicamento deletado com sucesso!");
            } else if ("CRIAR".equals(modoEdicao)) {
                if (!validarFormulario()) return;
                Medicamento medicamento = construirMedicamentoDoFormulario();
                service.salvar(medicamento);
                mostrarSucesso("Sucesso", "Medicamento cadastrado com sucesso!");
            } else if ("ALTERAR".equals(modoEdicao)) {
                if (!validarFormulario()) return;
                if (itemSelecionado == null || itemSelecionado.getIdItem() == null) {
                    mostrarErro("Erro", "Item selecionado inválido.");
                    return;
                }
                Medicamento medicamento = construirMedicamentoDoFormulario();
                medicamento.setIdItem(itemSelecionado.getIdItem());
                service.atualizar(itemSelecionado.getIdItem(), medicamento);
                mostrarSucesso("Sucesso", "Medicamento atualizado com sucesso!");
            }
            
            carregarDados();
            limparFormulario();
            resetarBotoes();
            habilitarCampos(false);
            modoEdicao = null;
            RefreshManager.getInstance().notifyRefresh("Medicamento");
            
        } catch (Exception e) {
            logger.error("Erro ao processar operação", e);
            mostrarErro("Erro", "Erro ao processar: " + e.getMessage());
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
            mostrarErro("Erro", "Selecione um medicamento para excluir");
            return;
        }
        ativarModoEdicao();
        modoEdicao = "DELETAR";
    }
    
    protected boolean validarFormulario() {
        // Validar nome obrigatório
        if (!br.com.simplehealth.estoque.util.ValidationUtils.validarCampoObrigatorio(txtNome.getText(), "Nome")) {
            mostrarErro("Validação", br.com.simplehealth.estoque.util.ValidationUtils.mensagemCampoObrigatorio("Nome"));
            return false;
        }
        
        // Validar quantidade obrigatória e positiva
        if (!br.com.simplehealth.estoque.util.ValidationUtils.validarQuantidade(txtQuantidade.getText())) {
            mostrarErro("Validação", "A quantidade deve ser um número inteiro positivo.");
            return false;
        }
        
        // Validar lote (se preenchido)
        if (!txtLote.getText().trim().isEmpty() && 
            !br.com.simplehealth.estoque.util.ValidationUtils.validarLote(txtLote.getText())) {
            mostrarErro("Validação", br.com.simplehealth.estoque.util.ValidationUtils.mensagemFormatoInvalido("Lote", "3-20 caracteres alfanuméricos"));
            return false;
        }
        
        // Validar nota fiscal (se preenchida)
        if (!txtNf.getText().trim().isEmpty() && 
            !br.com.simplehealth.estoque.util.ValidationUtils.validarNotaFiscal(txtNf.getText())) {
            mostrarErro("Validação", br.com.simplehealth.estoque.util.ValidationUtils.mensagemFormatoInvalido("Nota Fiscal", "6-44 caracteres"));
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
        txtPrescricao.clear();
        txtComposicao.clear();
        txtBula.clear();
        txtTarga.clear();
        txtModoConsumo.clear();
        tableMedicamentos.getSelectionModel().clearSelection();
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
        txtPrescricao.setDisable(!habilitar);
        txtComposicao.setDisable(!habilitar);
        txtBula.setDisable(!habilitar);
        txtTarga.setDisable(!habilitar);
        txtModoConsumo.setDisable(!habilitar);
    }
    
    private Medicamento construirMedicamentoDoFormulario() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNome(txtNome.getText());
        medicamento.setQuantidadeTotal(Integer.parseInt(txtQuantidade.getText()));
        medicamento.setValidade(new java.util.Date()); // Data atual
        // txtDescricao, txtTipo, txtUnidadeMedida, txtLote, txtNf removidos - não existem mais no modelo
        medicamento.setPrescricao(txtPrescricao.getText());
        medicamento.setComposicao(txtComposicao.getText());
        medicamento.setBula(txtBula.getText());
        medicamento.setTarga(txtTarga.getText());
        medicamento.setModoConsumo(txtModoConsumo.getText());
        return medicamento;
    }
}

