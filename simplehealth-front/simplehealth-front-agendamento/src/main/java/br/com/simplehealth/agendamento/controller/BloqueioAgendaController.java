package br.com.simplehealth.agendamento.controller;

import br.com.simplehealth.agendamento.model.BloqueioAgenda;
import br.com.simplehealth.agendamento.model.enums.AcaoBloqueioEnum;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Controller para gerenciar a interface de Bloqueios de Agenda.
 */
public class BloqueioAgendaController extends AbstractCrudController<BloqueioAgenda> implements RefreshManager.RefreshListener {

    private static final Logger logger = LoggerFactory.getLogger(BloqueioAgendaController.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @FXML private TableView<BloqueioAgenda> tableView;
    @FXML private TableColumn<BloqueioAgenda, String> colId;
    @FXML private TableColumn<BloqueioAgenda, String> colMedicoCrm;
    @FXML private TableColumn<BloqueioAgenda, String> colMotivo;
    @FXML private TableColumn<BloqueioAgenda, String> colDataInicio;
    @FXML private TableColumn<BloqueioAgenda, String> colDataFim;
    @FXML private TableColumn<BloqueioAgenda, Boolean> colAtivo;

    @FXML private TextField txtMedicoCrm;
    @FXML private DatePicker dtDataInicio;
    @FXML private TextField txtHoraInicio;
    @FXML private DatePicker dtDataFim;
    @FXML private TextField txtHoraFim;
    @FXML private TextField txtAntecedenciaMinima;
    @FXML private TextArea txtMotivo;
    @FXML private TextField txtUsuarioCriador;
    @FXML private CheckBox chkAtivo;
    @FXML private ComboBox<AcaoBloqueioEnum> cbAcao;
    @FXML private TextField txtBusca;

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
    @FXML private Button btnBuscar;

    private final BloqueioAgendaService service;
    private final ObservableList<BloqueioAgenda> bloqueios;
    private final ObservableList<BloqueioAgenda> todosBloqueios;

    public BloqueioAgendaController() {
        this.service = new BloqueioAgendaService();
        this.bloqueios = FXCollections.observableArrayList();
        this.todosBloqueios = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        super.btnCriar = this.btnCriar;
        super.btnAlterar = this.btnAlterar;
        super.btnDeletar = this.btnDeletar;
        super.btnConfirmar = this.btnConfirmar;
        super.btnCancelar = this.btnCancelar;

        logger.info("Inicializando BloqueioAgendaController");

        // Configurar ComboBox de Ação
        cbAcao.setItems(FXCollections.observableArrayList(AcaoBloqueioEnum.values()));
        cbAcao.setVisible(false);

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
                cbAcao.setVisible(true);
                cbAcao.setValue(null);
            } else {
                cbAcao.setVisible(false);
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
                todosBloqueios.clear();
                todosBloqueios.addAll(service.listarTodos());
                bloqueios.clear();
                bloqueios.addAll(todosBloqueios);
                logger.info("Dados carregados: {} bloqueios", bloqueios.size());
            } catch (Exception e) {
                logger.error("Erro ao carregar bloqueios", e);
                mostrarErro("Erro ao carregar dados", extrairMensagemErro(e));
            }
        });
    }

    private void preencherFormulario(BloqueioAgenda bloqueio) {
        txtMedicoCrm.setText(bloqueio.getMedicoCrm());
        
        if (bloqueio.getDataInicio() != null) {
            dtDataInicio.setValue(bloqueio.getDataInicio().toLocalDate());
            txtHoraInicio.setText(bloqueio.getDataInicio().format(TIME_FORMATTER));
        }
        
        if (bloqueio.getDataFim() != null) {
            dtDataFim.setValue(bloqueio.getDataFim().toLocalDate());
            txtHoraFim.setText(bloqueio.getDataFim().format(TIME_FORMATTER));
        }
        
        txtAntecedenciaMinima.setText(bloqueio.getAntecedenciaMinima() != null ? 
            bloqueio.getAntecedenciaMinima().toString() : "");
        txtMotivo.setText(bloqueio.getMotivo());
        txtUsuarioCriador.setText(bloqueio.getUsuarioCriadorLogin());
        chkAtivo.setSelected(bloqueio.getAtivo() != null && bloqueio.getAtivo());
    }

    protected void limparFormulario() {
        itemSelecionado = null;
        txtMedicoCrm.clear();
        dtDataInicio.setValue(null);
        txtHoraInicio.clear();
        dtDataFim.setValue(null);
        txtHoraFim.clear();
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
        
        if (cbAcao.getValue() == null) {
            mostrarErro("Erro", "Selecione uma ação no ComboBox.");
            return;
        }
        
        switch (cbAcao.getValue()) {
            case ALTERAR_DADOS:
                habilitarCampos(true);
                ativarModoEdicao();
                modoEdicao = "ALTERAR";
                break;
                
            case DESATIVAR:
                handleDesativar();
                break;
                
            default:
                mostrarErro("Erro", "Ação não reconhecida.");
        }
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
                BloqueioAgenda bloqueio = construirBloqueioAgendaDoFormulario();
                bloqueio.setId(itemSelecionado.getId());
                bloqueio.setDataCriacao(itemSelecionado.getDataCriacao());
                service.atualizar(itemSelecionado.getId(), bloqueio);
                mostrarSucesso("Sucesso", "Bloqueio atualizado com sucesso!");
            }

            carregarDados();
            limparFormulario();
            resetarBotoes();
            habilitarCampos(false);
            modoEdicao = null;
            RefreshManager.getInstance().notifyRefresh();

        } catch (Exception e) {
            logger.error("Erro ao salvar bloqueio", e);
            mostrarErro("Erro ao salvar", extrairMensagemErro(e));
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

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Excluir Bloqueio de Agenda");
        confirmacao.setContentText("Deseja realmente excluir este bloqueio?\n\n" +
            "Médico CRM: " + itemSelecionado.getMedicoCrm() + "\n" +
            "Motivo: " + itemSelecionado.getMotivo() + "\n\n" +
            "Esta ação não pode ser desfeita!");
        
        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    service.deletar(itemSelecionado.getId());
                    mostrarSucesso("Sucesso", "Bloqueio excluído com sucesso!");
                    carregarDados();
                    limparFormulario();
                } catch (Exception e) {
                    logger.error("Erro ao excluir bloqueio", e);
                    mostrarErro("Erro ao excluir", extrairMensagemErro(e));
                }
            }
        });
    }

    /**
     * Desativa um bloqueio de agenda usando o endpoint PATCH /bloqueio-agenda/{id}/desativar
     */
    private void handleDesativar() {
        if (itemSelecionado == null || itemSelecionado.getId() == null) {
            mostrarErro("Erro", "Selecione um bloqueio para desativar");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Desativação");
        confirmacao.setHeaderText("Desativar Bloqueio de Agenda");
        confirmacao.setContentText("Deseja realmente desativar este bloqueio?\n\n" +
            "Médico CRM: " + itemSelecionado.getMedicoCrm() + "\n" +
            "Motivo: " + itemSelecionado.getMotivo());
        
        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    BloqueioAgenda bloqueioDesativado = service.desativar(itemSelecionado.getId());
                    mostrarSucesso("Sucesso", "Bloqueio desativado com sucesso!");
                    carregarDados();
                    limparFormulario();
                } catch (Exception e) {
                    logger.error("Erro ao desativar bloqueio", e);
                    mostrarErro("Erro ao desativar", extrairMensagemErro(e));
                }
            }
        });
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
        if (dtDataInicio.getValue() == null) {
            mostrarAviso("Data de início é obrigatória");
            dtDataInicio.requestFocus();
            return false;
        }
        if (!ValidationUtils.validarCampoObrigatorio(txtHoraInicio.getText())) {
            mostrarAviso("Hora de início é obrigatória");
            txtHoraInicio.requestFocus();
            return false;
        }
        if (!ValidationUtils.validarFormatoHora(txtHoraInicio.getText())) {
            mostrarAviso("Formato de hora inválido. Use: HH:mm\nExemplo: 08:00");
            txtHoraInicio.requestFocus();
            return false;
        }

        // Validar Data de Fim
        if (dtDataFim.getValue() == null) {
            mostrarAviso("Data de fim é obrigatória");
            dtDataFim.requestFocus();
            return false;
        }
        if (!ValidationUtils.validarCampoObrigatorio(txtHoraFim.getText())) {
            mostrarAviso("Hora de fim é obrigatória");
            txtHoraFim.requestFocus();
            return false;
        }
        if (!ValidationUtils.validarFormatoHora(txtHoraFim.getText())) {
            mostrarAviso("Formato de hora inválido. Use: HH:mm\nExemplo: 18:00");
            txtHoraFim.requestFocus();
            return false;
        }

        // Validar Período (início antes do fim)
        try {
            LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText(), TIME_FORMATTER);
            LocalTime horaFim = LocalTime.parse(txtHoraFim.getText(), TIME_FORMATTER);
            LocalDateTime inicio = LocalDateTime.of(dtDataInicio.getValue(), horaInicio);
            LocalDateTime fim = LocalDateTime.of(dtDataFim.getValue(), horaFim);
            
            if (!ValidationUtils.validarPeriodo(inicio, fim)) {
                mostrarAviso("Data/hora de início deve ser anterior à data/hora de fim");
                dtDataInicio.requestFocus();
                return false;
            }
        } catch (DateTimeParseException e) {
            mostrarAviso("Erro ao processar horas. Verifique o formato: HH:mm");
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
        dtDataInicio.setDisable(!habilitar);
        txtHoraInicio.setDisable(!habilitar);
        dtDataFim.setDisable(!habilitar);
        txtHoraFim.setDisable(!habilitar);
        txtAntecedenciaMinima.setDisable(!habilitar);
        txtMotivo.setDisable(!habilitar);
        txtUsuarioCriador.setDisable(!habilitar);
        chkAtivo.setDisable(!habilitar);
    }

    private BloqueioAgenda construirBloqueioAgendaDoFormulario() {
        BloqueioAgenda bloqueio = new BloqueioAgenda();
        bloqueio.setMedicoCrm(txtMedicoCrm.getText());
        LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText(), TIME_FORMATTER);
        LocalTime horaFim = LocalTime.parse(txtHoraFim.getText(), TIME_FORMATTER);
        bloqueio.setDataInicio(LocalDateTime.of(dtDataInicio.getValue(), horaInicio));
        bloqueio.setDataFim(LocalDateTime.of(dtDataFim.getValue(), horaFim));
        
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

    @FXML
    private void handleBuscar() {
        String termoBusca = txtBusca != null ? txtBusca.getText() : "";
        
        if (termoBusca == null || termoBusca.trim().isEmpty()) {
            // Se busca vazia, mostra todos
            bloqueios.clear();
            bloqueios.addAll(todosBloqueios);
            logger.info("Busca limpa, mostrando todos {} bloqueios", bloqueios.size());
            return;
        }

        // Busca multi-campo
        String busca = termoBusca.trim().toLowerCase();
        String buscaSemFormatacao = busca.replaceAll("[^0-9a-z]", "");
        
        bloqueios.clear();
        for (BloqueioAgenda bloqueio : todosBloqueios) {
            boolean encontrado = false;
            
            // Busca por CRM do médico
            if (bloqueio.getMedicoCrm() != null) {
                String crm = bloqueio.getMedicoCrm().toLowerCase();
                String crmSemFormatacao = crm.replaceAll("[^0-9]", "");
                if (crm.contains(busca) || crmSemFormatacao.contains(buscaSemFormatacao)) {
                    encontrado = true;
                }
            }
            
            // Busca por motivo
            if (!encontrado && bloqueio.getMotivo() != null) {
                if (bloqueio.getMotivo().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            // Busca por ID
            if (!encontrado && bloqueio.getId() != null) {
                if (bloqueio.getId().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            if (encontrado) {
                bloqueios.add(bloqueio);
            }
        }
        
        logger.info("Busca por '{}': {} resultados encontrados", termoBusca, bloqueios.size());
        
        if (bloqueios.isEmpty()) {
            mostrarAviso("Nenhum bloqueio encontrado com o termo: " + termoBusca);
        }
    }

    private void mostrarAviso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
