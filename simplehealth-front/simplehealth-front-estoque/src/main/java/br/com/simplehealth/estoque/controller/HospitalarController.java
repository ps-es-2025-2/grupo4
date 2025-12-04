package br.com.simplehealth.estoque.controller;

import br.com.simplehealth.estoque.model.Hospitalar;
import br.com.simplehealth.estoque.service.HospitalarService;
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

public class HospitalarController extends AbstractCrudController<Hospitalar> {
    
    private static final Logger logger = LoggerFactory.getLogger(HospitalarController.class);
    
    @FXML private TableView<Hospitalar> tableHospitalares;
    @FXML private TableColumn<Hospitalar, Long> colId;
    @FXML private TableColumn<Hospitalar, String> colNome;
    @FXML private TableColumn<Hospitalar, String> colTipo;
    @FXML private TableColumn<Hospitalar, Integer> colQuantidade;
    @FXML private TableColumn<Hospitalar, Boolean> colDescartavel;
    
    @FXML private TextField txtNome;
    @FXML private TextArea txtDescricao;
    @FXML private TextField txtTipo;
    @FXML private TextField txtUnidadeMedida;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtLote;
    @FXML private TextField txtNf;
    @FXML private CheckBox chkDescartabilidade;
    @FXML private TextField txtUso;
    
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
    private final ObservableList<Hospitalar> hospitalares;
    
    public HospitalarController() {
        this.service = new HospitalarService();
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
        carregarDados();
        setupTableSelection();
        setupRefreshListener();
        configurarEstadoInicialBotoes();
        habilitarCampos(false);
    }
    
    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidadeTotal"));
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
            mostrarErro("Erro", "Erro ao carregar itens hospitalares: " + e.getMessage());
        }
    }
    
    private void preencherFormulario(Hospitalar hospitalar) {
        txtNome.setText(hospitalar.getNome());
        txtDescricao.setText(hospitalar.getDescricao());
        txtTipo.setText(hospitalar.getTipo());
        txtUnidadeMedida.setText(hospitalar.getUnidadeMedida());
        txtQuantidade.setText(String.valueOf(hospitalar.getQuantidadeTotal()));
        txtLote.setText(hospitalar.getLote());
        txtNf.setText(hospitalar.getNf());
        chkDescartabilidade.setSelected(hospitalar.getDescartabilidade() != null && hospitalar.getDescartabilidade());
        txtUso.setText(hospitalar.getUso());
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
        
        if (!txtLote.getText().trim().isEmpty() && !ValidationUtils.validarLote(txtLote.getText())) {
            mostrarErro("Validação", ValidationUtils.mensagemFormatoInvalido("Lote", "3-20 caracteres alfanuméricos"));
            return false;
        }
        
        if (!txtNf.getText().trim().isEmpty() && !ValidationUtils.validarNotaFiscal(txtNf.getText())) {
            mostrarErro("Validação", ValidationUtils.mensagemFormatoInvalido("Nota Fiscal", "6-44 caracteres"));
            return false;
        }
        
        return true;
    }
    
    @Override
    protected void limparFormulario() {
        itemSelecionado = null;
        txtNome.clear();
        txtDescricao.clear();
        txtTipo.clear();
        txtUnidadeMedida.clear();
        txtQuantidade.clear();
        txtLote.clear();
        txtNf.clear();
        chkDescartabilidade.setSelected(false);
        txtUso.clear();
        tableHospitalares.getSelectionModel().clearSelection();
    }
    
    @Override
    protected void habilitarCampos(boolean habilitar) {
        txtNome.setDisable(!habilitar);
        txtDescricao.setDisable(!habilitar);
        txtTipo.setDisable(!habilitar);
        txtUnidadeMedida.setDisable(!habilitar);
        txtQuantidade.setDisable(!habilitar);
        txtLote.setDisable(!habilitar);
        txtNf.setDisable(!habilitar);
        chkDescartabilidade.setDisable(!habilitar);
        txtUso.setDisable(!habilitar);
    }
    
    private Hospitalar construirHospitalarDoFormulario() {
        Hospitalar hospitalar = new Hospitalar();
        hospitalar.setNome(txtNome.getText());
        hospitalar.setDescricao(txtDescricao.getText());
        hospitalar.setTipo(txtTipo.getText());
        hospitalar.setUnidadeMedida(txtUnidadeMedida.getText());
        hospitalar.setQuantidadeTotal(Integer.parseInt(txtQuantidade.getText()));
        hospitalar.setValidade(LocalDateTime.now().plusYears(2));
        hospitalar.setLote(txtLote.getText());
        hospitalar.setNf(txtNf.getText());
        hospitalar.setDescartabilidade(chkDescartabilidade.isSelected());
        hospitalar.setUso(txtUso.getText());
        return hospitalar;
    }
}
