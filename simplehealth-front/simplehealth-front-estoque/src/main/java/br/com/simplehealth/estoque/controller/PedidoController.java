package br.com.simplehealth.estoque.controller;

import br.com.simplehealth.estoque.model.Fornecedor;
import br.com.simplehealth.estoque.model.Item;
import br.com.simplehealth.estoque.model.Pedido;
import br.com.simplehealth.estoque.service.AlimentoService;
import br.com.simplehealth.estoque.service.FornecedorService;
import br.com.simplehealth.estoque.service.HospitalarService;
import br.com.simplehealth.estoque.service.MedicamentoService;
import br.com.simplehealth.estoque.service.PedidoService;
import br.com.simplehealth.estoque.util.RefreshManager;
import br.com.simplehealth.estoque.util.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PedidoController extends AbstractCrudController<Pedido> {
    
    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);
    
    @FXML private TableView<Pedido> tablePedidos;
    @FXML private TableColumn<Pedido, UUID> colId;
    @FXML private TableColumn<Pedido, Date> colData;
    @FXML private TableColumn<Pedido, String> colStatus;
    @FXML private TableColumn<Pedido, String> colFornecedor;
    
    @FXML private DatePicker dtDataPedido;
    @FXML private ComboBox<String> cbStatus;
    @FXML private ComboBox<Fornecedor> cbFornecedor;
    @FXML private ListView<Item> listItensDisponiveis;
    @FXML private ListView<Item> listItensSelecionados;
    @FXML private Button btnAdicionarItem;
    @FXML private Button btnRemoverItem;
    
    @FXML
    private Button btnCriar;
    @FXML
    private Button btnAlterar;
    @FXML
    private Button btnDeletar;
    @FXML
    private Button btnConfirmar;
    @FXML
    private Button btnCancelar;
    
    private final PedidoService service;
    private final FornecedorService fornecedorService;
    private final AlimentoService alimentoService;
    private final MedicamentoService medicamentoService;
    private final HospitalarService hospitalarService;
    private final ObservableList<Pedido> pedidos;
    private final ObservableList<Item> itensDisponiveis;
    private final ObservableList<Item> itensSelecionados;
    
    public PedidoController() {
        this.service = new PedidoService();
        this.fornecedorService = new FornecedorService();
        this.alimentoService = new AlimentoService();
        this.medicamentoService = new MedicamentoService();
        this.hospitalarService = new HospitalarService();
        this.pedidos = FXCollections.observableArrayList();
        this.itensDisponiveis = FXCollections.observableArrayList();
        this.itensSelecionados = FXCollections.observableArrayList();
    }
    
    @FXML
    public void initialize() {
        super.btnCriar = this.btnCriar;
        super.btnAlterar = this.btnAlterar;
        super.btnDeletar = this.btnDeletar;
        super.btnConfirmar = this.btnConfirmar;
        super.btnCancelar = this.btnCancelar;

        setupTableColumns();
        setupComboBox();
        setupListViews();
        carregarDados();
        carregarItens();
        setupTableSelection();
        setupRefreshListener();
        configurarEstadoInicialBotoes();
        habilitarCampos(false);
    }
    
    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataPedido"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colFornecedor.setCellValueFactory(cellData -> {
            try {
                UUID fornecedorId = cellData.getValue().getFornecedorId();
                if (fornecedorId != null) {
                    return fornecedorService.listar().stream()
                        .filter(f -> f.getIdFornecedor().equals(fornecedorId))
                        .findFirst()
                        .map(f -> new javafx.beans.property.SimpleStringProperty(f.getNome()))
                        .orElse(new javafx.beans.property.SimpleStringProperty("N/A"));
                }
            } catch (Exception e) {
                logger.error("Erro ao buscar fornecedor", e);
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
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
        
        try {
            cbFornecedor.setItems(FXCollections.observableArrayList(fornecedorService.listar()));
            cbFornecedor.setConverter(new StringConverter<Fornecedor>() {
                @Override
                public String toString(Fornecedor fornecedor) {
                    return fornecedor != null ? fornecedor.getNome() + " (" + fornecedor.getCnpj() + ")" : "";
                }
                
                @Override
                public Fornecedor fromString(String string) {
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("Erro ao carregar fornecedores", e);
            mostrarErro("Erro", "Erro ao carregar fornecedores: " + extrairMensagemErro(e));
        }
    }
    
    private void setupListViews() {
        listItensDisponiveis.setItems(itensDisponiveis);
        listItensSelecionados.setItems(itensSelecionados);
        
        listItensDisponiveis.setCellFactory(lv -> new javafx.scene.control.ListCell<Item>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome() + " (Qt: " + item.getQuantidadeTotal() + ")");
            }
        });
        
        listItensSelecionados.setCellFactory(lv -> new javafx.scene.control.ListCell<Item>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome() + " (Qt: " + item.getQuantidadeTotal() + ")");
            }
        });
    }
    
    private void carregarItens() {
        try {
            itensDisponiveis.clear();
            itensDisponiveis.addAll(alimentoService.listar());
            itensDisponiveis.addAll(medicamentoService.listar());
            itensDisponiveis.addAll(hospitalarService.listar());
        } catch (Exception e) {
            logger.error("Erro ao carregar itens", e);
            mostrarErro("Erro", "Erro ao carregar itens: " + extrairMensagemErro(e));
        }
    }
    
    private void setupTableSelection() {
        tablePedidos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    itemSelecionado = newSelection;
                    preencherFormulario(newSelection);
                    habilitarBotoesSelecao();
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
            mostrarErro("Erro", "Erro ao carregar pedidos: " + extrairMensagemErro(e));
        }
    }
    
    @FXML
    private void handleAdicionarItem() {
        Item itemSelecionado = listItensDisponiveis.getSelectionModel().getSelectedItem();
        if (itemSelecionado != null) {
            itensSelecionados.add(itemSelecionado);
            itensDisponiveis.remove(itemSelecionado);
        }
    }
    
    @FXML
    private void handleRemoverItem() {
        Item itemSelecionado = listItensSelecionados.getSelectionModel().getSelectedItem();
        if (itemSelecionado != null) {
            itensDisponiveis.add(itemSelecionado);
            itensSelecionados.remove(itemSelecionado);
        }
    }
    
    private void preencherFormulario(Pedido pedido) {
        if (pedido.getDataPedido() != null) {
            LocalDate localDate = pedido.getDataPedido().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
            dtDataPedido.setValue(localDate);
        }
        cbStatus.setValue(pedido.getStatus());
        
        if (pedido.getFornecedorId() != null) {
            cbFornecedor.getItems().stream()
                .filter(f -> f.getIdFornecedor().equals(pedido.getFornecedorId()))
                .findFirst()
                .ifPresent(cbFornecedor::setValue);
        }
        
        // Recarregar todos os itens disponíveis antes de selecionar
        carregarItens();
        itensSelecionados.clear();
        
        // Carregar itens do pedido
        if (pedido.getItemIds() != null && !pedido.getItemIds().isEmpty()) {
            for (UUID itemId : pedido.getItemIds()) {
                // Buscar na lista carregada e mover para selecionados
                Item itemEncontrado = null;
                for (Item item : itensDisponiveis) {
                    if (item.getIdItem().equals(itemId)) {
                        itemEncontrado = item;
                        break;
                    }
                }
                if (itemEncontrado != null) {
                    itensSelecionados.add(itemEncontrado);
                    itensDisponiveis.remove(itemEncontrado);
                }
            }
        }
    }
    
    @FXML
    private void handleCriar() {
        limparFormulario();
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "CRIAR";
        dtDataPedido.requestFocus();
    }
    
    @FXML
    private void handleAlterar() {
        if (itemSelecionado == null) {
            mostrarErro("Erro", "Selecione um pedido para alterar.");
            return;
        }
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "ALTERAR";
    }
    
    @FXML
    private void handleAtualizar() {
        handleAlterar();
    }
    
    @FXML
    private void handleConfirmar() {
        try {
            if ("DELETAR".equals(modoEdicao)) {
                if (itemSelecionado == null || itemSelecionado.getIdPedido() == null) {
                    mostrarErro("Erro", "Item selecionado inválido.");
                    return;
                }
                service.deletar(itemSelecionado.getIdPedido());
                mostrarSucesso("Sucesso", "Pedido deletado com sucesso!");
            } else if ("CRIAR".equals(modoEdicao)) {
                if (!validarFormulario()) return;
                Pedido pedido = construirPedidoDoFormulario();
                service.salvar(pedido);
                mostrarSucesso("Sucesso", "Pedido cadastrado com sucesso!");
            } else if ("ALTERAR".equals(modoEdicao)) {
                if (!validarFormulario()) return;
                if (itemSelecionado == null || itemSelecionado.getIdPedido() == null) {
                    mostrarErro("Erro", "Item selecionado inválido.");
                    return;
                }
                Pedido pedido = construirPedidoDoFormulario();
                pedido.setIdPedido(itemSelecionado.getIdPedido());
                service.atualizar(itemSelecionado.getIdPedido(), pedido);
                mostrarSucesso("Sucesso", "Pedido atualizado com sucesso!");
            }
            
            carregarDados();
            limparFormulario();
            resetarBotoes();
            habilitarCampos(false);
            modoEdicao = null;
            RefreshManager.getInstance().notifyRefresh("Pedido");
            
        } catch (Exception e) {
            logger.error("Erro ao processar operação", e);
            mostrarErro("Erro", "Erro ao salvar: " + extrairMensagemErro(e));
        }
    }
    
    @FXML
    private void handleCancelar() {
        limparFormulario();
        resetarBotoes();
        habilitarCampos(false);
        modoEdicao = null;
    }
    
    @FXML
    private void handleDeletar() {
        if (itemSelecionado == null || itemSelecionado.getIdPedido() == null) {
            mostrarErro("Erro", "Selecione um pedido para excluir");
            return;
        }
        ativarModoEdicao();
        modoEdicao = "DELETAR";
    }
    
    protected boolean validarFormulario() {
        if (dtDataPedido.getValue() == null) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("Data"));
            return false;
        }
        
        if (cbStatus.getValue() == null) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("Status"));
            return false;
        }
        
        if (cbFornecedor.getValue() == null) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("Fornecedor"));
            return false;
        }
        
        if (itensSelecionados.isEmpty()) {
            mostrarErro("Validação", "Selecione pelo menos um item para o pedido.");
            return false;
        }
        
        return true;
    }
    
    @Override
    protected void limparFormulario() {
        itemSelecionado = null;
        dtDataPedido.setValue(null);
        cbStatus.setValue(null);
        cbFornecedor.setValue(null);
        tablePedidos.getSelectionModel().clearSelection();
        
        // Limpar listas de itens
        itensSelecionados.clear();
        carregarItens();
    }
    
    @Override
    protected void habilitarCampos(boolean habilitar) {
        dtDataPedido.setDisable(!habilitar);
        cbStatus.setDisable(!habilitar);
        cbFornecedor.setDisable(!habilitar);
        listItensDisponiveis.setDisable(!habilitar);
        listItensSelecionados.setDisable(!habilitar);
        btnAdicionarItem.setDisable(!habilitar);
        btnRemoverItem.setDisable(!habilitar);
    }
    
    private Pedido construirPedidoDoFormulario() {
        Pedido pedido = new Pedido();
        
        LocalDate localDate = dtDataPedido.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        pedido.setDataPedido(date);
        
        pedido.setStatus(cbStatus.getValue());
        pedido.setFornecedorId(cbFornecedor.getValue().getIdFornecedor());
        
        // Extrair UUIDs dos itens selecionados
        List<UUID> itemIds = itensSelecionados.stream()
            .map(Item::getIdItem)
            .collect(java.util.stream.Collectors.toList());
        pedido.setItemIds(itemIds);
        
        return pedido;
    }
}
