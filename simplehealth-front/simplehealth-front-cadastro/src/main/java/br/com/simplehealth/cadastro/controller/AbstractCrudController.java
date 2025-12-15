package br.com.simplehealth.cadastro.controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Controller abstrato com métodos comuns para os CRUDs.
 * Implementa a nova lógica de botões: Criar, Alterar, Deletar + Confirmar, Cancelar
 */
public abstract class AbstractCrudController<T> {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractCrudController.class);
    
    // Controle de modo de edição
    protected String modoEdicao = null; // "CRIAR", "ALTERAR" ou null
    protected T itemSelecionado = null;
    
    // Botões - devem ser injetados via @FXML nas subclasses
    protected Button btnCriar;
    protected Button btnAlterar;
    protected Button btnDeletar;
    protected Button btnConfirmar;
    protected Button btnCancelar;
    
    /**
     * Configura as cores dos botões seguindo o padrão:
     * - Criar: Azul
     * - Alterar/Atualizar: Verde
     * - Deletar: Vermelho
     * - Confirmar: Laranja
     * - Cancelar: Cinza
     */
    protected void configurarCoresBotoes() {
        if (btnCriar != null) {
            btnCriar.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        }
        if (btnAlterar != null) {
            btnAlterar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        }
        if (btnDeletar != null) {
            btnDeletar.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold;");
        }
        if (btnConfirmar != null) {
            btnConfirmar.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
        }
        if (btnCancelar != null) {
            btnCancelar.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-font-weight: bold;");
        }
    }

    /**
     * Extrai mensagem de erro limpa (sem stack trace completo)
     */
    protected String extrairMensagemErro(Exception e) {
        String msg = e.getMessage();
        if (msg == null || msg.trim().isEmpty()) {
            return "Erro: " + e.getClass().getSimpleName().replace("Exception", "");
        }
        
        // Se a mensagem tem múltiplas linhas (stack trace), pega só a primeira
        int index = msg.indexOf('\n');
        if (index > 0) {
            msg = msg.substring(0, index);
        }
        
        // Remove espaços em branco extras
        msg = msg.trim();
        
        // Limita tamanho máximo
        if (msg.length() > 300) {
            msg = msg.substring(0, 300) + "...";
        }
        
        return msg;
    }
    
    /**
     * Exibe uma mensagem de erro.
     */
    protected void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Exibe uma mensagem de sucesso.
     */
    protected void mostrarSucesso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Exibe uma mensagem de confirmação.
     */
    protected boolean mostrarConfirmacao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Valida se um campo não está vazio.
     */
    protected boolean validarCampoNaoVazio(String valor, String nomeCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            mostrarErro("Validação", "O campo " + nomeCampo + " é obrigatório.");
            return false;
        }
        return true;
    }
    
    /**
     * Reseta o estado dos botões para o padrão.
     * Habilita Criar, Alterar e Deletar. Desabilita Confirmar e Cancelar.
     */
    protected void resetarBotoes() {
        configurarCoresBotoes();
        if (btnCriar != null) btnCriar.setDisable(false);
        if (btnAlterar != null) btnAlterar.setDisable(false);
        if (btnDeletar != null) btnDeletar.setDisable(false);
        if (btnConfirmar != null) btnConfirmar.setDisable(true);
        if (btnCancelar != null) btnCancelar.setDisable(true);
    }
    
    /**
     * Ativa o modo de edição (criar/alterar/deletar).
     * Desabilita Criar, Alterar e Deletar. Habilita Confirmar e Cancelar.
     */
    protected void ativarModoEdicao() {
        configurarCoresBotoes();
        if (btnCriar != null) btnCriar.setDisable(true);
        if (btnAlterar != null) btnAlterar.setDisable(true);
        if (btnDeletar != null) btnDeletar.setDisable(true);
        if (btnConfirmar != null) btnConfirmar.setDisable(false);
        if (btnCancelar != null) btnCancelar.setDisable(false);
    }
    
    /**
     * Configura o estado inicial dos botões.
     * Habilita apenas Criar, desabilita os demais.
     */
    protected void configurarEstadoInicialBotoes() {
        Platform.runLater(() -> {
            System.out.println(">>> configurarEstadoInicialBotoes - runLater");
            configurarCoresBotoes();
            if (btnCriar != null) btnCriar.setDisable(false);
            if (btnAlterar != null) btnAlterar.setDisable(true);
            if (btnDeletar != null) btnDeletar.setDisable(true);
            if (btnConfirmar != null) btnConfirmar.setDisable(true);
            if (btnCancelar != null) btnCancelar.setDisable(true);
            
            System.out.println(">>> DEPOIS setDisable:");
            if (btnConfirmar != null) System.out.println("    btnConfirmar.isDisabled() = " + btnConfirmar.isDisabled());
            if (btnCancelar != null) System.out.println("    btnCancelar.isDisabled() = " + btnCancelar.isDisabled());
        });
    }
    
    /**
     * Habilita botões Alterar e Deletar quando um item é selecionado.
     * Só deve ser chamado se não estiver em modo de edição.
     */
    protected void habilitarBotoesSelecao() {
        if (modoEdicao == null) {
            if (btnAlterar != null) btnAlterar.setDisable(false);
            if (btnDeletar != null) btnDeletar.setDisable(false);
        }
    }

    /**
     * Método abstrato para carregar dados.
     */
    protected abstract void carregarDados();

    /**
     * Método abstrato para limpar o formulário.
     */
    protected abstract void limparFormulario();
    
    /**
     * Método abstrato para habilitar/desabilitar campos de entrada.
     */
    protected abstract void habilitarCampos(boolean habilitar);
    
    /**
     * Método abstrato para validar o formulário.
     */
    protected abstract boolean validarFormulario();
}
