package br.com.simplehealth.agendamento.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

            Scene scene = new Scene(tabPane, 1200, 700);
            primaryStage.setTitle("SimpleHealth - Módulo de Agendamento");
            primaryStage.setScene(scene);
            primaryStage.show();

            logger.info("Aplicação iniciada com sucesso");

        } catch (Exception e) {
            logger.error("Erro ao iniciar aplicação", e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
