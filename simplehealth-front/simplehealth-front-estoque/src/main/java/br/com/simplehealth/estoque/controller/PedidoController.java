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
import java.util.UUID;

public class PedidoController extends AbstractCrudController<Pedido> {
    
    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);
    
    @FXML private TableView<Pedido> tablePedidos;
    @FXML private TableColumn<Pedido, UUID> colId;
    @FXML private TableColumn<Pedido, LocalDateTime> colData;
    @FXML private TableColumn<Pedido, String> colStatus;
    @FXML private TableColumn<Pedido, String> colFornecedor;
    
    @FXML private DatePicker dtDataPedido;
    @FXML private ComboBox<String> cbStatus;
    @FXML private TextField txtIdFornecedor;
    @FXML private TextArea txtObservacoes;
    
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
    private final ObservableList<Pedido> pedidos;
    
    public PedidoController() {
        this.service = new PedidoService();
        this.pedidos = FXCollections.observableArrayList();
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
        carregarDados();
        setupTableSelection();
        setupRefreshListener();
        configurarEstadoInicialBotoes();
        habilitarCampos(false);
    }
    
    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataPedido"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colFornecedor.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getFornecedorId() != null ? 
                cellData.getValue().getFornecedorId().toString() : "N/A"
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
            mostrarErro("Erro", "Erro ao carregar pedidos: " + e.getMessage());
        }
    }
    
    private void preencherFormulario(Pedido pedido) {
        if (pedido.getDataPedido() != null) {
            // Converter Date para LocalDate
            dtDataPedido.setValue(new java.sql.Date(pedido.getDataPedido().getTime()).toLocalDate());
        }
        cbStatus.setValue(pedido.getStatus());
        if (pedido.getFornecedorId() != null) {
            txtIdFornecedor.setText(String.valueOf(pedido.getFornecedorId()));
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
            mostrarErro("Erro", "Erro ao salvar: " + e.getMessage());
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
        if (cbStatus.getValue() == null) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("Status"));
            return false;
        }
        
        return true;
    }
    
    @Override
    protected void limparFormulario() {
        itemSelecionado = null;
        dtDataPedido.setValue(null);
        cbStatus.setValue(null);
        txtIdFornecedor.clear();
        txtObservacoes.clear();
        tablePedidos.getSelectionModel().clearSelection();
    }
    
    @Override
    protected void habilitarCampos(boolean habilitar) {
        dtDataPedido.setDisable(!habilitar);
        cbStatus.setDisable(!habilitar);
        txtIdFornecedor.setDisable(!habilitar);
        txtObservacoes.setDisable(!habilitar);
    }
    
    private Pedido construirPedidoDoFormulario() {
        Pedido pedido = new Pedido();
        if (dtDataPedido.getValue() != null) {
            // Converter LocalDate para Date
            pedido.setDataPedido(java.sql.Date.valueOf(dtDataPedido.getValue()));
        } else {
            pedido.setDataPedido(new java.util.Date());
        }
        pedido.setStatus(cbStatus.getValue());
        // Note: Fornecedor e Itens devem ser configurados via relacionamento no backend
        return pedido;
    }
}
