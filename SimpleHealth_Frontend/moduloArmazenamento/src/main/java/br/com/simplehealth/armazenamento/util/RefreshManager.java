package br.com.simplehealth.armazenamento.util;

import br.com.simplehealth.armazenamento.controller.AbstractCrudController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária para gerenciar refresh global da aplicação.
 * Fornece métodos estáticos para forçar atualizações em todos os controladores.
 * 
 * @version 1.0
 */
public class RefreshManager {

    private static final Logger logger = LoggerFactory.getLogger(RefreshManager.class);
    
    private static final List<AbstractCrudController<?, ?, ?>> controladores = new ArrayList<>();

    /**
     * Registra um controlador para receber notificações de refresh.
     * 
     * @param controlador O controlador a ser registrado.
     */
    public static void registrarControlador(AbstractCrudController<?, ?, ?> controlador) {
        if (!controladores.contains(controlador)) {
            controladores.add(controlador);
            logger.debug("Controlador registrado no RefreshManager: {}", controlador.getClass().getSimpleName());
        }
    }

    /**
     * Remove um controlador da lista de notificações.
     * 
     * @param controlador O controlador a ser removido.
     */
    public static void removerControlador(AbstractCrudController<?, ?, ?> controlador) {
        controladores.remove(controlador);
        logger.debug("Controlador removido do RefreshManager: {}", controlador.getClass().getSimpleName());
    }

    /**
     * Força um refresh global em todos os controladores registrados.
     * Este método pode ser chamado manualmente quando necessário.
     */
    public static void forcarRefreshGlobal() {
        logger.info("Forçando refresh global em {} controladores", controladores.size());
        
        for (AbstractCrudController<?, ?, ?> controlador : controladores) {
            try {
                // Recarregar dados das tabelas
                controlador.carregarDados();
                // Atualizar selects e ComboBoxes
                controlador.atualizarSelectsEComboBoxes();
            } catch (Exception e) {
                logger.error("Erro ao fazer refresh do controlador {}: {}", 
                    controlador.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    /**
     * Força refresh apenas dos selects e ComboBoxes em todos os controladores.
     */
    public static void forcarRefreshSelects() {
        logger.info("Forçando refresh dos selects em {} controladores", controladores.size());
        
        for (AbstractCrudController<?, ?, ?> controlador : controladores) {
            try {
                controlador.atualizarSelectsEComboBoxes();
            } catch (Exception e) {
                logger.error("Erro ao fazer refresh dos selects do controlador {}: {}", 
                    controlador.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    /**
     * Retorna o número de controladores registrados.
     * 
     * @return O número de controladores ativos.
     */
    public static int getNumeroControladores() {
        return controladores.size();
    }

    /**
     * Limpa todos os controladores registrados.
     * Útil para reinicialização da aplicação.
     */
    public static void limparControladores() {
        logger.info("Limpando todos os controladores registrados");
        controladores.clear();
    }
}