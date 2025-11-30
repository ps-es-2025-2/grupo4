package br.com.simplehealth.estoque.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aplicação principal JavaFX - Módulo de Estoque
 */
public class MainApp extends Application {
    
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);
    
    @Override
    public void start(Stage primaryStage) {
        try {
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
            
            Scene scene = new Scene(tabPane, 1400, 800);
            primaryStage.setTitle("SimpleHealth - Gestão de Estoque");
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
