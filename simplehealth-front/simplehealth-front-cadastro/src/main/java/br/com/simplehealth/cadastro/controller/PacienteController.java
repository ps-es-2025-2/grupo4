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
public class PacienteController extends AbstractCrudController {

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
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnAtualizar;
    @FXML
    private Button btnDeletar;
    @FXML
    private Button btnLimpar;

    private final PacienteService pacienteService = new PacienteService();
    private final ConvenioService convenioService = new ConvenioService();
    private final ObservableList<Paciente> listaPacientes = FXCollections.observableArrayList();
    private FilteredList<Paciente> pacientesFiltrados;
    private Paciente pacienteSelecionado;

    @FXML
    public void initialize() {
        configurarTabela();
        carregarDados();
        configurarListeners();
        carregarConvenios();
        configurarBusca();
        
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
                    pacienteSelecionado = newValue;
                    preencherFormulario(newValue);
                    btnAtualizar.setDisable(false);
                    btnDeletar.setDisable(false);
                } else {
                    btnAtualizar.setDisable(true);
                    btnDeletar.setDisable(true);
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
    private void handleSalvar() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Paciente novoPaciente = new Paciente();
            novoPaciente.setNomeCompleto(txtNome.getText().trim());
            novoPaciente.setDataNascimento(dtDataNascimento.getValue());
            novoPaciente.setCpf(txtCpf.getText().trim());
            novoPaciente.setTelefone(txtTelefone.getText().trim());
            novoPaciente.setEmail(txtEmail.getText().trim());
            novoPaciente.setConvenio(cbConvenio.getValue());

            pacienteService.criar(novoPaciente);
            mostrarSucesso("Sucesso", "Paciente cadastrado com sucesso!");
            limparFormulario();
            carregarDados();
            RefreshManager.notifyRefresh();
        } catch (Exception e) {
            logger.error("Erro ao criar paciente", e);
            mostrarErro("Erro", "Não foi possível criar o paciente: " + e.getMessage());
        }
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
    private void handleAtualizar() {
        if (pacienteSelecionado == null) {
            mostrarErro("Erro", "Selecione um paciente para atualizar.");
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        try {
            Paciente pacienteAtualizado = new Paciente();
            pacienteAtualizado.setNomeCompleto(txtNome.getText().trim());
            pacienteAtualizado.setDataNascimento(dtDataNascimento.getValue());
            pacienteAtualizado.setCpf(txtCpf.getText().trim());
            pacienteAtualizado.setTelefone(txtTelefone.getText().trim());
            pacienteAtualizado.setEmail(txtEmail.getText().trim());
            pacienteAtualizado.setConvenio(cbConvenio.getValue());

            pacienteService.atualizar(pacienteSelecionado.getId(), pacienteAtualizado);
            mostrarSucesso("Sucesso", "Paciente atualizado com sucesso!");
            limparFormulario();
            carregarDados();
            RefreshManager.notifyRefresh();
        } catch (Exception e) {
            logger.error("Erro ao atualizar paciente", e);
            mostrarErro("Erro", "Não foi possível atualizar o paciente: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeletar() {
        if (pacienteSelecionado == null) {
            mostrarErro("Erro", "Selecione um paciente para deletar.");
            return;
        }

        if (!mostrarConfirmacao("Confirmar", "Deseja realmente deletar o paciente " + 
                pacienteSelecionado.getNomeCompleto() + "?")) {
            return;
        }

        try {
            pacienteService.deletar(pacienteSelecionado.getId());
            mostrarSucesso("Sucesso", "Paciente deletado com sucesso!");
            limparFormulario();
            carregarDados();
            RefreshManager.notifyRefresh();
        } catch (Exception e) {
            logger.error("Erro ao deletar paciente", e);
            mostrarErro("Erro", "Não foi possível deletar o paciente: " + e.getMessage());
        }
    }

    @FXML
    private void handleLimpar() {
        limparFormulario();
    }

    @Override
    protected void limparFormulario() {
        txtNome.clear();
        dtDataNascimento.setValue(null);
        txtCpf.clear();
        txtTelefone.clear();
        txtEmail.clear();
        cbConvenio.setValue(null);
        pacienteSelecionado = null;
        tabelaPacientes.getSelectionModel().clearSelection();
        btnAtualizar.setDisable(true);
        btnDeletar.setDisable(true);
    }

    private boolean validarFormulario() {
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
}
