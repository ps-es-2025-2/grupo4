package br.com.simplehealth.agendamento.controller;

import br.com.simplehealth.agendamento.model.Consulta;
import br.com.simplehealth.agendamento.model.enums.ModalidadeEnum;
import br.com.simplehealth.agendamento.model.enums.StatusAgendamentoEnum;
import br.com.simplehealth.agendamento.model.enums.TipoConsultaEnum;
import br.com.simplehealth.agendamento.service.ConsultaService;
import br.com.simplehealth.agendamento.service.EncaixeService;
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
 * Controller para gerenciar a interface de Consultas.
 */
public class ConsultaController extends AbstractCrudController<Consulta> implements RefreshManager.RefreshListener {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaController.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @FXML private TableView<Consulta> tableView;
    @FXML private TableColumn<Consulta, String> colId;
    @FXML private TableColumn<Consulta, String> colPacienteCpf;
    @FXML private TableColumn<Consulta, String> colMedicoCrm;
    @FXML private TableColumn<Consulta, String> colEspecialidade;
    @FXML private TableColumn<Consulta, String> colDataHora;
    @FXML private TableColumn<Consulta, String> colStatus;

    @FXML private TextField txtPacienteCpf;
    @FXML private TextField txtMedicoCrm;
    @FXML private TextField txtEspecialidade;
    @FXML private TextField txtConvenioNome;
    @FXML private DatePicker dtDataInicio;
    @FXML private TextField txtHoraInicio;
    @FXML private DatePicker dtDataFim;
    @FXML private TextField txtHoraFim;
    @FXML private ComboBox<TipoConsultaEnum> cbTipoConsulta;
    @FXML private ComboBox<ModalidadeEnum> cbModalidade;
    @FXML private ComboBox<StatusAgendamentoEnum> cbStatus;
    @FXML private CheckBox chkEncaixe;
    @FXML private TextArea txtObservacoes;
    @FXML private TextField txtUsuarioCriador;
    @FXML private TextField txtMotivoEncaixe;

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
    
    @FXML private TextField txtBusca;

    private final ConsultaService service;
    private final EncaixeService encaixeService;
    private final ObservableList<Consulta> consultas;
    private final ObservableList<Consulta> todasConsultas;

    public ConsultaController() {
        this.service = new ConsultaService();
        this.encaixeService = new EncaixeService();
        this.consultas = FXCollections.observableArrayList();
        this.todasConsultas = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        super.btnCriar = this.btnCriar;
        super.btnAlterar = this.btnAlterar;
        super.btnDeletar = this.btnDeletar;
        super.btnConfirmar = this.btnConfirmar;
        super.btnCancelar = this.btnCancelar;

        logger.info("Inicializando ConsultaController");

        // Configurar colunas da tabela
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPacienteCpf.setCellValueFactory(new PropertyValueFactory<>("pacienteCpf"));
        colMedicoCrm.setCellValueFactory(new PropertyValueFactory<>("medicoCrm"));
        colEspecialidade.setCellValueFactory(new PropertyValueFactory<>("especialidade"));
        colDataHora.setCellValueFactory(cellData -> {
            LocalDateTime dataHora = cellData.getValue().getDataHoraInicio();
            return new javafx.beans.property.SimpleStringProperty(
                dataHora != null ? dataHora.format(DATE_TIME_FORMATTER) : ""
            );
        });
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.setItems(consultas);

        // Listener para seleção na tabela
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                itemSelecionado = newSelection;
                preencherFormulario(newSelection);
                habilitarBotoesSelecao();
            }
        });

        // Configurar ComboBoxes com enums
        cbTipoConsulta.setItems(FXCollections.observableArrayList(TipoConsultaEnum.values()));
        cbModalidade.setItems(FXCollections.observableArrayList(ModalidadeEnum.values()));
        cbStatus.setItems(FXCollections.observableArrayList(StatusAgendamentoEnum.values()));

        // Configurar estado inicial dos botões
        configurarEstadoInicialBotoes();
        habilitarCampos(false);

        // Registrar listener para refresh
        RefreshManager.getInstance().addListener(this);

        // Carregar dados
        carregarDados();
    }

    protected void carregarDados() {
        Platform.runLater(() -> {
            try {
                todasConsultas.clear();
                todasConsultas.addAll(service.listarTodos());
                consultas.clear();
                consultas.addAll(todasConsultas);
                logger.info("Dados carregados: {} consultas", consultas.size());
            } catch (Exception e) {
                logger.error("Erro ao carregar consultas", e);
                mostrarErro("Erro ao carregar dados", e.getMessage());
            }
        });
    }

    private void preencherFormulario(Consulta consulta) {
        txtPacienteCpf.setText(consulta.getPacienteCpf());
        txtMedicoCrm.setText(consulta.getMedicoCrm());
        txtEspecialidade.setText(consulta.getEspecialidade());
        txtConvenioNome.setText(consulta.getConvenioNome());
        
        if (consulta.getDataHoraInicio() != null) {
            dtDataInicio.setValue(consulta.getDataHoraInicio().toLocalDate());
            txtHoraInicio.setText(consulta.getDataHoraInicio().format(TIME_FORMATTER));
        }
        
        if (consulta.getDataHoraFim() != null) {
            dtDataFim.setValue(consulta.getDataHoraFim().toLocalDate());
            txtHoraFim.setText(consulta.getDataHoraFim().format(TIME_FORMATTER));
        }
        
        cbTipoConsulta.setValue(consulta.getTipoConsulta());
        cbModalidade.setValue(consulta.getModalidade());
        cbStatus.setValue(consulta.getStatus());
        chkEncaixe.setSelected(consulta.getIsEncaixe() != null && consulta.getIsEncaixe());
        txtObservacoes.setText(consulta.getObservacoes());
        txtUsuarioCriador.setText(consulta.getUsuarioCriadorLogin());
        txtMotivoEncaixe.setText(consulta.getMotivoEncaixe());
    }

    protected void limparFormulario() {
        itemSelecionado = null;
        txtPacienteCpf.clear();
        txtMedicoCrm.clear();
        txtEspecialidade.clear();
        txtConvenioNome.clear();
        dtDataInicio.setValue(null);
        txtHoraInicio.clear();
        dtDataFim.setValue(null);
        txtHoraFim.clear();
        cbTipoConsulta.setValue(null);
        cbModalidade.setValue(ModalidadeEnum.PRESENCIAL);
        cbStatus.setValue(StatusAgendamentoEnum.ATIVO);
        chkEncaixe.setSelected(false);
        txtObservacoes.clear();
        txtUsuarioCriador.clear();
        txtMotivoEncaixe.clear();
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleCriar() {
        limparFormulario();
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "CRIAR";
        txtPacienteCpf.requestFocus();
    }

    @FXML
    private void handleAlterar() {
        if (itemSelecionado == null) {
            mostrarErro("Erro", "Selecione uma consulta para alterar.");
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
                Consulta consulta = construirConsultaDoFormulario();
                
                // Verificar se é encaixe e usar o serviço apropriado
                if (chkEncaixe.isSelected()) {
                    encaixeService.solicitarEncaixe(consulta);
                    mostrarSucesso("Sucesso", "Encaixe solicitado com sucesso!");
                } else {
                    service.criar(consulta);
                    mostrarSucesso("Sucesso", "Consulta agendada com sucesso!");
                }
            } else if ("ALTERAR".equals(modoEdicao)) {
                mostrarErro("Operação não suportada", 
                    "A API não permite alteração de consultas.\n" +
                    "Para modificar um agendamento, cancele a consulta atual e crie uma nova.");
                return;
            }

            carregarDados();
            limparFormulario();
            resetarBotoes();
            habilitarCampos(false);
            modoEdicao = null;
            RefreshManager.getInstance().notifyRefresh();

        } catch (Exception e) {
            logger.error("Erro ao salvar consulta", e);
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
            mostrarErro("Erro", "Selecione uma consulta para cancelar");
            return;
        }

        // Confirmação antes de cancelar
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Cancelamento");
        confirmacao.setHeaderText("Deseja realmente cancelar esta consulta?");
        confirmacao.setContentText("Paciente: " + itemSelecionado.getPacienteCpf() + "\n" +
                                  "Médico: " + itemSelecionado.getMedicoCrm() + "\n" +
                                  "Data: " + (itemSelecionado.getDataHoraInicio() != null ? 
                                             itemSelecionado.getDataHoraInicio().format(DATE_TIME_FORMATTER) : ""));

        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Solicitar motivo do cancelamento
                TextInputDialog motivoDialog = new TextInputDialog();
                motivoDialog.setTitle("Motivo do Cancelamento");
                motivoDialog.setHeaderText("Informe o motivo do cancelamento:");
                motivoDialog.setContentText("Motivo:");
                
                motivoDialog.showAndWait().ifPresent(motivo -> {
                    if (motivo == null || motivo.trim().isEmpty()) {
                        mostrarErro("Erro", "O motivo do cancelamento é obrigatório");
                        return;
                    }
                    
                    // Solicitar login do usuário
                    TextInputDialog usuarioDialog = new TextInputDialog();
                    usuarioDialog.setTitle("Identificação do Usuário");
                    usuarioDialog.setHeaderText("Informe seu login:");
                    usuarioDialog.setContentText("Login:");
                    
                    usuarioDialog.showAndWait().ifPresent(usuarioLogin -> {
                        if (usuarioLogin == null || usuarioLogin.trim().isEmpty()) {
                            mostrarErro("Erro", "O login do usuário é obrigatório");
                            return;
                        }
                        
                        try {
                            service.cancelar(itemSelecionado.getId(), motivo, usuarioLogin);
                            mostrarSucesso("Sucesso", "Consulta cancelada com sucesso!");
                            carregarDados();
                            limparFormulario();
                            resetarBotoes();
                            RefreshManager.getInstance().notifyRefresh();
                        } catch (Exception e) {
                            logger.error("Erro ao cancelar consulta", e);
                            mostrarErro("Erro ao cancelar", e.getMessage());
                        }
                    });
                });
            }
        });
    }

    protected boolean validarFormulario() {
        // Validar CPF do Paciente
        if (!ValidationUtils.validarCampoObrigatorio(txtPacienteCpf.getText())) {
            mostrarAviso("CPF do paciente é obrigatório");
            txtPacienteCpf.requestFocus();
            return false;
        }
        if (!ValidationUtils.validarCPF(txtPacienteCpf.getText())) {
            mostrarAviso("CPF do paciente inválido. Use o formato: xxx.xxx.xxx-xx ou apenas números");
            txtPacienteCpf.requestFocus();
            return false;
        }

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

        // Validar Especialidade
        if (!ValidationUtils.validarCampoObrigatorio(txtEspecialidade.getText())) {
            mostrarAviso("Especialidade é obrigatória");
            txtEspecialidade.requestFocus();
            return false;
        }

        // Validar Data/Hora Início
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
            mostrarAviso("Formato de hora inválido. Use: HH:mm\nExemplo: 14:30");
            txtHoraInicio.requestFocus();
            return false;
        }

        // Validar Data/Hora Fim
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
            mostrarAviso("Formato de hora inválido. Use: HH:mm\nExemplo: 15:30");
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

        // Validar Tipo de Consulta
        if (cbTipoConsulta.getValue() == null) {
            mostrarAviso("Tipo de consulta é obrigatório");
            cbTipoConsulta.requestFocus();
            return false;
        }

        // Validar Modalidade
        if (cbModalidade.getValue() == null) {
            mostrarAviso("Modalidade é obrigatória");
            cbModalidade.requestFocus();
            return false;
        }

        // Validar motivo de encaixe se for encaixe
        if (chkEncaixe.isSelected() && !ValidationUtils.validarCampoObrigatorio(txtMotivoEncaixe.getText())) {
            mostrarAviso("Motivo do encaixe é obrigatório quando marcado como encaixe");
            txtMotivoEncaixe.requestFocus();
            return false;
        }

        // Validar Usuário Criador (obrigatório conforme API)
        if (!ValidationUtils.validarCampoObrigatorio(txtUsuarioCriador.getText())) {
            mostrarAviso("Usuário criador é obrigatório");
            txtUsuarioCriador.requestFocus();
            return false;
        }

        return true;
    }

    protected void habilitarCampos(boolean habilitar) {
        txtPacienteCpf.setDisable(!habilitar);
        txtMedicoCrm.setDisable(!habilitar);
        txtEspecialidade.setDisable(!habilitar);
        txtConvenioNome.setDisable(!habilitar);
        dtDataInicio.setDisable(!habilitar);
        txtHoraInicio.setDisable(!habilitar);
        dtDataFim.setDisable(!habilitar);
        txtHoraFim.setDisable(!habilitar);
        cbTipoConsulta.setDisable(!habilitar);
        cbModalidade.setDisable(!habilitar);
        cbStatus.setDisable(!habilitar);
        chkEncaixe.setDisable(!habilitar);
        txtObservacoes.setDisable(!habilitar);
        txtUsuarioCriador.setDisable(!habilitar);
        txtMotivoEncaixe.setDisable(!habilitar);
    }

    private Consulta construirConsultaDoFormulario() {
        Consulta consulta = new Consulta();
        consulta.setPacienteCpf(txtPacienteCpf.getText());
        consulta.setMedicoCrm(txtMedicoCrm.getText());
        consulta.setEspecialidade(txtEspecialidade.getText());
        consulta.setConvenioNome(txtConvenioNome.getText());
        
        LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText(), TIME_FORMATTER);
        LocalTime horaFim = LocalTime.parse(txtHoraFim.getText(), TIME_FORMATTER);
        consulta.setDataHoraInicio(LocalDateTime.of(dtDataInicio.getValue(), horaInicio));
        consulta.setDataHoraFim(LocalDateTime.of(dtDataFim.getValue(), horaFim));
        
        consulta.setTipoConsulta(cbTipoConsulta.getValue());
        consulta.setModalidade(cbModalidade.getValue());
        consulta.setStatus(cbStatus.getValue());
        consulta.setIsEncaixe(chkEncaixe.isSelected());
        consulta.setObservacoes(txtObservacoes.getText());
        consulta.setUsuarioCriadorLogin(txtUsuarioCriador.getText());
        consulta.setMotivoEncaixe(txtMotivoEncaixe.getText());
        return consulta;
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
            consultas.clear();
            consultas.addAll(todasConsultas);
            logger.info("Busca limpa, mostrando todas {} consultas", consultas.size());
            return;
        }

        // Busca avançada multi-campo
        String busca = termoBusca.trim().toLowerCase();
        String buscaSemFormatacao = busca.replaceAll("[^0-9a-z]", "");
        
        consultas.clear();
        for (Consulta consulta : todasConsultas) {
            boolean encontrado = false;
            
            // Busca por CPF do paciente (com ou sem formatação)
            if (consulta.getPacienteCpf() != null) {
                String cpf = consulta.getPacienteCpf().toLowerCase();
                String cpfSemFormatacao = cpf.replaceAll("[^0-9]", "");
                if (cpf.contains(busca) || cpfSemFormatacao.contains(buscaSemFormatacao)) {
                    encontrado = true;
                }
            }
            
            // Busca por CRM do médico
            if (!encontrado && consulta.getMedicoCrm() != null) {
                String crm = consulta.getMedicoCrm().toLowerCase();
                String crmSemFormatacao = crm.replaceAll("[^0-9]", "");
                if (crm.contains(busca) || crmSemFormatacao.contains(buscaSemFormatacao)) {
                    encontrado = true;
                }
            }
            
            // Busca por especialidade
            if (!encontrado && consulta.getEspecialidade() != null) {
                if (consulta.getEspecialidade().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            // Busca por convênio
            if (!encontrado && consulta.getConvenioNome() != null) {
                if (consulta.getConvenioNome().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            // Busca por tipo de consulta
            if (!encontrado && consulta.getTipoConsulta() != null) {
                if (consulta.getTipoConsulta().name().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            // Busca por status
            if (!encontrado && consulta.getStatus() != null) {
                if (consulta.getStatus().name().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            // Busca por ID
            if (!encontrado && consulta.getId() != null) {
                if (consulta.getId().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            if (encontrado) {
                consultas.add(consulta);
            }
        }
        
        logger.info("Busca por '{}': {} resultados encontrados", termoBusca, consultas.size());
        
        if (consultas.isEmpty()) {
            mostrarAviso("Nenhuma consulta encontrada com o termo: " + termoBusca);
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
