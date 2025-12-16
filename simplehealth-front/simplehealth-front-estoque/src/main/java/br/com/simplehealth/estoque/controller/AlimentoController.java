package br.com.simplehealth.estoque.controller;

import br.com.simplehealth.estoque.model.Alimento;
import br.com.simplehealth.estoque.model.Estoque;
import br.com.simplehealth.estoque.service.AlimentoService;
import br.com.simplehealth.estoque.service.EstoqueService;
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
import java.util.UUID;

public class AlimentoController extends AbstractCrudController<Alimento> {
    
    private static final Logger logger = LoggerFactory.getLogger(AlimentoController.class);
    
    @FXML private TableView<Alimento> tableAlimentos;
    @FXML private TableColumn<Alimento, UUID> colId;
    @FXML private TableColumn<Alimento, String> colNome;
    @FXML private TableColumn<Alimento, Integer> colQuantidade;
    @FXML private TableColumn<Alimento, String> colEstoque;
    @FXML private TableColumn<Alimento, String> colAlergenicos;
    
    @FXML private TextField txtNome;
    @FXML private TextField txtQuantidade;
    @FXML private DatePicker dpValidade;
    @FXML private ComboBox<Estoque> cbEstoque;
    @FXML private TextArea txtAlergenicos;
    
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
    
    private final AlimentoService service;
    private final EstoqueService estoqueService;
    private final ObservableList<Alimento> alimentos;
    
    public AlimentoController() {
        this.service = new AlimentoService();
        this.estoqueService = new EstoqueService();
        this.alimentos = FXCollections.observableArrayList();
    }
    
    @FXML
    public void initialize() {
        super.btnCriar = this.btnCriar;
        super.btnAlterar = this.btnAlterar;
        super.btnDeletar = this.btnDeletar;
        super.btnConfirmar = this.btnConfirmar;
        super.btnCancelar = this.btnCancelar;

        setupTableColumns();
        setupComboBoxEstoque();
        carregarDados();
        setupTableSelection();
        setupRefreshListener();
        configurarEstadoInicialBotoes();
        habilitarCampos(false);
    }
    
    private void setupComboBoxEstoque() {
        try {
            cbEstoque.setItems(FXCollections.observableArrayList(estoqueService.listar()));
            cbEstoque.setConverter(new StringConverter<Estoque>() {
                @Override
                public String toString(Estoque estoque) {
                    return estoque != null ? estoque.getNome() + " - " + estoque.getLocalizacao() : "";
                }
                
                @Override
                public Estoque fromString(String string) {
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("Erro ao carregar estoques", e);
            mostrarErro("Erro", "Erro ao carregar estoques: " + extrairMensagemErro(e));
        }
    }
    
    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidadeTotal"));
        colEstoque.setCellValueFactory(cellData -> {
            try {
                UUID estoqueId = cellData.getValue().getEstoqueId();
                if (estoqueId != null) {
                    return estoqueService.listar().stream()
                        .filter(e -> e.getIdEstoque().equals(estoqueId))
                        .findFirst()
                        .map(e -> new javafx.beans.property.SimpleStringProperty(e.getNome()))
                        .orElse(new javafx.beans.property.SimpleStringProperty("N/A"));
                }
            } catch (Exception e) {
                logger.error("Erro ao buscar estoque", e);
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        colAlergenicos.setCellValueFactory(new PropertyValueFactory<>("alergenicos"));
        
        tableAlimentos.setItems(alimentos);
    }
    
    private void setupTableSelection() {
        tableAlimentos.getSelectionModel().selectedItemProperty().addListener(
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
            if (!"Alimento".equals(source)) {
                carregarDados();
            }
        });
    }
    
    @Override
    protected void carregarDados() {
        try {
            alimentos.clear();
            alimentos.addAll(service.listar());
            logger.info("Alimentos carregados: {}", alimentos.size());
        } catch (Exception e) {
            logger.error("Erro ao carregar alimentos", e);
            mostrarErro("Erro", "Erro ao carregar alimentos: " + extrairMensagemErro(e));
        }
    }
    
    private void preencherFormulario(Alimento alimento) {
        txtNome.setText(alimento.getNome());
        txtQuantidade.setText(String.valueOf(alimento.getQuantidadeTotal()));
        
        if (alimento.getValidade() != null) {
            LocalDate localDate = alimento.getValidade().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
            dpValidade.setValue(localDate);
        }
        
        if (alimento.getEstoqueId() != null) {
            cbEstoque.getItems().stream()
                .filter(e -> e.getIdEstoque().equals(alimento.getEstoqueId()))
                .findFirst()
                .ifPresent(cbEstoque::setValue);
        }
        
        txtAlergenicos.setText(alimento.getAlergenicos());
    }
    
    @FXML
    private void handleCriar() {
        limparFormulario();
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "CRIAR";
        txtNome.requestFocus();
    }
    
    @FXML
    private void handleAlterar() {
        if (itemSelecionado == null) {
            mostrarErro("Erro", "Selecione um alimento para alterar.");
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
                if (itemSelecionado == null || itemSelecionado.getIdItem() == null) {
                    mostrarErro("Erro", "Item selecionado inválido.");
                    return;
                }
                service.deletar(itemSelecionado.getIdItem());
                mostrarSucesso("Sucesso", "Alimento deletado com sucesso!");
            } else if ("CRIAR".equals(modoEdicao)) {
                if (!validarFormulario()) return;
                Alimento alimento = construirAlimentoDoFormulario();
                service.salvar(alimento);
                mostrarSucesso("Sucesso", "Alimento cadastrado com sucesso!");
            } else if ("ALTERAR".equals(modoEdicao)) {
                if (!validarFormulario()) return;
                if (itemSelecionado == null || itemSelecionado.getIdItem() == null) {
                    mostrarErro("Erro", "Item selecionado inválido.");
                    return;
                }
                Alimento alimento = construirAlimentoDoFormulario();
                alimento.setIdItem(itemSelecionado.getIdItem());
                service.atualizar(itemSelecionado.getIdItem(), alimento);
                mostrarSucesso("Sucesso", "Alimento atualizado com sucesso!");
            }
            
            carregarDados();
            limparFormulario();
            resetarBotoes();
            habilitarCampos(false);
            modoEdicao = null;
            RefreshManager.getInstance().notifyRefresh("Alimento");
            
        } catch (Exception e) {
            logger.error("Erro ao salvar alimento", e);
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
        if (itemSelecionado == null || itemSelecionado.getIdItem() == null) {
            mostrarErro("Erro", "Selecione um alimento para excluir");
            return;
        }
        ativarModoEdicao();
        modoEdicao = "DELETAR";
    }
    
    protected boolean validarFormulario() {
        // Validar nome obrigatório
        if (!ValidationUtils.validarCampoObrigatorio(txtNome.getText(), "Nome")) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("Nome"));
            return false;
        }
        
        // Validar quantidade obrigatória e positiva
        if (!ValidationUtils.validarQuantidade(txtQuantidade.getText())) {
            mostrarErro("Validação", "A quantidade deve ser um número inteiro positivo.");
            return false;
        }
        
        // Validar validade obrigatória
        if (dpValidade.getValue() == null) {
            mostrarErro("Validação", "A data de validade é obrigatória.");
            return false;
        }
        
        // Validar estoque obrigatório
        if (cbEstoque.getValue() == null) {
            mostrarErro("Validação", "Selecione um estoque.");
            return false;
        }
        
        return true;
    }
    
    @Override
    protected void limparFormulario() {
        itemSelecionado = null;
        txtNome.clear();
        txtQuantidade.clear();
        dpValidade.setValue(null);
        cbEstoque.setValue(null);
        txtAlergenicos.clear();
        tableAlimentos.getSelectionModel().clearSelection();
    }
    
    @Override
    protected void habilitarCampos(boolean habilitar) {
        txtNome.setDisable(!habilitar);
        txtQuantidade.setDisable(!habilitar);
        dpValidade.setDisable(!habilitar);
        cbEstoque.setDisable(!habilitar);
        txtAlergenicos.setDisable(!habilitar);
    }
    
    private Alimento construirAlimentoDoFormulario() {
        Alimento alimento = new Alimento();
        alimento.setNome(txtNome.getText());
        alimento.setQuantidadeTotal(Integer.parseInt(txtQuantidade.getText()));
        
        LocalDate localDate = dpValidade.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        alimento.setValidade(date);
        
        alimento.setEstoqueId(cbEstoque.getValue().getIdEstoque());
        alimento.setAlergenicos(txtAlergenicos.getText());
        return alimento;
    }
}
