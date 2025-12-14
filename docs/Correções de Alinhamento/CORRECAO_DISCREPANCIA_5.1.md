# Correção da Discrepância 5.1: UC07 - Gerar Alerta de Estoque Crítico (Redução de Escopo)

## 1. Descrição da Discrepância

**Tipo**: Redução de escopo - Funcionalidade planejada mas não implementada

**Problema Identificado**: O UC07 (Gerar Alerta de Estoque Crítico) foi documentado como caso de uso completo com fluxos básicos, alternativos, regras de negócio e diagramas de sequência, mas a implementação no backend está **incompleta**. O módulo Estoque **NÃO possui subscriber Redis** para processar solicitações de alertas automáticos, apenas uma verificação básica retornando booleano.

**Localização**:
- Documentação: Múltiplos arquivos (3.2, 3.3, 3.5, 3.6, 3.9, 3.10)
- Backend Cadastro: `GerarAlertaEstoqueCriticoUseCase.java`, `EstoqueAlertaPublisher.java`
- Backend Estoque: **Subscriber ausente** (nenhum listener para canal `estoque.alerta.request`)

---

## 2. Análise do Backend

### 2.1. Módulo Cadastro - Publisher Implementado

**Arquivo**: `simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/application/usecases/GerarAlertaEstoqueCriticoUseCase.java`

```java
@Component
@RequiredArgsConstructor
public class GerarAlertaEstoqueCriticoUseCase {

  private final EstoqueAlertaPublisher publisher;
  private final ConcurrentHashMap<String, Object> cache;

  public List<AlertaEstoqueDTO> execute() {
    String correlationId = UUID.randomUUID().toString();

    publisher.solicitarAlertasEstoqueCritico(correlationId);

    List<AlertaEstoqueDTO> alertas = Collections.emptyList();

    int tentativas = 0;
    final int maxTentativas = 50;

    while (tentativas < maxTentativas) {
      var response = cache.get(correlationId + ":alerta");

      if (response instanceof EstoqueAlertaResponseEvent) {
        alertas = ((EstoqueAlertaResponseEvent) response).getAlertas();
        if (alertas == null) {
          alertas = Collections.emptyList();
        }
        cache.remove(correlationId + ":alerta");
        break;
      }

      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }

      tentativas++;
    }

    return alertas;
  }
}
```

**Arquivo**: `simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/infrastructure/redis/publishers/EstoqueAlertaPublisher.java`

```java
@Component
@RequiredArgsConstructor
public class EstoqueAlertaPublisher {

  private final RedisTemplate<String, Object> redisTemplate;

  public void solicitarAlertasEstoqueCritico(String correlationId) {
    redisTemplate.convertAndSend(
        "estoque.alerta.request",
        new EstoqueAlertaRequestEvent(correlationId));
  }
}
```

**Análise**:
- ✅ Cadastro **publica** solicitação no canal Redis `estoque.alerta.request`
- ✅ Aguarda resposta no cache com `correlationId + ":alerta"`
- ❌ **Problema**: Timeout de 5 segundos (50 tentativas × 100ms) - sempre retorna lista vazia

### 2.2. Módulo Estoque - Subscriber Ausente

**Estrutura de diretórios verificada**:
```
simplehealth-back-estoque/
├── src/main/java/com/simplehealth/estoque/
│   ├── application/
│   │   ├── usecases/
│   │   │   ├── ControlarValidadeUseCase.java
│   │   │   ├── DarBaixaInsumosUseCase.java
│   │   │   └── EntradaItensUseCase.java
│   │   └── service/
│   │       └── EstoqueService.java
│   ├── infrastructure/
│   │   └── repositories/  (APENAS repositories, SEM redis/)
│   └── ...
```

**Verificação por grep**:
```bash
# Busca por subscriber ou listener Redis
grep -r "@RedisSubscriber" simplehealth-back/simplehealth-back-estoque/**/*.java
# Resultado: No matches found

grep -r "EstoqueAlertaRequest" simplehealth-back/simplehealth-back-estoque/**/*.java
# Resultado: No matches found

grep -r "estoque.alerta.request" simplehealth-back/simplehealth-back-estoque/**/*.java
# Resultado: No matches found
```

**Conclusão**: 
- ❌ **NÃO existe** subscriber Redis no módulo Estoque
- ❌ **NÃO existe** diretório `infrastructure/redis/` no módulo Estoque
- ❌ **NÃO existe** processamento do canal `estoque.alerta.request`

### 2.3. Verificação Básica Implementada

**Arquivo**: `simplehealth-back-estoque/src/main/java/com/simplehealth/estoque/application/service/EstoqueService.java`

```java
public boolean verificarEstoqueCritico(UUID itemId) {
  // Implementação básica apenas retorna booleano
  // Não gera alertas, não envia notificações, não publica eventos
}
```

**Uso em DarBaixaInsumosUseCase**:
```java
boolean estoqueCritico = estoqueService.verificarEstoqueCritico(dto.getItemId());
// Retorna booleano no response, mas não aciona UC07
```

**Implementação atual**:
- ✅ Verifica se quantidade está abaixo do ponto de reposição
- ✅ Retorna flag booleano `estoqueCritico` no response DTO
- ❌ **NÃO gera** registro de alerta no sistema
- ❌ **NÃO envia** notificações ao Gestor
- ❌ **NÃO processa** solicitações via Redis Pub/Sub

---

## 3. Comparação: Documentação vs. Implementação

### 3.1. UC07 Documentado (Estado Anterior)

**Características documentadas**:
1. **Ator**: Sistema (background) e Gestor (notificado)
2. **Fluxo Completo**: 
   - Sistema percorre todos os itens ativos
   - Compara quantidadeTotal com Ponto de Reposição
   - Gera registro de alerta
   - Notifica Gestor (email, notificação interna)
3. **Regras de Negócio**:
   - RN-ALERTA.1: Notificação imediata ou consolidada
   - RN-ALERTA.2: Considera validade próxima (integração UC10)
4. **Arquitetura**: Redis Pub/Sub para comunicação assíncrona
5. **Integração**: Disparado por UC05 (Baixa) e UC06 (Entrada)

### 3.2. Implementação Real

**O que existe**:
- ✅ `EstoqueService.verificarEstoqueCritico(itemId)` - retorna boolean
- ✅ `GerarAlertaEstoqueCriticoUseCase` no módulo Cadastro (publisher)
- ✅ `EstoqueAlertaPublisher` publica no Redis
- ✅ Canal Redis `estoque.alerta.request` configurado

**O que NÃO existe**:
- ❌ Subscriber Redis no módulo Estoque para processar solicitações
- ❌ Geração de registros de alerta persistidos
- ❌ Sistema de notificações ao Gestor
- ❌ Verificação automática periódica (timer/scheduler)
- ❌ Integração com UC10 (validade próxima)
- ❌ Listagem de alertas gerados
- ❌ Dashboard ou relatório de alertas

### 3.3. Tabela Comparativa

| Funcionalidade | Documentado | Implementado | Status |
|----------------|-------------|--------------|--------|
| **Verificação básica (item individual)** | ✅ | ✅ | ✅ Parcial |
| **Percorrer todos os itens** | ✅ | ❌ | ❌ Não implementado |
| **Gerar registro de alerta** | ✅ | ❌ | ❌ Não implementado |
| **Notificar Gestor** | ✅ | ❌ | ❌ Não implementado |
| **Redis Pub/Sub (publisher)** | ✅ | ✅ | ✅ Implementado |
| **Redis Pub/Sub (subscriber)** | ✅ | ❌ | ❌ **Não implementado** |
| **Processamento assíncrono** | ✅ | ❌ | ❌ Não implementado |
| **Integração UC05/UC06** | ✅ | 🟡 | 🟡 Parcial (apenas flag boolean) |
| **Verificação periódica (timer)** | ✅ | ❌ | ❌ Não implementado |
| **Dashboard de alertas** | ✅ | ❌ | ❌ Não implementado |
| **Consideração de validade (RN-ALERTA.2)** | ✅ | ❌ | ❌ Não implementado |

---

## 4. Identificação da Redução de Escopo

### 4.1. Causa Raiz

**Decisão de desenvolvimento**: O UC07 foi **planejado** (documentado completamente) mas a implementação foi **reduzida ao mínimo viável**:
- Apenas verificação básica por item individual
- Sem sistema de notificações
- Sem persistência de alertas
- Sem processamento assíncrono completo

**Possíveis razões**:
1. **Priorização**: Funcionalidades core (UC01-UC06) tiveram prioridade
2. **Complexidade**: Sistema de notificações requer infraestrutura adicional
3. **Tempo**: Constraints de prazo levaram a redução de escopo
4. **Dependências**: Falta de sistema de email/notificações push

### 4.2. Impacto Arquitetural

**Redis Pub/Sub incompleto**:
```
[Módulo Cadastro]                        [Módulo Estoque]
       │                                         │
       │ GerarAlertaEstoqueCriticoUseCase        │
       │           │                             │
       │           ↓                             │
       │   EstoqueAlertaPublisher                │
       │           │                             │
       │           ↓                             │
       │  Redis Pub: "estoque.alerta.request"   │
       │           │                             │
       │           X ← ← ← ← ← ← ← ← ← ← ← ← ← ❌ SUBSCRIBER AUSENTE
       │                                         │
       │   Timeout após 5 segundos               │
       │   Retorna lista vazia                   │
```

**Problema de arquitetura**:
- Sistema publica mensagem mas ninguém está "ouvindo"
- Timeout desperdiça recursos (50 tentativas de polling)
- Usuário sempre recebe lista vazia de alertas

---

## 5. Correções Aplicadas na Documentação

### 5.1. Arquivo 3.2 - Diagrama Global de Casos de Uso

**Modificação 1 - Lista de UCs**:

**ANTES**:
```markdown
- **UC07**: Gerar Alerta de Estoque Crítico
```

**DEPOIS**:
```markdown
- **UC07**: Gerar Alerta de Estoque Crítico **[REDUÇÃO DE ESCOPO - NÃO IMPLEMENTADO]**
```

**Modificação 2 - Diagrama PlantUML**:

**ANTES**:
```plantuml
usecase "Gerar Alerta de Estoque Crítico" as UC7
```

**DEPOIS**:
```plantuml
usecase "Gerar Alerta de Estoque Crítico\n[REDUÇÃO DE ESCOPO]" as UC7 #LightGray
```

### 5.2. Arquivo 3.3 - Descrição Detalhada de Cada Caso de Uso

**Modificação 1 - Cabeçalho UC07**:

**ANTES**:
```markdown
## UC07: Gerar Alerta de Estoque Crítico

**Nome**: Gerar Alerta de Estoque Crítico

**Descrição**: O sistema verifica a quantidade de Itens no Estoque e notifica o Gestor se o saldo cair abaixo do ponto de reposição configurado.
```

**DEPOIS**:
```markdown
## UC07: Gerar Alerta de Estoque Crítico **[REDUÇÃO DE ESCOPO - NÃO IMPLEMENTADO]**

**Nome**: Gerar Alerta de Estoque Crítico

**Status**: ⚠️ **REDUÇÃO DE ESCOPO** - Este caso de uso foi planejado mas NÃO foi implementado. O backend do módulo Estoque não possui subscriber Redis para processar solicitações de alertas. Apenas verificação básica de estoque crítico está implementada (retorno booleano).

**Descrição**: O sistema verifica a quantidade de Itens no Estoque e notifica o Gestor se o saldo cair abaixo do ponto de reposição configurado.
```

**Modificação 2 - Regras de Negócio**:

**ANTES**:
```markdown
(RN-ALERTA.1) A notificação deve ser imediata ou ocorrer em intervalos definidos (ex: consolidado a cada hora).
(RN-ALERTA.2) Itens com validade próxima (UC10) também devem ser considerados no cálculo de estoque crítico, mesmo que a quantidade seja alta.
```

**DEPOIS**:
```markdown
(RN-ALERTA.1) A notificação deve ser imediata ou ocorrer em intervalos definidos (ex: consolidado a cada hora). **[NÃO IMPLEMENTADO - Sem subscriber no módulo Estoque]**
(RN-ALERTA.2) Itens com validade próxima (UC10) também devem ser considerados no cálculo de estoque crítico, mesmo que a quantidade seja alta. **[NÃO IMPLEMENTADO]**

**Observação de Implementação**: O módulo Cadastro possui `GerarAlertaEstoqueCriticoUseCase` e `EstoqueAlertaPublisher` que tentam solicitar alertas via Redis Pub/Sub (`estoque.alerta.request`), mas o módulo Estoque NÃO possui subscriber para processar essas solicitações. Apenas a verificação básica `verificarEstoqueCritico(itemId)` está implementada, retornando um booleano.
```

**Modificação 3 - Referências em UC05**:

**ANTES**:
```markdown
O Sistema dispara a verificação de Estoque Crítico (UC07).
```

**DEPOIS**:
```markdown
O Sistema dispara a verificação de Estoque Crítico (UC07). **[NÃO IMPLEMENTADO - Apenas verificação básica]**
```

**Modificação 4 - Referências em UC06**:

**ANTES**:
```markdown
O Sistema dispara a verificação de Estoque Crítico (UC07).
```

**DEPOIS**:
```markdown
O Sistema dispara a verificação de Estoque Crítico (UC07). **[NÃO IMPLEMENTADO - Apenas verificação básica]**
```

### 5.3. Arquivo 3.5 - Diagramas de Processos de Negócio (BPM)

**Modificação**:

**ANTES**:
```markdown
### UC07: Gerar Alerta de Estoque Crítico (Monitoramento)

- Natureza: É um processo de sistema (automatizado), geralmente
  disparado por um *Timer* (tempo) ou *Signal* (após baixa/entrada).

- Lógica: O sistema itera sobre os itens. Se Quantidade Total \< Ponto
  Reposição , ele envia uma notificação ao Gestor.
```

**DEPOIS**:
```markdown
### UC07: Gerar Alerta de Estoque Crítico (Monitoramento) **[REDUÇÃO DE ESCOPO - NÃO IMPLEMENTADO]**

⚠️ **REDUÇÃO DE ESCOPO**: Este processo NÃO está implementado. O módulo Estoque não possui subscriber Redis para processar solicitações de alertas automáticos.

- Natureza: É um processo de sistema (automatizado), geralmente
  disparado por um *Timer* (tempo) ou *Signal* (após baixa/entrada).

- Lógica: O sistema itera sobre os itens. Se Quantidade Total \< Ponto
  Reposição , ele envia uma notificação ao Gestor.

**Implementação Atual**: Apenas verificação básica `verificarEstoqueCritico(itemId)` retorna booleano, mas sem geração automática de alertas ou notificações.
```

### 5.4. Arquivo 3.6 - Arquitetura do Sistema

**Modificação - Redis Estoque (Porta 6381)**:

**ANTES**:
```markdown
**Por quê?**

1. **Cache de Estoque Crítico**: Lista de itens abaixo do mínimo
2. **Pub/Sub**: Alertas para módulo Cadastro quando estoque crítico
```

**DEPOIS**:
```markdown
**Por quê?**

1. **Cache de Estoque Crítico**: Lista de itens abaixo do mínimo **[PLANEJADO - NÃO IMPLEMENTADO]**
2. **Pub/Sub**: Alertas para módulo Cadastro quando estoque crítico **[REDUÇÃO DE ESCOPO - Cadastro publica mas Estoque não possui subscriber]**

⚠️ **Observação**: O canal Redis `estoque.alerta.request` é usado pelo módulo Cadastro para solicitar alertas, mas o módulo Estoque NÃO implementa o subscriber correspondente. UC07 não está funcional.
```

### 5.5. Arquivo 3.9 - Modelagem de Interações

**Modificação - Cabeçalho UC07**:

**ANTES**:
```markdown
## UC07: Gerar Alerta de Estoque Crítico

### Descrição
Diagrama de Sequência para UC07 baseado no Fluxo Básico. O Ator é o Sistema, disparado por um gatilho (timer ou chamada de UC05/UC06).
```

**DEPOIS**:
```markdown
## UC07: Gerar Alerta de Estoque Crítico **[REDUÇÃO DE ESCOPO - NÃO IMPLEMENTADO]**

### Descrição
⚠️ **REDUÇÃO DE ESCOPO**: Este caso de uso foi planejado mas NÃO está implementado. O módulo Estoque não possui subscriber Redis para processar solicitações de alertas automáticos.

Diagrama de Sequência para UC07 baseado no Fluxo Básico. O Ator é o Sistema, disparado por um gatilho (timer ou chamada de UC05/UC06).
```

### 5.6. Arquivo 3.10 - Modelagem de Estados

**Modificação - Referências a UC07**:

**ANTES**:
```markdown
- **DTE Item**: UC05 (Dar Baixa), UC06 (Processar Entrada NF), UC07 (Alerta de Estoque Crítico), UC10 (Controlar Validade)
- **DTE Pedido**: UC06 (Processar Entrada NF), UC07 (Gerar Alerta)
```

**DEPOIS**:
```markdown
- **DTE Item**: UC05 (Dar Baixa), UC06 (Processar Entrada NF), UC07 (Alerta de Estoque Crítico - NÃO IMPLEMENTADO), UC10 (Controlar Validade)
- **DTE Pedido**: UC06 (Processar Entrada NF), UC07 (Gerar Alerta - NÃO IMPLEMENTADO)
```

---

## 6. Resumo das Mudanças

### 6.1. Arquivos Modificados

| Arquivo | Modificações | Tipo |
|---------|-------------|------|
| **3.2. Diagrama Global de Casos de Uso** | Adicionada tag [REDUÇÃO DE ESCOPO] na lista e diagrama PlantUML (cor cinza) | 2 alterações |
| **3.3. Descrição Detalhada de Casos de Uso** | Status de redução no cabeçalho, notas em RN-ALERTA.1/2, notas em UC05/UC06 | 6 alterações |
| **3.5. Diagramas BPM** | Tag de redução e nota sobre implementação atual | 1 alteração |
| **3.6. Arquitetura do Sistema** | Notas em Redis Pub/Sub e cache de estoque crítico | 2 alterações |
| **3.9. Modelagem de Interações** | Status de redução no cabeçalho UC07 | 1 alteração |
| **3.10. Modelagem de Estados** | Notas em referências DTE Item e DTE Pedido | 2 alterações |

**Total**: 6 arquivos modificados, 14 alterações

### 6.2. Tipo de Correção

- ✅ **Documentação atualizada** (redução de escopo marcada)
- ✅ **Backend parcialmente implementado** (verificação básica funcional)
- ❌ **Subscriber Redis ausente** (funcionalidade completa não implementada)

---

## 7. Impacto nas Funcionalidades

### 7.1. O que FUNCIONA

1. **Verificação básica**: `verificarEstoqueCritico(itemId)` retorna boolean
2. **Flag em responses**: UC05 e UC06 retornam `estoqueCritico: true/false`
3. **Publisher Redis**: Cadastro consegue publicar solicitações (mas ninguém responde)

### 7.2. O que NÃO FUNCIONA

1. **Geração automática de alertas**: Sem subscriber, nenhum alerta é criado
2. **Notificações ao Gestor**: Sem sistema de notificações implementado
3. **Listagem de alertas**: `GerarAlertaEstoqueCriticoUseCase.execute()` sempre retorna lista vazia
4. **Verificação periódica**: Sem scheduler/timer para verificação automática
5. **Dashboard de alertas**: Não existe interface para visualização
6. **Integração UC10**: Validade próxima não é considerada

### 7.3. Casos de Uso Afetados

| UC | Descrição | Impacto |
|----|-----------|---------|
| **UC05** | Dar Baixa em Insumos | 🟡 Parcial - Retorna flag `estoqueCritico` mas não aciona UC07 |
| **UC06** | Processar Entrada de NF/Itens | 🟡 Parcial - Retorna flag mas não aciona UC07 |
| **UC07** | Gerar Alerta de Estoque Crítico | ❌ **Não funcional** - Apenas verificação básica |
| **UC10** | Controlar Validade de Itens | 🟡 Parcial - Sem integração com alertas de estoque |

---

## 8. Opções de Evolução Futura

Se houver necessidade de implementar UC07 completamente, as seguintes ações seriam necessárias:

### 8.1. Implementação Mínima

1. **Criar Subscriber Redis** no módulo Estoque:
   ```java
   @Component
   @RequiredArgsConstructor
   public class EstoqueAlertaSubscriber {
     
     private final EstoqueService estoqueService;
     private final RedisTemplate<String, Object> redisTemplate;
     
     @RedisListener(topics = "estoque.alerta.request")
     public void processarSolicitacaoAlertas(EstoqueAlertaRequestEvent event) {
       List<AlertaEstoqueDTO> alertas = estoqueService.buscarItensAbaixoMinimo();
       redisTemplate.convertAndSend(
         "estoque.alerta.response",
         new EstoqueAlertaResponseEvent(event.getCorrelationId(), alertas)
       );
     }
   }
   ```

2. **Implementar `buscarItensAbaixoMinimo()`** em `EstoqueService`:
   ```java
   public List<AlertaEstoqueDTO> buscarItensAbaixoMinimo() {
     return estoqueRepository.findAll().stream()
       .filter(item -> item.getQuantidadeTotal() < item.getPontoReposicao())
       .map(this::toAlertaDTO)
       .collect(Collectors.toList());
   }
   ```

3. **Criar entidade `AlertaEstoque`** para persistência:
   ```java
   @Entity
   public class AlertaEstoque {
     @Id private UUID id;
     private UUID itemId;
     private String itemNome;
     private Integer quantidadeAtual;
     private Integer pontoReposicao;
     private LocalDateTime dataHoraAlerta;
     private StatusAlerta status; // PENDENTE, RESOLVIDO
   }
   ```

### 8.2. Implementação Completa

1. **Sistema de Notificações**:
   - Integração com serviço de email (SendGrid, AWS SES)
   - Notificações push (Firebase, OneSignal)
   - WebSocket para notificações em tempo real

2. **Verificação Periódica**:
   ```java
   @Scheduled(cron = "0 0 */1 * * *") // A cada hora
   public void verificarEstoqueCriticoPeriodico() {
     gerarAlertaEstoqueCriticoUseCase.execute();
   }
   ```

3. **Dashboard de Alertas**:
   - API REST para listar alertas pendentes
   - Interface frontend para visualização
   - Ações: marcar como resolvido, gerar pedido de compra

4. **Integração UC10**:
   - Considerar validade próxima no cálculo de criticidade
   - Alertas compostos (quantidade baixa + validade curta)

### 8.3. Estimativa de Esforço

| Componente | Complexidade | Estimativa |
|------------|--------------|------------|
| **Subscriber Redis** | Baixa | 2-4 horas |
| **Busca itens críticos** | Baixa | 2-3 horas |
| **Persistência de alertas** | Média | 4-6 horas |
| **Sistema de notificações** | Alta | 16-24 horas |
| **Dashboard frontend** | Alta | 16-24 horas |
| **Integração UC10** | Média | 4-8 horas |
| **Testes** | - | 8-12 horas |
| **TOTAL** | - | **52-81 horas** (1-2 semanas) |

---

## 9. Validação da Correção

### 9.1. Comandos de Verificação

**1. Verificar ausência de subscriber no Estoque**:
```bash
grep -r "@RedisListener\|@RedisSubscriber" \
  simplehealth-back/simplehealth-back-estoque/src/main/java/
```
**Resultado esperado**: `No matches found`

**2. Verificar publisher no Cadastro**:
```bash
grep -r "solicitarAlertasEstoqueCritico" \
  simplehealth-back/simplehealth-back-cadastro/src/main/java/
```
**Resultado esperado**: Encontra `EstoqueAlertaPublisher.java` e `GerarAlertaEstoqueCriticoUseCase.java`

**3. Verificar verificação básica no Estoque**:
```bash
grep -r "verificarEstoqueCritico" \
  simplehealth-back/simplehealth-back-estoque/src/main/java/
```
**Resultado esperado**: Encontra `EstoqueService.java`, `DarBaixaInsumosUseCase.java`, `EntradaItensUseCase.java`

**4. Verificar documentação atualizada**:
```bash
grep -r "UC07.*REDUÇÃO DE ESCOPO" \
  docs/documentos-finais-definitivos/
```
**Resultado esperado**: 6 arquivos com marcações de redução de escopo

### 9.2. Teste de Comportamento Atual

**Cenário**: Tentar gerar alerta de estoque crítico via API

**Request**:
```bash
curl -X GET http://localhost:8081/api/cadastro/alertas-estoque
```

**Comportamento atual**:
1. `GerarAlertaEstoqueCriticoUseCase.execute()` é chamado
2. `EstoqueAlertaPublisher` publica no Redis `estoque.alerta.request`
3. Aguarda resposta por 5 segundos (50 tentativas × 100ms)
4. **Timeout**: Nenhum subscriber responde
5. Retorna `[]` (lista vazia)

**Response**:
```json
[]
```

---

## 10. Conclusão

### 10.1. Status da Discrepância

✅ **DOCUMENTADA**: A redução de escopo foi marcada em todos os arquivos relevantes da documentação.

### 10.2. Arquivos Sincronizados

- ✅ `3.2. Diagrama global de Casos de Uso.md` (2 alterações)
- ✅ `3.3. Descrição detalhada de cada Caso de Uso.md` (6 alterações)
- ✅ `3.5. Diagramas de Processos de Negócio (BPM).md` (1 alteração)
- ✅ `3.6. Arquitetura do Sistema - Lógica e Física.md` (2 alterações)
- ✅ `3.9. Modelagem de Interações.md` (1 alteração)
- ✅ `3.10. Modelagem de Estados.md` (2 alterações)

### 10.3. Backend vs. Documentação

| Componente | Documentação | Backend | Status |
|-----------|--------------|---------|--------|
| **UC07 completo** | ✅ Documentado | ❌ Não implementado | ⚠️ **Redução de escopo marcada** |
| **Verificação básica** | 🟡 Implícito | ✅ Implementado | ✅ Funcional |
| **Publisher Redis** | ✅ Documentado | ✅ Implementado | ✅ Funcional (mas inútil sem subscriber) |
| **Subscriber Redis** | ✅ Documentado | ❌ **Não implementado** | ⚠️ **Redução de escopo marcada** |
| **Sincronização** | ✅ | ✅ | ✅ Completa |

### 10.4. Recomendações

1. **Curto Prazo**: Manter documentação com marcações de redução de escopo (✅ **FEITO**)
2. **Médio Prazo**: Avaliar necessidade real de UC07 com stakeholders
3. **Longo Prazo**: Se necessário, implementar UC07 completo seguindo estimativas da Seção 8

---

**Documento criado em**: 2025-12-14  
**Discrepância**: 5.1 - UC07: Gerar Alerta de Estoque Crítico (Redução de Escopo)  
**Tipo de correção**: Documentação (marcação de redução de escopo)  
**Status**: ✅ Concluída
