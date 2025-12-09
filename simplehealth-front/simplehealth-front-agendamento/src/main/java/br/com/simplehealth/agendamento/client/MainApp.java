package br.com.simplehealth.agendamento.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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

import java.util.Optional;

/**
 * Aplicação principal do módulo de Agendamento do SimpleHealth.
 * Gerencia a interface JavaFX com abas para Consultas, Exames, Procedimentos e Bloqueios de Agenda.
 */
public class MainApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Iniciando aplicação SimpleHealth - Módulo de Agendamento");

            // Criar layout principal
            BorderPane root = new BorderPane();
            
            // Criar barra superior com botão Reiniciar
            HBox topBar = criarBarraSuperior(primaryStage);
            root.setTop(topBar);

            TabPane tabPane = new TabPane();

            // Aba de Consultas
            Tab consultaTab = new Tab("Consultas");
            consultaTab.setClosable(false);
            Parent consultaRoot = FXMLLoader.load(getClass().getResource("/view/consulta.fxml"));
            consultaTab.setContent(consultaRoot);
            tabPane.getTabs().add(consultaTab);

            // Aba de Exames
            Tab exameTab = new Tab("Exames");
            exameTab.setClosable(false);
            Parent exameRoot = FXMLLoader.load(getClass().getResource("/view/exame.fxml"));
            exameTab.setContent(exameRoot);
            tabPane.getTabs().add(exameTab);

            // Aba de Procedimentos
            Tab procedimentoTab = new Tab("Procedimentos");
            procedimentoTab.setClosable(false);
            Parent procedimentoRoot = FXMLLoader.load(getClass().getResource("/view/procedimento.fxml"));
            procedimentoTab.setContent(procedimentoRoot);
            tabPane.getTabs().add(procedimentoTab);

            // Aba de Bloqueio de Agenda
            Tab bloqueioTab = new Tab("Bloqueios de Agenda");
            bloqueioTab.setClosable(false);
            Parent bloqueioRoot = FXMLLoader.load(getClass().getResource("/view/bloqueio.fxml"));
            bloqueioTab.setContent(bloqueioRoot);
            tabPane.getTabs().add(bloqueioTab);

            root.setCenter(tabPane);

            Scene scene = new Scene(root, 1200, 700);
            primaryStage.setTitle("SimpleHealth - Módulo de Agendamento");
            primaryStage.setScene(scene);
            primaryStage.show();

            logger.info("Aplicação iniciada com sucesso");

        } catch (Exception e) {
            logger.error("Erro ao iniciar aplicação", e);
            e.printStackTrace();
        }
    }
    
    private HBox criarBarraSuperior(Stage primaryStage) {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(10, 15, 10, 15));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #27ae60; -fx-border-color: #229954; -fx-border-width: 0 0 2 0;");
        
        Label titleLabel = new Label("SimpleHealth - Módulo de Agendamento");
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
            logger.info("Reiniciando módulo de Agendamento...");
            try {
                primaryStage.close();
                Platform.runLater(() -> {
                    try {
                        Stage novoStage = new Stage();
                        start(novoStage);
                        logger.info("Módulo reiniciado com sucesso");
                    } catch (Exception ex) {
                        logger.error("Erro ao reiniciar módulo: ", ex);
                    }
                });
            } catch (Exception ex) {
                logger.error("Erro ao fechar stage: ", ex);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
