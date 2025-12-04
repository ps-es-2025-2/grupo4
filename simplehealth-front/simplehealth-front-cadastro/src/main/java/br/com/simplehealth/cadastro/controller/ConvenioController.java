package br.com.simplehealth.cadastro.controller;

import br.com.simplehealth.cadastro.model.Convenio;
import br.com.simplehealth.cadastro.service.ConvenioService;
import br.com.simplehealth.cadastro.util.RefreshManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller para o CRUD de Convênios.
 */
public class ConvenioController extends AbstractCrudController<Convenio> {

    @FXML
    private TableView<Convenio> tabelaConvenios;
    @FXML
    private TableColumn<Convenio, Long> colunaId;
    @FXML
    private TableColumn<Convenio, String> colunaNome;
    @FXML
    private TableColumn<Convenio, String> colunaPlano;
    @FXML
    private TableColumn<Convenio, Boolean> colunaAtivo;

    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtPlano;
    @FXML
    private CheckBox chkAtivo;
    
    // Botões - Redeclarados para injeção do JavaFX
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

    private final ConvenioService convenioService = new ConvenioService();
    private final ObservableList<Convenio> listaConvenios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        super.btnCriar = this.btnCriar;
        super.btnAlterar = this.btnAlterar;
        super.btnDeletar = this.btnDeletar;
        super.btnConfirmar = this.btnConfirmar;
        super.btnCancelar = this.btnCancelar;
        configurarTabela();
        carregarDados();
        configurarListeners();
        configurarEstadoInicialBotoes();
        habilitarCampos(false);
        
        RefreshManager.addRefreshListener(this::carregarDados);
    }

    private void configurarTabela() {
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPlano.setCellValueFactory(new PropertyValueFactory<>("plano"));
        colunaAtivo.setCellValueFactory(new PropertyValueFactory<>("ativo"));
        
        // Customizar coluna de status ativo
        colunaAtivo.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Sim" : "Não");
                    setStyle(item ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
                }
            }
        });
        
        tabelaConvenios.setItems(listaConvenios);
    }

    private void configurarListeners() {
        tabelaConvenios.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    itemSelecionado = newValue;
                    preencherFormulario(newValue);
                    habilitarBotoesSelecao();
                }
            }
        );
    }

    @Override
    protected void carregarDados() {
        try {
            listaConvenios.clear();
            listaConvenios.addAll(convenioService.listarTodos());
        } catch (Exception e) {
            logger.error("Erro ao carregar convênios", e);
            mostrarErro("Erro", "Não foi possível carregar os convênios: " + e.getMessage());
        }
    }

    private void preencherFormulario(Convenio convenio) {
        txtNome.setText(convenio.getNome());
        txtPlano.setText(convenio.getPlano());
        chkAtivo.setSelected(convenio.getAtivo() != null && convenio.getAtivo());
    }

    @FXML
    private void handleCriar() {
        limparFormulario();
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "CRIAR";
    }

    @FXML
    private void handleAlterar() {
        if (itemSelecionado == null) {
            mostrarErro("Erro", "Selecione um convênio para alterar.");
            return;
        }
        
        habilitarCampos(true);
        ativarModoEdicao();
        modoEdicao = "ALTERAR";
    }

    @FXML
    private void handleDeletar() {
        if (itemSelecionado == null) {
            mostrarErro("Erro", "Selecione um convênio para deletar.");
            return;
        }

        if (!mostrarConfirmacao("Confirmar", "Deseja realmente deletar o convênio " + 
                itemSelecionado.getNome() + "?")) {
            return;
        }

        try {
            convenioService.deletar(itemSelecionado.getId());
            mostrarSucesso("Sucesso", "Convênio deletado com sucesso!");
            limparFormulario();
            carregarDados();
            RefreshManager.notifyRefresh();
            resetarBotoes();
        } catch (Exception e) {
            logger.error("Erro ao deletar convênio", e);
            mostrarErro("Erro", "Não foi possível deletar o convênio: " + e.getMessage());
        }
    }

    @FXML
    private void handleConfirmar() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Convenio convenio = construirConvenioDoFormulario();
            
            if ("CRIAR".equals(modoEdicao)) {
                convenioService.criar(convenio);
                mostrarSucesso("Sucesso", "Convênio cadastrado com sucesso!");
            } else if ("ALTERAR".equals(modoEdicao)) {
                convenioService.atualizar(itemSelecionado.getId(), convenio);
                mostrarSucesso("Sucesso", "Convênio atualizado com sucesso!");
            }

            limparFormulario();
            carregarDados();
            RefreshManager.notifyRefresh();
            resetarBotoes();
            habilitarCampos(false);
            modoEdicao = null;
        } catch (Exception e) {
            logger.error("Erro ao confirmar operação", e);
            mostrarErro("Erro", "Não foi possível completar a operação: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelar() {
        limparFormulario();
        resetarBotoes();
        habilitarCampos(false);
        modoEdicao = null;
    }

    @Override
    protected void limparFormulario() {
        txtNome.clear();
        txtPlano.clear();
        chkAtivo.setSelected(true);
        itemSelecionado = null;
        tabelaConvenios.getSelectionModel().clearSelection();
    }

    @Override
    protected void habilitarCampos(boolean habilitar) {
        txtNome.setDisable(!habilitar);
        txtPlano.setDisable(!habilitar);
        chkAtivo.setDisable(!habilitar);
    }

    @Override
    protected boolean validarFormulario() {
        if (!validarCampoNaoVazio(txtNome.getText(), "Nome")) {
            return false;
        }
        if (!validarCampoNaoVazio(txtPlano.getText(), "Plano")) {
            return false;
        }
        return true;
    }

    private Convenio construirConvenioDoFormulario() {
        Convenio convenio = new Convenio();
        convenio.setNome(txtNome.getText().trim());
        convenio.setPlano(txtPlano.getText().trim());
        convenio.setAtivo(chkAtivo.isSelected());
        return convenio;
    }
}
