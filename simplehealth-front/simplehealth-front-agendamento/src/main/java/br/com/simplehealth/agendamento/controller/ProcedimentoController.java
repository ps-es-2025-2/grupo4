package br.com.simplehealth.agendamento.controller;

import br.com.simplehealth.agendamento.model.Procedimento;
import br.com.simplehealth.agendamento.model.enums.AcaoAgendamentoEnum;
import br.com.simplehealth.agendamento.model.enums.ModalidadeEnum;
import br.com.simplehealth.agendamento.model.enums.StatusAgendamentoEnum;
import br.com.simplehealth.agendamento.service.ProcedimentoService;
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
import java.util.Optional;

/**
 * Controller para gerenciar a interface de Procedimentos.
 */
public class ProcedimentoController extends AbstractCrudController<Procedimento> implements RefreshManager.RefreshListener {

    private static final Logger logger = LoggerFactory.getLogger(ProcedimentoController.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @FXML private TableView<Procedimento> tableView;
    @FXML private TableColumn<Procedimento, String> colId;
    @FXML private TableColumn<Procedimento, String> colPacienteCpf;
    @FXML private TableColumn<Procedimento, String> colDescricao;
    @FXML private TableColumn<Procedimento, String> colDataHora;
    @FXML private TableColumn<Procedimento, String> colStatus;

    @FXML private TextField txtPacienteCpf;
    @FXML private TextField txtMedicoCrm;
    @FXML private TextArea txtDescricaoProcedimento;
    @FXML private TextField txtSalaEquipamento;
    @FXML private TextField txtConvenioNome;
    @FXML private DatePicker dtDataInicio;
    @FXML private TextField txtHoraInicio;
    @FXML private DatePicker dtDataFim;
    @FXML private TextField txtHoraFim;
    @FXML private ComboBox<String> cbNivelRisco;
    @FXML private ComboBox<ModalidadeEnum> cbModalidade;
    @FXML private ComboBox<StatusAgendamentoEnum> cbStatus;
    @FXML private TextArea txtObservacoes;
    @FXML private TextField txtUsuarioCriador;
    @FXML private TextField txtBusca;
    @FXML private ComboBox<AcaoAgendamentoEnum> cbAcao;
    
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

    private final ProcedimentoService service;
    private final ObservableList<Procedimento> procedimentos;
    private final ObservableList<Procedimento> todosProcedimentos;

    public ProcedimentoController() {
        this.service = new ProcedimentoService();
        this.procedimentos = FXCollections.observableArrayList();
        this.todosProcedimentos = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        super.btnCriar = this.btnCriar;
        super.btnAlterar = this.btnAlterar;
        super.btnDeletar = this.btnDeletar;
        super.btnConfirmar = this.btnConfirmar;
        super.btnCancelar = this.btnCancelar;

        logger.info("Inicializando ProcedimentoController");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPacienteCpf.setCellValueFactory(new PropertyValueFactory<>("pacienteCpf"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricaoProcedimento"));
        colDataHora.setCellValueFactory(cellData -> {
            LocalDateTime dataHora = cellData.getValue().getDataHoraInicioPrevista();
            return new javafx.beans.property.SimpleStringProperty(
                dataHora != null ? dataHora.format(DATE_TIME_FORMATTER) : ""
            );
        });
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.setItems(procedimentos);
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

        cbNivelRisco.setItems(FXCollections.observableArrayList("BAIXO", "MÉDIO", "ALTO"));
        // Configurar ComboBoxes com enums
        cbModalidade.setItems(FXCollections.observableArrayList(ModalidadeEnum.values()));
        cbStatus.setItems(FXCollections.observableArrayList(StatusAgendamentoEnum.values()));
        cbAcao.setItems(FXCollections.observableArrayList(AcaoAgendamentoEnum.values()));
        cbAcao.setVisible(false); // Inicialmente oculto

        // Configurar estado inicial dos botões
        configurarEstadoInicialBotoes();
        habilitarCampos(false);

        RefreshManager.getInstance().addListener(this);
        carregarDados();
    }

    protected void carregarDados() {
        Platform.runLater(() -> {
            try {
                todosProcedimentos.clear();
                todosProcedimentos.addAll(service.listarTodos());
                procedimentos.clear();
                procedimentos.addAll(todosProcedimentos);
                logger.info("Dados carregados: {} procedimentos", procedimentos.size());
            } catch (Exception e) {
                logger.error("Erro ao carregar procedimentos", e);
                mostrarErro("Erro ao carregar dados", e.getMessage());
            }
        });
    }

    private void preencherFormulario(Procedimento procedimento) {
        txtPacienteCpf.setText(procedimento.getPacienteCpf());
        txtMedicoCrm.setText(procedimento.getMedicoCrm());
        txtDescricaoProcedimento.setText(procedimento.getDescricaoProcedimento());
        txtSalaEquipamento.setText(procedimento.getSalaEquipamentoNecessario());
        txtConvenioNome.setText(procedimento.getConvenioNome());
        
        if (procedimento.getDataHoraInicioPrevista() != null) {
            dtDataInicio.setValue(procedimento.getDataHoraInicioPrevista().toLocalDate());
            txtHoraInicio.setText(procedimento.getDataHoraInicioPrevista().format(TIME_FORMATTER));
        }
        
        if (procedimento.getDataHoraFimPrevista() != null) {
            dtDataFim.setValue(procedimento.getDataHoraFimPrevista().toLocalDate());
            txtHoraFim.setText(procedimento.getDataHoraFimPrevista().format(TIME_FORMATTER));
        }
        
        cbNivelRisco.setValue(procedimento.getNivelRisco());
        cbModalidade.setValue(procedimento.getModalidade());
        cbStatus.setValue(procedimento.getStatus());
        txtObservacoes.setText(procedimento.getObservacoes());
        txtUsuarioCriador.setText(procedimento.getUsuarioCriadorLogin());
    }

    protected void limparFormulario() {
        itemSelecionado = null;
        txtPacienteCpf.clear();
        txtMedicoCrm.clear();
        txtDescricaoProcedimento.clear();
        txtSalaEquipamento.clear();
        txtConvenioNome.clear();
        dtDataInicio.setValue(null);
        txtHoraInicio.clear();
        dtDataFim.setValue(null);
        txtHoraFim.clear();
        cbNivelRisco.setValue(null);
        cbModalidade.setValue(ModalidadeEnum.PRESENCIAL);
        cbStatus.setValue(StatusAgendamentoEnum.ATIVO);
        txtObservacoes.clear();
        txtUsuarioCriador.clear();
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
            mostrarErro("Erro", "Selecione um procedimento para realizar uma ação.");
            return;
        }
        
        if (cbAcao.getValue() == null) {
            mostrarErro("Erro", "Selecione uma ação a ser realizada.");
            return;
        }
        
        AcaoAgendamentoEnum acao = cbAcao.getValue();
        
        switch (acao) {
            case ALTERAR_DADOS:
                habilitarCampos(true);
                ativarModoEdicao();
                modoEdicao = "ALTERAR";
                break;
            case INICIAR:
                handleIniciar();
                break;
            case FINALIZAR:
                handleFinalizar();
                break;
            case CANCELAR:
                handleCancelarProcedimento();
                break;
        }
    }

    @FXML
    private void handleConfirmar() {
        try {
            if (!validarFormulario()) {
                return;
            }

            if ("CRIAR".equals(modoEdicao)) {
                Procedimento procedimento = construirProcedimentoDoFormulario();
                service.criar(procedimento);
                mostrarSucesso("Sucesso", "Procedimento criado com sucesso!");
            } else if ("ALTERAR".equals(modoEdicao)) {
                if (itemSelecionado == null || itemSelecionado.getId() == null) {
                    mostrarErro("Erro", "Nenhum procedimento selecionado para alterar.");
                    return;
                }
                
                logger.info("Alterando procedimento com ID: {}", itemSelecionado.getId());
                
                Procedimento procedimento = construirProcedimentoDoFormulario();
                // Preservar campos que não estão no formulário
                procedimento.setId(itemSelecionado.getId());
                procedimento.setDataHoraAgendamento(itemSelecionado.getDataHoraAgendamento());
                procedimento.setDataHoraInicioExecucao(itemSelecionado.getDataHoraInicioExecucao());
                procedimento.setDataHoraFimExecucao(itemSelecionado.getDataHoraFimExecucao());
                procedimento.setDataCancelamento(itemSelecionado.getDataCancelamento());
                procedimento.setMotivoCancelamento(itemSelecionado.getMotivoCancelamento());
                procedimento.setUsuarioCanceladorLogin(itemSelecionado.getUsuarioCanceladorLogin());
                
                service.atualizar(itemSelecionado.getId(), procedimento);
                mostrarSucesso("Sucesso", "Procedimento atualizado com sucesso!");
            }

            carregarDados();
            limparFormulario();
            resetarBotoes();
            habilitarCampos(false);
            modoEdicao = null;
            RefreshManager.getInstance().notifyRefresh();

        } catch (Exception e) {
            logger.error("Erro ao salvar procedimento", e);
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
            mostrarErro("Erro", "Selecione um procedimento para excluir");
            return;
        }

        // Confirmação antes de excluir
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir este procedimento?");
        confirmacao.setContentText("Paciente: " + itemSelecionado.getPacienteCpf() + "\n" +
                                  "Procedimento: " + itemSelecionado.getDescricaoProcedimento() + "\n" +
                                  "Data: " + (itemSelecionado.getDataHoraInicioPrevista() != null ? 
                                             itemSelecionado.getDataHoraInicioPrevista().format(DATE_TIME_FORMATTER) : "") + "\n\n" +
                                  "Esta ação não pode ser desfeita.");

        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    service.deletar(itemSelecionado.getId());
                    mostrarSucesso("Sucesso", "Procedimento excluído com sucesso!");
                    carregarDados();
                    limparFormulario();
                    resetarBotoes();
                    RefreshManager.getInstance().notifyRefresh();
                } catch (Exception e) {
                    logger.error("Erro ao excluir procedimento", e);
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

        // Validar CRM do Médico (se preenchido)
        if (ValidationUtils.validarCampoObrigatorio(txtMedicoCrm.getText()) && 
            !ValidationUtils.validarCRM(txtMedicoCrm.getText())) {
            mostrarAviso("CRM inválido. Deve conter entre 4 e 7 dígitos");
            txtMedicoCrm.requestFocus();
            return false;
        }

        // Validar Descrição do Procedimento
        if (!ValidationUtils.validarCampoObrigatorio(txtDescricaoProcedimento.getText())) {
            mostrarAviso("Descrição do procedimento é obrigatória");
            txtDescricaoProcedimento.requestFocus();
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

        // Validar Data/Hora Fim (opcional)
        if (dtDataFim.getValue() != null || ValidationUtils.validarCampoObrigatorio(txtHoraFim.getText())) {
            if (dtDataFim.getValue() == null) {
                mostrarAviso("Data de fim é obrigatória quando hora de fim é preenchida");
                dtDataFim.requestFocus();
                return false;
            }
            if (!ValidationUtils.validarCampoObrigatorio(txtHoraFim.getText())) {
                mostrarAviso("Hora de fim é obrigatória quando data de fim é preenchida");
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
        }

        // Validar Modalidade
        if (cbModalidade.getValue() == null) {
            mostrarAviso("Modalidade é obrigatória");
            cbModalidade.requestFocus();
            return false;
        }

        // Validar Nível de Risco
        if (cbNivelRisco.getValue() == null || cbNivelRisco.getValue().isEmpty()) {
            mostrarAviso("Nível de risco é obrigatório");
            cbNivelRisco.requestFocus();
            return false;
        }

        return true;
    }

    protected void habilitarCampos(boolean habilitar) {
        txtPacienteCpf.setDisable(!habilitar);
        txtMedicoCrm.setDisable(!habilitar);
        txtDescricaoProcedimento.setDisable(!habilitar);
        txtSalaEquipamento.setDisable(!habilitar);
        txtConvenioNome.setDisable(!habilitar);
        dtDataInicio.setDisable(!habilitar);
        txtHoraInicio.setDisable(!habilitar);
        dtDataFim.setDisable(!habilitar);
        txtHoraFim.setDisable(!habilitar);
        cbNivelRisco.setDisable(!habilitar);
        cbModalidade.setDisable(!habilitar);
        cbStatus.setDisable(!habilitar);
        txtObservacoes.setDisable(!habilitar);
        txtUsuarioCriador.setDisable(!habilitar);
    }

    private Procedimento construirProcedimentoDoFormulario() {
        Procedimento procedimento = new Procedimento();
        
        // Aplicar validações e limitações
        String cpf = ValidationUtils.limitarCPF(txtPacienteCpf.getText());
        procedimento.setPacienteCpf(cpf);
        
        String crm = ValidationUtils.limitarCRM(txtMedicoCrm.getText());
        procedimento.setMedicoCrm(crm);
        
        procedimento.setDescricaoProcedimento(txtDescricaoProcedimento.getText());
        procedimento.setSalaEquipamentoNecessario(txtSalaEquipamento.getText());
        procedimento.setConvenioNome(txtConvenioNome.getText());
        
        LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText(), TIME_FORMATTER);
        procedimento.setDataHoraInicioPrevista(LocalDateTime.of(dtDataInicio.getValue(), horaInicio));
        
        if (dtDataFim.getValue() != null && ValidationUtils.validarCampoObrigatorio(txtHoraFim.getText())) {
            LocalTime horaFim = LocalTime.parse(txtHoraFim.getText(), TIME_FORMATTER);
            procedimento.setDataHoraFimPrevista(LocalDateTime.of(dtDataFim.getValue(), horaFim));
        }
        
        procedimento.setNivelRisco(cbNivelRisco.getValue());
        procedimento.setModalidade(cbModalidade.getValue());
        procedimento.setStatus(cbStatus.getValue());
        procedimento.setObservacoes(txtObservacoes.getText());
        procedimento.setUsuarioCriadorLogin(txtUsuarioCriador.getText());
        
        return procedimento;
    }

    @FXML
    private void handleBuscar() {
        String termoBusca = txtBusca != null ? txtBusca.getText() : "";
        
        if (termoBusca == null || termoBusca.trim().isEmpty()) {
            procedimentos.clear();
            procedimentos.addAll(todosProcedimentos);
            logger.info("Busca limpa, mostrando todos {} procedimentos", procedimentos.size());
            return;
        }

        String busca = termoBusca.trim().toLowerCase();
        String buscaSemFormatacao = busca.replaceAll("[^0-9a-z]", "");
        
        procedimentos.clear();
        for (Procedimento procedimento : todosProcedimentos) {
            boolean encontrado = false;
            
            // Busca por CPF do paciente
            if (procedimento.getPacienteCpf() != null) {
                String cpf = procedimento.getPacienteCpf().toLowerCase();
                String cpfSemFormatacao = cpf.replaceAll("[^0-9]", "");
                if (cpf.contains(busca) || cpfSemFormatacao.contains(buscaSemFormatacao)) {
                    encontrado = true;
                }
            }
            
            // Busca por descrição do procedimento
            if (!encontrado && procedimento.getDescricaoProcedimento() != null) {
                if (procedimento.getDescricaoProcedimento().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            // Busca por CRM do médico
            if (!encontrado && procedimento.getMedicoCrm() != null) {
                String crm = procedimento.getMedicoCrm().toLowerCase();
                if (crm.contains(busca)) {
                    encontrado = true;
                }
            }
            
            // Busca por convênio
            if (!encontrado && procedimento.getConvenioNome() != null) {
                if (procedimento.getConvenioNome().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            // Busca por modalidade
            if (!encontrado && procedimento.getModalidade() != null) {
                if (procedimento.getModalidade().name().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            // Busca por status
            if (!encontrado && procedimento.getStatus() != null) {
                if (procedimento.getStatus().name().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            // Busca por ID
            if (!encontrado && procedimento.getId() != null) {
                if (procedimento.getId().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            if (encontrado) {
                procedimentos.add(procedimento);
            }
        }
        
        logger.info("Busca por '{}': {} resultados encontrados", termoBusca, procedimentos.size());
        
        if (procedimentos.isEmpty()) {
            mostrarAviso("Nenhum procedimento encontrado com o termo: " + termoBusca);
        }
    }

    @Override
    public void onRefresh() {
        carregarDados();
    }
    
    /**
     * Inicia um procedimento através da API
     */
    private void handleIniciar() {
        try {
            if (itemSelecionado == null || itemSelecionado.getId() == null) {
                mostrarErro("Erro", "Selecione um procedimento para iniciar.");
                return;
            }
            
            // Solicitar login do usuário
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Iniciar Procedimento");
            dialog.setHeaderText("Digite seu login de usuário:");
            dialog.setContentText("Login:");
            
            Optional<String> resultado = dialog.showAndWait();
            if (resultado.isPresent() && !resultado.get().trim().isEmpty()) {
                String usuarioLogin = resultado.get().trim();
                service.iniciar(itemSelecionado.getId(), usuarioLogin);
                mostrarSucesso("Sucesso", "Procedimento iniciado com sucesso!");
                carregarDados();
                limparFormulario();
                cbAcao.setValue(null);
                RefreshManager.getInstance().notifyRefresh();
            }
        } catch (Exception e) {
            logger.error("Erro ao iniciar procedimento", e);
            mostrarErro("Erro ao iniciar", e.getMessage());
        }
    }
    
    /**
     * Finaliza um procedimento através da API
     */
    private void handleFinalizar() {
        try {
            if (itemSelecionado == null || itemSelecionado.getId() == null) {
                mostrarErro("Erro", "Selecione um procedimento para finalizar.");
                return;
            }
            
            // Solicitar login do usuário
            TextInputDialog dialogLogin = new TextInputDialog();
            dialogLogin.setTitle("Finalizar Procedimento");
            dialogLogin.setHeaderText("Digite seu login de usuário:");
            dialogLogin.setContentText("Login:");
            
            Optional<String> resultadoLogin = dialogLogin.showAndWait();
            if (!resultadoLogin.isPresent() || resultadoLogin.get().trim().isEmpty()) {
                return;
            }
            
            String usuarioLogin = resultadoLogin.get().trim();
            
            // Solicitar observações
            TextInputDialog dialogObs = new TextInputDialog();
            dialogObs.setTitle("Finalizar Procedimento");
            dialogObs.setHeaderText("Digite observações sobre o atendimento (opcional):");
            dialogObs.setContentText("Observações:");
            
            Optional<String> resultadoObs = dialogObs.showAndWait();
            String observacoes = resultadoObs.orElse("");
            
            service.finalizar(itemSelecionado.getId(), usuarioLogin, observacoes);
            mostrarSucesso("Sucesso", "Procedimento finalizado com sucesso!");
            carregarDados();
            limparFormulario();
            cbAcao.setValue(null);
            RefreshManager.getInstance().notifyRefresh();
            
        } catch (Exception e) {
            logger.error("Erro ao finalizar procedimento", e);
            mostrarErro("Erro ao finalizar", e.getMessage());
        }
    }
    
    /**
     * Cancela um procedimento através da API
     */
    private void handleCancelarProcedimento() {
        try {
            if (itemSelecionado == null || itemSelecionado.getId() == null) {
                mostrarErro("Erro", "Selecione um procedimento para cancelar.");
                return;
            }
            
            // Solicitar motivo do cancelamento
            TextInputDialog dialogMotivo = new TextInputDialog();
            dialogMotivo.setTitle("Cancelar Procedimento");
            dialogMotivo.setHeaderText("Digite o motivo do cancelamento:");
            dialogMotivo.setContentText("Motivo:");
            
            Optional<String> resultadoMotivo = dialogMotivo.showAndWait();
            if (!resultadoMotivo.isPresent() || resultadoMotivo.get().trim().isEmpty()) {
                mostrarErro("Erro", "O motivo do cancelamento é obrigatório.");
                return;
            }
            
            String motivo = resultadoMotivo.get().trim();
            
            // Solicitar login do usuário
            TextInputDialog dialogLogin = new TextInputDialog();
            dialogLogin.setTitle("Cancelar Procedimento");
            dialogLogin.setHeaderText("Digite seu login de usuário:");
            dialogLogin.setContentText("Login:");
            
            Optional<String> resultadoLogin = dialogLogin.showAndWait();
            if (!resultadoLogin.isPresent() || resultadoLogin.get().trim().isEmpty()) {
                return;
            }
            
            String usuarioLogin = resultadoLogin.get().trim();
            
            service.cancelar(itemSelecionado.getId(), motivo, usuarioLogin);
            mostrarSucesso("Sucesso", "Procedimento cancelado com sucesso!");
            carregarDados();
            limparFormulario();
            cbAcao.setValue(null);
            RefreshManager.getInstance().notifyRefresh();
            
        } catch (Exception e) {
            logger.error("Erro ao cancelar procedimento", e);
            mostrarErro("Erro ao cancelar", e.getMessage());
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
