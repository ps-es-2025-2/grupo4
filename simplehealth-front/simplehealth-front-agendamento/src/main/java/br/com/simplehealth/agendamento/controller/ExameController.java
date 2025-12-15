package br.com.simplehealth.agendamento.controller;

import br.com.simplehealth.agendamento.model.Exame;
import br.com.simplehealth.agendamento.model.enums.AcaoAgendamentoEnum;
import br.com.simplehealth.agendamento.model.enums.ModalidadeEnum;
import br.com.simplehealth.agendamento.model.enums.StatusAgendamentoEnum;
import br.com.simplehealth.agendamento.service.ExameService;
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
 * Controller para gerenciar a interface de Exames.
 */
public class ExameController extends AbstractCrudController<Exame> implements RefreshManager.RefreshListener {

    private static final Logger logger = LoggerFactory.getLogger(ExameController.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @FXML private TableView<Exame> tableView;
    @FXML private TableColumn<Exame, String> colId;
    @FXML private TableColumn<Exame, String> colPacienteCpf;
    @FXML private TableColumn<Exame, String> colNomeExame;
    @FXML private TableColumn<Exame, String> colDataHora;
    @FXML private TableColumn<Exame, String> colStatus;

    @FXML private TextField txtPacienteCpf;
    @FXML private TextField txtMedicoCrm;
    @FXML private TextField txtNomeExame;
    @FXML private TextField txtConvenioNome;
    @FXML private DatePicker dtDataInicio;
    @FXML private TextField txtHoraInicio;
    @FXML private DatePicker dtDataFim;
    @FXML private TextField txtHoraFim;
    @FXML private ComboBox<ModalidadeEnum> cbModalidade;
    @FXML private ComboBox<StatusAgendamentoEnum> cbStatus;
    @FXML private CheckBox chkRequerPreparo;
    @FXML private TextArea txtInstrucoesPreparo;
    @FXML private TextArea txtObservacoes;
    @FXML private TextField txtUsuarioCriador;
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
    @FXML private TextField txtBusca;

    private final ExameService service;
    private final ObservableList<Exame> exames;
    private final ObservableList<Exame> todosExames;

    public ExameController() {
        this.service = new ExameService();
        this.exames = FXCollections.observableArrayList();
        this.todosExames = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        super.btnCriar = this.btnCriar;
        super.btnAlterar = this.btnAlterar;
        super.btnDeletar = this.btnDeletar;
        super.btnConfirmar = this.btnConfirmar;
        super.btnCancelar = this.btnCancelar;

        logger.info("Inicializando ExameController");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPacienteCpf.setCellValueFactory(new PropertyValueFactory<>("pacienteCpf"));
        colNomeExame.setCellValueFactory(new PropertyValueFactory<>("nomeExame"));
        colDataHora.setCellValueFactory(cellData -> {
            LocalDateTime dataHora = cellData.getValue().getDataHoraInicioPrevista();
            return new javafx.beans.property.SimpleStringProperty(
                dataHora != null ? dataHora.format(DATE_TIME_FORMATTER) : ""
            );
        });
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.setItems(exames);
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
                todosExames.clear();
                todosExames.addAll(service.listarTodos());
                exames.clear();
                exames.addAll(todosExames);
                logger.info("Dados carregados: {} exames", exames.size());
            } catch (Exception e) {
                logger.error("Erro ao carregar exames", e);
                mostrarErro("Erro ao carregar dados", extrairMensagemErro(e));
            }
        });
    }

    private void preencherFormulario(Exame exame) {
        txtPacienteCpf.setText(exame.getPacienteCpf());
        txtMedicoCrm.setText(exame.getMedicoCrm());
        txtNomeExame.setText(exame.getNomeExame());
        txtConvenioNome.setText(exame.getConvenioNome());
        
        if (exame.getDataHoraInicioPrevista() != null) {
            dtDataInicio.setValue(exame.getDataHoraInicioPrevista().toLocalDate());
            txtHoraInicio.setText(exame.getDataHoraInicioPrevista().format(TIME_FORMATTER));
        }
        
        if (exame.getDataHoraFimPrevista() != null) {
            dtDataFim.setValue(exame.getDataHoraFimPrevista().toLocalDate());
            txtHoraFim.setText(exame.getDataHoraFimPrevista().format(TIME_FORMATTER));
        }
        
        cbModalidade.setValue(exame.getModalidade());
        cbStatus.setValue(exame.getStatus());
        chkRequerPreparo.setSelected(exame.getRequerPreparo() != null && exame.getRequerPreparo());
        txtInstrucoesPreparo.setText(exame.getInstrucoesPreparo());
        txtObservacoes.setText(exame.getObservacoes());
        txtUsuarioCriador.setText(exame.getUsuarioCriadorLogin());
    }

    protected void limparFormulario() {
        itemSelecionado = null;
        txtPacienteCpf.clear();
        txtMedicoCrm.clear();
        txtNomeExame.clear();
        txtConvenioNome.clear();
        dtDataInicio.setValue(null);
        txtHoraInicio.clear();
        dtDataFim.setValue(null);
        txtHoraFim.clear();
        cbModalidade.setValue(ModalidadeEnum.PRESENCIAL);
        cbStatus.setValue(StatusAgendamentoEnum.ATIVO);
        chkRequerPreparo.setSelected(false);
        txtInstrucoesPreparo.clear();
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
            mostrarErro("Erro", "Selecione um exame para realizar uma ação.");
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
                handleCancelarExame();
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
                Exame exame = construirExameDoFormulario();
                service.criar(exame);
                mostrarSucesso("Sucesso", "Exame criado com sucesso!");
            } else if ("ALTERAR".equals(modoEdicao)) {
                if (itemSelecionado == null || itemSelecionado.getId() == null) {
                    mostrarErro("Erro", "Nenhum exame selecionado para alterar.");
                    return;
                }
                
                logger.info("Alterando exame com ID: {}", itemSelecionado.getId());
                
                Exame exame = construirExameDoFormulario();
                // Preservar campos que não estão no formulário
                exame.setId(itemSelecionado.getId());
                exame.setDataHoraAgendamento(itemSelecionado.getDataHoraAgendamento());
                exame.setDataHoraInicioExecucao(itemSelecionado.getDataHoraInicioExecucao());
                exame.setDataHoraFimExecucao(itemSelecionado.getDataHoraFimExecucao());
                exame.setDataCancelamento(itemSelecionado.getDataCancelamento());
                exame.setMotivoCancelamento(itemSelecionado.getMotivoCancelamento());
                exame.setUsuarioCanceladorLogin(itemSelecionado.getUsuarioCanceladorLogin());
                
                service.atualizar(itemSelecionado.getId(), exame);
                mostrarSucesso("Sucesso", "Exame atualizado com sucesso!");
            }

            carregarDados();
            limparFormulario();
            resetarBotoes();
            habilitarCampos(false);
            modoEdicao = null;
            RefreshManager.getInstance().notifyRefresh();

        } catch (Exception e) {
            logger.error("Erro ao salvar exame", e);
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
            mostrarErro("Erro", "Selecione um exame para excluir");
            return;
        }

        // Confirmação antes de excluir
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir este exame?");
        confirmacao.setContentText("Paciente: " + itemSelecionado.getPacienteCpf() + "\n" +
                                  "Exame: " + itemSelecionado.getNomeExame() + "\n" +
                                  "Data: " + (itemSelecionado.getDataHoraInicioPrevista() != null ? 
                                             itemSelecionado.getDataHoraInicioPrevista().format(DATE_TIME_FORMATTER) : "") + "\n\n" +
                                  "Esta ação não pode ser desfeita.");

        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    service.deletar(itemSelecionado.getId());
                    mostrarSucesso("Sucesso", "Exame excluído com sucesso!");
                    carregarDados();
                    limparFormulario();
                    resetarBotoes();
                    RefreshManager.getInstance().notifyRefresh();
                } catch (Exception e) {
                    logger.error("Erro ao excluir exame", e);
                    mostrarErro("Erro ao excluir", extrairMensagemErro(e));
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

        // Validar Nome do Exame
        if (!ValidationUtils.validarCampoObrigatorio(txtNomeExame.getText())) {
            mostrarAviso("Nome do exame é obrigatório");
            txtNomeExame.requestFocus();
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

        // Validar instruções de preparo se requer preparo
        if (chkRequerPreparo.isSelected() && !ValidationUtils.validarCampoObrigatorio(txtInstrucoesPreparo.getText())) {
            mostrarAviso("Instruções de preparo são obrigatórias quando marcado 'Requer Preparo'");
            txtInstrucoesPreparo.requestFocus();
            return false;
        }

        return true;
    }

    protected void habilitarCampos(boolean habilitar) {
        txtPacienteCpf.setDisable(!habilitar);
        txtMedicoCrm.setDisable(!habilitar);
        txtNomeExame.setDisable(!habilitar);
        txtConvenioNome.setDisable(!habilitar);
        dtDataInicio.setDisable(!habilitar);
        txtHoraInicio.setDisable(!habilitar);
        dtDataFim.setDisable(!habilitar);
        txtHoraFim.setDisable(!habilitar);
        cbModalidade.setDisable(!habilitar);
        cbStatus.setDisable(!habilitar);
        chkRequerPreparo.setDisable(!habilitar);
        txtInstrucoesPreparo.setDisable(!habilitar);
        txtObservacoes.setDisable(!habilitar);
        txtUsuarioCriador.setDisable(!habilitar);
    }

    private Exame construirExameDoFormulario() {
        Exame exame = new Exame();
        
        // Aplicar validações e limitações
        String cpf = ValidationUtils.limitarCPF(txtPacienteCpf.getText());
        exame.setPacienteCpf(cpf);
        
        String crm = ValidationUtils.limitarCRM(txtMedicoCrm.getText());
        exame.setMedicoCrm(crm);
        
        exame.setNomeExame(txtNomeExame.getText());
        exame.setConvenioNome(txtConvenioNome.getText());
        
        LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText(), TIME_FORMATTER);
        exame.setDataHoraInicioPrevista(LocalDateTime.of(dtDataInicio.getValue(), horaInicio));
        
        if (dtDataFim.getValue() != null && ValidationUtils.validarCampoObrigatorio(txtHoraFim.getText())) {
            LocalTime horaFim = LocalTime.parse(txtHoraFim.getText(), TIME_FORMATTER);
            exame.setDataHoraFimPrevista(LocalDateTime.of(dtDataFim.getValue(), horaFim));
        }
        
        exame.setModalidade(cbModalidade.getValue());
        exame.setStatus(cbStatus.getValue());
        exame.setRequerPreparo(chkRequerPreparo.isSelected());
        exame.setInstrucoesPreparo(txtInstrucoesPreparo.getText());
        exame.setObservacoes(txtObservacoes.getText());
        exame.setUsuarioCriadorLogin(txtUsuarioCriador.getText());
        return exame;
    }

    @Override
    public void onRefresh() {
        carregarDados();
    }

    @FXML
    private void handleBuscar() {
        String termoBusca = txtBusca != null ? txtBusca.getText() : "";
        
        if (termoBusca == null || termoBusca.trim().isEmpty()) {
            exames.clear();
            exames.addAll(todosExames);
            logger.info("Busca limpa, mostrando todos {} exames", exames.size());
            return;
        }

        String busca = termoBusca.trim().toLowerCase();
        String buscaSemFormatacao = busca.replaceAll("[^0-9a-z]", "");
        
        exames.clear();
        for (Exame exame : todosExames) {
            boolean encontrado = false;
            
            // Busca por CPF do paciente
            if (exame.getPacienteCpf() != null) {
                String cpf = exame.getPacienteCpf().toLowerCase();
                String cpfSemFormatacao = cpf.replaceAll("[^0-9]", "");
                if (cpf.contains(busca) || cpfSemFormatacao.contains(buscaSemFormatacao)) {
                    encontrado = true;
                }
            }
            
            // Busca por nome do exame
            if (!encontrado && exame.getNomeExame() != null) {
                if (exame.getNomeExame().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            // Busca por CRM do médico
            if (!encontrado && exame.getMedicoCrm() != null) {
                String crm = exame.getMedicoCrm().toLowerCase();
                if (crm.contains(busca)) {
                    encontrado = true;
                }
            }
            
            // Busca por status
            if (!encontrado && exame.getStatus() != null) {
                if (exame.getStatus().name().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            // Busca por ID
            if (!encontrado && exame.getId() != null) {
                if (exame.getId().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            if (encontrado) {
                exames.add(exame);
            }
        }
        
        logger.info("Busca por '{}': {} resultados encontrados", termoBusca, exames.size());
        
        if (exames.isEmpty()) {
            mostrarAviso("Nenhum exame encontrado com o termo: " + termoBusca);
        }
    }
    
    /**
     * Inicia um exame através da API
     */
    private void handleIniciar() {
        try {
            if (itemSelecionado == null || itemSelecionado.getId() == null) {
                mostrarErro("Erro", "Selecione um exame para iniciar.");
                return;
            }
            
            // Solicitar login do usuário
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Iniciar Exame");
            dialog.setHeaderText("Digite seu login de usuário:");
            dialog.setContentText("Login:");
            
            Optional<String> resultado = dialog.showAndWait();
            if (resultado.isPresent() && !resultado.get().trim().isEmpty()) {
                String usuarioLogin = resultado.get().trim();
                service.iniciar(itemSelecionado.getId(), usuarioLogin);
                mostrarSucesso("Sucesso", "Exame iniciado com sucesso!");
                carregarDados();
                limparFormulario();
                cbAcao.setValue(null);
                RefreshManager.getInstance().notifyRefresh();
            }
        } catch (Exception e) {
            logger.error("Erro ao iniciar exame", e);
            mostrarErro("Erro ao iniciar", extrairMensagemErro(e));
        }
    }
    
    /**
     * Finaliza um exame através da API
     */
    private void handleFinalizar() {
        try {
            if (itemSelecionado == null || itemSelecionado.getId() == null) {
                mostrarErro("Erro", "Selecione um exame para finalizar.");
                return;
            }
            
            // Solicitar login do usuário
            TextInputDialog dialogLogin = new TextInputDialog();
            dialogLogin.setTitle("Finalizar Exame");
            dialogLogin.setHeaderText("Digite seu login de usuário:");
            dialogLogin.setContentText("Login:");
            
            Optional<String> resultadoLogin = dialogLogin.showAndWait();
            if (!resultadoLogin.isPresent() || resultadoLogin.get().trim().isEmpty()) {
                return;
            }
            
            String usuarioLogin = resultadoLogin.get().trim();
            
            // Solicitar observações
            TextInputDialog dialogObs = new TextInputDialog();
            dialogObs.setTitle("Finalizar Exame");
            dialogObs.setHeaderText("Digite observações sobre o atendimento (opcional):");
            dialogObs.setContentText("Observações:");
            
            Optional<String> resultadoObs = dialogObs.showAndWait();
            String observacoes = resultadoObs.orElse("");
            
            service.finalizar(itemSelecionado.getId(), usuarioLogin, observacoes);
            mostrarSucesso("Sucesso", "Exame finalizado com sucesso!");
            carregarDados();
            limparFormulario();
            cbAcao.setValue(null);
            RefreshManager.getInstance().notifyRefresh();
            
        } catch (Exception e) {
            logger.error("Erro ao finalizar exame", e);
            mostrarErro("Erro ao finalizar", extrairMensagemErro(e));
        }
    }
    
    /**
     * Cancela um exame através da API
     */
    private void handleCancelarExame() {
        try {
            if (itemSelecionado == null || itemSelecionado.getId() == null) {
                mostrarErro("Erro", "Selecione um exame para cancelar.");
                return;
            }
            
            // Solicitar motivo do cancelamento
            TextInputDialog dialogMotivo = new TextInputDialog();
            dialogMotivo.setTitle("Cancelar Exame");
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
            dialogLogin.setTitle("Cancelar Exame");
            dialogLogin.setHeaderText("Digite seu login de usuário:");
            dialogLogin.setContentText("Login:");
            
            Optional<String> resultadoLogin = dialogLogin.showAndWait();
            if (!resultadoLogin.isPresent() || resultadoLogin.get().trim().isEmpty()) {
                return;
            }
            
            String usuarioLogin = resultadoLogin.get().trim();
            
            service.cancelar(itemSelecionado.getId(), motivo, usuarioLogin);
            mostrarSucesso("Sucesso", "Exame cancelado com sucesso!");
            carregarDados();
            limparFormulario();
            cbAcao.setValue(null);
            RefreshManager.getInstance().notifyRefresh();
            
        } catch (Exception e) {
            logger.error("Erro ao cancelar exame", e);
            mostrarErro("Erro ao cancelar", extrairMensagemErro(e));
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
