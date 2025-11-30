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

public class HospitalarController extends AbstractCrudController {
    
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
    
    private final HospitalarService service;
    private final ObservableList<Hospitalar> hospitalares;
    private Hospitalar hospitalarSelecionado;
    
    public HospitalarController() {
        this.service = new HospitalarService();
        this.hospitalares = FXCollections.observableArrayList();
    }
    
    @FXML
    public void initialize() {
        setupTableColumns();
        carregarDados();
        setupTableSelection();
        setupRefreshListener();
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
                    preencherFormulario(newSelection);
                    hospitalarSelecionado = newSelection;
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
    private void handleNovo() {
        limparFormulario();
        hospitalarSelecionado = null;
    }
    
    @FXML
    private void handleSalvar() {
        try {
            if (!validarCampos()) return;
            
            Hospitalar hospitalar = hospitalarSelecionado != null ? 
                hospitalarSelecionado : new Hospitalar();
            
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
            
            if (hospitalarSelecionado != null && hospitalarSelecionado.getIdItem() != null) {
                service.atualizar(hospitalarSelecionado.getIdItem(), hospitalar);
                mostrarSucesso("Sucesso", "Item hospitalar atualizado com sucesso!");
            } else {
                service.salvar(hospitalar);
                mostrarSucesso("Sucesso", "Item hospitalar cadastrado com sucesso!");
            }
            
            carregarDados();
            limparFormulario();
            RefreshManager.getInstance().notifyRefresh("Hospitalar");
            
        } catch (Exception e) {
            logger.error("Erro ao salvar hospitalar", e);
            mostrarErro("Erro", "Erro ao salvar: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeletar() {
        if (hospitalarSelecionado == null) {
            mostrarErro("Atenção", "Selecione um item hospitalar para deletar");
            return;
        }
        
        if (mostrarConfirmacao("Confirmar Exclusão", 
            "Deseja realmente excluir o item: " + hospitalarSelecionado.getNome() + "?")) {
            try {
                service.deletar(hospitalarSelecionado.getIdItem());
                mostrarSucesso("Sucesso", "Item hospitalar deletado com sucesso!");
                carregarDados();
                limparFormulario();
                RefreshManager.getInstance().notifyRefresh("Hospitalar");
            } catch (Exception e) {
                logger.error("Erro ao deletar hospitalar", e);
                mostrarErro("Erro", "Erro ao deletar: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleAtualizar() {
        carregarDados();
    }
    
    private boolean validarCampos() {
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
        txtNome.clear();
        txtDescricao.clear();
        txtTipo.clear();
        txtUnidadeMedida.clear();
        txtQuantidade.clear();
        txtLote.clear();
        txtNf.clear();
        chkDescartabilidade.setSelected(false);
        txtUso.clear();
        hospitalarSelecionado = null;
        tableHospitalares.getSelectionModel().clearSelection();
    }
}
