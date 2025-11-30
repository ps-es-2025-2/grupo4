package br.com.simplehealth.estoque.controller;

import br.com.simplehealth.estoque.model.Alimento;
import br.com.simplehealth.estoque.service.AlimentoService;
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

public class AlimentoController extends AbstractCrudController {
    
    private static final Logger logger = LoggerFactory.getLogger(AlimentoController.class);
    
    @FXML private TableView<Alimento> tableAlimentos;
    @FXML private TableColumn<Alimento, Long> colId;
    @FXML private TableColumn<Alimento, String> colNome;
    @FXML private TableColumn<Alimento, String> colTipo;
    @FXML private TableColumn<Alimento, Integer> colQuantidade;
    @FXML private TableColumn<Alimento, String> colArmazenamento;
    
    @FXML private TextField txtNome;
    @FXML private TextArea txtDescricao;
    @FXML private TextField txtTipo;
    @FXML private TextField txtUnidadeMedida;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtLote;
    @FXML private TextField txtNf;
    @FXML private TextArea txtAlergenicos;
    @FXML private ComboBox<String> cbTipoArmazenamento;
    
    private final AlimentoService service;
    private final ObservableList<Alimento> alimentos;
    private Alimento alimentoSelecionado;
    
    public AlimentoController() {
        this.service = new AlimentoService();
        this.alimentos = FXCollections.observableArrayList();
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
        colId.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidadeTotal"));
        colArmazenamento.setCellValueFactory(new PropertyValueFactory<>("tipoArmazenamento"));
        
        tableAlimentos.setItems(alimentos);
    }
    
    private void setupComboBox() {
        cbTipoArmazenamento.setItems(FXCollections.observableArrayList(
            "Ambiente",
            "Refrigerado",
            "Congelado",
            "Seco",
            "Climatizado"
        ));
    }
    
    private void setupTableSelection() {
        tableAlimentos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    preencherFormulario(newSelection);
                    alimentoSelecionado = newSelection;
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
            mostrarErro("Erro", "Erro ao carregar alimentos: " + e.getMessage());
        }
    }
    
    private void preencherFormulario(Alimento alimento) {
        txtNome.setText(alimento.getNome());
        txtDescricao.setText(alimento.getDescricao());
        txtTipo.setText(alimento.getTipo());
        txtUnidadeMedida.setText(alimento.getUnidadeMedida());
        txtQuantidade.setText(String.valueOf(alimento.getQuantidadeTotal()));
        txtLote.setText(alimento.getLote());
        txtNf.setText(alimento.getNf());
        txtAlergenicos.setText(alimento.getAlergenicos());
        cbTipoArmazenamento.setValue(alimento.getTipoArmazenamento());
    }
    
    @FXML
    private void handleNovo() {
        limparFormulario();
        alimentoSelecionado = null;
    }
    
    @FXML
    private void handleSalvar() {
        try {
            if (!validarCampos()) return;
            
            Alimento alimento = alimentoSelecionado != null ? 
                alimentoSelecionado : new Alimento();
            
            alimento.setNome(txtNome.getText());
            alimento.setDescricao(txtDescricao.getText());
            alimento.setTipo(txtTipo.getText());
            alimento.setUnidadeMedida(txtUnidadeMedida.getText());
            alimento.setQuantidadeTotal(Integer.parseInt(txtQuantidade.getText()));
            alimento.setValidade(LocalDateTime.now().plusMonths(6)); // Default 6 meses
            alimento.setLote(txtLote.getText());
            alimento.setNf(txtNf.getText());
            alimento.setAlergenicos(txtAlergenicos.getText());
            alimento.setTipoArmazenamento(cbTipoArmazenamento.getValue());
            
            if (alimentoSelecionado != null && alimentoSelecionado.getIdItem() != null) {
                service.atualizar(alimentoSelecionado.getIdItem(), alimento);
                mostrarSucesso("Sucesso", "Alimento atualizado com sucesso!");
            } else {
                service.salvar(alimento);
                mostrarSucesso("Sucesso", "Alimento cadastrado com sucesso!");
            }
            
            carregarDados();
            limparFormulario();
            RefreshManager.getInstance().notifyRefresh("Alimento");
            
        } catch (Exception e) {
            logger.error("Erro ao salvar alimento", e);
            mostrarErro("Erro", "Erro ao salvar: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeletar() {
        if (alimentoSelecionado == null) {
            mostrarErro("Atenção", "Selecione um alimento para deletar");
            return;
        }
        
        if (mostrarConfirmacao("Confirmar Exclusão", 
            "Deseja realmente excluir o alimento: " + alimentoSelecionado.getNome() + "?")) {
            try {
                service.deletar(alimentoSelecionado.getIdItem());
                mostrarSucesso("Sucesso", "Alimento deletado com sucesso!");
                carregarDados();
                limparFormulario();
                RefreshManager.getInstance().notifyRefresh("Alimento");
            } catch (Exception e) {
                logger.error("Erro ao deletar alimento", e);
                mostrarErro("Erro", "Erro ao deletar: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleAtualizar() {
        carregarDados();
    }
    
    private boolean validarCampos() {
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
        
        // Validar tipo de armazenamento obrigatório
        if (cbTipoArmazenamento.getValue() == null) {
            mostrarErro("Validação", ValidationUtils.mensagemCampoObrigatorio("Tipo de Armazenamento"));
            return false;
        }
        
        // Validar lote (se preenchido)
        if (!txtLote.getText().trim().isEmpty() && 
            !ValidationUtils.validarLote(txtLote.getText())) {
            mostrarErro("Validação", ValidationUtils.mensagemFormatoInvalido("Lote", "3-20 caracteres alfanuméricos"));
            return false;
        }
        
        // Validar nota fiscal (se preenchida)
        if (!txtNf.getText().trim().isEmpty() && 
            !ValidationUtils.validarNotaFiscal(txtNf.getText())) {
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
        txtAlergenicos.clear();
        cbTipoArmazenamento.setValue(null);
        alimentoSelecionado = null;
        tableAlimentos.getSelectionModel().clearSelection();
    }
}
