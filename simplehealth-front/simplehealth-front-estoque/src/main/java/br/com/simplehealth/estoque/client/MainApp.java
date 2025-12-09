package br.com.simplehealth.estoque.client;

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

import java.util.Optional;

/**
 * Aplicação principal JavaFX - Módulo de Estoque
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
            
            TabPane tabPane = new TabPane();
            
            // Tab Medicamentos
            Tab tabMedicamentos = new Tab("Medicamentos");
            tabMedicamentos.setClosable(false);
            FXMLLoader loaderMedicamentos = new FXMLLoader(
                getClass().getResource("/view/medicamento.fxml"));
            tabMedicamentos.setContent(loaderMedicamentos.load());
            
            // Tab Alimentos
            Tab tabAlimentos = new Tab("Alimentos");
            tabAlimentos.setClosable(false);
            FXMLLoader loaderAlimentos = new FXMLLoader(
                getClass().getResource("/view/alimento.fxml"));
            tabAlimentos.setContent(loaderAlimentos.load());
            
            // Tab Hospitalares
            Tab tabHospitalares = new Tab("Hospitalares");
            tabHospitalares.setClosable(false);
            FXMLLoader loaderHospitalares = new FXMLLoader(
                getClass().getResource("/view/hospitalar.fxml"));
            tabHospitalares.setContent(loaderHospitalares.load());
            
            // Tab Fornecedores
            Tab tabFornecedores = new Tab("Fornecedores");
            tabFornecedores.setClosable(false);
            FXMLLoader loaderFornecedores = new FXMLLoader(
                getClass().getResource("/view/fornecedor.fxml"));
            tabFornecedores.setContent(loaderFornecedores.load());
            
            // Tab Estoques
            Tab tabEstoques = new Tab("Estoques");
            tabEstoques.setClosable(false);
            FXMLLoader loaderEstoques = new FXMLLoader(
                getClass().getResource("/view/estoque.fxml"));
            tabEstoques.setContent(loaderEstoques.load());
            
            // Tab Pedidos
            Tab tabPedidos = new Tab("Pedidos");
            tabPedidos.setClosable(false);
            FXMLLoader loaderPedidos = new FXMLLoader(
                getClass().getResource("/view/pedido.fxml"));
            tabPedidos.setContent(loaderPedidos.load());
            
            // Tab Itens (visualização)
            Tab tabItens = new Tab("Todos os Itens");
            tabItens.setClosable(false);
            FXMLLoader loaderItens = new FXMLLoader(
                getClass().getResource("/view/item.fxml"));
            tabItens.setContent(loaderItens.load());
            
            tabPane.getTabs().addAll(
                tabMedicamentos,
                tabAlimentos,
                tabHospitalares,
                tabFornecedores,
                tabEstoques,
                tabPedidos,
                tabItens
            );
            
            root.setCenter(tabPane);
            
            Scene scene = new Scene(root, 1400, 800);
            primaryStage.setTitle("SimpleHealth - Gestão de Estoque");
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
        topBar.setStyle("-fx-background-color: #e67e22; -fx-border-color: #d35400; -fx-border-width: 0 0 2 0;");
        
        Label titleLabel = new Label("SimpleHealth - Gestão de Estoque");
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
            logger.info("Reiniciando módulo de Estoque...");
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
