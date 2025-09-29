package br.com.simplehealth.armazenamento.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador abstrato que implementa a lógica CRUD (Create, Read, Update, Delete)
 * com validação genérica de campos e atualização automática entre telas.
 * Adaptado para consumir APIs REST ao invés de usar repositórios locais.
 *
 * @param <E>  O tipo da entidade do modelo (ex: Item, Fornecedor).
 * @param <V>  O tipo do ViewModel para a tabela (ex: view.Item, view.Fornecedor).
 * @param <ID> O tipo do ID da entidade (ex: Long).
 * @version 1.0
 */
public abstract class AbstractCrudController<E, V, ID> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCrudController.class);

    /**
     * Lista estática que rastreia todas as instâncias ativas de controladores CRUD.
     * Usada para notificar todas as telas sobre atualizações de dados.
     */
    private static final List<AbstractCrudController<?, ?, ?>> activeControllers = new ArrayList<>();

    /**
     * Enum para controlar a ação pendente do usuário (Criar, Atualizar, Deletar).
     */
    private enum Action { NONE, NOVO, ATUALIZAR, DELETAR }
    private Action pendingAction = Action.NONE;

    @FXML protected TableView<V> tabela;
    @FXML protected Button novoButton;
    @FXML protected Button atualizarButton;
    @FXML protected Button deletarButton;
    @FXML protected Button confirmarButton;
    @FXML protected Button cancelarButton;
    @FXML protected Text confirmacaoText;

    // --- MÉTODOS ABSTRATOS (a serem implementados pelas classes filhas) ---

    /**
     * Busca todas as entidades via API.
     *
     * @return Lista de entidades.
     * @throws IOException Se houver erro na comunicação com a API.
     */
    protected abstract List<E> buscarTodosViaApi() throws IOException;

    /**
     * Busca uma entidade por ID via API.
     *
     * @param id O ID da entidade.
     * @return A entidade encontrada ou null.
     * @throws IOException Se houver erro na comunicação com a API.
     */
    protected abstract E buscarPorIdViaApi(ID id) throws IOException;

    /**
     * Cria uma nova entidade via API.
     *
     * @param entidade A entidade a ser criada.
     * @return A entidade criada.
     * @throws IOException Se houver erro na comunicação com a API.
     */
    protected abstract E criarViaApi(E entidade) throws IOException;

    /**
     * Atualiza uma entidade via API.
     *
     * @param id O ID da entidade.
     * @param entidade A entidade com os novos dados.
     * @return A entidade atualizada.
     * @throws IOException Se houver erro na comunicação com a API.
     */
    protected abstract E atualizarViaApi(ID id, E entidade) throws IOException;

    /**
     * Deleta uma entidade via API.
     *
     * @param id O ID da entidade.
     * @return true se deletado com sucesso.
     * @throws IOException Se houver erro na comunicação com a API.
     */
    protected abstract boolean deletarViaApi(ID id) throws IOException;

    /**
     * Converte um objeto do modelo para um objeto do view model.
     *
     * @param entidade A entidade do modelo.
     * @return O view model.
     */
    protected abstract V modelToView(E entidade);

    /**
     * Converte um objeto do view model para um objeto do modelo.
     *
     * @param viewModel O view model.
     * @return O modelo.
     */
    protected abstract E viewToModel(V viewModel);

    /**
     * Preenche os campos da tela com os dados de um item.
     *
     * @param item O item a ser exibido.
     */
    protected abstract void preencherCampos(V item);

    /**
     * Limpa todos os campos da tela.
     */
    protected abstract void limparCampos();

    /**
     * Habilita ou desabilita os campos da tela.
     *
     * @param desabilitado `true` para desabilitar, `false` para habilitar.
     */
    protected abstract void desabilitarCampos(boolean desabilitado);

    /**
     * Retorna o ID de um view model.
     *
     * @param viewModel O view model.
     * @return O ID.
     */
    protected abstract ID getIdFromViewModel(V viewModel);

    /**
     * Valida se os campos obrigatórios estão preenchidos.
     *
     * @return `true` se todos os campos obrigatórios estão preenchidos.
     */
    protected abstract boolean validarCampos();

    /**
     * Cria uma nova entidade com os dados dos campos da tela.
     *
     * @return A nova entidade.
     */
    protected abstract E criarEntidadeDosCampos();

    // --- MÉTODOS PÚBLICOS ---

    /**
     * Inicializa o controlador. Deve ser chamado após o carregamento do FXML.
     */
    @FXML
    public void initialize() {
        activeControllers.add(this);
        configurarTabela();
        configurarBotoes();
        carregarDados();
    }

    /**
     * Manipula o clique no botão "Novo".
     */
    @FXML
    protected void onNovoClick() {
        pendingAction = Action.NOVO;
        limparCampos();
        desabilitarCampos(false);
        configurarBotoesParaEdicao(true);
        mostrarConfirmacao("Preencha os campos e clique em 'Confirmar' para criar um novo registro.");
    }

    /**
     * Manipula o clique no botão "Atualizar".
     */
    @FXML
    protected void onAtualizarClick() {
        V selectedItem = tabela.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            mostrarAlerta("Seleção necessária", "Por favor, selecione um item para atualizar.");
            return;
        }

        pendingAction = Action.ATUALIZAR;
        preencherCampos(selectedItem);
        desabilitarCampos(false);
        configurarBotoesParaEdicao(true);
        mostrarConfirmacao("Modifique os campos desejados e clique em 'Confirmar' para salvar as alterações.");
    }

    /**
     * Manipula o clique no botão "Deletar".
     */
    @FXML
    protected void onDeletarClick() {
        V selectedItem = tabela.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            mostrarAlerta("Seleção necessária", "Por favor, selecione um item para deletar.");
            return;
        }

        pendingAction = Action.DELETAR;
        preencherCampos(selectedItem);
        desabilitarCampos(true);
        configurarBotoesParaEdicao(true);
        mostrarConfirmacao("Tem certeza que deseja deletar este registro? Clique em 'Confirmar' para continuar.");
    }

    /**
     * Manipula o clique no botão "Confirmar".
     */
    @FXML
    protected void onConfirmarClick() {
        try {
            switch (pendingAction) {
                case NOVO -> confirmarNovo();
                case ATUALIZAR -> confirmarAtualizar();
                case DELETAR -> confirmarDeletar();
                default -> mostrarAlerta("Erro", "Nenhuma ação pendente.");
            }
        } catch (IOException e) {
            logger.error("Erro ao executar operação: ", e);
            mostrarAlerta("Erro de Comunicação", "Erro ao comunicar com o servidor: " + e.getMessage());
        }
    }

    /**
     * Manipula o clique no botão "Cancelar".
     */
    @FXML
    protected void onCancelarClick() {
        pendingAction = Action.NONE;
        limparCampos();
        desabilitarCampos(true);
        configurarBotoesParaEdicao(false);
        ocultarConfirmacao();
    }

    // --- MÉTODOS PRIVADOS ---

    private void configurarTabela() {
        if (tabela != null) {
            tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null && pendingAction == Action.NONE) {
                    preencherCampos(newSelection);
                }
            });
        }
    }

    private void configurarBotoes() {
        configurarBotoesParaEdicao(false);
        desabilitarCampos(true);
    }

    private void configurarBotoesParaEdicao(boolean editando) {
        if (novoButton != null) novoButton.setDisable(editando);
        if (atualizarButton != null) atualizarButton.setDisable(editando);
        if (deletarButton != null) deletarButton.setDisable(editando);
        if (confirmarButton != null) confirmarButton.setDisable(!editando);
        if (cancelarButton != null) cancelarButton.setDisable(!editando);
    }

    private void carregarDados() {
        try {
            List<E> entidades = buscarTodosViaApi();
            ObservableList<V> viewModels = FXCollections.observableArrayList();
            
            for (E entidade : entidades) {
                viewModels.add(modelToView(entidade));
            }
            
            if (tabela != null) {
                tabela.setItems(viewModels);
            }
            
        } catch (IOException e) {
            logger.error("Erro ao carregar dados: ", e);
            mostrarAlerta("Erro", "Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void confirmarNovo() throws IOException {
        if (!validarCampos()) {
            mostrarAlerta("Campos obrigatórios", "Por favor, preencha todos os campos obrigatórios.");
            return;
        }

        E novaEntidade = criarEntidadeDosCampos();
        E entidadeCriada = criarViaApi(novaEntidade);
        
        if (entidadeCriada != null) {
            mostrarSucesso("Registro criado com sucesso!");
            resetarTela();
            carregarDados();
            notificarOutrasTelasSobreAtualizacao();
        }
    }

    private void confirmarAtualizar() throws IOException {
        if (!validarCampos()) {
            mostrarAlerta("Campos obrigatórios", "Por favor, preencha todos os campos obrigatórios.");
            return;
        }

        V selectedItem = tabela.getSelectionModel().getSelectedItem();
        ID id = getIdFromViewModel(selectedItem);
        E entidadeAtualizada = criarEntidadeDosCampos();
        
        E resultado = atualizarViaApi(id, entidadeAtualizada);
        
        if (resultado != null) {
            mostrarSucesso("Registro atualizado com sucesso!");
            resetarTela();
            carregarDados();
            notificarOutrasTelasSobreAtualizacao();
        }
    }

    private void confirmarDeletar() throws IOException {
        V selectedItem = tabela.getSelectionModel().getSelectedItem();
        ID id = getIdFromViewModel(selectedItem);
        
        boolean sucesso = deletarViaApi(id);
        
        if (sucesso) {
            mostrarSucesso("Registro deletado com sucesso!");
            resetarTela();
            carregarDados();
            notificarOutrasTelasSobreAtualizacao();
        } else {
            mostrarAlerta("Erro", "Não foi possível deletar o registro.");
        }
    }

    private void resetarTela() {
        pendingAction = Action.NONE;
        limparCampos();
        desabilitarCampos(true);
        configurarBotoesParaEdicao(false);
        ocultarConfirmacao();
    }

    private void mostrarConfirmacao(String mensagem) {
        if (confirmacaoText != null) {
            confirmacaoText.setText(mensagem);
            confirmacaoText.setVisible(true);
        }
    }

    private void ocultarConfirmacao() {
        if (confirmacaoText != null) {
            confirmacaoText.setVisible(false);
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void notificarOutrasTelasSobreAtualizacao() {
        for (AbstractCrudController<?, ?, ?> controller : activeControllers) {
            if (controller != this) {
                controller.carregarDados();
            }
        }
    }

    /**
     * Remove este controlador da lista de controladores ativos.
     * Deve ser chamado quando a tela é fechada.
     */
    public void fecharTela() {
        activeControllers.remove(this);
    }
}