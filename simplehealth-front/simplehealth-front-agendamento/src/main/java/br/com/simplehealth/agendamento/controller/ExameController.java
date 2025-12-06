package br.com.simplehealth.agendamento.controller;

import br.com.simplehealth.agendamento.model.Exame;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Controller para gerenciar a interface de Exames.
 */
public class ExameController extends AbstractCrudController<Exame> implements RefreshManager.RefreshListener {

    private static final Logger logger = LoggerFactory.getLogger(ExameController.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
    @FXML private TextField txtDataHoraInicio;
    @FXML private TextField txtDataHoraFim;
    @FXML private ComboBox<ModalidadeEnum> cbModalidade;
    @FXML private ComboBox<StatusAgendamentoEnum> cbStatus;
    @FXML private CheckBox chkRequerPreparo;
    @FXML private TextArea txtInstrucoesPreparo;
    @FXML private TextArea txtObservacoes;
    @FXML private TextField txtUsuarioCriador;
    
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
            LocalDateTime dataHora = cellData.getValue().getDataHoraInicio();
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
            }
        });

        // Configurar ComboBoxes com enums
        cbModalidade.setItems(FXCollections.observableArrayList(ModalidadeEnum.values()));
        cbStatus.setItems(FXCollections.observableArrayList(StatusAgendamentoEnum.values()));

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
                mostrarErro("Erro ao carregar dados", e.getMessage());
            }
        });
    }

    private void preencherFormulario(Exame exame) {
        txtPacienteCpf.setText(exame.getPacienteCpf());
        txtMedicoCrm.setText(exame.getMedicoCrm());
        txtNomeExame.setText(exame.getNomeExame());
        txtConvenioNome.setText(exame.getConvenioNome());
        txtDataHoraInicio.setText(exame.getDataHoraInicio() != null ? 
            exame.getDataHoraInicio().format(DATE_TIME_FORMATTER) : "");
        txtDataHoraFim.setText(exame.getDataHoraFim() != null ? 
            exame.getDataHoraFim().format(DATE_TIME_FORMATTER) : "");
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
        txtDataHoraInicio.clear();
        txtDataHoraFim.clear();
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
            mostrarErro("Erro", "Selecione um exame para alterar.");
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
                Exame exame = construirExameDoFormulario();
                service.criar(exame);
                mostrarSucesso("Sucesso", "Exame criado com sucesso!");
            } else if ("ALTERAR".equals(modoEdicao)) {
                if (itemSelecionado == null || itemSelecionado.getId() == null) {
                    mostrarErro("Erro", "Item selecionado inválido.");
                    return;
                }
                Exame exame = construirExameDoFormulario();
                exame.setId(itemSelecionado.getId());
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
            mostrarErro("Erro", "Selecione um exame para excluir");
            return;
        }

        // Confirmação antes de excluir
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir este exame?");
        confirmacao.setContentText("Paciente: " + itemSelecionado.getPacienteCpf() + "\n" +
                                  "Exame: " + itemSelecionado.getNomeExame() + "\n" +
                                  "Data: " + (itemSelecionado.getDataHoraInicio() != null ? 
                                             itemSelecionado.getDataHoraInicio().format(DATE_TIME_FORMATTER) : "") + "\n\n" +
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

        // Validar Nome do Exame
        if (!ValidationUtils.validarCampoObrigatorio(txtNomeExame.getText())) {
            mostrarAviso("Nome do exame é obrigatório");
            txtNomeExame.requestFocus();
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

        // Validar Data/Hora Fim (se preenchida)
        if (ValidationUtils.validarCampoObrigatorio(txtDataHoraFim.getText())) {
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
        txtDataHoraInicio.setDisable(!habilitar);
        txtDataHoraFim.setDisable(!habilitar);
        cbModalidade.setDisable(!habilitar);
        cbStatus.setDisable(!habilitar);
        chkRequerPreparo.setDisable(!habilitar);
        txtInstrucoesPreparo.setDisable(!habilitar);
        txtObservacoes.setDisable(!habilitar);
        txtUsuarioCriador.setDisable(!habilitar);
    }

    private Exame construirExameDoFormulario() {
        Exame exame = new Exame();
        exame.setPacienteCpf(txtPacienteCpf.getText());
        exame.setMedicoCrm(txtMedicoCrm.getText());
        exame.setNomeExame(txtNomeExame.getText());
        exame.setConvenioNome(txtConvenioNome.getText());
        exame.setDataHoraInicio(LocalDateTime.parse(txtDataHoraInicio.getText(), DATE_TIME_FORMATTER));
        if (txtDataHoraFim.getText() != null && !txtDataHoraFim.getText().trim().isEmpty()) {
            exame.setDataHoraFim(LocalDateTime.parse(txtDataHoraFim.getText(), DATE_TIME_FORMATTER));
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

    private void mostrarAviso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
