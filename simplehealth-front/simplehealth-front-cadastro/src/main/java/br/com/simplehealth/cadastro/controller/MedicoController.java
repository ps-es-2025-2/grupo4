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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller para o CRUD de Médicos.
 */
public class MedicoController extends AbstractCrudController<Medico> {

    private static final Logger logger = LoggerFactory.getLogger(MedicoController.class);

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
    
    // Botões - Precisam ser redeclarados para injeção do JavaFX
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

    private final MedicoService medicoService = new MedicoService();
    private final ObservableList<Medico> listaMedicos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Atribuir aos campos herdados do AbstractCrudController
        super.btnCriar = this.btnCriar;
        super.btnAlterar = this.btnAlterar;
        super.btnDeletar = this.btnDeletar;
        super.btnConfirmar = this.btnConfirmar;
        super.btnCancelar = this.btnCancelar;
        configurarTabela();
        carregarDados();
        configurarListeners();
        
        // Debug: Verificar se os botões foram injetados
        System.out.println("=== DEBUG MedicoController ===");
        System.out.println("btnCriar: " + (btnCriar != null));
        System.out.println("btnAlterar: " + (btnAlterar != null));
        System.out.println("btnDeletar: " + (btnDeletar != null));
        System.out.println("btnConfirmar: " + (btnConfirmar != null));
        System.out.println("btnCancelar: " + (btnCancelar != null));
        
        configurarCoresBotoes();
        configurarEstadoInicialBotoes();
        habilitarCampos(false);
        
        // Debug: Verificar estado dos botões após configuração
        if (btnCriar != null) System.out.println("btnCriar disabled: " + btnCriar.isDisabled());
        if (btnAlterar != null) System.out.println("btnAlterar disabled: " + btnAlterar.isDisabled());
        if (btnDeletar != null) System.out.println("btnDeletar disabled: " + btnDeletar.isDisabled());
        if (btnConfirmar != null) System.out.println("btnConfirmar disabled: " + btnConfirmar.isDisabled());
        if (btnCancelar != null) System.out.println("btnCancelar disabled: " + btnCancelar.isDisabled());
        System.out.println("==============================");
        
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
    private void handleCriar() {
        limparFormulario();
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "CRIAR";
    }

    @FXML
    private void handleAlterar() {
        if (itemSelecionado == null) {
            mostrarErro("Erro", "Selecione um médico para alterar.");
            return;
        }
        
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "ALTERAR";
    }

    @FXML
    private void handleDeletar() {
        if (itemSelecionado == null) {
            mostrarErro("Erro", "Selecione um médico para deletar.");
            return;
        }

        if (!mostrarConfirmacao("Confirmar", "Deseja realmente deletar o médico " + 
                itemSelecionado.getNomeCompleto() + "?")) {
            return;
        }

        try {
            medicoService.deletar(itemSelecionado.getId());
            mostrarSucesso("Sucesso", "Médico deletado com sucesso!");
            limparFormulario();
            carregarDados();
            RefreshManager.notifyRefresh();
            resetarBotoes();
        } catch (Exception e) {
            logger.error("Erro ao deletar médico", e);
            mostrarErro("Erro", "Não foi possível deletar o médico: " + e.getMessage());
        }
    }

    @FXML
    private void handleConfirmar() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Medico medico = construirMedicoDoFormulario();
            
            if ("CRIAR".equals(modoEdicao)) {
                medicoService.criar(medico);
                mostrarSucesso("Sucesso", "Médico cadastrado com sucesso!");
            } else if ("ALTERAR".equals(modoEdicao)) {
                medicoService.atualizar(itemSelecionado.getId(), medico);
                mostrarSucesso("Sucesso", "Médico atualizado com sucesso!");
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
        txtCrm.clear();
        txtEspecialidade.clear();
        txtTelefone.clear();
        txtEmail.clear();
        itemSelecionado = null;
        tabelaMedicos.getSelectionModel().clearSelection();
    }

    @Override
    protected void habilitarCampos(boolean habilitar) {
        txtNome.setDisable(!habilitar);
        txtCrm.setDisable(!habilitar);
        txtEspecialidade.setDisable(!habilitar);
        txtTelefone.setDisable(!habilitar);
        txtEmail.setDisable(!habilitar);
    }

    @Override
    protected boolean validarFormulario() {
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
        // Especialidade não é obrigatória no backend
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

    private Medico construirMedicoDoFormulario() {
        Medico medico = new Medico();
        medico.setNomeCompleto(txtNome.getText().trim());
        medico.setCrm(txtCrm.getText().trim());
        // Só envia campos opcionais se estiverem preenchidos
        String especialidade = txtEspecialidade.getText().trim();
        medico.setEspecialidade(especialidade.isEmpty() ? null : especialidade);
        String telefone = txtTelefone.getText().trim();
        medico.setTelefone(telefone.isEmpty() ? null : telefone);
        String email = txtEmail.getText().trim();
        medico.setEmail(email.isEmpty() ? null : email);
        return medico;
    }
}
