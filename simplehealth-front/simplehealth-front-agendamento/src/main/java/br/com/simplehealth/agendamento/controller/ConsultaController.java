package br.com.simplehealth.agendamento.controller;

import br.com.simplehealth.agendamento.model.Consulta;
import br.com.simplehealth.agendamento.service.ConsultaService;
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
 * Controller para gerenciar a interface de Consultas.
 */
public class ConsultaController extends AbstractCrudController<Consulta> implements RefreshManager.RefreshListener {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaController.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
    @FXML private TextField txtDataHoraInicio;
    @FXML private TextField txtDataHoraFim;
    @FXML private ComboBox<String> cbTipoConsulta;
    @FXML private ComboBox<String> cbModalidade;
    @FXML private ComboBox<String> cbStatus;
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
    private final ObservableList<Consulta> consultas;
    private final ObservableList<Consulta> todasConsultas;

    public ConsultaController() {
        this.service = new ConsultaService();
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

        // Configurar ComboBoxes
        cbTipoConsulta.setItems(FXCollections.observableArrayList("PRIMEIRA_VEZ", "RETORNO"));
        cbModalidade.setItems(FXCollections.observableArrayList("PRESENCIAL", "ONLINE"));
        cbStatus.setItems(FXCollections.observableArrayList("ATIVO", "CANCELADO", "REALIZADO"));

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
        txtDataHoraInicio.setText(consulta.getDataHoraInicio() != null ? 
            consulta.getDataHoraInicio().format(DATE_TIME_FORMATTER) : "");
        txtDataHoraFim.setText(consulta.getDataHoraFim() != null ? 
            consulta.getDataHoraFim().format(DATE_TIME_FORMATTER) : "");
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
        txtDataHoraInicio.clear();
        txtDataHoraFim.clear();
        cbTipoConsulta.setValue(null);
        cbModalidade.setValue("PRESENCIAL");
        cbStatus.setValue("ATIVO");
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
                service.criar(consulta);
                mostrarSucesso("Sucesso", "Consulta criada com sucesso!");
            } else if ("ALTERAR".equals(modoEdicao)) {
                if (itemSelecionado == null || itemSelecionado.getId() == null) {
                    mostrarErro("Erro", "Item selecionado inválido.");
                    return;
                }
                Consulta consulta = construirConsultaDoFormulario();
                consulta.setId(itemSelecionado.getId());
                service.atualizar(itemSelecionado.getId(), consulta);
                mostrarSucesso("Sucesso", "Consulta atualizada com sucesso!");
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
            mostrarErro("Erro", "Selecione uma consulta para excluir");
            return;
        }

        // Confirmação antes de excluir
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir esta consulta?");
        confirmacao.setContentText("Paciente: " + itemSelecionado.getPacienteCpf() + "\n" +
                                  "Médico: " + itemSelecionado.getMedicoCrm() + "\n" +
                                  "Data: " + (itemSelecionado.getDataHoraInicio() != null ? 
                                             itemSelecionado.getDataHoraInicio().format(DATE_TIME_FORMATTER) : "") + "\n\n" +
                                  "Esta ação não pode ser desfeita.");

        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    service.deletar(itemSelecionado.getId());
                    mostrarSucesso("Sucesso", "Consulta excluída com sucesso!");
                    carregarDados();
                    limparFormulario();
                    resetarBotoes();
                    RefreshManager.getInstance().notifyRefresh();
                } catch (Exception e) {
                    logger.error("Erro ao excluir consulta", e);
                    mostrarErro("Erro ao excluir", e.getMessage());
                }
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
        if (!ValidationUtils.validarCampoObrigatorio(txtDataHoraInicio.getText())) {
            mostrarAviso("Data/hora de início é obrigatória");
            txtDataHoraInicio.requestFocus();
            return false;
        }
        if (!ValidationUtils.validarFormatoDataHora(txtDataHoraInicio.getText())) {
            mostrarAviso("Formato de data/hora inválido. Use: yyyy-MM-dd HH:mm\nExemplo: 2025-12-31 14:30");
            txtDataHoraInicio.requestFocus();
            return false;
        }

        // Validar Data/Hora Fim
        if (!ValidationUtils.validarCampoObrigatorio(txtDataHoraFim.getText())) {
            mostrarAviso("Data/hora de fim é obrigatória");
            txtDataHoraFim.requestFocus();
            return false;
        }
        if (!ValidationUtils.validarFormatoDataHora(txtDataHoraFim.getText())) {
            mostrarAviso("Formato de data/hora inválido. Use: yyyy-MM-dd HH:mm\nExemplo: 2025-12-31 15:30");
            txtDataHoraFim.requestFocus();
            return false;
        }

        // Validar Período (início antes do fim)
        try {
            LocalDateTime inicio = LocalDateTime.parse(txtDataHoraInicio.getText(), DATE_TIME_FORMATTER);
            LocalDateTime fim = LocalDateTime.parse(txtDataHoraFim.getText(), DATE_TIME_FORMATTER);
            
            if (!ValidationUtils.validarPeriodo(inicio, fim)) {
                mostrarAviso("Data/hora de início deve ser anterior à data/hora de fim");
                txtDataHoraInicio.requestFocus();
                return false;
            }
        } catch (DateTimeParseException e) {
            mostrarAviso("Erro ao processar datas. Verifique o formato: yyyy-MM-dd HH:mm");
            return false;
        }

        // Validar Tipo de Consulta
        if (cbTipoConsulta.getValue() == null || cbTipoConsulta.getValue().isEmpty()) {
            mostrarAviso("Tipo de consulta é obrigatório");
            cbTipoConsulta.requestFocus();
            return false;
        }

        // Validar Modalidade
        if (cbModalidade.getValue() == null || cbModalidade.getValue().isEmpty()) {
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

        return true;
    }

    protected void habilitarCampos(boolean habilitar) {
        txtPacienteCpf.setDisable(!habilitar);
        txtMedicoCrm.setDisable(!habilitar);
        txtEspecialidade.setDisable(!habilitar);
        txtConvenioNome.setDisable(!habilitar);
        txtDataHoraInicio.setDisable(!habilitar);
        txtDataHoraFim.setDisable(!habilitar);
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
        consulta.setDataHoraInicio(LocalDateTime.parse(txtDataHoraInicio.getText(), DATE_TIME_FORMATTER));
        consulta.setDataHoraFim(LocalDateTime.parse(txtDataHoraFim.getText(), DATE_TIME_FORMATTER));
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
                if (consulta.getTipoConsulta().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            // Busca por status
            if (!encontrado && consulta.getStatus() != null) {
                if (consulta.getStatus().toLowerCase().contains(busca)) {
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
