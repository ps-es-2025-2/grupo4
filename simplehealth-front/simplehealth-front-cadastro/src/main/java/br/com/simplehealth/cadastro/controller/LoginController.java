package br.com.simplehealth.cadastro.controller;

import br.com.simplehealth.cadastro.client.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller para tela de Login (UC01).
 * Responsável pela autenticação de usuários no sistema.
 */
public class LoginController extends AbstractCrudController {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    @FXML
    private TextField txtLogin;
    
    @FXML
    private PasswordField txtSenha;
    
    /**
     * Inicializa o controller.
     * Configura listeners e comportamentos iniciais.
     */
    @FXML
    public void initialize() {
        // Adicionar listener para Enter no campo de senha
        if (txtSenha != null) {
            txtSenha.setOnAction(event -> handleLogin());
        }
        
        logger.info("Tela de login inicializada");
    }
    
    /**
     * Manipula o evento de clique no botão "Entrar".
     * Realiza a validação dos campos e autenticação do usuário.
     */
    @FXML
    private void handleLogin() {
        String login = txtLogin.getText();
        String senha = txtSenha.getText();
        
        // Validar campos obrigatórios
        if (!validarCampoNaoVazio(login, "Login")) {
            return;
        }
        
        if (!validarCampoNaoVazio(senha, "Senha")) {
            return;
        }
        
        logger.info("Tentativa de login para usuário: {}", login);
        
        try {
            // TODO: Implementar autenticação real via API backend
            // Por ora, aceitar qualquer login/senha para desenvolvimento
            // Em produção, deve chamar UsuarioService.autenticar(login, senha)
            
            logger.info("Login realizado com sucesso: {}", login);
            mostrarSucesso("Login", "Bem-vindo ao SimpleHealth!");
            
            // Fechar janela de login e abrir tela principal
            txtLogin.getScene().getWindow().hide();
            
            // Abrir tela principal (implementar no MainApp)
            // MainApp.getInstance().mostrarTelaPrincipal();
            
        } catch (Exception e) {
            logger.error("Erro ao realizar login", e);
            mostrarErro("Erro de Autenticação", 
                "Não foi possível realizar o login. Verifique suas credenciais.");
        }
    }
    
    /**
     * Manipula o evento de clique no link "Esqueci minha senha".
     * Exibe informação sobre recuperação de senha.
     */
    @FXML
    private void handleEsqueciSenha() {
        logger.info("Solicitação de recuperação de senha");
        mostrarInformacao("Recuperação de Senha", 
            "Funcionalidade de recuperação de senha será implementada em versão futura.\n" +
            "Entre em contato com o administrador do sistema.");
    }
    
    /**
     * Método auxiliar para exibir mensagens informativas.
     */
    private void mostrarInformacao(String titulo, String mensagem) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    @Override
    protected void carregarDados() {
        // Não aplicável para tela de login
    }
    
    @Override
    protected void limparFormulario() {
        txtLogin.clear();
        txtSenha.clear();
        txtLogin.requestFocus();
    }
}
