package br.com.simplehealth.agendamento.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Controller abstrato com métodos comuns para os CRUDs de agendamento.
 */
public abstract class AbstractCrudController {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractCrudController.class);

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
     * Método abstrato para carregar dados.
     */
    protected abstract void carregarDados();

    /**
     * Método abstrato para limpar o formulário.
     */
    protected abstract void limparFormulario();
}
