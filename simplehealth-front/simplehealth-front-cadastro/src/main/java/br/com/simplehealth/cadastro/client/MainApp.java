package br.com.simplehealth.cadastro.client;

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
 * Classe principal da aplicação JavaFX para o módulo de cadastro.
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
            
            // Aba de Pacientes
            Tab pacienteTab = criarAba("Pacientes", "/view/paciente.fxml");
            tabPane.getTabs().add(pacienteTab);
            
            // Aba de Médicos
            Tab medicoTab = criarAba("Médicos", "/view/medico.fxml");
            tabPane.getTabs().add(medicoTab);
            
            // Aba de Convênios
            Tab convenioTab = criarAba("Convênios", "/view/convenio.fxml");
            tabPane.getTabs().add(convenioTab);
            
            // Configurar cena
            Scene scene = new Scene(tabPane, 1400, 900);
            
            // Configurar palco principal
            primaryStage.setTitle("SimpleHealth - Módulo de Cadastro");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
            
            logger.info("Aplicação de Cadastro iniciada com sucesso");
            
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
        logger.info("Aplicação de Cadastro finalizada");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
