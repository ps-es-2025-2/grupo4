package br.com.simplehealth.estoque.controller;

import br.com.simplehealth.estoque.model.Pedido;
import br.com.simplehealth.estoque.service.PedidoService;
import br.com.simplehealth.estoque.util.RefreshManager;
import br.com.simplehealth.estoque.util.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

public class PedidoController extends AbstractCrudController {
    
    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);
    
    @FXML private TableView<Pedido> tablePedidos;
    @FXML private TableColumn<Pedido, Long> colId;
    @FXML private TableColumn<Pedido, LocalDateTime> colData;
    @FXML private TableColumn<Pedido, String> colStatus;
    @FXML private TableColumn<Pedido, String> colFornecedor;
    
    @FXML private DatePicker dtDataPedido;
    @FXML private ComboBox<String> cbStatus;
    @FXML private TextField txtIdFornecedor;
    @FXML private TextArea txtObservacoes;
    
    private final PedidoService service;
    private final ObservableList<Pedido> pedidos;
    private Pedido pedidoSelecionado;
    
    public PedidoController() {
        this.service = new PedidoService();
        this.pedidos = FXCollections.observableArrayList();
    }
    
    @FXML
    public void initialize() {
        setupTableColumns();
        setupComboBox();
        carregarDados();
        setupTableSelection();
        setupRefreshListener();
    }
    
    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataPedido"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colFornecedor.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getFornecedor() != null ? 
                cellData.getValue().getFornecedor().getNome() : "N/A"
            )
        );
        tablePedidos.setItems(pedidos);
    }
    
    private void setupComboBox() {
        cbStatus.setItems(FXCollections.observableArrayList(
            "Pendente",
            "Em Processamento",
            "Enviado",
            "Entregue",
            "Cancelado"
        ));
    }
    
    private void setupTableSelection() {
        tablePedidos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    preencherFormulario(newSelection);
                    pedidoSelecionado = newSelection;
                }
            }
        );
    }
    
    private void setupRefreshListener() {
        RefreshManager.getInstance().addListener(source -> {
            if (!"Pedido".equals(source)) {
                carregarDados();
            }
        });
    }
    
    @Override
    protected void carregarDados() {
        try {
            pedidos.clear();
            pedidos.addAll(service.listar());
            logger.info("Pedidos carregados: {}", pedidos.size());
        } catch (Exception e) {
            logger.error("Erro ao carregar pedidos", e);
            mostrarErro("Erro", "Erro ao carregar pedidos: " + e.getMessage());
        }
    }
    
    private void preencherFormulario(Pedido pedido) {
        if (pedido.getDataPedido() != null) {
            dtDataPedido.setValue(pedido.getDataPedido().toLocalDate());
        }
        cbStatus.setValue(pedido.getStatus());
        if (pedido.getFornecedor() != null && pedido.getFornecedor().getIdFornecedor() != null) {
            txtIdFornecedor.setText(String.valueOf(pedido.getFornecedor().getIdFornecedor()));
        }
    }
    
    @FXML
    private void handleNovo() {
        limparFormulario();
        pedidoSelecionado = null;
    }
    
    @FXML
    private void handleSalvar() {
        try {
            if (!validarCampos()) return;
            
            Pedido pedido = pedidoSelecionado != null ? 
                pedidoSelecionado : new Pedido();
            
            if (dtDataPedido.getValue() != null) {
                pedido.setDataPedido(dtDataPedido.getValue().atStartOfDay());
            } else {
                pedido.setDataPedido(LocalDateTime.now());
            }
            pedido.setStatus(cbStatus.getValue());
            // Note: Fornecedor e Itens devem ser configurados via relacionamento no backend
            
            if (pedidoSelecionado != null && pedidoSelecionado.getIdPedido() != null) {
                service.atualizar(pedidoSelecionado.getIdPedido(), pedido);
                mostrarSucesso("Sucesso", "Pedido atualizado com sucesso!");
            } else {
                service.salvar(pedido);
                mostrarSucesso("Sucesso", "Pedido cadastrado com sucesso!");
            }
            
            carregarDados();
            limparFormulario();
            RefreshManager.getInstance().notifyRefresh("Pedido");
            
        } catch (Exception e) {
            logger.error("Erro ao salvar pedido", e);
            mostrarErro("Erro", "Erro ao salvar: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeletar() {
        if (pedidoSelecionado == null) {
            mostrarErro("Atenção", "Selecione um pedido para deletar");
            return;
        }
        
        if (mostrarConfirmacao("Confirmar Exclusão", 
            "Deseja realmente excluir o pedido #" + pedidoSelecionado.getIdPedido() + "?")) {
            try {
                service.deletar(pedidoSelecionado.getIdPedido());
                mostrarSucesso("Sucesso", "Pedido deletado com sucesso!");
                carregarDados();
                limparFormulario();
                RefreshManager.getInstance().notifyRefresh("Pedido");
            } catch (Exception e) {
                logger.error("Erro ao deletar pedido", e);
                mostrarErro("Erro", "Erro ao deletar: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleAtualizar() {
        carregarDados();
    }
    
    private boolean validarCampos() {
        if (cbStatus.getValue() == null) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("Status"));
            return false;
        }
        
        return true;
    }
    
    @Override
    protected void limparFormulario() {
        dtDataPedido.setValue(null);
        cbStatus.setValue(null);
        txtIdFornecedor.clear();
        txtObservacoes.clear();
        pedidoSelecionado = null;
        tablePedidos.getSelectionModel().clearSelection();
    }
}
