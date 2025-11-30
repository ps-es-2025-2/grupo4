package br.com.simplehealth.estoque.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerenciador de atualizações entre diferentes abas
 * Implementa padrão Observer para notificar mudanças
 */
public class RefreshManager {
    
    private static RefreshManager instance;
    private List<RefreshListener> listeners;
    
    private RefreshManager() {
        this.listeners = new ArrayList<>();
    }
    
    public static synchronized RefreshManager getInstance() {
        if (instance == null) {
            instance = new RefreshManager();
        }
        return instance;
    }
    
    public void addListener(RefreshListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public void removeListener(RefreshListener listener) {
        listeners.remove(listener);
    }
    
    public void notifyRefresh(String source) {
        for (RefreshListener listener : listeners) {
            listener.onRefresh(source);
        }
    }
    
    @FunctionalInterface
    public interface RefreshListener {
        void onRefresh(String source);
    }
}
