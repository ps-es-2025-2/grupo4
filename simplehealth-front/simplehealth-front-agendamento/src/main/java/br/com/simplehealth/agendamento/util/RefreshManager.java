package br.com.simplehealth.agendamento.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerenciador de refresh para sincronizar dados entre diferentes abas/telas.
 * Implementa o padrão Observer para notificar listeners quando dados são modificados.
 */
public class RefreshManager {

    private static RefreshManager instance;
    private List<RefreshListener> listeners = new ArrayList<>();

    private RefreshManager() {
    }

    public static synchronized RefreshManager getInstance() {
        if (instance == null) {
            instance = new RefreshManager();
        }
        return instance;
    }

    public void addListener(RefreshListener listener) {
        listeners.add(listener);
    }

    public void removeListener(RefreshListener listener) {
        listeners.remove(listener);
    }

    public void notifyRefresh() {
        for (RefreshListener listener : listeners) {
            listener.onRefresh();
        }
    }

    public interface RefreshListener {
        void onRefresh();
    }
}
