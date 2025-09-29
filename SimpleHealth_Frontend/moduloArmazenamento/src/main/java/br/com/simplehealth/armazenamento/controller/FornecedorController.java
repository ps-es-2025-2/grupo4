package br.com.simplehealth.armazenamento.controller;

import br.com.simplehealth.armazenamento.model.Fornecedor;
import br.com.simplehealth.armazenamento.service.FornecedorService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Controlador para gerenciamento de fornecedores.
 * 
 * @version 1.0
 */
public class FornecedorController extends AbstractCrudController<Fornecedor, br.com.simplehealth.armazenamento.view.Fornecedor, Long> {

    private static final Logger logger = LoggerFactory.getLogger(FornecedorController.class);

    private final FornecedorService fornecedorService;

    // Campos da interface
    @FXML private TextField nomeField;
    @FXML private TextField cnpjField;
    @FXML private TextField contatoField;
    @FXML private TextArea enderecoField;

    // Colunas da tabela
    @FXML private TableColumn<br.com.simplehealth.armazenamento.view.Fornecedor, Long> idColumn;
    @FXML private TableColumn<br.com.simplehealth.armazenamento.view.Fornecedor, String> nomeColumn;
    @FXML private TableColumn<br.com.simplehealth.armazenamento.view.Fornecedor, String> cnpjColumn;
    @FXML private TableColumn<br.com.simplehealth.armazenamento.view.Fornecedor, String> contatoColumn;
    @FXML private TableColumn<br.com.simplehealth.armazenamento.view.Fornecedor, String> enderecoColumn;

    public FornecedorController() {
        this.fornecedorService = new FornecedorService();
    }

    @Override
    public void initialize() {
        super.initialize();
        configurarColunas();
    }

    private void configurarColunas() {
        if (idColumn != null) idColumn.setCellValueFactory(cellData -> cellData.getValue().idFornecedorProperty().asObject());
        if (nomeColumn != null) nomeColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        if (cnpjColumn != null) cnpjColumn.setCellValueFactory(cellData -> cellData.getValue().cnpjProperty());
        if (contatoColumn != null) contatoColumn.setCellValueFactory(cellData -> cellData.getValue().contatoProperty());
        if (enderecoColumn != null) enderecoColumn.setCellValueFactory(cellData -> cellData.getValue().enderecoProperty());
    }

    @Override
    protected List<Fornecedor> buscarTodosViaApi() throws IOException {
        return fornecedorService.buscarTodos();
    }

    @Override
    protected Fornecedor buscarPorIdViaApi(Long id) throws IOException {
        return fornecedorService.buscarPorId(id);
    }

    @Override
    protected Fornecedor criarViaApi(Fornecedor entidade) throws IOException {
        return fornecedorService.criar(entidade);
    }

    @Override
    protected Fornecedor atualizarViaApi(Long id, Fornecedor entidade) throws IOException {
        return fornecedorService.atualizar(id, entidade);
    }

    @Override
    protected boolean deletarViaApi(Long id) throws IOException {
        return fornecedorService.deletar(id);
    }

    @Override
    protected br.com.simplehealth.armazenamento.view.Fornecedor modelToView(Fornecedor entidade) {
        return new br.com.simplehealth.armazenamento.view.Fornecedor(
            entidade.getIdFornecedor(),
            entidade.getNome(),
            entidade.getCnpj(),
            entidade.getContato(),
            entidade.getEndereco()
        );
    }

    @Override
    protected Fornecedor viewToModel(br.com.simplehealth.armazenamento.view.Fornecedor viewModel) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdFornecedor(viewModel.getIdFornecedor());
        fornecedor.setNome(viewModel.getNome());
        fornecedor.setCnpj(viewModel.getCnpj());
        fornecedor.setContato(viewModel.getContato());
        fornecedor.setEndereco(viewModel.getEndereco());
        return fornecedor;
    }

    @Override
    protected void preencherCampos(br.com.simplehealth.armazenamento.view.Fornecedor item) {
        if (nomeField != null) nomeField.setText(item.getNome());
        if (cnpjField != null) cnpjField.setText(item.getCnpj());
        if (contatoField != null) contatoField.setText(item.getContato());
        if (enderecoField != null) enderecoField.setText(item.getEndereco());
    }

    @Override
    protected void limparCampos() {
        if (nomeField != null) nomeField.clear();
        if (cnpjField != null) cnpjField.clear();
        if (contatoField != null) contatoField.clear();
        if (enderecoField != null) enderecoField.clear();
    }

    @Override
    protected void desabilitarCampos(boolean desabilitado) {
        if (nomeField != null) nomeField.setDisable(desabilitado);
        if (cnpjField != null) cnpjField.setDisable(desabilitado);
        if (contatoField != null) contatoField.setDisable(desabilitado);
        if (enderecoField != null) enderecoField.setDisable(desabilitado);
    }

    @Override
    protected Long getIdFromViewModel(br.com.simplehealth.armazenamento.view.Fornecedor viewModel) {
        return viewModel.getIdFornecedor();
    }

    @Override
    protected boolean validarCampos() {
        return nomeField != null && !nomeField.getText().trim().isEmpty() &&
               cnpjField != null && !cnpjField.getText().trim().isEmpty() &&
               contatoField != null && !contatoField.getText().trim().isEmpty();
    }

    @Override
    protected Fornecedor criarEntidadeDosCampos() {
        Fornecedor fornecedor = new Fornecedor();
        
        if (nomeField != null) fornecedor.setNome(nomeField.getText().trim());
        if (cnpjField != null) fornecedor.setCnpj(cnpjField.getText().trim());
        if (contatoField != null) fornecedor.setContato(contatoField.getText().trim());
        if (enderecoField != null) fornecedor.setEndereco(enderecoField.getText().trim());
        
        return fornecedor;
    }

    @Override
    public void atualizarSelectsEComboBoxes() {
        // FornecedorController não possui ComboBoxes que dependem de dados externos
        // Apenas log para debug
        logger.debug("FornecedorController: Não possui ComboBoxes para atualizar");
    }
}