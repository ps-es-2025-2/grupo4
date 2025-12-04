package br.com.simplehealth.cadastro.controller;

import br.com.simplehealth.cadastro.model.Usuario;
import br.com.simplehealth.cadastro.service.UsuarioService;
import br.com.simplehealth.cadastro.util.RefreshManager;
import br.com.simplehealth.cadastro.util.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Controller para gerenciamento de Usuários.
 */
public class UsuarioController extends AbstractCrudController<Usuario> {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @FXML
    private TableView<Usuario> tableUsuarios;
    @FXML
    private TableColumn<Usuario, Long> colId;
    @FXML
    private TableColumn<Usuario, String> colNome;
    @FXML
    private TableColumn<Usuario, String> colLogin;
    @FXML
    private TableColumn<Usuario, String> colTelefone;
    @FXML
    private TableColumn<Usuario, String> colEmail;
    @FXML
    private TableColumn<Usuario, String> colPerfil;

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNomeCompleto;
    @FXML
    private TextField txtLogin;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private TextField txtTelefone;
    @FXML
    private TextField txtEmail;
    @FXML
    private ComboBox<String> cbPerfil;

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

    private final UsuarioService service = new UsuarioService();
    private final ObservableList<Usuario> usuarios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        super.btnCriar = this.btnCriar;
        super.btnAlterar = this.btnAlterar;
        super.btnDeletar = this.btnDeletar;
        super.btnConfirmar = this.btnConfirmar;
        super.btnCancelar = this.btnCancelar;

        configurarTabela();
        configurarComboBox();
        carregarDados();
        configurarEventos();
        configurarEstadoInicialBotoes();
        habilitarCampos(false);
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nomeCompleto"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPerfil.setCellValueFactory(new PropertyValueFactory<>("perfil"));
        
        tableUsuarios.setItems(usuarios);
    }

    private void configurarComboBox() {
        cbPerfil.setItems(FXCollections.observableArrayList(
            "MEDICO", "SECRETARIA", "GESTOR", "FINANCEIRO", "TESOURARIA"
        ));
    }

    private void configurarEventos() {
        tableUsuarios.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    itemSelecionado = newSelection;
                    preencherFormulario(newSelection);
                    habilitarBotoesSelecao();
                }
            }
        );
    }

    @Override
    protected void carregarDados() {
        try {
            usuarios.clear();
            usuarios.addAll(service.buscarTodos());
            logger.info("Usuários carregados: {}", usuarios.size());
        } catch (IOException | org.apache.hc.core5.http.ParseException e) {
            logger.error("Erro ao carregar usuários", e);
            mostrarErro("Erro", "Não foi possível carregar os usuários: " + e.getMessage());
        }
    }

    @FXML
    private void handleCriar() {
        limparFormulario();
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "CRIAR";
        txtNomeCompleto.requestFocus();
    }

    @FXML
    private void handleAlterar() {
        if (itemSelecionado == null) {
            mostrarErro("Atenção", "Selecione um usuário para alterar.");
            return;
        }
        
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "ALTERAR";
    }

    @FXML
    private void handleDeletar() {
        if (itemSelecionado == null) {
            mostrarErro("Atenção", "Selecione um usuário para deletar.");
            return;
        }

        if (!mostrarConfirmacao("Confirmação", 
            "Deseja realmente deletar o usuário " + itemSelecionado.getNomeCompleto() + "?")) {
            return;
        }

        try {
            service.deletar(itemSelecionado.getId());
            usuarios.remove(itemSelecionado);
            
            mostrarSucesso("Sucesso", "Usuário deletado com sucesso!");
            limparFormulario();
            resetarBotoes();
            habilitarCampos(false);
        } catch (IOException | org.apache.hc.core5.http.ParseException e) {
            logger.error("Erro ao deletar usuário", e);
            mostrarErro("Erro", "Não foi possível deletar o usuário: " + e.getMessage());
        }
    }

    @FXML
    private void handleConfirmar() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Usuario usuario = construirUsuarioDoFormulario();
            
            if ("CRIAR".equals(modoEdicao)) {
                Usuario criado = service.criar(usuario);
                usuarios.add(criado);
                mostrarSucesso("Sucesso", "Usuário criado com sucesso!");
            } else if ("ALTERAR".equals(modoEdicao)) {
                itemSelecionado.setNomeCompleto(usuario.getNomeCompleto());
                itemSelecionado.setLogin(usuario.getLogin());
                if (!txtSenha.getText().isEmpty()) {
                    itemSelecionado.setSenha(usuario.getSenha());
                }
                itemSelecionado.setTelefone(usuario.getTelefone());
                itemSelecionado.setEmail(usuario.getEmail());
                itemSelecionado.setPerfil(usuario.getPerfil());
                
                service.atualizar(itemSelecionado.getId(), itemSelecionado);
                mostrarSucesso("Sucesso", "Usuário atualizado com sucesso!");
            }

            limparFormulario();
            habilitarCampos(false);
            carregarDados();
            resetarBotoes();
            modoEdicao = null;
        } catch (IOException | org.apache.hc.core5.http.ParseException e) {
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
        txtId.clear();
        txtNomeCompleto.clear();
        txtLogin.clear();
        txtSenha.clear();
        txtTelefone.clear();
        txtEmail.clear();
        cbPerfil.setValue(null);
        tableUsuarios.getSelectionModel().clearSelection();
        itemSelecionado = null;
    }

    private void preencherFormulario(Usuario usuario) {
        txtId.setText(usuario.getId() != null ? usuario.getId().toString() : "");
        txtNomeCompleto.setText(usuario.getNomeCompleto());
        txtLogin.setText(usuario.getLogin());
        txtSenha.clear(); // Não mostra senha por segurança
        txtTelefone.setText(usuario.getTelefone());
        txtEmail.setText(usuario.getEmail());
        cbPerfil.setValue(usuario.getPerfil());
    }

    @Override
    protected void habilitarCampos(boolean habilitar) {
        txtNomeCompleto.setDisable(!habilitar);
        txtLogin.setDisable(!habilitar);
        txtSenha.setDisable(!habilitar);
        txtTelefone.setDisable(!habilitar);
        txtEmail.setDisable(!habilitar);
        cbPerfil.setDisable(!habilitar);
    }

    @Override
    protected boolean validarFormulario() {
        if (!ValidationUtils.validarCampoObrigatorio(txtNomeCompleto.getText())) {
            mostrarErro("Validação", "O campo Nome Completo é obrigatório.");
            return false;
        }
        if (!ValidationUtils.validarCampoObrigatorio(txtLogin.getText())) {
            mostrarErro("Validação", "O campo Login é obrigatório.");
            return false;
        }
        // Validar tamanho mínimo do login
        if (txtLogin.getText().trim().length() < 3) {
            mostrarErro("Validação", "O Login deve ter pelo menos 3 caracteres.");
            return false;
        }
        // Senha obrigatória apenas na criação
        if ("CRIAR".equals(modoEdicao) && !ValidationUtils.validarCampoObrigatorio(txtSenha.getText())) {
            mostrarErro("Validação", "O campo Senha é obrigatório.");
            return false;
        }
        // Validar tamanho mínimo da senha
        if (ValidationUtils.validarCampoObrigatorio(txtSenha.getText()) && txtSenha.getText().length() < 6) {
            mostrarErro("Validação", "A Senha deve ter pelo menos 6 caracteres.");
            return false;
        }
        if (cbPerfil.getValue() == null) {
            mostrarErro("Validação", "Selecione um perfil.");
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

    private Usuario construirUsuarioDoFormulario() {
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(txtNomeCompleto.getText());
        usuario.setLogin(txtLogin.getText());
        usuario.setSenha(txtSenha.getText());
        usuario.setTelefone(txtTelefone.getText());
        usuario.setEmail(txtEmail.getText());
        usuario.setPerfil(cbPerfil.getValue());
        return usuario;
    }
}
