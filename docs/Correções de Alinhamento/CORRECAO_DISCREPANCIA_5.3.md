# Correção da Discrepância 5.3: UC10 - Controlar Validade de Itens

## 1. Resumo da Discrepância

**Tipo**: Redução de Escopo - Implementação Parcial

**Descrição**: O UC10 (Controlar Validade de Itens) está parcialmente implementado. A funcionalidade de consulta de itens por validade existe e funciona via endpoint REST `/validade`, mas a persistência de movimentações de descarte em banco de dados não foi implementada (apenas logs no console). Não há integração com Módulo Financeiro nem alertas automáticos via Redis Pub/Sub.

**Impacto**: 
- ⚠️ **Funcionalidade Core Operacional** (busca de itens por validade)
- ❌ **Rastreabilidade de Descarte** (movimentações não persistidas)
- ❌ **Integração Financeira** (código de custo não registrado)
- ❌ **Alertas Automáticos** (sem Redis Pub/Sub)

---

## 2. Análise da Implementação no Backend

### 2.1. Estrutura Implementada

**Localização**: `simplehealth-back/simplehealth-back-estoque/src/main/java/com/simplehealth/estoque/`

**Arquivos Chave**:

1. **ControlarValidadeUseCase.java** (`application/usecases/`)
   - ✅ Implementado com lógica completa
   - ✅ Busca itens por dias de antecedência
   - ✅ Filtra itens vencidos/próximos da validade
   - ❌ **Movimentação NÃO persistida**

2. **ControleValidadeDTO.java** (`application/dto/`)
   - ✅ Campos: `diasAntecedencia`, `incluirVencidos`, `descartarItens`, `codigoCusto`
   - ✅ Validação de código de custo obrigatório para descarte

3. **EstoqueController.java** (`web/controllers/`)
   - ✅ Endpoint REST: `POST /validade`
   - ✅ Chama `controlarValidadeUseCase.execute(dto)`

### 2.2. Código do ControlarValidadeUseCase

```java
@Component
@RequiredArgsConstructor
public class ControlarValidadeUseCase {

  private final ItemService itemService;

  public List<Item> execute(ControleValidadeDTO dto) {
    // Defaults
    if (dto.getDiasAntecedencia() == null) {
      dto.setDiasAntecedencia(30); // Default: 30 dias
    }
    if (dto.getIncluirVencidos() == null) {
      dto.setIncluirVencidos(false);
    }
    if (dto.getDescartarItens() == null) {
      dto.setDescartarItens(false);
    }

    // Validação RN-ESTOQUE.4
    if (dto.getDescartarItens() && 
        (dto.getCodigoCusto() == null || dto.getCodigoCusto().trim().isEmpty())) {
      throw new IllegalArgumentException(
          "Código de custo é obrigatório para descarte de itens (RN-ESTOQUE.4).");
    }

    // Busca itens por validade
    List<Item> itensParaControle = buscarItensPorValidade(
        dto.getDiasAntecedencia(),
        dto.getIncluirVencidos());

    if (itensParaControle.isEmpty()) {
      System.out.println("Nenhum item encontrado no critério de validade.");
      return itensParaControle;
    }

    // Descarte (se solicitado)
    if (dto.getDescartarItens()) {
      descartarItens(itensParaControle, dto.getCodigoCusto());
    }

    return itensParaControle;
  }

  private List<Item> buscarItensPorValidade(int diasAntecedencia, boolean incluirVencidos) {
    List<Item> todosItens = itemService.listarTodos();
    Date hoje = new Date();
    List<Item> resultado = new ArrayList<>();
    int itensSemValidade = 0;

    for (Item item : todosItens) {
      // Itens sem validade são ignorados
      if (item.getValidade() == null) {
        itensSemValidade++;
        continue;
      }

      long diff = item.getValidade().getTime() - hoje.getTime();
      long dias = diff / (1000 * 60 * 60 * 24);

      if ((incluirVencidos && dias < 0) || (dias >= 0 && dias <= diasAntecedencia)) {
        resultado.add(item);
      }
    }

    if (itensSemValidade > 0) {
      System.out.println("AVISO: " + itensSemValidade + 
          " item(ns) sem data de validade foram ignorados.");
    }

    return resultado;
  }

  private void descartarItens(List<Item> itens, String codigoCusto) {
    Date dataAtual = new Date();
    int totalDescartado = 0;

    System.out.println("=== DESCARTE DE ITENS ===");
    System.out.println("Código de Custo: " + codigoCusto + " | Data: " + dataAtual);

    for (Item item : itens) {
      int qtde = item.getQuantidadeTotal() != null ? item.getQuantidadeTotal() : 0;
      if (qtde <= 0) {
        continue;
      }

      // ❌ PROBLEMA: Zera quantidade mas NÃO persiste movimentação
      item.setQuantidadeTotal(0);
      itemService.salvar(item);

      // ❌ PROBLEMA: Apenas log no console
      registrarMovimentacaoDescarte(item, qtde, codigoCusto, dataAtual);

      totalDescartado++;
    }

    System.out.println("Total de itens descartados: " + totalDescartado);
    System.out.println("=== FIM DESCARTE ===\n");
  }

  // ❌ PROBLEMA: Método NÃO persiste em banco de dados
  private void registrarMovimentacaoDescarte(Item item, int quantidade, 
                                            String codigoCusto, Date data) {
    System.out.println(
        "  [DESCARTE] Item: " + item.getNome() +
            " | Qtd: " + quantidade +
            " | Código de Custo: " + codigoCusto +
            " | Validade: " + item.getValidade());
  }
}
```

### 2.3. Análise de Gaps de Implementação

| Funcionalidade | Status | Observação |
|----------------|--------|------------|
| **Busca por Validade** | ✅ **Implementado** | Método `buscarItensPorValidade()` funciona corretamente |
| **Filtro por Dias** | ✅ **Implementado** | Configurable via `diasAntecedencia` (default 30) |
| **Incluir Vencidos** | ✅ **Implementado** | Flag `incluirVencidos` permite filtrar itens já vencidos |
| **Validação Código Custo** | ✅ **Implementado** | RN-ESTOQUE.4 valida obrigatoriedade do código |
| **Zerar Quantidade** | ✅ **Implementado** | `item.setQuantidadeTotal(0)` atualiza estoque |
| **Persistir Movimentação** | ❌ **NÃO Implementado** | `registrarMovimentacaoDescarte()` apenas imprime no console |
| **Integração Financeira** | ❌ **NÃO Implementado** | Módulo Financeiro não existe no projeto |
| **Alertas Automáticos** | ❌ **NÃO Implementado** | Sem Redis publisher/subscriber para alertas de validade |
| **Endpoint REST** | ✅ **Implementado** | `POST /validade` disponível |

### 2.4. Comparação com UC05 (Dar Baixa em Insumos)

**UC05** possui persistência completa de movimentações:

```java
// UC05: DarBaixaInsumosUseCase.java
private void registrarMovimentacao(Item item, int quantidade, String consulta) {
  Movimentacao mov = new Movimentacao();
  mov.setItemId(item.getId());
  mov.setTipo("SAIDA");
  mov.setQuantidade(quantidade);
  mov.setMotivo("BAIXA_CONSULTA");
  mov.setReferencia(consulta);
  mov.setData(new Date());
  
  movimentacaoRepository.save(mov); // ✅ Persistido em Cassandra
}
```

**UC10** **NÃO** possui esta persistência:

```java
// UC10: ControlarValidadeUseCase.java
private void registrarMovimentacaoDescarte(Item item, int quantidade, 
                                          String codigoCusto, Date data) {
  System.out.println("[DESCARTE] Item: " + item.getNome() + 
                    " | Qtd: " + quantidade); // ❌ Apenas log
  // NÃO há movimentacaoRepository.save()
}
```

**Conclusão**: UC10 foi implementado com funcionalidade de consulta mas **sem rastreabilidade** de movimentações de descarte.

---

## 3. Análise da Documentação

### 3.1. O que a Documentação Descreve

**3.3. Descrição detalhada de cada Caso de Uso** (linhas 420-464):

```markdown
## UC10: Controlar Validade de Itens

**Pós-condições**:
- É gerado um relatório ou lista de itens vencidos/próximos.
- O saldo de Itens descartados é deduzido da quantidadeTotal do Estoque 
  (Baixa por Descarte).

**Fluxo Básico**:
1. O Gestor acessa a função de Controle de Validade.
2. O usuário informa o critério de busca (ex: Vence nos Próximos 30 dias).
3. O Sistema busca todos os Itens no Estoque que atendem ao critério.
4. O Sistema exibe a lista, detalhando nome, validade, lote e quantidade.
5. O Gestor seleciona os Itens que serão descartados e confirma a ação.
6. O Sistema registra a saída por descarte e atualiza o saldo do Estoque.
7. O caso de uso é encerrado.

**Regras de Negócio**:
- (RN-ESTOQUE.3) Itens vencidos (validade < data atual) devem ser removidos 
  do saldo disponível para Baixa de Insumos (UC05).
- (RN-ESTOQUE.4) A baixa por descarte deve ser registrada com um código de 
  custo (Módulo Financeiro).
```

**3.9. Modelagem de Interações** (linha 754+):

```plantuml
' Passo 6: O Sistema registra a saída por descarte
loop para cada item em listaItensParaDescarte
  ' Reutiliza o método `darBaixa` do UC05, mas com motivo "Descarte"
  Service -> Repository: darBaixa(item.id, item.quantidade, "Descarte")
  activate Repository
  Repository --> Service: return
  deactivate Repository
end
```

### 3.2. Discrepâncias Identificadas

| Item Documentado | Status Real | Discrepância |
|------------------|-------------|--------------|
| "Sistema registra a saída por descarte" | ❌ Não persiste | **Documentação descreve persistência que não existe** |
| "Analogia ao UC05" | ❌ Não reutiliza | **UC05 tem persistência, UC10 não** |
| "Baixa por descarte com código de custo (RN-ESTOQUE.4)" | ⚠️ Código validado mas não registrado | **Validação existe, registro não** |
| "Módulo Financeiro" | ❌ Não existe | **Módulo não foi implementado** |
| "darBaixa(item.id, quantidade, 'Descarte')" no diagrama | ❌ Não existe | **Método não é chamado** |

### 3.3. Integração com UC07 (Gerar Alerta de Estoque Crítico)

**Documentação UC07** menciona UC10:

```markdown
(RN-ALERTA.2) Itens com validade próxima (UC10) também devem ser 
considerados no cálculo de estoque crítico. [NÃO IMPLEMENTADO]
```

**Análise**:
- ✅ UC10 consegue listar itens com validade próxima
- ❌ UC07 não está implementado (0% funcional - veja CORRECAO_DISCREPANCIA_5.1.md)
- ❌ Não há integração entre UC10 e UC07 via Redis Pub/Sub

---

## 4. Impacto da Redução de Escopo

### 4.1. Funcionalidades Comprometidas

1. **Rastreabilidade de Descarte** ❌
   - **Problema**: Movimentações de descarte não são armazenadas
   - **Consequência**: Impossível auditar/consultar histórico de descartes
   - **Impacto**: Perda de rastreabilidade para regulamentações sanitárias
   - **Workaround**: Logs no console (não persistentes, perdidos após restart)

2. **Integração com Módulo Financeiro** ❌
   - **Problema**: Módulo Financeiro não existe no projeto
   - **Consequência**: RN-ESTOQUE.4 não pode ser cumprida (código de custo não registrado)
   - **Impacto**: Sem lançamento contábil de descartes (prejuízo financeiro não registrado)
   - **Workaround**: Validação manual de código de custo no DTO (apenas validação, sem uso)

3. **Alertas Automáticos de Validade** ❌
   - **Problema**: Sem Redis Pub/Sub para alertas de validade próxima
   - **Consequência**: Gestor precisa consultar manualmente via endpoint `/validade`
   - **Impacto**: Risco de itens vencerem sem detecção proativa
   - **Workaround**: Consultas manuais periódicas

4. **Integração UC10 ↔ UC07** ❌
   - **Problema**: UC07 não implementado (RN-ALERTA.2 não funciona)
   - **Consequência**: Validade próxima não dispara alertas de estoque crítico
   - **Impacto**: Itens próximos da validade não são priorizados para uso
   - **Workaround**: Nenhum (requer implementação de UC07)

### 4.2. Funcionalidades Operacionais

| Funcionalidade | Status | Usabilidade |
|----------------|--------|-------------|
| **Consultar itens por validade** | ✅ Funcional | Gestor pode usar `POST /validade` com `diasAntecedencia` |
| **Filtrar itens vencidos** | ✅ Funcional | Flag `incluirVencidos=true` retorna itens já vencidos |
| **Descarte (zerar quantidade)** | ✅ Funcional | Flag `descartarItens=true` zera estoque |
| **Validação de código de custo** | ✅ Funcional | RN-ESTOQUE.4 validada (mas não registrada) |
| **Histórico de descartes** | ❌ Não Funcional | Sem persistência de movimentações |
| **Alertas automáticos** | ❌ Não Funcional | Sem Redis Pub/Sub |

### 4.3. Comparação com Outros UCs Parciais

| UC | Funcionalidade Core | Persistência | Integração Redis | Status |
|----|---------------------|--------------|------------------|--------|
| **UC07** | Gerar alertas de estoque crítico | N/A | ❌ 0% (sem subscriber) | 🔴 **0% funcional** |
| **UC08** | Consultar histórico do paciente | ✅ Funcional | ⚠️ 60% (3 de 5 canais) | 🟡 **60% funcional** |
| **UC10** | Controlar validade de itens | ✅ Funcional | ❌ 0% (sem publisher/subscriber) | 🟡 **50% funcional** |

**Padrão observado**:
- UC07: **Publisher existe**, subscriber não = 0% funcional
- UC08: **Publisher existe**, subscriber parcial = 60% funcional  
- UC10: **UseCase existe**, sem integração Redis = 50% funcional

---

## 5. Correções Realizadas na Documentação

### 5.1. Arquivos Modificados

**Total**: 4 arquivos corrigidos

1. **3.2. Diagrama global de Casos de Uso.md** (2 alterações)
   - Linha 52: Tag `[REDUÇÃO DE ESCOPO - IMPLEMENTAÇÃO PARCIAL]` adicionada à lista
   - Linha 99: PlantUML `#LightYellow` aplicado ao UC10 (amarelo = parcial)

2. **3.3. Descrição detalhada de cada Caso de Uso.md** (2 alterações)
   - Linha 420: Cabeçalho com tag e nota de implementação parcial
   - RN-ESTOQUE.4: Nota explicando que código de custo é validado mas não registrado

3. **3.5. Diagramas de Processos de Negócio (BPM).md** (1 alteração)
   - Linha 132: Tag e nota de que baixa não é persistida em banco de dados

4. **3.9. Modelagem de Interações.md** (1 alteração)
   - Linha 754: Cabeçalho com nota de que `registrarMovimentacaoDescarte()` não persiste

### 5.2. Detalhamento das Correções

#### 5.2.1. Diagrama Global (3.2)

**ANTES**:
```markdown
- **UC10**: Controlar Validade de Itens

usecase "Controlar Validade de Itens" as UC10
```

**DEPOIS**:
```markdown
- **UC10**: Controlar Validade de Itens **[REDUÇÃO DE ESCOPO - IMPLEMENTAÇÃO PARCIAL]**

usecase "Controlar Validade de Itens" as UC10 #LightYellow
```

**Motivo**: Indicar visualmente (amarelo) que UC10 está parcialmente implementado.

#### 5.2.2. Descrição Detalhada (3.3)

**ANTES**:
```markdown
## UC10: Controlar Validade de Itens

**Nome**: Controlar Validade de Itens

[...]

(RN-ESTOQUE.4) A baixa por descarte deve ser registrada com um código de 
custo (Módulo Financeiro).
```

**DEPOIS**:
```markdown
## UC10: Controlar Validade de Itens **[REDUÇÃO DE ESCOPO - IMPLEMENTAÇÃO PARCIAL]**

**Nome**: Controlar Validade de Itens

> **Status de Implementação**: UC10 está **parcialmente implementado**. 
> A funcionalidade de consulta de itens por validade e descarte via endpoint 
> REST está operacional. Porém, a persistência de movimentações de descarte 
> em banco de dados não foi implementada (apenas logs no console). Não há 
> integração com Módulo Financeiro (inexistente) nem alertas automáticos 
> via Redis Pub/Sub.

[...]

(RN-ESTOQUE.4) A baixa por descarte deve ser registrada com um código de 
custo (Módulo Financeiro). **[IMPLEMENTAÇÃO PARCIAL]** - O código de custo 
é validado no DTO, mas a movimentação não é persistida em banco de dados 
(apenas log no console). Módulo Financeiro não existe.
```

**Motivo**: Esclarecer exatamente o que está implementado (consulta, validação) vs não implementado (persistência, integração financeira).

#### 5.2.3. BPM (3.5)

**ANTES**:
```markdown
### UC10: Controlar Validade (Limpeza)

- Ação: O Gestor seleciona os itens para descarte. O sistema realiza uma
  baixa especial do tipo "Descarte", reduzindo o saldo contábil e físico.
```

**DEPOIS**:
```markdown
### UC10: Controlar Validade (Limpeza) **[REDUÇÃO DE ESCOPO - IMPLEMENTAÇÃO PARCIAL]**

> **Status de Implementação**: UC10 está **parcialmente implementado**. 
> A busca de itens por validade funciona via endpoint REST. Porém, a baixa 
> por descarte **não é persistida em banco de dados** (apenas logs no console). 
> Não há integração com Módulo Financeiro.

- Ação: O Gestor seleciona os itens para descarte. O sistema realiza uma
  baixa especial do tipo "Descarte", reduzindo o saldo contábil e físico.
```

**Motivo**: Adicionar disclaimer no processo BPM de que a movimentação descrita não é persistida.

#### 5.2.4. Modelagem de Interações (3.9)

**ANTES**:
```markdown
## UC10: Controlar Validade de Itens

### Descrição
Diagrama de Sequência para UC10 baseado no Fluxo Básico. Demonstra busca 
de itens por validade e descarte.
```

**DEPOIS**:
```markdown
## UC10: Controlar Validade de Itens **[REDUÇÃO DE ESCOPO - IMPLEMENTAÇÃO PARCIAL]**

> **Status de Implementação**: UC10 está **parcialmente implementado**. 
> A busca de itens por validade (endpoint `/validade`) funciona corretamente. 
> Porém, o método `registrarMovimentacaoDescarte()` apenas imprime logs no 
> console, não persiste movimentações em banco de dados conforme descrito 
> no diagrama abaixo.

### Descrição
Diagrama de Sequência para UC10 baseado no Fluxo Básico. Demonstra busca 
de itens por validade e descarte.
```

**Motivo**: Avisar que o diagrama de sequência mostra `darBaixa()` mas a implementação real não persiste.

---

## 6. Esforço Estimado para Implementação Completa

### 6.1. Tarefas Pendentes

| Tarefa | Complexidade | Estimativa |
|--------|--------------|------------|
| **1. Criar entidade Movimentacao** | Baixa | 2-3 horas |
| **2. Criar MovimentacaoRepository (Cassandra)** | Baixa | 1-2 horas |
| **3. Implementar persistência em `registrarMovimentacaoDescarte()`** | Média | 3-5 horas |
| **4. Criar endpoint GET `/descartes` (consultar histórico)** | Média | 4-6 horas |
| **5. Implementar Redis Publisher para alertas de validade** | Alta | 8-12 horas |
| **6. Criar subscriber no Cadastro para alertas de validade** | Alta | 6-10 horas |
| **7. Integração com UC07 (RN-ALERTA.2)** | Alta | 8-12 horas |
| **8. Criar módulo Financeiro (básico)** | Muito Alta | 40-60 horas |
| **9. Integração financeira (lançamento de custos de descarte)** | Alta | 12-18 horas |
| **10. Testes de integração** | Média | 8-12 horas |

### 6.2. Estimativa Total

**Sem Módulo Financeiro**: 40-60 horas (1-1.5 semanas de desenvolvimento)

**Com Módulo Financeiro**: 92-138 horas (2.3-3.5 semanas de desenvolvimento)

### 6.3. Priorização Sugerida

**🔴 Alta Prioridade** (impacto regulatório):
1. Persistência de movimentações de descarte (Tarefas 1-3)
2. Endpoint de consulta de histórico (Tarefa 4)

**🟡 Média Prioridade** (melhoria operacional):
5. Redis Publisher para alertas de validade (Tarefa 5)
6. Subscriber no Cadastro (Tarefa 6)

**🟢 Baixa Prioridade** (feature adicional):
7. Integração com UC07 (Tarefa 7)
8-9. Módulo Financeiro completo (Tarefas 8-9)

---

## 7. Conclusão

### 7.1. Resumo da Correção

- ✅ **4 arquivos de documentação corrigidos** (3.2, 3.3, 3.5, 3.9)
- ✅ **Status "IMPLEMENTAÇÃO PARCIAL" documentado** em todos os UCs relevantes
- ✅ **RN-ESTOQUE.4 marcada com disclaimer** de implementação parcial
- ✅ **Cor amarela (#LightYellow)** aplicada no diagrama PlantUML

### 7.2. Funcionalidades Operacionais vs Não Implementadas

**Operacional** (50% do UC10):
- ✅ Busca de itens por validade (endpoint `/validade`)
- ✅ Filtro por dias de antecedência (configurable)
- ✅ Filtro de itens vencidos (`incluirVencidos=true`)
- ✅ Descarte (zerar quantidade em estoque)
- ✅ Validação de código de custo (RN-ESTOQUE.4)

**Não Implementado** (50% do UC10):
- ❌ Persistência de movimentações de descarte em BD
- ❌ Integração com Módulo Financeiro (módulo não existe)
- ❌ Alertas automáticos via Redis Pub/Sub
- ❌ Consulta de histórico de descartes
- ❌ Integração com UC07 (RN-ALERTA.2)

### 7.3. Comparação com Discrepâncias Anteriores

| Discrepância | UC | Status | Funcionalidade Core | Integração |
|--------------|-----|--------|---------------------|------------|
| **5.1** | UC07 | 🔴 **0% funcional** | ❌ Sem subscriber | ❌ Redis não funciona |
| **5.2** | UC08 | 🟡 **60% funcional** | ✅ Core funciona | ⚠️ 3 de 5 canais |
| **5.3** | UC10 | 🟡 **50% funcional** | ✅ Core funciona | ❌ Sem Redis |

**Padrão Observado**: UCs do Estoque (UC07, UC10) têm mais gaps de integração que UCs de Agendamento.

### 7.4. Recomendações

1. **Imediato**: Implementar persistência de movimentações (Tarefas 1-3) para compliance regulatório
2. **Curto Prazo**: Endpoint de consulta de histórico (Tarefa 4) para auditoria
3. **Médio Prazo**: Redis Publisher/Subscriber (Tarefas 5-6) para alertas proativos
4. **Longo Prazo**: Módulo Financeiro completo (Tarefas 8-9) se houver necessidade contábil

---

**Versão**: 1.0  
**Data de Correção**: Janeiro 2025  
**Equipe**: Grupo 4 - SimpleHealth  
**Revisores**: [A completar]
