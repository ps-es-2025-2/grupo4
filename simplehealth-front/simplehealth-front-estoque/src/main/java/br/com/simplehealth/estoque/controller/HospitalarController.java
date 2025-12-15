package br.com.simplehealth.estoque.controller;

import br.com.simplehealth.estoque.model.Estoque;
import br.com.simplehealth.estoque.model.Hospitalar;
import br.com.simplehealth.estoque.service.EstoqueService;
import br.com.simplehealth.estoque.service.HospitalarService;
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

public class HospitalarController extends AbstractCrudController<Hospitalar> {
    
    private static final Logger logger = LoggerFactory.getLogger(HospitalarController.class);
    
    @FXML private TableView<Hospitalar> tableHospitalares;
    @FXML private TableColumn<Hospitalar, UUID> colId;
    @FXML private TableColumn<Hospitalar, String> colNome;
    @FXML private TableColumn<Hospitalar, Integer> colQuantidade;
    @FXML private TableColumn<Hospitalar, String> colEstoque;
    @FXML private TableColumn<Hospitalar, String> colDescartavel;
    
    @FXML private TextField txtNome;
    @FXML private TextField txtQuantidade;
    @FXML private DatePicker dpValidade;
    @FXML private ComboBox<Estoque> cbEstoque;
    @FXML private CheckBox chkDescartabilidade;
    
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
    
    private final HospitalarService service;
    private final EstoqueService estoqueService;
    private final ObservableList<Hospitalar> hospitalares;
    
    public HospitalarController() {
        this.service = new HospitalarService();
        this.estoqueService = new EstoqueService();
        this.hospitalares = FXCollections.observableArrayList();
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
        colDescartavel.setCellValueFactory(new PropertyValueFactory<>("descartabilidade"));
        tableHospitalares.setItems(hospitalares);
    }
    
    private void setupTableSelection() {
        tableHospitalares.getSelectionModel().selectedItemProperty().addListener(
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
            if (!"Hospitalar".equals(source)) {
                carregarDados();
            }
        });
    }
    
    @Override
    protected void carregarDados() {
        try {
            hospitalares.clear();
            hospitalares.addAll(service.listar());
            logger.info("Hospitalares carregados: {}", hospitalares.size());
        } catch (Exception e) {
            logger.error("Erro ao carregar hospitalares", e);
            mostrarErro("Erro", "Erro ao carregar itens hospitalares: " + extrairMensagemErro(e));
        }
    }
    
    private void preencherFormulario(Hospitalar hospitalar) {
        txtNome.setText(hospitalar.getNome());
        txtQuantidade.setText(String.valueOf(hospitalar.getQuantidadeTotal()));
        
        if (hospitalar.getValidade() != null) {
            LocalDate localDate = hospitalar.getValidade().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
            dpValidade.setValue(localDate);
        }
        
        if (hospitalar.getEstoqueId() != null) {
            cbEstoque.getItems().stream()
                .filter(e -> e.getIdEstoque().equals(hospitalar.getEstoqueId()))
                .findFirst()
                .ifPresent(cbEstoque::setValue);
        }
        
        chkDescartabilidade.setSelected(hospitalar.getDescartabilidade() != null && hospitalar.getDescartabilidade());
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
            mostrarErro("Erro", "Selecione um item para alterar.");
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
                mostrarSucesso("Sucesso", "Item hospitalar deletado com sucesso!");
            } else if ("CRIAR".equals(modoEdicao)) {
                if (!validarFormulario()) return;
                Hospitalar hospitalar = construirHospitalarDoFormulario();
                service.salvar(hospitalar);
                mostrarSucesso("Sucesso", "Item hospitalar cadastrado com sucesso!");
            } else if ("ALTERAR".equals(modoEdicao)) {
                if (!validarFormulario()) return;
                if (itemSelecionado == null || itemSelecionado.getIdItem() == null) {
                    mostrarErro("Erro", "Item selecionado inválido.");
                    return;
                }
                Hospitalar hospitalar = construirHospitalarDoFormulario();
                hospitalar.setIdItem(itemSelecionado.getIdItem());
                service.atualizar(itemSelecionado.getIdItem(), hospitalar);
                mostrarSucesso("Sucesso", "Item hospitalar atualizado com sucesso!");
            }
            
            carregarDados();
            limparFormulario();
            resetarBotoes();
            habilitarCampos(false);
            modoEdicao = null;
            RefreshManager.getInstance().notifyRefresh("Hospitalar");
            
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
        if (itemSelecionado == null || itemSelecionado.getIdItem() == null) {
            mostrarErro("Erro", "Selecione um item hospitalar para excluir");
            return;
        }
        ativarModoEdicao();
        modoEdicao = "DELETAR";
    }
    
    protected boolean validarFormulario() {
        if (!ValidationUtils.validarCampoObrigatorio(txtNome.getText(), "Nome")) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("Nome"));
            return false;
        }
        
        if (!ValidationUtils.validarQuantidade(txtQuantidade.getText())) {
            mostrarErro("Validação", "A quantidade deve ser um número inteiro positivo.");
            return false;
        }
        
        if (dpValidade.getValue() == null) {
            mostrarErro("Validação", "A data de validade é obrigatória.");
            return false;
        }
        
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
        chkDescartabilidade.setSelected(false);
        tableHospitalares.getSelectionModel().clearSelection();
    }
    
    @Override
    protected void habilitarCampos(boolean habilitar) {
        txtNome.setDisable(!habilitar);
        txtQuantidade.setDisable(!habilitar);
        dpValidade.setDisable(!habilitar);
        cbEstoque.setDisable(!habilitar);
        chkDescartabilidade.setDisable(!habilitar);
    }
    
    private Hospitalar construirHospitalarDoFormulario() {
        Hospitalar hospitalar = new Hospitalar();
        hospitalar.setNome(txtNome.getText());
        hospitalar.setQuantidadeTotal(Integer.parseInt(txtQuantidade.getText()));
        
        LocalDate localDate = dpValidade.getValue();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        hospitalar.setValidade(date);
        
        hospitalar.setEstoqueId(cbEstoque.getValue().getIdEstoque());
        hospitalar.setDescartabilidade(chkDescartabilidade.isSelected());
        return hospitalar;
    }
}
