package br.com.simplehealth.cadastro.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

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
            // Criar layout principal
            BorderPane root = new BorderPane();
            
            // Criar barra superior com botão Reiniciar
            HBox topBar = criarBarraSuperior(primaryStage);
            root.setTop(topBar);
            
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
            
            root.setCenter(tabPane);
            
            // Configurar cena
            Scene scene = new Scene(root, 1400, 900);
            
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

    private HBox criarBarraSuperior(Stage primaryStage) {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(10, 15, 10, 15));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #2c3e50; -fx-border-color: #34495e; -fx-border-width: 0 0 2 0;");
        
        Label titleLabel = new Label("SimpleHealth - Módulo de Cadastro");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        
        Button reiniciarBtn = new Button("🔄 Reiniciar");
        reiniciarBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; " +
                             "-fx-padding: 8 20 8 20; -fx-cursor: hand; -fx-background-radius: 5;");
        reiniciarBtn.setOnMouseEntered(e -> 
            reiniciarBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-size: 14px; " +
                                 "-fx-padding: 8 20 8 20; -fx-cursor: hand; -fx-background-radius: 5;"));
        reiniciarBtn.setOnMouseExited(e -> 
            reiniciarBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; " +
                                 "-fx-padding: 8 20 8 20; -fx-cursor: hand; -fx-background-radius: 5;"));
        reiniciarBtn.setOnAction(e -> reiniciarModulo(primaryStage));
        
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        
        topBar.getChildren().addAll(titleLabel, spacer, reiniciarBtn);
        return topBar;
    }
    
    private void reiniciarModulo(Stage primaryStage) {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Reinicialização");
        confirmacao.setHeaderText("Deseja reiniciar o módulo?");
        confirmacao.setContentText("Todas as telas serão recarregadas e dados não salvos serão perdidos.");
        
        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            logger.info("Reiniciando módulo de Cadastro...");
            try {
                primaryStage.close();
                Platform.runLater(() -> {
                    try {
                        Stage novoStage = new Stage();
                        start(novoStage);
                        logger.info("Módulo reiniciado com sucesso");
                    } catch (Exception ex) {
                        logger.error("Erro ao reiniciar módulo: ", ex);
                        mostrarErro("Erro ao Reiniciar", "Não foi possível reiniciar o módulo: " + ex.getMessage());
                    }
                });
            } catch (Exception ex) {
                logger.error("Erro ao fechar stage: ", ex);
            }
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
