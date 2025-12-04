package br.com.simplehealth.cadastro.controller;

import br.com.simplehealth.cadastro.model.Convenio;
import br.com.simplehealth.cadastro.model.Paciente;
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
            mostrarErro("Erro", "Não foi possível carregar os pacientes: " + e.getMessage());
        }
    }

    private void preencherFormulario(Paciente paciente) {
        txtNome.setText(paciente.getNomeCompleto());
        dtDataNascimento.setValue(paciente.getDataNascimento());
        txtCpf.setText(paciente.getCpf());
        txtTelefone.setText(paciente.getTelefone());
        txtEmail.setText(paciente.getEmail());
        cbConvenio.setValue(paciente.getConvenio());
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
            mostrarErro("Erro", "Não foi possível deletar o paciente: " + e.getMessage());
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
            mostrarErro("Erro", "Não foi possível completar a operação: " + e.getMessage());
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
        paciente.setCpf(txtCpf.getText().trim());
        paciente.setTelefone(txtTelefone.getText().trim());
        paciente.setEmail(txtEmail.getText().trim());
        paciente.setConvenio(cbConvenio.getValue());
        return paciente;
    }
}
