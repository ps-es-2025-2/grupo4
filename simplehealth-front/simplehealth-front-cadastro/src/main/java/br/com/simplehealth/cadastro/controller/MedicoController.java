package br.com.simplehealth.cadastro.controller;

import br.com.simplehealth.cadastro.model.Medico;
import br.com.simplehealth.cadastro.service.MedicoService;
import br.com.simplehealth.cadastro.util.RefreshManager;
import br.com.simplehealth.cadastro.util.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller para o CRUD de Médicos.
 */
public class MedicoController extends AbstractCrudController {

    @FXML
    private TableView<Medico> tabelaMedicos;
    @FXML
    private TableColumn<Medico, Long> colunaId;
    @FXML
    private TableColumn<Medico, String> colunaNome;
    @FXML
    private TableColumn<Medico, String> colunaCrm;
    @FXML
    private TableColumn<Medico, String> colunaEspecialidade;
    @FXML
    private TableColumn<Medico, String> colunaTelefone;
    @FXML
    private TableColumn<Medico, String> colunaEmail;

    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtCrm;
    @FXML
    private TextField txtEspecialidade;
    @FXML
    private TextField txtTelefone;
    @FXML
    private TextField txtEmail;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnAtualizar;
    @FXML
    private Button btnDeletar;
    @FXML
    private Button btnLimpar;

    private final MedicoService medicoService = new MedicoService();
    private final ObservableList<Medico> listaMedicos = FXCollections.observableArrayList();
    private Medico medicoSelecionado;

    @FXML
    public void initialize() {
        configurarTabela();
        carregarDados();
        configurarListeners();
        
        RefreshManager.addRefreshListener(this::carregarDados);
    }

    private void configurarTabela() {
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nomeCompleto"));
        colunaCrm.setCellValueFactory(new PropertyValueFactory<>("crm"));
        colunaEspecialidade.setCellValueFactory(new PropertyValueFactory<>("especialidade"));
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        tabelaMedicos.setItems(listaMedicos);
    }

    private void configurarListeners() {
        tabelaMedicos.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    medicoSelecionado = newValue;
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
            listaMedicos.clear();
            listaMedicos.addAll(medicoService.listarTodos());
        } catch (Exception e) {
            logger.error("Erro ao carregar médicos", e);
            mostrarErro("Erro", "Não foi possível carregar os médicos: " + e.getMessage());
        }
    }

    private void preencherFormulario(Medico medico) {
        txtNome.setText(medico.getNomeCompleto());
        txtCrm.setText(medico.getCrm());
        txtEspecialidade.setText(medico.getEspecialidade());
        txtTelefone.setText(medico.getTelefone());
        txtEmail.setText(medico.getEmail());
    }

    @FXML
    private void handleSalvar() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Medico novoMedico = new Medico();
            novoMedico.setNomeCompleto(txtNome.getText().trim());
            novoMedico.setCrm(txtCrm.getText().trim());
            novoMedico.setEspecialidade(txtEspecialidade.getText().trim());
            novoMedico.setTelefone(txtTelefone.getText().trim());
            novoMedico.setEmail(txtEmail.getText().trim());

            medicoService.criar(novoMedico);
            mostrarSucesso("Sucesso", "Médico cadastrado com sucesso!");
            limparFormulario();
            carregarDados();
            RefreshManager.notifyRefresh();
        } catch (Exception e) {
            logger.error("Erro ao criar médico", e);
            mostrarErro("Erro", "Não foi possível criar o médico: " + e.getMessage());
        }
    }

    @FXML
    private void handleAtualizar() {
        if (medicoSelecionado == null) {
            mostrarErro("Erro", "Selecione um médico para atualizar.");
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        try {
            Medico medicoAtualizado = new Medico();
            medicoAtualizado.setNomeCompleto(txtNome.getText().trim());
            medicoAtualizado.setCrm(txtCrm.getText().trim());
            medicoAtualizado.setEspecialidade(txtEspecialidade.getText().trim());
            medicoAtualizado.setTelefone(txtTelefone.getText().trim());
            medicoAtualizado.setEmail(txtEmail.getText().trim());

            medicoService.atualizar(medicoSelecionado.getId(), medicoAtualizado);
            mostrarSucesso("Sucesso", "Médico atualizado com sucesso!");
            limparFormulario();
            carregarDados();
            RefreshManager.notifyRefresh();
        } catch (Exception e) {
            logger.error("Erro ao atualizar médico", e);
            mostrarErro("Erro", "Não foi possível atualizar o médico: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeletar() {
        if (medicoSelecionado == null) {
            mostrarErro("Erro", "Selecione um médico para deletar.");
            return;
        }

        if (!mostrarConfirmacao("Confirmar", "Deseja realmente deletar o médico " + 
                medicoSelecionado.getNomeCompleto() + "?")) {
            return;
        }

        try {
            medicoService.deletar(medicoSelecionado.getId());
            mostrarSucesso("Sucesso", "Médico deletado com sucesso!");
            limparFormulario();
            carregarDados();
            RefreshManager.notifyRefresh();
        } catch (Exception e) {
            logger.error("Erro ao deletar médico", e);
            mostrarErro("Erro", "Não foi possível deletar o médico: " + e.getMessage());
        }
    }

    @FXML
    private void handleLimpar() {
        limparFormulario();
    }

    @Override
    protected void limparFormulario() {
        txtNome.clear();
        txtCrm.clear();
        txtEspecialidade.clear();
        txtTelefone.clear();
        txtEmail.clear();
        medicoSelecionado = null;
        tabelaMedicos.getSelectionModel().clearSelection();
        btnAtualizar.setDisable(true);
        btnDeletar.setDisable(true);
    }

    private boolean validarFormulario() {
        if (!ValidationUtils.validarCampoObrigatorio(txtNome.getText())) {
            mostrarErro("Validação", "O campo Nome é obrigatório.");
            return false;
        }
        if (!ValidationUtils.validarCampoObrigatorio(txtCrm.getText())) {
            mostrarErro("Validação", "O campo CRM é obrigatório.");
            return false;
        }
        if (!ValidationUtils.validarCRM(txtCrm.getText())) {
            mostrarErro("Validação", "CRM inválido. Deve conter de 4 a 7 dígitos.");
            return false;
        }
        if (!ValidationUtils.validarCampoObrigatorio(txtEspecialidade.getText())) {
            mostrarErro("Validação", "O campo Especialidade é obrigatório.");
            return false;
        }
        // Validações opcionais
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
