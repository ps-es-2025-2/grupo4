package br.com.simplehealth.agendamento.controller;

import br.com.simplehealth.agendamento.model.Procedimento;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Controller para gerenciar a interface de Procedimentos.
 */
public class ProcedimentoController implements RefreshManager.RefreshListener {

    private static final Logger logger = LoggerFactory.getLogger(ProcedimentoController.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
    @FXML private TextField txtDataHoraInicio;
    @FXML private TextField txtDataHoraFim;
    @FXML private ComboBox<String> cbNivelRisco;
    @FXML private ComboBox<String> cbModalidade;
    @FXML private ComboBox<String> cbStatus;
    @FXML private TextArea txtObservacoes;
    @FXML private TextField txtUsuarioCriador;
    @FXML private TextField txtBusca;
    @FXML private Button btnBuscar;

    private final ProcedimentoService service;
    private final ObservableList<Procedimento> procedimentos;
    private final ObservableList<Procedimento> todosProcedimentos;
    private Procedimento procedimentoSelecionado;

    public ProcedimentoController() {
        this.service = new ProcedimentoService();
        this.procedimentos = FXCollections.observableArrayList();
        this.todosProcedimentos = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        logger.info("Inicializando ProcedimentoController");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPacienteCpf.setCellValueFactory(new PropertyValueFactory<>("pacienteCpf"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricaoProcedimento"));
        colDataHora.setCellValueFactory(cellData -> {
            LocalDateTime dataHora = cellData.getValue().getDataHoraInicio();
            return new javafx.beans.property.SimpleStringProperty(
                dataHora != null ? dataHora.format(DATE_TIME_FORMATTER) : ""
            );
        });
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.setItems(procedimentos);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                procedimentoSelecionado = newSelection;
                preencherFormulario(newSelection);
            }
        });

        cbNivelRisco.setItems(FXCollections.observableArrayList("BAIXO", "MÉDIO", "ALTO"));
        cbModalidade.setItems(FXCollections.observableArrayList("PRESENCIAL", "ONLINE"));
        cbStatus.setItems(FXCollections.observableArrayList("ATIVO", "CANCELADO", "REALIZADO"));

        RefreshManager.getInstance().addListener(this);
        carregarDados();
    }

    private void carregarDados() {
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
        txtDataHoraInicio.setText(procedimento.getDataHoraInicio() != null ? 
            procedimento.getDataHoraInicio().format(DATE_TIME_FORMATTER) : "");
        txtDataHoraFim.setText(procedimento.getDataHoraFim() != null ? 
            procedimento.getDataHoraFim().format(DATE_TIME_FORMATTER) : "");
        cbNivelRisco.setValue(procedimento.getNivelRisco());
        cbModalidade.setValue(procedimento.getModalidade());
        cbStatus.setValue(procedimento.getStatus());
        txtObservacoes.setText(procedimento.getObservacoes());
        txtUsuarioCriador.setText(procedimento.getUsuarioCriadorLogin());
    }

    private void limparFormulario() {
        procedimentoSelecionado = null;
        txtPacienteCpf.clear();
        txtMedicoCrm.clear();
        txtDescricaoProcedimento.clear();
        txtSalaEquipamento.clear();
        txtConvenioNome.clear();
        txtDataHoraInicio.clear();
        txtDataHoraFim.clear();
        cbNivelRisco.setValue(null);
        cbModalidade.setValue("PRESENCIAL");
        cbStatus.setValue("ATIVO");
        txtObservacoes.clear();
        txtUsuarioCriador.clear();
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleNovo() {
        limparFormulario();
        txtPacienteCpf.requestFocus();
    }

    @FXML
    private void handleSalvar() {
        try {
            if (!validarCampos()) {
                return;
            }

            Procedimento procedimento = procedimentoSelecionado != null ? procedimentoSelecionado : new Procedimento();
            
            procedimento.setPacienteCpf(txtPacienteCpf.getText());
            procedimento.setMedicoCrm(txtMedicoCrm.getText());
            procedimento.setDescricaoProcedimento(txtDescricaoProcedimento.getText());
            procedimento.setSalaEquipamentoNecessario(txtSalaEquipamento.getText());
            procedimento.setConvenioNome(txtConvenioNome.getText());
            procedimento.setDataHoraInicio(LocalDateTime.parse(txtDataHoraInicio.getText(), DATE_TIME_FORMATTER));
            procedimento.setDataHoraFim(LocalDateTime.parse(txtDataHoraFim.getText(), DATE_TIME_FORMATTER));
            procedimento.setNivelRisco(cbNivelRisco.getValue());
            procedimento.setModalidade(cbModalidade.getValue());
            procedimento.setStatus(cbStatus.getValue());
            procedimento.setObservacoes(txtObservacoes.getText());
            procedimento.setUsuarioCriadorLogin(txtUsuarioCriador.getText());

            if (procedimentoSelecionado != null && procedimentoSelecionado.getId() != null) {
                service.atualizar(procedimentoSelecionado.getId(), procedimento);
                mostrarSucesso("Procedimento atualizado com sucesso!");
            } else {
                service.criar(procedimento);
                mostrarSucesso("Procedimento criado com sucesso!");
            }

            carregarDados();
            limparFormulario();
            RefreshManager.getInstance().notifyRefresh();

        } catch (Exception e) {
            logger.error("Erro ao salvar procedimento", e);
            mostrarErro("Erro ao salvar", e.getMessage());
        }
    }

    @FXML
    private void handleCancelar() {
        limparFormulario();
    }

    @FXML
    private void handleExcluir() {
        if (procedimentoSelecionado == null || procedimentoSelecionado.getId() == null) {
            mostrarAviso("Selecione um procedimento para excluir");
            return;
        }

        // Confirmação antes de excluir
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir este procedimento?");
        confirmacao.setContentText("Paciente: " + procedimentoSelecionado.getPacienteCpf() + "\n" +
                                  "Procedimento: " + procedimentoSelecionado.getDescricaoProcedimento() + "\n" +
                                  "Data: " + (procedimentoSelecionado.getDataHoraInicio() != null ? 
                                             procedimentoSelecionado.getDataHoraInicio().format(DATE_TIME_FORMATTER) : "") + "\n\n" +
                                  "Esta ação não pode ser desfeita.");

        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    service.deletar(procedimentoSelecionado.getId());
                    mostrarSucesso("Procedimento excluído com sucesso!");
                    carregarDados();
                    limparFormulario();
                    RefreshManager.getInstance().notifyRefresh();
                } catch (Exception e) {
                    logger.error("Erro ao excluir procedimento", e);
                    mostrarErro("Erro ao excluir", e.getMessage());
                }
            }
        });
    }

    private boolean validarCampos() {
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
        if (cbModalidade.getValue() == null || cbModalidade.getValue().isEmpty()) {
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
                if (procedimento.getModalidade().toLowerCase().contains(busca)) {
                    encontrado = true;
                }
            }
            
            // Busca por status
            if (!encontrado && procedimento.getStatus() != null) {
                if (procedimento.getStatus().toLowerCase().contains(busca)) {
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

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAviso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
