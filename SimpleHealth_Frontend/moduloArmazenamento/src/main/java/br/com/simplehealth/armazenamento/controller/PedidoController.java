package br.com.simplehealth.armazenamento.controller;

import br.com.simplehealth.armazenamento.model.Pedido;
import br.com.simplehealth.armazenamento.model.Item;
import br.com.simplehealth.armazenamento.model.Fornecedor;
import br.com.simplehealth.armazenamento.service.PedidoService;
import br.com.simplehealth.armazenamento.service.ItemService;
import br.com.simplehealth.armazenamento.service.FornecedorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador para operações CRUD de Pedido.
 */
public class PedidoController extends AbstractCrudController<Pedido, br.com.simplehealth.armazenamento.view.Pedido, Long> {
    
    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Campos do formulário
    @FXML private DatePicker dataPedidoField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField notaFiscalField;
    @FXML private ComboBox<Fornecedor> fornecedorComboBox;
    
    // Lista de itens do pedido
    @FXML private ListView<Item> itensListView;
    @FXML private ComboBox<Item> itensComboBox;
    @FXML private Button adicionarItemButton;
    @FXML private Button removerItemButton;
    
    // Colunas da tabela
    @FXML private TableColumn<br.com.simplehealth.armazenamento.view.Pedido, Long> idColumn;
    @FXML private TableColumn<br.com.simplehealth.armazenamento.view.Pedido, String> dataPedidoColumn;
    @FXML private TableColumn<br.com.simplehealth.armazenamento.view.Pedido, String> statusColumn;
    @FXML private TableColumn<br.com.simplehealth.armazenamento.view.Pedido, String> notaFiscalColumn;
    @FXML private TableColumn<br.com.simplehealth.armazenamento.view.Pedido, String> fornecedorColumn;
    @FXML private TableColumn<br.com.simplehealth.armazenamento.view.Pedido, Integer> quantidadeItensColumn;
    
    private final PedidoService pedidoService;
    private final ItemService itemService;
    private final FornecedorService fornecedorService;
    private ObservableList<Item> itensDoPedido;
    private ObservableList<Item> itensDisponiveis;
    private ObservableList<Fornecedor> fornecedoresDisponiveis;

    public PedidoController() {
        this.pedidoService = new PedidoService();
        this.itemService = new ItemService();
        this.fornecedorService = new FornecedorService();
    }

    @Override
    public void initialize() {
        super.initialize();
        configurarColunas();
        configurarComboBoxes();
        configurarListaItens();
        inicializarObservableLists();
        carregarDadosIniciais();
    }
    
    private void configurarColunas() {
        if (idColumn != null) idColumn.setCellValueFactory(cellData -> cellData.getValue().idPedidoProperty().asObject());
        if (dataPedidoColumn != null) dataPedidoColumn.setCellValueFactory(cellData -> cellData.getValue().dataPedidoProperty());
        if (statusColumn != null) statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        if (notaFiscalColumn != null) notaFiscalColumn.setCellValueFactory(cellData -> cellData.getValue().nfProperty());
        if (fornecedorColumn != null) fornecedorColumn.setCellValueFactory(cellData -> cellData.getValue().fornecedorNomeProperty());
        if (quantidadeItensColumn != null) quantidadeItensColumn.setCellValueFactory(cellData -> cellData.getValue().quantidadeItensProperty().asObject());
    }
    
    private void configurarComboBoxes() {
        if (statusComboBox != null) {
            statusComboBox.getItems().addAll("PENDENTE", "PROCESSANDO", "ENVIADO", "ENTREGUE", "CANCELADO");
        }
        
        if (fornecedorComboBox != null) {
            fornecedorComboBox.setCellFactory(param -> new ListCell<Fornecedor>() {
                @Override
                protected void updateItem(Fornecedor fornecedor, boolean empty) {
                    super.updateItem(fornecedor, empty);
                    if (empty || fornecedor == null) {
                        setText(null);
                    } else {
                        setText(fornecedor.getNome() + " - " + fornecedor.getCnpj());
                    }
                }
            });
            
            fornecedorComboBox.setButtonCell(new ListCell<Fornecedor>() {
                @Override
                protected void updateItem(Fornecedor fornecedor, boolean empty) {
                    super.updateItem(fornecedor, empty);
                    if (empty || fornecedor == null) {
                        setText(null);
                    } else {
                        setText(fornecedor.getNome() + " - " + fornecedor.getCnpj());
                    }
                }
            });
        }
    }
    
    private void configurarListaItens() {
        if (itensListView != null) {
            itensListView.setCellFactory(param -> new ListCell<Item>() {
                @Override
                protected void updateItem(Item item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNome() + " - " + item.getTipo() + " (Lote: " + item.getLote() + ")");
                    }
                }
            });
        }
        
        if (itensComboBox != null) {
            itensComboBox.setCellFactory(param -> new ListCell<Item>() {
                @Override
                protected void updateItem(Item item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNome() + " - " + item.getTipo() + " (Lote: " + item.getLote() + ")");
                    }
                }
            });
            
            itensComboBox.setButtonCell(new ListCell<Item>() {
                @Override
                protected void updateItem(Item item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNome() + " - " + item.getTipo() + " (Lote: " + item.getLote() + ")");
                    }
                }
            });
        }
        
        // Configurar botões
        if (adicionarItemButton != null) {
            adicionarItemButton.setOnAction(e -> adicionarItem());
        }
        if (removerItemButton != null) {
            removerItemButton.setOnAction(e -> removerItem());
        }
    }
    
    private void inicializarObservableLists() {
        itensDoPedido = FXCollections.observableArrayList();
        itensDisponiveis = FXCollections.observableArrayList();
        fornecedoresDisponiveis = FXCollections.observableArrayList();
        
        if (itensListView != null) {
            itensListView.setItems(itensDoPedido);
        }
        if (itensComboBox != null) {
            itensComboBox.setItems(itensDisponiveis);
        }
        if (fornecedorComboBox != null) {
            fornecedorComboBox.setItems(fornecedoresDisponiveis);
        }
    }
    
    private void carregarDadosIniciais() {
        try {
            List<Item> itens = itemService.buscarTodos();
            itensDisponiveis.setAll(itens);
            
            List<Fornecedor> fornecedores = fornecedorService.buscarTodos();
            fornecedoresDisponiveis.setAll(fornecedores);
        } catch (IOException e) {
            logger.error("Erro ao carregar dados iniciais", e);
        }
    }

    // Implementação dos métodos abstratos
    
    @Override
    protected List<Pedido> buscarTodosViaApi() throws IOException {
        return pedidoService.buscarTodos();
    }

    @Override
    protected Pedido buscarPorIdViaApi(Long id) throws IOException {
        return pedidoService.buscarPorId(id);
    }

    @Override
    protected Pedido criarViaApi(Pedido pedido) throws IOException {
        return pedidoService.criar(pedido);
    }

    @Override
    protected Pedido atualizarViaApi(Long id, Pedido pedido) throws IOException {
        return pedidoService.atualizar(id, pedido);
    }

    @Override
    protected boolean deletarViaApi(Long id) throws IOException {
        return pedidoService.deletar(id);
    }

    @Override
    protected br.com.simplehealth.armazenamento.view.Pedido modelToView(Pedido pedido) {
        String dataPedidoStr = pedido.getDataPedido() != null ? 
            pedido.getDataPedido().format(DATE_FORMATTER) : "";
        
        String fornecedorNome = pedido.getFornecedor() != null ? 
            pedido.getFornecedor().getNome() : "";
            
        int quantidadeItens = pedido.getItens() != null ? pedido.getItens().size() : 0;
        
        return new br.com.simplehealth.armazenamento.view.Pedido(
            pedido.getIdPedido(),
            dataPedidoStr,
            pedido.getStatus(),
            pedido.getNf(),
            fornecedorNome,
            quantidadeItens
        );
    }

    @Override
    protected Pedido viewToModel(br.com.simplehealth.armazenamento.view.Pedido pedidoView) {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(pedidoView.getIdPedido());
        pedido.setStatus(pedidoView.getStatus());
        pedido.setNf(pedidoView.getNf());
        
        if (pedidoView.getDataPedido() != null && !pedidoView.getDataPedido().isEmpty()) {
            pedido.setDataPedido(LocalDate.parse(pedidoView.getDataPedido(), DATE_FORMATTER));
        }
        
        return pedido;
    }

    @Override
    protected void preencherCampos(br.com.simplehealth.armazenamento.view.Pedido pedidoView) {
        if (dataPedidoField != null && pedidoView.getDataPedido() != null && !pedidoView.getDataPedido().isEmpty()) {
            dataPedidoField.setValue(LocalDate.parse(pedidoView.getDataPedido(), DATE_FORMATTER));
        }
        
        if (statusComboBox != null) {
            statusComboBox.setValue(pedidoView.getStatus());
        }
        
        if (notaFiscalField != null) {
            notaFiscalField.setText(pedidoView.getNf());
        }
        
        // Carregar os itens e fornecedor do pedido
        try {
            Pedido pedido = buscarPorIdViaApi(pedidoView.getIdPedido());
            if (pedido != null) {
                if (pedido.getItens() != null) {
                    itensDoPedido.setAll(pedido.getItens());
                } else {
                    itensDoPedido.clear();
                }
                
                if (pedido.getFornecedor() != null && fornecedorComboBox != null) {
                    fornecedorComboBox.setValue(pedido.getFornecedor());
                }
            }
        } catch (IOException e) {
            logger.error("Erro ao carregar dados do pedido", e);
            itensDoPedido.clear();
        }
    }

    @Override
    protected void limparCampos() {
        if (dataPedidoField != null) {
            dataPedidoField.setValue(null);
        }
        if (statusComboBox != null) {
            statusComboBox.getSelectionModel().clearSelection();
        }
        if (notaFiscalField != null) {
            notaFiscalField.clear();
        }
        if (fornecedorComboBox != null) {
            fornecedorComboBox.getSelectionModel().clearSelection();
        }
        itensDoPedido.clear();
    }

    @Override
    protected void desabilitarCampos(boolean desabilitado) {
        if (dataPedidoField != null) {
            dataPedidoField.setDisable(desabilitado);
        }
        if (statusComboBox != null) {
            statusComboBox.setDisable(desabilitado);
        }
        if (notaFiscalField != null) {
            notaFiscalField.setDisable(desabilitado);
        }
        if (fornecedorComboBox != null) {
            fornecedorComboBox.setDisable(desabilitado);
        }
        if (itensComboBox != null) {
            itensComboBox.setDisable(desabilitado);
        }
        if (adicionarItemButton != null) {
            adicionarItemButton.setDisable(desabilitado);
        }
        if (removerItemButton != null) {
            removerItemButton.setDisable(desabilitado);
        }
    }

    @Override
    protected Long getIdFromViewModel(br.com.simplehealth.armazenamento.view.Pedido pedidoView) {
        return pedidoView.getIdPedido();
    }

    @Override
    protected boolean validarCampos() {
        if (dataPedidoField == null || dataPedidoField.getValue() == null) {
            return false;
        }
        if (statusComboBox == null || statusComboBox.getValue() == null) {
            return false;
        }
        if (fornecedorComboBox == null || fornecedorComboBox.getValue() == null) {
            return false;
        }
        return true;
    }

    @Override
    protected Pedido criarEntidadeDosCampos() {
        Pedido pedido = new Pedido();
        
        if (dataPedidoField != null) {
            pedido.setDataPedido(dataPedidoField.getValue());
        }
        if (statusComboBox != null) {
            pedido.setStatus(statusComboBox.getValue());
        }
        if (notaFiscalField != null) {
            pedido.setNf(notaFiscalField.getText());
        }
        if (fornecedorComboBox != null) {
            pedido.setFornecedor(fornecedorComboBox.getValue());
        }
        
        pedido.setItens(List.copyOf(itensDoPedido));
        
        return pedido;
    }
    
    @FXML
    private void adicionarItem() {
        Item itemSelecionado = itensComboBox.getSelectionModel().getSelectedItem();
        if (itemSelecionado != null && !itensDoPedido.contains(itemSelecionado)) {
            itensDoPedido.add(itemSelecionado);
            itensComboBox.getSelectionModel().clearSelection();
        }
    }
    
    @FXML
    private void removerItem() {
        Item itemSelecionado = itensListView.getSelectionModel().getSelectedItem();
        if (itemSelecionado != null) {
            itensDoPedido.remove(itemSelecionado);
        }
    }
}