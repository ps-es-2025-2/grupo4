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

public class MedicamentoController extends AbstractCrudController {
    
    private static final Logger logger = LoggerFactory.getLogger(MedicamentoController.class);
    
    @FXML private TableView<Medicamento> tableMedicamentos;
    @FXML private TableColumn<Medicamento, Long> colId;
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
    
    private final MedicamentoService service;
    private final ObservableList<Medicamento> medicamentos;
    private Medicamento medicamentoSelecionado;
    
    public MedicamentoController() {
        this.service = new MedicamentoService();
        this.medicamentos = FXCollections.observableArrayList();
    }
    
    @FXML
    public void initialize() {
        configurarTabela();
        carregarDados();
        
        tableMedicamentos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    medicamentoSelecionado = newSelection;
                    preencherFormulario(newSelection);
                }
            }
        );
        
        RefreshManager.getInstance().addListener(source -> {
            if (!"Medicamento".equals(source)) {
                carregarDados();
            }
        });
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
        txtDescricao.setText(medicamento.getDescricao());
        txtTipo.setText(medicamento.getTipo());
        txtUnidadeMedida.setText(medicamento.getUnidadeMedida());
        txtQuantidade.setText(String.valueOf(medicamento.getQuantidadeTotal()));
        txtLote.setText(medicamento.getLote());
        txtNf.setText(medicamento.getNf());
        txtPrescricao.setText(medicamento.getPrescricao());
        txtComposicao.setText(medicamento.getComposicao());
        txtBula.setText(medicamento.getBula());
        txtTarga.setText(medicamento.getTarga());
        txtModoConsumo.setText(medicamento.getModoConsumo());
    }
    
    @FXML
    private void handleNovo() {
        limparFormulario();
        medicamentoSelecionado = null;
    }
    
    @FXML
    private void handleSalvar() {
        try {
            if (!validarCampos()) return;
            
            Medicamento medicamento = medicamentoSelecionado != null ? 
                medicamentoSelecionado : new Medicamento();
            
            medicamento.setNome(txtNome.getText());
            medicamento.setDescricao(txtDescricao.getText());
            medicamento.setTipo(txtTipo.getText());
            medicamento.setUnidadeMedida(txtUnidadeMedida.getText());
            medicamento.setQuantidadeTotal(Integer.parseInt(txtQuantidade.getText()));
            medicamento.setValidade(LocalDateTime.now().plusYears(1)); // Default
            medicamento.setLote(txtLote.getText());
            medicamento.setNf(txtNf.getText());
            medicamento.setPrescricao(txtPrescricao.getText());
            medicamento.setComposicao(txtComposicao.getText());
            medicamento.setBula(txtBula.getText());
            medicamento.setTarga(txtTarga.getText());
            medicamento.setModoConsumo(txtModoConsumo.getText());
            
            if (medicamentoSelecionado != null && medicamentoSelecionado.getIdItem() != null) {
                service.atualizar(medicamentoSelecionado.getIdItem(), medicamento);
                mostrarSucesso("Medicamento atualizado com sucesso!");
            } else {
                service.salvar(medicamento);
                mostrarSucesso("Sucesso", "Medicamento cadastrado com sucesso!");
            }
            
            carregarDados();
            limparFormulario();
            RefreshManager.getInstance().notifyRefresh("Medicamento");
            
        } catch (Exception e) {
            logger.error("Erro ao salvar medicamento", e);
            mostrarErro("Erro", "Erro ao salvar: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeletar() {
        if (medicamentoSelecionado == null) {
            mostrarErro("Atenção", "Selecione um medicamento para deletar");
            return;
        }
        
        if (mostrarConfirmacao("Confirmar Exclusão", 
            "Deseja realmente excluir o medicamento: " + medicamentoSelecionado.getNome() + "?")) {
            try {
                service.deletar(medicamentoSelecionado.getIdItem());
                mostrarSucesso("Sucesso", "Medicamento deletado com sucesso!");
                carregarDados();
                limparFormulario();
                RefreshManager.getInstance().notifyRefresh("Medicamento");
            } catch (Exception e) {
                logger.error("Erro ao deletar medicamento", e);
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
        medicamentoSelecionado = null;
        tableMedicamentos.getSelectionModel().clearSelection();
    }
    
    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

