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
public class ConvenioController extends AbstractCrudController {

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
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnAtualizar;
    @FXML
    private Button btnDeletar;
    @FXML
    private Button btnLimpar;

    private final ConvenioService convenioService = new ConvenioService();
    private final ObservableList<Convenio> listaConvenios = FXCollections.observableArrayList();
    private Convenio convenioSelecionado;

    @FXML
    public void initialize() {
        configurarTabela();
        carregarDados();
        configurarListeners();
        
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
                    convenioSelecionado = newValue;
                    preencherFormulario(newValue);
                    btnAtualizar.setDisable(false);
                    btnDeletar.setDisable(false);
                } else {
                    btnAtualizar.setDisable(true);
                    btnDeletar.setDisable(true);
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
    private void handleSalvar() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Convenio novoConvenio = new Convenio();
            novoConvenio.setNome(txtNome.getText().trim());
            novoConvenio.setPlano(txtPlano.getText().trim());
            novoConvenio.setAtivo(chkAtivo.isSelected());

            convenioService.criar(novoConvenio);
            mostrarSucesso("Sucesso", "Convênio cadastrado com sucesso!");
            limparFormulario();
            carregarDados();
            RefreshManager.notifyRefresh();
        } catch (Exception e) {
            logger.error("Erro ao criar convênio", e);
            mostrarErro("Erro", "Não foi possível criar o convênio: " + e.getMessage());
        }
    }

    @FXML
    private void handleAtualizar() {
        if (convenioSelecionado == null) {
            mostrarErro("Erro", "Selecione um convênio para atualizar.");
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        try {
            Convenio convenioAtualizado = new Convenio();
            convenioAtualizado.setNome(txtNome.getText().trim());
            convenioAtualizado.setPlano(txtPlano.getText().trim());
            convenioAtualizado.setAtivo(chkAtivo.isSelected());

            convenioService.atualizar(convenioSelecionado.getId(), convenioAtualizado);
            mostrarSucesso("Sucesso", "Convênio atualizado com sucesso!");
            limparFormulario();
            carregarDados();
            RefreshManager.notifyRefresh();
        } catch (Exception e) {
            logger.error("Erro ao atualizar convênio", e);
            mostrarErro("Erro", "Não foi possível atualizar o convênio: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeletar() {
        if (convenioSelecionado == null) {
            mostrarErro("Erro", "Selecione um convênio para deletar.");
            return;
        }

        if (!mostrarConfirmacao("Confirmar", "Deseja realmente deletar o convênio " + 
                convenioSelecionado.getNome() + "?")) {
            return;
        }

        try {
            convenioService.deletar(convenioSelecionado.getId());
            mostrarSucesso("Sucesso", "Convênio deletado com sucesso!");
            limparFormulario();
            carregarDados();
            RefreshManager.notifyRefresh();
        } catch (Exception e) {
            logger.error("Erro ao deletar convênio", e);
            mostrarErro("Erro", "Não foi possível deletar o convênio: " + e.getMessage());
        }
    }

    @FXML
    private void handleLimpar() {
        limparFormulario();
    }

    @Override
    protected void limparFormulario() {
        txtNome.clear();
        txtPlano.clear();
        chkAtivo.setSelected(true); // Padrão: ativo
        convenioSelecionado = null;
        tabelaConvenios.getSelectionModel().clearSelection();
        btnAtualizar.setDisable(true);
        btnDeletar.setDisable(true);
    }

    private boolean validarFormulario() {
        if (!validarCampoNaoVazio(txtNome.getText(), "Nome")) {
            return false;
        }
        if (!validarCampoNaoVazio(txtPlano.getText(), "Plano")) {
            return false;
        }
        return true;
    }
}
