package br.com.simplehealth.cadastro.util;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerenciador de refresh para atualizar views automaticamente.
 */
public class RefreshManager {

    private static final List<Runnable> refreshListeners = new ArrayList<>();

    /**
     * Adiciona um listener de refresh.
     */
    public static void addRefreshListener(Runnable listener) {
        refreshListeners.add(listener);
    }

    /**
     * Remove um listener de refresh.
     */
    public static void removeRefreshListener(Runnable listener) {
        refreshListeners.remove(listener);
    }

    /**
     * Notifica todos os listeners para atualizar.
     */
    public static void notifyRefresh() {
        Platform.runLater(() -> {
            for (Runnable listener : refreshListeners) {
                listener.run();
            }
        });
    }
}
