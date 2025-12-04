# Instruções para Atualização dos Controllers

## Resumo das Alterações

Os FXMLs foram atualizados com novo padrão de botões:
- **Botões de Ação:** Criar, Alterar, Deletar
- **Botões de Confirmação:** Confirmar, Cancelar

## Cores por Módulo

- **Cadastro:** `#2196F3` (Azul)
- **Agendamento:** `#9C27B0` (Roxo)
- **Estoque:** `#FF9800` (Laranja)

## Alterações Necessárias nos Controllers

### 1. Atualizar Declaração dos Botões FXML

**ANTES:**
```java
@FXML
private Button btnSalvar;
@FXML
private Button btnAtualizar;
@FXML
private Button btnDeletar;
@FXML
private Button btnLimpar;
```

**DEPOIS:**
```java
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
```

### 2. Renomear Métodos de Handler

**ANTES:**
```java
@FXML
private void handleSalvar() { ... }

@FXML
private void handleAtualizar() { ... }

@FXML
private void handleLimpar() { ... }
```

**DEPOIS:**
```java
@FXML
private void handleCriar() { ... }

@FXML
private void handleAlterar() { ... }

@FXML
private void handleConfirmar() { ... }

@FXML
private void handleCancelar() { ... }
```

### 3. Nova Lógica dos Botões

#### handleCriar()
```java
@FXML
private void handleCriar() {
    // Ativa modo de criação
    limparFormulario();
    habilitarCampos(true);
    btnCriar.setDisable(true);
    btnAlterar.setDisable(true);
    btnDeletar.setDisable(true);
    btnConfirmar.setDisable(false);
    modoEdicao = "CRIAR";
}
```

#### handleAlterar()
```java
@FXML
private void handleAlterar() {
    if (itemSelecionado == null) {
        mostrarErro("Erro", "Selecione um item para alterar.");
        return;
    }
    // Ativa modo de alteração
    habilitarCampos(true);
    btnCriar.setDisable(true);
    btnAlterar.setDisable(true);
    btnDeletar.setDisable(true);
    btnConfirmar.setDisable(false);
    modoEdicao = "ALTERAR";
}
```

#### handleDeletar()
```java
@FXML
private void handleDeletar() {
    if (itemSelecionado == null) {
        mostrarErro("Erro", "Selecione um item para deletar.");
        return;
    }
    
    if (!mostrarConfirmacao("Confirmar", "Deseja realmente deletar este item?")) {
        return;
    }
    
    try {
        service.deletar(itemSelecionado.getId());
        mostrarSucesso("Sucesso", "Item deletado com sucesso!");
        limparFormulario();
        carregarDados();
        RefreshManager.notifyRefresh();
    } catch (Exception e) {
        logger.error("Erro ao deletar item", e);
        mostrarErro("Erro", "Não foi possível deletar o item: " + e.getMessage());
    }
}
```

#### handleConfirmar()
```java
@FXML
private void handleConfirmar() {
    if (!validarFormulario()) {
        return;
    }
    
    try {
        if ("CRIAR".equals(modoEdicao)) {
            // Criar novo item
            Item novoItem = construirItemDoFormulario();
            service.criar(novoItem);
            mostrarSucesso("Sucesso", "Item cadastrado com sucesso!");
        } else if ("ALTERAR".equals(modoEdicao)) {
            // Atualizar item existente
            Item itemAtualizado = construirItemDoFormulario();
            service.atualizar(itemSelecionado.getId(), itemAtualizado);
            mostrarSucesso("Sucesso", "Item atualizado com sucesso!");
        }
        
        limparFormulario();
        carregarDados();
        RefreshManager.notifyRefresh();
        resetarBotoes();
    } catch (Exception e) {
        logger.error("Erro ao confirmar operação", e);
        mostrarErro("Erro", "Não foi possível completar a operação: " + e.getMessage());
    }
}
```

#### handleCancelar()
```java
@FXML
private void handleCancelar() {
    limparFormulario();
    resetarBotoes();
    habilitarCampos(false);
    modoEdicao = null;
}
```

### 4. Adicionar Variável de Controle

```java
private String modoEdicao = null; // "CRIAR", "ALTERAR" ou null
```

### 5. Métodos Auxiliares

```java
private void resetarBotoes() {
    btnCriar.setDisable(false);
    btnAlterar.setDisable(itemSelecionado == null);
    btnDeletar.setDisable(itemSelecionado == null);
    btnConfirmar.setDisable(true);
}

private void habilitarCampos(boolean habilitar) {
    // Habilita/desabilita todos os campos de entrada
    txtCampo1.setDisable(!habilitar);
    txtCampo2.setDisable(!habilitar);
    // ... outros campos
}

private Item construirItemDoFormulario() {
    Item item = new Item();
    item.setCampo1(txtCampo1.getText().trim());
    item.setCampo2(txtCampo2.getText().trim());
    // ... outros campos
    return item;
}
```

### 6. Atualizar Listener da Tabela

```java
private void configurarListeners() {
    tabela.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
            if (newValue != null) {
                itemSelecionado = newValue;
                preencherFormulario(newValue);
                if (modoEdicao == null) {
                    btnAlterar.setDisable(false);
                    btnDeletar.setDisable(false);
                }
            } else {
                if (modoEdicao == null) {
                    btnAlterar.setDisable(true);
                    btnDeletar.setDisable(true);
                }
            }
        }
    );
}
```

### 7. Atualizar Initialize

```java
@FXML
public void initialize() {
    configurarTabela();
    carregarDados();
    configurarListeners();
    resetarBotoes();
    habilitarCampos(false);
    
    RefreshManager.addRefreshListener(this::carregarDados);
}
```

## Checklist de Atualização

Para cada controller:

- [ ] Atualizar declarações @FXML dos botões
- [ ] Renomear métodos de handler
- [ ] Adicionar variável `modoEdicao`
- [ ] Implementar nova lógica de `handleCriar()`
- [ ] Implementar nova lógica de `handleAlterar()`
- [ ] Atualizar lógica de `handleDeletar()`
- [ ] Implementar `handleConfirmar()`
- [ ] Implementar `handleCancelar()`
- [ ] Adicionar métodos auxiliares (`resetarBotoes`, `habilitarCampos`, `construirItemDoFormulario`)
- [ ] Atualizar listener da tabela
- [ ] Atualizar método `initialize()`
- [ ] Testar todas as operações CRUD

## Controllers a Atualizar

### Módulo Cadastro
- [ ] ConvenioController
- [ ] MedicoController
- [ ] PacienteController
- [ ] UsuarioController

### Módulo Agendamento
- [ ] ConsultaController
- [ ] ExameController
- [ ] ProcedimentoController
- [ ] BloqueioAgendaController

### Módulo Estoque
- [ ] AlimentoController
- [ ] MedicamentoController
- [ ] FornecedorController
- [ ] HospitalarController
- [ ] EstoqueController
- [ ] PedidoController

## Observações Importantes

1. **Validação:** Todos os métodos `handleConfirmar()` devem chamar `validarFormulario()` antes de prosseguir
2. **Feedback ao Usuário:** Sempre mostrar mensagens de sucesso/erro apropriadas
3. **Refresh:** Chamar `RefreshManager.notifyRefresh()` após operações bem-sucedidas
4. **Estado dos Campos:** Campos devem estar desabilitados por padrão e habilitados apenas durante criação/edição
5. **Confirmação de Exclusão:** Sempre pedir confirmação antes de deletar

## Exemplo Completo

Veja o controller `ConvenioController` como referência para implementação completa do novo padrão.
