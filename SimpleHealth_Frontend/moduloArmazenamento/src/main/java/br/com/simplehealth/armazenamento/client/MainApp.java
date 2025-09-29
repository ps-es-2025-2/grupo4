package br.com.simplehealth.armazenamento.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Classe principal da aplicação JavaFX para o módulo de armazenamento.
 * 
 * @version 1.0
 */
public class MainApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            // Criar TabPane principal
            TabPane tabPane = new TabPane();
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            
            // Aba de Estoque
            Tab estoqueTab = criarAba("Estoque", "/view/estoque.fxml");
            tabPane.getTabs().add(estoqueTab);
            
            // Aba de Itens
            Tab itemTab = criarAba("Itens", "/view/item.fxml");
            tabPane.getTabs().add(itemTab);
            
            // Aba de Pedidos
            Tab pedidoTab = criarAba("Pedidos", "/view/pedido.fxml");
            tabPane.getTabs().add(pedidoTab);
            
            // Aba de Fornecedores
            Tab fornecedorTab = criarAba("Fornecedores", "/view/fornecedor.fxml");
            tabPane.getTabs().add(fornecedorTab);
            
            // Configurar cena
            Scene scene = new Scene(tabPane, 1400, 900);
            
            // Configurar palco principal
            primaryStage.setTitle("SimpleHealth - Módulo de Armazenamento");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
            
            logger.info("Aplicação iniciada com sucesso");
            
        } catch (Exception e) {
            logger.error("Erro ao iniciar aplicação: ", e);
            mostrarErro("Erro de Inicialização", 
                       "Não foi possível iniciar a aplicação: " + e.getMessage());
        }
    }

    private Tab criarAba(String titulo, String fxmlPath) throws IOException {
        Tab tab = new Tab(titulo);
        tab.setClosable(false);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        tab.setContent(loader.load());
        
        return tab;
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        logger.info("Aplicação finalizada");
    }

    public static void main(String[] args) {
        launch(args);
    }
}