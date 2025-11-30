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
public class UsuarioController extends AbstractCrudController {

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

    @FXML
    private Button btnNovo;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnExcluir;

    private final UsuarioService service = new UsuarioService();
    private final ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    private Usuario usuarioSelecionado;

    @FXML
    public void initialize() {
        configurarTabela();
        configurarComboBox();
        carregarDados();
        configurarEventos();
        desabilitarCampos();
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
                    usuarioSelecionado = newSelection;
                    preencherFormulario(newSelection);
                    btnEditar.setDisable(false);
                    btnExcluir.setDisable(false);
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
    private void handleNovo() {
        limparFormulario();
        habilitarCampos();
        txtNomeCompleto.requestFocus();
        btnSalvar.setDisable(false);
        btnEditar.setDisable(true);
        btnExcluir.setDisable(true);
    }

    @FXML
    private void handleSalvar() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Usuario usuario = new Usuario();
            usuario.setNomeCompleto(txtNomeCompleto.getText());
            usuario.setLogin(txtLogin.getText());
            usuario.setSenha(txtSenha.getText());
            usuario.setTelefone(txtTelefone.getText());
            usuario.setEmail(txtEmail.getText());
            usuario.setPerfil(cbPerfil.getValue());

            Usuario criado = service.criar(usuario);
            usuarios.add(criado);
            
            mostrarSucesso("Sucesso", "Usuário criado com sucesso!");
            limparFormulario();
            desabilitarCampos();
            carregarDados();
        } catch (IOException | org.apache.hc.core5.http.ParseException e) {
            logger.error("Erro ao criar usuário", e);
            mostrarErro("Erro", "Não foi possível criar o usuário: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditar() {
        if (usuarioSelecionado == null) {
            mostrarErro("Atenção", "Selecione um usuário para editar.");
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        try {
            usuarioSelecionado.setNomeCompleto(txtNomeCompleto.getText());
            usuarioSelecionado.setLogin(txtLogin.getText());
            if (!txtSenha.getText().isEmpty()) {
                usuarioSelecionado.setSenha(txtSenha.getText());
            }
            usuarioSelecionado.setTelefone(txtTelefone.getText());
            usuarioSelecionado.setEmail(txtEmail.getText());
            usuarioSelecionado.setPerfil(cbPerfil.getValue());

            service.atualizar(usuarioSelecionado.getId(), usuarioSelecionado);
            
            mostrarSucesso("Sucesso", "Usuário atualizado com sucesso!");
            limparFormulario();
            desabilitarCampos();
            carregarDados();
        } catch (IOException | org.apache.hc.core5.http.ParseException e) {
            logger.error("Erro ao atualizar usuário", e);
            mostrarErro("Erro", "Não foi possível atualizar o usuário: " + e.getMessage());
        }
    }

    @FXML
    private void handleExcluir() {
        if (usuarioSelecionado == null) {
            mostrarErro("Atenção", "Selecione um usuário para excluir.");
            return;
        }

        if (mostrarConfirmacao("Confirmação", 
            "Deseja realmente excluir o usuário " + usuarioSelecionado.getNomeCompleto() + "?")) {
            try {
                service.deletar(usuarioSelecionado.getId());
                usuarios.remove(usuarioSelecionado);
                
                mostrarSucesso("Sucesso", "Usuário excluído com sucesso!");
                limparFormulario();
                desabilitarCampos();
            } catch (IOException | org.apache.hc.core5.http.ParseException e) {
                logger.error("Erro ao excluir usuário", e);
                mostrarErro("Erro", "Não foi possível excluir o usuário: " + e.getMessage());
            }
        }
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
        usuarioSelecionado = null;
        btnSalvar.setDisable(true);
        btnEditar.setDisable(true);
        btnExcluir.setDisable(true);
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

    private void habilitarCampos() {
        txtNomeCompleto.setDisable(false);
        txtLogin.setDisable(false);
        txtSenha.setDisable(false);
        txtTelefone.setDisable(false);
        txtEmail.setDisable(false);
        cbPerfil.setDisable(false);
    }

    private void desabilitarCampos() {
        txtNomeCompleto.setDisable(true);
        txtLogin.setDisable(true);
        txtSenha.setDisable(true);
        txtTelefone.setDisable(true);
        txtEmail.setDisable(true);
        cbPerfil.setDisable(true);
    }

    private boolean validarFormulario() {
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
        if (usuarioSelecionado == null && !ValidationUtils.validarCampoObrigatorio(txtSenha.getText())) {
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
}
