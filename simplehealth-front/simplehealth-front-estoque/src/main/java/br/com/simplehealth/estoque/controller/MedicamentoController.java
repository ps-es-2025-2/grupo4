package br.com.simplehealth.estoque.controller;

import br.com.simplehealth.estoque.model.Estoque;
import br.com.simplehealth.estoque.model.Medicamento;
import br.com.simplehealth.estoque.service.EstoqueService;
import br.com.simplehealth.estoque.service.MedicamentoService;
import br.com.simplehealth.estoque.util.RefreshManager;
import br.com.simplehealth.estoque.util.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class MedicamentoController extends AbstractCrudController<Medicamento> {
    
    private static final Logger logger = LoggerFactory.getLogger(MedicamentoController.class);
    
    @FXML private TableView<Medicamento> tableMedicamentos;
    @FXML private TableColumn<Medicamento, UUID> colId;
    @FXML private TableColumn<Medicamento, String> colNome;
    @FXML private TableColumn<Medicamento, Integer> colQuantidade;
    @FXML private TableColumn<Medicamento, String> colEstoque;
    @FXML private TableColumn<Medicamento, String> colPrescricao;
    @FXML private TableColumn<Medicamento, String> colTarga;
    
    @FXML private TextField txtNome;
    @FXML private TextField txtQuantidade;
    @FXML private DatePicker dpValidade;
    @FXML private ComboBox<Estoque> cbEstoque;
    @FXML private TextField txtPrescricao;
    @FXML private TextField txtTarga;
    
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
    private final EstoqueService estoqueService;
    private final ObservableList<Medicamento> medicamentos;
    
    public MedicamentoController() {
        this.service = new MedicamentoService();
        this.estoqueService = new EstoqueService();
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
        setupComboBoxEstoque();
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
    
    private void setupComboBoxEstoque() {
        try {
            cbEstoque.setItems(FXCollections.observableArrayList(estoqueService.listar()));
            cbEstoque.setConverter(new StringConverter<Estoque>() {
                @Override
                public String toString(Estoque estoque) {
                    return estoque != null ? estoque.getNome() + " - " + estoque.getLocalizacao() : "";
                }
                
                @Override
                public Estoque fromString(String string) {
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("Erro ao carregar estoques", e);
            mostrarErro("Erro", "Erro ao carregar estoques: " + extrairMensagemErro(e));
        }
    }
    
    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidadeTotal"));
        colEstoque.setCellValueFactory(cellData -> {
            try {
                UUID estoqueId = cellData.getValue().getEstoqueId();
                if (estoqueId != null) {
                    return estoqueService.listar().stream()
                        .filter(e -> e.getIdEstoque().equals(estoqueId))
                        .findFirst()
                        .map(e -> new javafx.beans.property.SimpleStringProperty(e.getNome()))
                        .orElse(new javafx.beans.property.SimpleStringProperty("N/A"));
                }
            } catch (Exception e) {
                logger.error("Erro ao buscar estoque", e);
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        colPrescricao.setCellValueFactory(new PropertyValueFactory<>("prescricao"));
        colTarga.setCellValueFactory(new PropertyValueFactory<>("targa"));
        
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
            mostrarErro("Erro", "Erro ao carregar medicamentos: " + extrairMensagemErro(e));
        }
    }
    
    private void preencherFormulario(Medicamento medicamento) {
        txtNome.setText(medicamento.getNome());
        txtQuantidade.setText(String.valueOf(medicamento.getQuantidadeTotal()));
        
        if (medicamento.getValidade() != null) {
            LocalDate localDate = medicamento.getValidade().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
            dpValidade.setValue(localDate);
        }
        
        if (medicamento.getEstoqueId() != null) {
            cbEstoque.getItems().stream()
                .filter(e -> e.getIdEstoque().equals(medicamento.getEstoqueId()))
                .findFirst()
                .ifPresent(cbEstoque::setValue);
        }
        
        txtPrescricao.setText(medicamento.getPrescricao());
        txtTarga.setText(medicamento.getTarga());
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
            mostrarErro("Erro", "Erro ao processar: " + extrairMensagemErro(e));
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
        if (!ValidationUtils.validarCampoObrigatorio(txtNome.getText(), "Nome")) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("Nome"));
            return false;
        }
        
        // Validar quantidade obrigatória e positiva
        if (!ValidationUtils.validarQuantidade(txtQuantidade.getText())) {
            mostrarErro("Validação", "A quantidade deve ser um número inteiro positivo.");
            return false;
        }
        
        // Validar validade obrigatória
        if (dpValidade.getValue() == null) {
            mostrarErro("Validação", "A data de validade é obrigatória.");
            return false;
        }
        
        // Validar estoque obrigatório
        if (cbEstoque.getValue() == null) {
            mostrarErro("Validação", "Selecione um estoque.");
            return false;
        }
        
        return true;
    }
    
    @Override
    protected void limparFormulario() {
        itemSelecionado = null;
        txtNome.clear();
        txtQuantidade.clear();
        dpValidade.setValue(null);
        cbEstoque.setValue(null);
        txtPrescricao.clear();
        txtTarga.clear();
        tableMedicamentos.getSelectionModel().clearSelection();
    }
    
    @Override
    protected void habilitarCampos(boolean habilitar) {
        txtNome.setDisable(!habilitar);
        txtQuantidade.setDisable(!habilitar);
        dpValidade.setDisable(!habilitar);
        cbEstoque.setDisable(!habilitar);
        txtPrescricao.setDisable(!habilitar);
        txtTarga.setDisable(!habilitar);
    }
    
    private Medicamento construirMedicamentoDoFormulario() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNome(txtNome.getText());
        medicamento.setQuantidadeTotal(Integer.parseInt(txtQuantidade.getText()));
        
        LocalDate localDate = dpValidade.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        medicamento.setValidade(date);
        
        medicamento.setEstoqueId(cbEstoque.getValue().getIdEstoque());
        medicamento.setPrescricao(txtPrescricao.getText());
        medicamento.setTarga(txtTarga.getText());
        return medicamento;
    }
}

