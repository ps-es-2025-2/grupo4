package br.com.simplehealth.agendamento.controller;

import br.com.simplehealth.agendamento.model.BloqueioAgenda;
import br.com.simplehealth.agendamento.service.BloqueioAgendaService;
import br.com.simplehealth.agendamento.util.RefreshManager;
import br.com.simplehealth.agendamento.util.ValidationUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Controller para gerenciar a interface de Bloqueios de Agenda.
 */
public class BloqueioAgendaController extends AbstractCrudController<BloqueioAgenda> implements RefreshManager.RefreshListener {

    private static final Logger logger = LoggerFactory.getLogger(BloqueioAgendaController.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML private TableView<BloqueioAgenda> tableView;
    @FXML private TableColumn<BloqueioAgenda, String> colId;
    @FXML private TableColumn<BloqueioAgenda, String> colMedicoCrm;
    @FXML private TableColumn<BloqueioAgenda, String> colMotivo;
    @FXML private TableColumn<BloqueioAgenda, String> colDataInicio;
    @FXML private TableColumn<BloqueioAgenda, String> colDataFim;
    @FXML private TableColumn<BloqueioAgenda, Boolean> colAtivo;

    @FXML private TextField txtMedicoCrm;
    @FXML private TextField txtDataInicio;
    @FXML private TextField txtDataFim;
    @FXML private TextField txtAntecedenciaMinima;
    @FXML private TextArea txtMotivo;
    @FXML private TextField txtUsuarioCriador;
    @FXML private CheckBox chkAtivo;

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

    private final BloqueioAgendaService service;
    private final ObservableList<BloqueioAgenda> bloqueios;

    public BloqueioAgendaController() {
        this.service = new BloqueioAgendaService();
        this.bloqueios = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        super.btnCriar = this.btnCriar;
        super.btnAlterar = this.btnAlterar;
        super.btnDeletar = this.btnDeletar;
        super.btnConfirmar = this.btnConfirmar;
        super.btnCancelar = this.btnCancelar;

        logger.info("Inicializando BloqueioAgendaController");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMedicoCrm.setCellValueFactory(new PropertyValueFactory<>("medicoCrm"));
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colDataInicio.setCellValueFactory(cellData -> {
            LocalDateTime dataInicio = cellData.getValue().getDataInicio();
            return new javafx.beans.property.SimpleStringProperty(
                dataInicio != null ? dataInicio.format(DATE_TIME_FORMATTER) : ""
            );
        });
        colDataFim.setCellValueFactory(cellData -> {
            LocalDateTime dataFim = cellData.getValue().getDataFim();
            return new javafx.beans.property.SimpleStringProperty(
                dataFim != null ? dataFim.format(DATE_TIME_FORMATTER) : ""
            );
        });
        colAtivo.setCellValueFactory(new PropertyValueFactory<>("ativo"));

        tableView.setItems(bloqueios);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                itemSelecionado = newSelection;
                preencherFormulario(newSelection);
                habilitarBotoesSelecao();
            }
        });

        // Configurar estado inicial dos botões
        configurarEstadoInicialBotoes();
        habilitarCampos(false);

        RefreshManager.getInstance().addListener(this);
        carregarDados();
    }

    protected void carregarDados() {
        Platform.runLater(() -> {
            try {
                bloqueios.clear();
                bloqueios.addAll(service.listarTodos());
                logger.info("Dados carregados: {} bloqueios", bloqueios.size());
            } catch (Exception e) {
                logger.error("Erro ao carregar bloqueios", e);
                mostrarErro("Erro ao carregar dados", e.getMessage());
            }
        });
    }

    private void preencherFormulario(BloqueioAgenda bloqueio) {
        txtMedicoCrm.setText(bloqueio.getMedicoCrm());
        txtDataInicio.setText(bloqueio.getDataInicio() != null ? 
            bloqueio.getDataInicio().format(DATE_TIME_FORMATTER) : "");
        txtDataFim.setText(bloqueio.getDataFim() != null ? 
            bloqueio.getDataFim().format(DATE_TIME_FORMATTER) : "");
        txtAntecedenciaMinima.setText(bloqueio.getAntecedenciaMinima() != null ? 
            bloqueio.getAntecedenciaMinima().toString() : "");
        txtMotivo.setText(bloqueio.getMotivo());
        txtUsuarioCriador.setText(bloqueio.getUsuarioCriadorLogin());
        chkAtivo.setSelected(bloqueio.getAtivo() != null && bloqueio.getAtivo());
    }

    protected void limparFormulario() {
        itemSelecionado = null;
        txtMedicoCrm.clear();
        txtDataInicio.clear();
        txtDataFim.clear();
        txtAntecedenciaMinima.clear();
        txtMotivo.clear();
        txtUsuarioCriador.clear();
        chkAtivo.setSelected(true);
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleCriar() {
        limparFormulario();
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "CRIAR";
        txtMedicoCrm.requestFocus();
    }

    @FXML
    private void handleAlterar() {
        if (itemSelecionado == null) {
            mostrarErro("Erro", "Selecione um bloqueio para alterar.");
            return;
        }
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "ALTERAR";
    }

    @FXML
    private void handleConfirmar() {
        try {
            if (!validarFormulario()) {
                return;
            }

            if ("CRIAR".equals(modoEdicao)) {
                BloqueioAgenda bloqueio = construirBloqueioAgendaDoFormulario();
                bloqueio.setDataCriacao(LocalDateTime.now());
                service.criar(bloqueio);
                mostrarSucesso("Sucesso", "Bloqueio criado com sucesso!");
            } else if ("ALTERAR".equals(modoEdicao)) {
                mostrarErro("Erro", "Alteração de bloqueios ainda não implementada no serviço.");
                return;
            }

            carregarDados();
            limparFormulario();
            resetarBotoes();
            habilitarCampos(false);
            modoEdicao = null;
            RefreshManager.getInstance().notifyRefresh();

        } catch (Exception e) {
            logger.error("Erro ao salvar bloqueio", e);
            mostrarErro("Erro ao salvar", e.getMessage());
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
        if (itemSelecionado == null || itemSelecionado.getId() == null) {
            mostrarErro("Erro", "Selecione um bloqueio para excluir");
            return;
        }

        mostrarErro("Erro", "Exclusão de bloqueios ainda não implementada no serviço.");
    }

    protected boolean validarFormulario() {
        // Validar CRM do Médico
        if (!ValidationUtils.validarCampoObrigatorio(txtMedicoCrm.getText())) {
            mostrarAviso("CRM do médico é obrigatório");
            txtMedicoCrm.requestFocus();
            return false;
        }
        if (!ValidationUtils.validarCRM(txtMedicoCrm.getText())) {
            mostrarAviso("CRM inválido. Deve conter entre 4 e 7 dígitos");
            txtMedicoCrm.requestFocus();
            return false;
        }

        // Validar Data de Início
        if (!ValidationUtils.validarCampoObrigatorio(txtDataInicio.getText())) {
            mostrarAviso("Data de início é obrigatória");
            txtDataInicio.requestFocus();
            return false;
        }
        if (!ValidationUtils.validarFormatoDataHora(txtDataInicio.getText())) {
            mostrarAviso("Formato de data/hora inválido. Use: yyyy-MM-dd HH:mm\nExemplo: 2025-12-31 08:00");
            txtDataInicio.requestFocus();
            return false;
        }

        // Validar Data de Fim
        if (!ValidationUtils.validarCampoObrigatorio(txtDataFim.getText())) {
            mostrarAviso("Data de fim é obrigatória");
            txtDataFim.requestFocus();
            return false;
        }
        if (!ValidationUtils.validarFormatoDataHora(txtDataFim.getText())) {
            mostrarAviso("Formato de data/hora inválido. Use: yyyy-MM-dd HH:mm\nExemplo: 2025-12-31 18:00");
            txtDataFim.requestFocus();
            return false;
        }

        // Validar Período (início antes do fim)
        try {
            LocalDateTime inicio = LocalDateTime.parse(txtDataInicio.getText(), DATE_TIME_FORMATTER);
            LocalDateTime fim = LocalDateTime.parse(txtDataFim.getText(), DATE_TIME_FORMATTER);
            
            if (!ValidationUtils.validarPeriodo(inicio, fim)) {
                mostrarAviso("Data/hora de início deve ser anterior à data/hora de fim");
                txtDataInicio.requestFocus();
                return false;
            }
        } catch (DateTimeParseException e) {
            mostrarAviso("Erro ao processar datas. Verifique o formato: yyyy-MM-dd HH:mm");
            return false;
        }

        // Validar Motivo
        if (!ValidationUtils.validarCampoObrigatorio(txtMotivo.getText())) {
            mostrarAviso("Motivo é obrigatório");
            txtMotivo.requestFocus();
            return false;
        }

        return true;
    }

    protected void habilitarCampos(boolean habilitar) {
        txtMedicoCrm.setDisable(!habilitar);
        txtDataInicio.setDisable(!habilitar);
        txtDataFim.setDisable(!habilitar);
        txtAntecedenciaMinima.setDisable(!habilitar);
        txtMotivo.setDisable(!habilitar);
        txtUsuarioCriador.setDisable(!habilitar);
        chkAtivo.setDisable(!habilitar);
    }

    private BloqueioAgenda construirBloqueioAgendaDoFormulario() {
        BloqueioAgenda bloqueio = new BloqueioAgenda();
        bloqueio.setMedicoCrm(txtMedicoCrm.getText());
        bloqueio.setDataInicio(LocalDateTime.parse(txtDataInicio.getText(), DATE_TIME_FORMATTER));
        bloqueio.setDataFim(LocalDateTime.parse(txtDataFim.getText(), DATE_TIME_FORMATTER));
        
        if (!txtAntecedenciaMinima.getText().trim().isEmpty()) {
            bloqueio.setAntecedenciaMinima(Integer.parseInt(txtAntecedenciaMinima.getText()));
        }
        
        bloqueio.setMotivo(txtMotivo.getText());
        bloqueio.setUsuarioCriadorLogin(txtUsuarioCriador.getText());
        bloqueio.setAtivo(chkAtivo.isSelected());
        return bloqueio;
    }

    @Override
    public void onRefresh() {
        carregarDados();
    }

    private void mostrarAviso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
