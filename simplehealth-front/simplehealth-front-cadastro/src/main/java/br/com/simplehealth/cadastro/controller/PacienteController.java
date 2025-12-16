package br.com.simplehealth.cadastro.controller;

import br.com.simplehealth.cadastro.model.*;
import br.com.simplehealth.cadastro.service.ConvenioService;
import br.com.simplehealth.cadastro.service.PacienteService;
import br.com.simplehealth.cadastro.util.RefreshManager;
import br.com.simplehealth.cadastro.util.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller para o CRUD de Pacientes.
 */
public class PacienteController extends AbstractCrudController<Paciente> {

    @FXML
    private TableView<Paciente> tabelaPacientes;
    @FXML
    private TableColumn<Paciente, Long> colunaId;
    @FXML
    private TableColumn<Paciente, String> colunaNome;
    @FXML
    private TableColumn<Paciente, LocalDate> colunaDataNascimento;
    @FXML
    private TableColumn<Paciente, String> colunaCpf;
    @FXML
    private TableColumn<Paciente, String> colunaTelefone;
    @FXML
    private TableColumn<Paciente, String> colunaEmail;
    @FXML
    private TableColumn<Paciente, String> colunaConvenio;

    @FXML
    private TextField txtBusca;
    @FXML
    private TextField txtNome;
    @FXML
    private DatePicker dtDataNascimento;
    @FXML
    private TextField txtCpf;
    @FXML
    private TextField txtTelefone;
    @FXML
    private TextField txtEmail;
    @FXML
    private ComboBox<Convenio> cbConvenio;
    
    // Botões herdados do AbstractCrudController
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
    @FXML
    private Button btnHistorico;

    private final PacienteService pacienteService = new PacienteService();
    private final ConvenioService convenioService = new ConvenioService();
    private final ObservableList<Paciente> listaPacientes = FXCollections.observableArrayList();
    private FilteredList<Paciente> pacientesFiltrados;

    @FXML
    public void initialize() {
        super.btnCriar = this.btnCriar;
        super.btnAlterar = this.btnAlterar;
        super.btnDeletar = this.btnDeletar;
        super.btnConfirmar = this.btnConfirmar;
        super.btnCancelar = this.btnCancelar;

        configurarTabela();
        carregarDados();
        configurarListeners();
        carregarConvenios();
        configurarBusca();
        configurarEstadoInicialBotoes();
        habilitarCampos(false);
        
        // Registrar para refresh automático
        RefreshManager.addRefreshListener(this::carregarDados);
    }

    private void configurarBusca() {
        pacientesFiltrados = new FilteredList<>(listaPacientes, p -> true);
        tabelaPacientes.setItems(pacientesFiltrados);
    }

    private void carregarConvenios() {
        try {
            List<Convenio> convenios = convenioService.listarTodos();
            cbConvenio.setItems(FXCollections.observableArrayList(convenios));
            cbConvenio.setPromptText("Selecione o convênio (opcional)");
        } catch (Exception e) {
            logger.error("Erro ao carregar convênios", e);
        }
    }

    private void configurarTabela() {
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nomeCompleto"));
        colunaDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        colunaCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colunaConvenio.setCellValueFactory(new PropertyValueFactory<>("convenioNome"));
        
        tabelaPacientes.setItems(listaPacientes);
    }

    private void configurarListeners() {
        tabelaPacientes.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    itemSelecionado = newValue;
                    preencherFormulario(newValue);
                    habilitarBotoesSelecao();
                }
            }
        );
    }

    @Override
    protected void carregarDados() {
        try {
            listaPacientes.clear();
            listaPacientes.addAll(pacienteService.listarTodos());
        } catch (Exception e) {
            logger.error("Erro ao carregar pacientes", e);
            mostrarErro("Erro", "Não foi possível carregar os pacientes: " + extrairMensagemErro(e));
        }
    }

    private void preencherFormulario(Paciente paciente) {
        txtNome.setText(paciente.getNomeCompleto());
        dtDataNascimento.setValue(paciente.getDataNascimento());
        txtCpf.setText(paciente.getCpf());
        txtTelefone.setText(paciente.getTelefone());
        txtEmail.setText(paciente.getEmail());
        
        // Buscar o convênio na lista do ComboBox pelo ID
        if (paciente.getConvenioId() != null) {
            Convenio convenioSelecionado = cbConvenio.getItems().stream()
                .filter(c -> c.getId().equals(paciente.getConvenioId()))
                .findFirst()
                .orElse(null);
            cbConvenio.setValue(convenioSelecionado);
        } else {
            cbConvenio.setValue(null);
        }
    }

    @FXML
    private void handleCriar() {
        limparFormulario();
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "CRIAR";
    }

    @FXML
    private void handleAlterar() {
        if (itemSelecionado == null) {
            mostrarErro("Erro", "Selecione um paciente para alterar.");
            return;
        }
        
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "ALTERAR";
    }

    @FXML
    private void handleBuscar() {
        String termoBusca = txtBusca.getText().toLowerCase().trim();
        
        if (termoBusca.isEmpty()) {
            pacientesFiltrados.setPredicate(p -> true);
        } else {
            pacientesFiltrados.setPredicate(paciente -> {
                if (paciente.getNomeCompleto() != null && 
                    paciente.getNomeCompleto().toLowerCase().contains(termoBusca)) {
                    return true;
                }
                if (paciente.getCpf() != null && 
                    paciente.getCpf().replaceAll("[^0-9]", "").contains(termoBusca.replaceAll("[^0-9]", ""))) {
                    return true;
                }
                if (paciente.getEmail() != null && 
                    paciente.getEmail().toLowerCase().contains(termoBusca)) {
                    return true;
                }
                if (paciente.getTelefone() != null && 
                    paciente.getTelefone().replaceAll("[^0-9]", "").contains(termoBusca.replaceAll("[^0-9]", ""))) {
                    return true;
                }
                return false;
            });
        }
    }

    @FXML
    private void handleLimparFiltro() {
        txtBusca.clear();
        pacientesFiltrados.setPredicate(p -> true);
    }

    @FXML
    private void handleDeletar() {
        if (itemSelecionado == null) {
            mostrarErro("Erro", "Selecione um paciente para deletar.");
            return;
        }

        if (!mostrarConfirmacao("Confirmar", "Deseja realmente deletar o paciente " + 
                itemSelecionado.getNomeCompleto() + "?")) {
            return;
        }

        try {
            pacienteService.deletar(itemSelecionado.getId());
            mostrarSucesso("Sucesso", "Paciente deletado com sucesso!");
            limparFormulario();
            carregarDados();
            RefreshManager.notifyRefresh();
            resetarBotoes();
        } catch (Exception e) {
            logger.error("Erro ao deletar paciente", e);
            mostrarErro("Erro", "Não foi possível deletar o paciente: " + extrairMensagemErro(e));
        }
    }

    @FXML
    private void handleConfirmar() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Paciente paciente = construirPacienteDoFormulario();
            
            if ("CRIAR".equals(modoEdicao)) {
                pacienteService.criar(paciente);
                mostrarSucesso("Sucesso", "Paciente cadastrado com sucesso!");
            } else if ("ALTERAR".equals(modoEdicao)) {
                pacienteService.atualizar(itemSelecionado.getId(), paciente);
                mostrarSucesso("Sucesso", "Paciente atualizado com sucesso!");
            }

            limparFormulario();
            carregarDados();
            RefreshManager.notifyRefresh();
            resetarBotoes();
            habilitarCampos(false);
            modoEdicao = null;
        } catch (Exception e) {
            logger.error("Erro ao confirmar operação", e);
            mostrarErro("Erro", "Não foi possível completar a operação: " + extrairMensagemErro(e));
        }
    }

    @FXML
    private void handleCancelar() {
        limparFormulario();
        resetarBotoes();
        habilitarCampos(false);
        modoEdicao = null;
    }

    @Override
    protected void limparFormulario() {
        txtNome.clear();
        dtDataNascimento.setValue(null);
        txtCpf.clear();
        txtTelefone.clear();
        txtEmail.clear();
        cbConvenio.setValue(null);
        itemSelecionado = null;
        tabelaPacientes.getSelectionModel().clearSelection();
    }

    @Override
    protected void habilitarCampos(boolean habilitar) {
        txtNome.setDisable(!habilitar);
        dtDataNascimento.setDisable(!habilitar);
        txtCpf.setDisable(!habilitar);
        txtTelefone.setDisable(!habilitar);
        txtEmail.setDisable(!habilitar);
        cbConvenio.setDisable(!habilitar);
    }

    @Override
    protected boolean validarFormulario() {
        if (!ValidationUtils.validarCampoObrigatorio(txtNome.getText())) {
            mostrarErro("Validação", "O campo Nome é obrigatório.");
            return false;
        }
        if (dtDataNascimento.getValue() == null) {
            mostrarErro("Validação", "O campo Data de Nascimento é obrigatório.");
            return false;
        }
        if (!ValidationUtils.validarCampoObrigatorio(txtCpf.getText())) {
            mostrarErro("Validação", "O campo CPF é obrigatório.");
            return false;
        }
        if (!ValidationUtils.validarCPF(txtCpf.getText())) {
            mostrarErro("Validação", "CPF inválido.");
            return false;
        }
        // Verificar tamanho do CPF (deve ter 11 caracteres sem formatação)
        String cpfLimpo = txtCpf.getText().replaceAll("[^0-9]", "");
        if (cpfLimpo.length() != 11) {
            mostrarErro("Validação", "CPF deve ter exatamente 11 dígitos.");
            return false;
        }
        // Validações opcionais (se campos não estiverem vazios)
        if (ValidationUtils.validarCampoObrigatorio(txtEmail.getText()) && 
            !ValidationUtils.validarEmail(txtEmail.getText())) {
            mostrarErro("Validação", "E-mail inválido.");
            return false;
        }
        if (ValidationUtils.validarCampoObrigatorio(txtTelefone.getText()) && 
            !ValidationUtils.validarTelefone(txtTelefone.getText())) {
            mostrarErro("Validação", "Telefone inválido. Use o formato: (XX) XXXXX-XXXX");
            return false;
        }
        return true;
    }

    private Paciente construirPacienteDoFormulario() {
        Paciente paciente = new Paciente();
        paciente.setNomeCompleto(txtNome.getText().trim());
        paciente.setDataNascimento(dtDataNascimento.getValue());
        // Remove formatação do CPF antes de enviar
        paciente.setCpf(txtCpf.getText().replaceAll("[^0-9]", ""));
        // Só envia telefone e email se estiverem preenchidos
        String telefone = txtTelefone.getText().trim();
        paciente.setTelefone(telefone.isEmpty() ? null : telefone);
        String email = txtEmail.getText().trim();
        paciente.setEmail(email.isEmpty() ? null : email);
        paciente.setConvenio(cbConvenio.getValue());
        return paciente;
    }

    @FXML
    private void handleHistorico() {
        if (itemSelecionado == null) {
            mostrarErro("Erro", "Selecione um paciente para visualizar o histórico.");
            return;
        }

        try {
            HistoricoPaciente historico = pacienteService.consultarHistorico(itemSelecionado.getCpf());
            exibirHistorico(historico);
        } catch (Exception e) {
            logger.error("Erro ao consultar histórico", e);
            mostrarErro("Erro", "Não foi possível consultar o histórico: " + extrairMensagemErro(e));
        }
    }

    private void exibirHistorico(HistoricoPaciente historico) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Histórico do Paciente");
        alert.setHeaderText("Histórico Completo - " + historico.getDadosCadastrais().getNomeCompleto());
        
        StringBuilder conteudo = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        // Dados cadastrais
        conteudo.append("=== DADOS CADASTRAIS ===\n");
        conteudo.append("Nome: ").append(historico.getDadosCadastrais().getNomeCompleto()).append("\n");
        conteudo.append("CPF: ").append(historico.getDadosCadastrais().getCpf()).append("\n");
        conteudo.append("Email: ").append(historico.getDadosCadastrais().getEmail()).append("\n");
        conteudo.append("Telefone: ").append(historico.getDadosCadastrais().getTelefone()).append("\n\n");
        
        // Consultas
        conteudo.append("=== CONSULTAS ===\n");
        if (historico.getConsultas() != null && !historico.getConsultas().isEmpty()) {
            for (Consulta cons : historico.getConsultas()) {
                conteudo.append("• ");
                if (cons.getDataHoraInicio() != null) {
                    conteudo.append(cons.getDataHoraInicio().format(formatter));
                } else if (cons.getDataHoraAgendamento() != null) {
                    conteudo.append(cons.getDataHoraAgendamento().format(formatter));
                } else {
                    conteudo.append("Data não disponível");
                }
                conteudo.append(" - ").append(cons.getStatus() != null ? cons.getStatus() : "SEM STATUS");
                if (cons.getEspecialidade() != null && !cons.getEspecialidade().isEmpty()) {
                    conteudo.append(" (").append(cons.getEspecialidade()).append(")");
                }
                if (cons.getTipoConsulta() != null && !cons.getTipoConsulta().isEmpty()) {
                    conteudo.append(" - ").append(cons.getTipoConsulta());
                }
                conteudo.append("\n");
            }
        } else {
            conteudo.append("Nenhuma consulta encontrada.\n");
        }
        conteudo.append("\n");
        
        // Exames
        conteudo.append("=== EXAMES ===\n");
        if (historico.getExames() != null && !historico.getExames().isEmpty()) {
            for (Exame exame : historico.getExames()) {
                conteudo.append("• ");
                if (exame.getNomeExame() != null && !exame.getNomeExame().isEmpty()) {
                    conteudo.append(exame.getNomeExame()).append(" - ");
                }
                if (exame.getDataHoraInicio() != null) {
                    conteudo.append(exame.getDataHoraInicio().format(formatter));
                } else if (exame.getDataHoraAgendamento() != null) {
                    conteudo.append(exame.getDataHoraAgendamento().format(formatter));
                } else {
                    conteudo.append("Data não disponível");
                }
                conteudo.append(" (").append(exame.getStatus() != null ? exame.getStatus() : "SEM STATUS").append(")");
                if (exame.getRequerPreparo() != null && exame.getRequerPreparo()) {
                    conteudo.append(" [Requer preparo]");
                }
                conteudo.append("\n");
            }
        } else {
            conteudo.append("Nenhum exame encontrado.\n");
        }
        conteudo.append("\n");
        
        // Procedimentos
        conteudo.append("=== PROCEDIMENTOS ===\n");
        if (historico.getProcedimentos() != null && !historico.getProcedimentos().isEmpty()) {
            for (Procedimento proc : historico.getProcedimentos()) {
                conteudo.append("• ");
                if (proc.getDescricaoProcedimento() != null) {
                    conteudo.append(proc.getDescricaoProcedimento()).append(" - ");
                }
                if (proc.getDataHoraInicio() != null) {
                    conteudo.append(proc.getDataHoraInicio().format(formatter));
                } else if (proc.getDataHoraAgendamento() != null) {
                    conteudo.append(proc.getDataHoraAgendamento().format(formatter));
                } else {
                    conteudo.append("Data não disponível");
                }
                if (proc.getNivelRisco() != null) {
                    conteudo.append(" (Risco: ").append(proc.getNivelRisco()).append(")");
                }
                conteudo.append("\n");
            }
        } else {
            conteudo.append("Nenhum procedimento encontrado.\n");
        }
        
        TextArea textArea = new TextArea(conteudo.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefRowCount(20);
        textArea.setPrefColumnCount(60);
        
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }
}
