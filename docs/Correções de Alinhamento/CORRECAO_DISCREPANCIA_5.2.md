# Correção da Discrepância 5.2: UC08 - Consultar Histórico do Paciente (Implementação Parcial)

## 1. Descrição da Discrepância

**Tipo**: Implementação parcial - Funcionalidade planejada mas implementada apenas parcialmente

**Problema Identificado**: O UC08 (Consultar Histórico do Paciente) foi documentado como caso de uso completo que integra dados de **5 módulos diferentes** (Cadastro, Agendamento, Estoque, Financeiro via Redis Pub/Sub), mas a implementação no backend está **incompleta**. Apenas o **módulo Agendamento** responde às solicitações. Os módulos **Estoque** e **Financeiro** **NÃO possuem subscribers Redis**, resultando em listas vazias para `itensBaixados` e `pagamentos`.

**Localização**:
- Documentação: Múltiplos arquivos (3.2, 3.3, 3.5, 3.9)
- Backend Cadastro: `ConsultarHistoricoPacienteUseCase.java`, `HistoricoPublisher.java`
- Backend Agendamento: `AgendamentoSubscriber.java` (✅ **implementado**)
- Backend Estoque: **Subscriber ausente** (❌ não responde a `historico.estoque.request`)
- Backend Financeiro: **Módulo não existe** (❌ não responde a `historico.pagamento.request`)

---

## 2. Análise do Backend

### 2.1. Módulo Cadastro - Orquestrador (Publisher)

**Arquivo**: `simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/application/usecases/ConsultarHistoricoPacienteUseCase.java`

```java
@Component
@RequiredArgsConstructor
public class ConsultarHistoricoPacienteUseCase {

  private final PacienteRepository pacienteRepository;
  private final HistoricoPublisher publisher;
  private final ConcurrentHashMap<String, Object> cache;

  public HistoricoPacienteDTO execute(String cpf) {

    var paciente = pacienteRepository.findByCpf(cpf)
        .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

    String cid = UUID.randomUUID().toString();

    // Publica 5 solicitações em canais Redis diferentes
    publisher.solicitarConsultas(cid, cpf);
    publisher.solicitarExames(cid, cpf);
    publisher.solicitarProcedimentos(cid, cpf);
    publisher.solicitarEstoque(cid, cpf);        // ❌ Ninguém responde
    publisher.solicitarPagamentos(cid, cpf);     // ❌ Ninguém responde

    List<ConsultaDTO> cons = Collections.emptyList();
    List<ExameDTO> exam = Collections.emptyList();
    List<ProcedimentoDTO> proc = Collections.emptyList();
    List<ItemEstoqueDTO> est = Collections.emptyList();
    List<PagamentoDTO> pag = Collections.emptyList();

    int tentativas = 0;
    final int maxTentativas = 50;
    
    boolean consultasRecebidas = false;
    boolean examesRecebidos = false;
    boolean procedimentosRecebidos = false;

    // Aguarda apenas 3 respostas (consultas, exames, procedimentos)
    // Estoque e Pagamentos ficam vazios após timeout
    while (tentativas < maxTentativas && 
           (!consultasRecebidas || !examesRecebidos || !procedimentosRecebidos)) {

      var r1 = cache.get(cid + ":cons");
      var r2 = cache.get(cid + ":exam");
      var r3 = cache.get(cid + ":proc");
      var r4 = cache.get(cid + ":est");   // Sempre null
      var r5 = cache.get(cid + ":pag");   // Sempre null

      // Processamento das respostas recebidas...
      
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }

      tentativas++;
    }

    return HistoricoPacienteDTO.builder()
        .dadosCadastrais(/* Paciente do Cadastro */)
        .consultas(cons)        // ✅ Preenchido (Agendamento responde)
        .exames(exam)           // ✅ Preenchido (Agendamento responde)
        .procedimentos(proc)    // ✅ Preenchido (Agendamento responde)
        .itensBaixados(est)     // ❌ SEMPRE VAZIO (Estoque não responde)
        .pagamentos(pag)        // ❌ SEMPRE VAZIO (Financeiro não existe)
        .build();
  }
}
```

**Arquivo**: `simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/infrastructure/redis/publishers/HistoricoPublisher.java`

```java
@Component
@RequiredArgsConstructor
public class HistoricoPublisher {

  private final RedisTemplate<String, Object> redisTemplate;

  public void solicitarConsultas(String cid, String cpf) {
    redisTemplate.convertAndSend("historico.consulta.request", 
        new HistoricoRequestEvent(cid, cpf));
  }

  public void solicitarExames(String cid, String cpf) {
    redisTemplate.convertAndSend("historico.exame.request", 
        new HistoricoRequestEvent(cid, cpf));
  }

  public void solicitarProcedimentos(String cid, String cpf) {
    redisTemplate.convertAndSend("historico.procedimento.request", 
        new HistoricoRequestEvent(cid, cpf));
  }

  public void solicitarEstoque(String cid, String cpf) {
    redisTemplate.convertAndSend("historico.estoque.request",   // ❌ Ninguém ouve
        new HistoricoRequestEvent(cid, cpf));
  }

  public void solicitarPagamentos(String cid, String cpf) {
    redisTemplate.convertAndSend("historico.pagamento.request", // ❌ Ninguém ouve
        new HistoricoRequestEvent(cid, cpf));
  }
}
```

**Análise**:
- ✅ Cadastro **publica** 5 solicitações em canais Redis separados
- ✅ Aguarda respostas no cache com `correlationId + ":cons/exam/proc/est/pag"`
- 🟡 Timeout de 5 segundos (50 tentativas × 100ms)
- ❌ **Problema**: 2 canais (`historico.estoque.request` e `historico.pagamento.request`) não têm subscribers

**DTO de resposta**:
```java
@Data
@Builder
public class HistoricoPacienteDTO {
  private PacienteDTO dadosCadastrais;
  private PessoaDTO pessoa;
  private List<ConsultaDTO> consultas;        // ✅ Preenchido
  private List<ExameDTO> exames;              // ✅ Preenchido
  private List<ProcedimentoDTO> procedimentos; // ✅ Preenchido
  private List<ItemEstoqueDTO> itensBaixados;  // ❌ Sempre vazio
  private List<PagamentoDTO> pagamentos;       // ❌ Sempre vazio
}
```

### 2.2. Módulo Agendamento - Responde 3 Canais (✅ Implementado)

**Arquivo**: `simplehealth-back-agendamento/src/main/java/com/simplehealth/agendamento/infrastructure/redis/subscribers/AgendamentoSubscriber.java`

```java
@Component
@RequiredArgsConstructor
public class AgendamentoSubscriber {

  // Processa solicitações de histórico
  private void handleRequestEvent(HistoricoRequestEvent requestEvent, String topic) {
    switch (topic) {
      case "historico.consulta.request" -> handleConsultaRequest(requestEvent);
      case "historico.exame.request" -> handleExameRequest(requestEvent);
      case "historico.procedimento.request" -> handleProcedimentoRequest(requestEvent);
      // Não processa: historico.estoque.request, historico.pagamento.request
    }
  }
}
```

**Configuração Redis**:
```java
@Configuration
public class RedisConfig {
  @Bean
  RedisMessageListenerContainer container(
      RedisConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter) {
    
    container.addMessageListener(listenerAdapter, Arrays.asList(
        new PatternTopic("historico.consulta.request"),
        new PatternTopic("historico.exame.request"),
        new PatternTopic("historico.procedimento.request")
        // NÃO inclui: historico.estoque.request, historico.pagamento.request
    ));
    
    return container;
  }
}
```

**Análise**:
- ✅ Agendamento **responde** a 3 dos 5 canais Redis
- ✅ Busca dados do MongoDB e publica respostas
- ✅ Implementação completa para consultas, exames e procedimentos

### 2.3. Módulo Estoque - Subscriber Ausente (❌ Não Implementado)

**Estrutura de diretórios verificada**:
```
simplehealth-back-estoque/
├── src/main/java/com/simplehealth/estoque/
│   ├── infrastructure/
│   │   └── repositories/  (APENAS repositories, SEM redis/)
```

**Verificação por grep**:
```bash
# Busca por subscriber Redis no Estoque
grep -r "@RedisListener\|RedisSubscriber\|historico" \
  simplehealth-back/simplehealth-back-estoque/**/*.java
# Resultado: No matches found
```

**Conclusão Estoque**: 
- ❌ **NÃO existe** subscriber Redis no módulo Estoque
- ❌ **NÃO existe** diretório `infrastructure/redis/` no módulo Estoque
- ❌ **NÃO existe** processamento do canal `historico.estoque.request`
- ❌ Campo `itensBaixados` sempre retorna lista vazia

### 2.4. Módulo Financeiro - Módulo Inexistente (❌ Não Implementado)

**Estrutura de projeto verificada**:
```
simplehealth-back/
├── simplehealth-back-cadastro/     ✅ Existe
├── simplehealth-back-agendamento/  ✅ Existe
├── simplehealth-back-estoque/      ✅ Existe
└── simplehealth-back-financeiro/   ❌ NÃO EXISTE
```

**Conclusão Financeiro**: 
- ❌ **Módulo não existe** no projeto
- ❌ Canal `historico.pagamento.request` não tem nenhum listener
- ❌ Campo `pagamentos` sempre retorna lista vazia

---

## 3. Comparação: Documentação vs. Implementação

### 3.1. UC08 Documentado (Estado Anterior)

**Características documentadas**:
1. **Integração completa**: Consolida dados de cadastro, agendamentos, procedimentos, exames, insumos baixados e pagamentos
2. **Fluxo Completo**: 
   - Exibe dados cadastrais (Pessoa, Paciente, Convênio)
   - Consolida lista de Agendamentos (passados e futuros)
   - Exibe resultados de Procedimento, Exame
   - **Exibe insumos baixados (UC05) vinculados aos Agendamentos**
3. **Arquitetura**: Redis Pub/Sub para comunicação assíncrona entre módulos
4. **5 Módulos Integrados**: Cadastro, Agendamento, Estoque, Financeiro

### 3.2. Implementação Real

**O que existe e FUNCIONA**:
- ✅ `ConsultarHistoricoPacienteUseCase` no módulo Cadastro (orquestrador)
- ✅ `HistoricoPublisher` publica 5 solicitações no Redis
- ✅ `AgendamentoSubscriber` responde a 3 canais:
  - `historico.consulta.request` ✅
  - `historico.exame.request` ✅
  - `historico.procedimento.request` ✅
- ✅ Dados cadastrais do paciente são retornados
- ✅ Consultas, exames e procedimentos são preenchidos

**O que NÃO existe ou NÃO FUNCIONA**:
- ❌ Subscriber Redis no módulo Estoque para `historico.estoque.request`
- ❌ Módulo Financeiro completo (não existe no projeto)
- ❌ Subscriber para `historico.pagamento.request`
- ❌ Campo `itensBaixados` sempre vazio (sem dados do Estoque)
- ❌ Campo `pagamentos` sempre vazio (sem dados do Financeiro)
- ❌ Integração UC08 ↔ UC05 (Dar Baixa em Insumos) não funcional

### 3.3. Tabela Comparativa

| Funcionalidade | Documentado | Implementado | Status |
|----------------|-------------|--------------|--------|
| **Dados cadastrais do paciente** | ✅ | ✅ | ✅ Funcional |
| **Lista de consultas** | ✅ | ✅ | ✅ Funcional (Agendamento responde) |
| **Lista de exames** | ✅ | ✅ | ✅ Funcional (Agendamento responde) |
| **Lista de procedimentos** | ✅ | ✅ | ✅ Funcional (Agendamento responde) |
| **Lista de insumos baixados** | ✅ | ❌ | ❌ **Não funcional** (Estoque sem subscriber) |
| **Lista de pagamentos** | ✅ | ❌ | ❌ **Não funcional** (Financeiro não existe) |
| **Redis Pub/Sub (5 canais)** | ✅ | 🟡 | 🟡 Parcial (3/5 canais funcionam) |
| **Integração UC08 ↔ UC05** | ✅ | ❌ | ❌ Não funcional |
| **Integração UC08 ↔ Financeiro** | ✅ | ❌ | ❌ Não funcional |
| **Filtro por tipo de documento** | ✅ | 🟡 | 🟡 Parcial (apenas frontend) |
| **Controle de acesso por perfil (RN-HIST.2)** | ✅ | 🟡 | 🟡 Parcial (implementação frontend) |

---

## 4. Identificação da Redução de Escopo

### 4.1. Causa Raiz

**Decisão de desenvolvimento**: O UC08 foi **planejado** como integração completa de 5 módulos, mas a implementação foi **reduzida**:
- Apenas 3 dos 5 canais Redis foram implementados
- Módulo Financeiro não foi criado
- Módulo Estoque não implementou histórico de baixas

**Possíveis razões**:
1. **Priorização**: Funcionalidades core dos módulos tiveram prioridade sobre integrações
2. **Complexidade**: Integração completa via Redis Pub/Sub requer sincronização
3. **Módulo Financeiro**: Decisão de não implementar módulo financeiro completo
4. **Tempo**: Constraints de prazo levaram a redução de escopo

### 4.2. Impacto Arquitetural

**Redis Pub/Sub incompleto**:
```
[Módulo Cadastro - Orquestrador]
       │
       │ ConsultarHistoricoPacienteUseCase
       │ HistoricoPublisher
       │
       ├─► Redis: "historico.consulta.request"      ──► [Agendamento] ✅ Responde
       ├─► Redis: "historico.exame.request"         ──► [Agendamento] ✅ Responde
       ├─► Redis: "historico.procedimento.request"  ──► [Agendamento] ✅ Responde
       ├─► Redis: "historico.estoque.request"       ──X [Estoque]     ❌ SEM SUBSCRIBER
       └─► Redis: "historico.pagamento.request"     ──X [Financeiro]  ❌ MÓDULO NÃO EXISTE
       
       Timeout após 5 segundos
       Retorna com 2 listas vazias (itensBaixados, pagamentos)
```

**Problema de arquitetura**:
- UseCase aguarda apenas 3 respostas (`consultasRecebidas && examesRecebidos && procedimentosRecebidos`)
- Canais de Estoque e Financeiro são publicados mas **nunca processados**
- Usuário recebe resposta parcial com campos vazios

---

## 5. Correções Aplicadas na Documentação

### 5.1. Arquivo 3.2 - Diagrama Global de Casos de Uso

**Modificação 1 - Lista de UCs**:

**ANTES**:
```markdown
- **UC08**: Consultar Histórico do Paciente
```

**DEPOIS**:
```markdown
- **UC08**: Consultar Histórico do Paciente **[REDUÇÃO DE ESCOPO - IMPLEMENTAÇÃO PARCIAL]**
```

**Modificação 2 - Diagrama PlantUML**:

**ANTES**:
```plantuml
usecase "Consultar Histórico do Paciente" as UC8
```

**DEPOIS**:
```plantuml
usecase "Consultar Histórico do Paciente\n[IMPLEMENTAÇÃO PARCIAL]" as UC8 #LightYellow
```

### 5.2. Arquivo 3.3 - Descrição Detalhada de Cada Caso de Uso

**Modificação 1 - Cabeçalho UC08**:

**ANTES**:
```markdown
## UC08: Consultar Histórico do Paciente

**Nome**: Consultar Histórico do Paciente

**Descrição**: O sistema permite a consulta consolidada do cadastro, agendamentos passados/futuros e procedimentos/exames realizados para um paciente.
```

**DEPOIS**:
```markdown
## UC08: Consultar Histórico do Paciente **[REDUÇÃO DE ESCOPO - IMPLEMENTAÇÃO PARCIAL]**

**Nome**: Consultar Histórico do Paciente

**Status**: ⚠️ **IMPLEMENTAÇÃO PARCIAL** - Este caso de uso está parcialmente implementado. O backend retorna apenas dados do módulo Agendamento (consultas, exames, procedimentos). Os seguintes módulos NÃO possuem subscribers Redis:
- **Estoque**: NÃO responde a `historico.estoque.request` (itensBaixados sempre vazio)
- **Financeiro**: NÃO responde a `historico.pagamento.request` (pagamentos sempre vazio)

**Descrição**: O sistema permite a consulta consolidada do cadastro, agendamentos passados/futuros e procedimentos/exames realizados para um paciente.
```

**Modificação 2 - Fluxo Básico Passo 5**:

**ANTES**:
```markdown
O Sistema exibe os resultados de Procedimento, Exame e os insumos baixados (UC05) vinculados aos Agendamentos realizados.
```

**DEPOIS**:
```markdown
O Sistema exibe os resultados de Procedimento, Exame e os insumos baixados (UC05) vinculados aos Agendamentos realizados. **[IMPLEMENTAÇÃO PARCIAL: Apenas Agendamento responde. Estoque e Financeiro não implementaram subscribers]**
```

**Modificação 3 - Regra RN-HIST.1** (já existia da correção 3.4, mantido):

```markdown
(RN-HIST.1) **[REDUÇÃO DE ESCOPO]** A consulta retorna dados apenas do módulo Agendamento (consultas do paciente). Integração com outros módulos (Estoque, Financeiro) não está implementada.
```

### 5.3. Arquivo 3.5 - Diagramas de Processos de Negócio (BPM)

**Modificação**:

**ANTES**:
```markdown
UC08: Consultar Histórico

- Segurança (Gateway de Acesso): Este é o ponto mais importante.
```

**DEPOIS**:
```markdown
UC08: Consultar Histórico **[IMPLEMENTAÇÃO PARCIAL]**

⚠️ **IMPLEMENTAÇÃO PARCIAL**: O sistema retorna apenas dados do módulo Agendamento. Módulos Estoque e Financeiro não implementaram subscribers Redis.

- Segurança (Gateway de Acesso): Este é o ponto mais importante.
```

### 5.4. Arquivo 3.9 - Modelagem de Interações

**Modificação - Cabeçalho UC08**:

**ANTES**:
```markdown
## UC08: Consultar Histórico do Paciente

### Descrição
Diagrama de Sequência para UC08 baseado no Fluxo Básico. O AgendamentoService orquestra, chamando outros serviços.
```

**DEPOIS**:
```markdown
## UC08: Consultar Histórico do Paciente **[IMPLEMENTAÇÃO PARCIAL]**

### Descrição
⚠️ **IMPLEMENTAÇÃO PARCIAL**: Este caso de uso está parcialmente implementado. O `ConsultarHistoricoPacienteUseCase` publica solicitações via Redis para 5 canais, mas apenas o módulo Agendamento responde (consultas, exames, procedimentos). Os módulos **Estoque** e **Financeiro** NÃO possuem subscribers, então `itensBaixados` e `pagamentos` sempre retornam vazios.

Diagrama de Sequência para UC08 baseado no Fluxo Básico. O AgendamentoService orquestra, chamando outros serviços.
```

**Nota**: O diagrama de sequência já continha notas sobre redução de escopo da correção 3.4 (integração Estoque), mantidas.

---

## 6. Resumo das Mudanças

### 6.1. Arquivos Modificados

| Arquivo | Modificações | Tipo |
|---------|-------------|------|
| **3.2. Diagrama Global de Casos de Uso** | Tag [IMPLEMENTAÇÃO PARCIAL] na lista e diagrama PlantUML (cor amarela) | 2 alterações |
| **3.3. Descrição Detalhada de Casos de Uso** | Status de implementação parcial no cabeçalho, nota no fluxo básico passo 5 | 2 alterações |
| **3.5. Diagramas BPM** | Tag e nota sobre módulos sem subscribers | 1 alteração |
| **3.9. Modelagem de Interações** | Status de implementação parcial no cabeçalho | 1 alteração |

**Total**: 4 arquivos modificados, 6 alterações

### 6.2. Tipo de Correção

- ✅ **Documentação atualizada** (implementação parcial marcada)
- ✅ **Backend parcialmente funcional** (3 de 5 integrações funcionam)
- ❌ **Subscribers Redis ausentes** (Estoque e Financeiro não respondem)

---

## 7. Impacto nas Funcionalidades

### 7.1. O que FUNCIONA

1. **Dados cadastrais**: Paciente, Pessoa, Convênio são retornados corretamente
2. **Consultas do paciente**: Lista de consultas (passadas e futuras) funciona via Agendamento
3. **Exames realizados**: Lista de exames funciona via Agendamento
4. **Procedimentos realizados**: Lista de procedimentos funciona via Agendamento
5. **API REST funcional**: `/api/cadastro/pacientes/{cpf}/historico` retorna JSON válido

### 7.2. O que NÃO FUNCIONA

1. **Insumos baixados**: Campo `itensBaixados` sempre retorna `[]` (lista vazia)
2. **Integração UC08 ↔ UC05**: Histórico de baixas de insumos não aparece
3. **Pagamentos do paciente**: Campo `pagamentos` sempre retorna `[]` (lista vazia)
4. **Histórico financeiro completo**: Sem dados de cobranças, recebimentos, inadimplência
5. **Rastreabilidade de materiais**: Não é possível ver quais insumos foram usados em cada consulta

### 7.3. Casos de Uso Afetados

| UC | Descrição | Impacto |
|----|-----------|---------|
| **UC05** | Dar Baixa em Insumos | 🟡 Funciona, mas histórico não aparece em UC08 |
| **UC08** | Consultar Histórico do Paciente | 🟡 **Parcialmente funcional** - Apenas 3 de 5 módulos respondem |
| **UC09** | Cancelar Agendamento | 🟡 Funciona, mas referência a UC08 mostra dados incompletos |

### 7.4. Exemplo de Response Atual

**Request**:
```bash
curl -X GET http://localhost:8081/api/cadastro/pacientes/12345678900/historico
```

**Response**:
```json
{
  "dadosCadastrais": {
    "id": 1,
    "nomeCompleto": "João da Silva",
    "cpf": "12345678900",
    "telefone": "(11) 98765-4321",
    "email": "joao@email.com",
    "convenioId": 10,
    "convenioNome": "Unimed"
  },
  "consultas": [
    {
      "id": "abc123",
      "dataHora": "2025-12-01T10:00:00",
      "medicoCrm": "12345-SP",
      "status": "REALIZADA"
    }
  ],
  "exames": [
    {
      "id": "exam456",
      "tipo": "Hemograma",
      "dataRealizacao": "2025-12-01T10:30:00"
    }
  ],
  "procedimentos": [
    {
      "id": "proc789",
      "descricao": "Limpeza dentária",
      "dataRealizacao": "2025-12-01T11:00:00"
    }
  ],
  "itensBaixados": [],     // ❌ SEMPRE VAZIO - Estoque não responde
  "pagamentos": []         // ❌ SEMPRE VAZIO - Financeiro não existe
}
```

---

## 8. Opções de Evolução Futura

Se houver necessidade de implementar UC08 completamente, as seguintes ações seriam necessárias:

### 8.1. Implementação Mínima - Histórico de Estoque

**1. Criar Subscriber Redis no módulo Estoque**:

```java
// simplehealth-back-estoque/infrastructure/redis/subscribers/EstoqueSubscriber.java
@Component
@RequiredArgsConstructor
public class EstoqueSubscriber {
  
  private final EstoqueService estoqueService;
  private final RedisTemplate<String, Object> redisTemplate;
  
  @RedisListener(topics = "historico.estoque.request")
  public void processarSolicitacaoHistorico(HistoricoRequestEvent event) {
    List<ItemEstoqueDTO> itens = estoqueService
        .buscarItensBaixadosPorPaciente(event.getCpf());
    
    redisTemplate.convertAndSend(
      "historico.estoque.response",
      new HistoricoEstoqueResponseEvent(event.getCorrelationId(), itens)
    );
  }
}
```

**2. Implementar método `buscarItensBaixadosPorPaciente()` em `EstoqueService`**:

```java
public List<ItemEstoqueDTO> buscarItensBaixadosPorPaciente(String cpf) {
  // Buscar movimentações de baixa (tipo = SAIDA) 
  // filtradas por destino contendo cpf do paciente
  return movimentacaoRepository
      .findByTipoAndDestinoContaining(TipoMovimentacao.SAIDA, cpf)
      .stream()
      .map(this::toItemEstoqueDTO)
      .collect(Collectors.toList());
}
```

**3. Adicionar campo `destino` em `Movimentacao` (se não existir)**:

```java
@Entity
public class Movimentacao {
  @Id private UUID id;
  private UUID itemId;
  private TipoMovimentacao tipo; // ENTRADA, SAIDA
  private Integer quantidade;
  private String destino; // "Consulta abc123" ou "Paciente CPF 12345678900"
  private LocalDateTime dataHora;
}
```

### 8.2. Implementação Completa - Módulo Financeiro

**1. Criar módulo `simplehealth-back-financeiro`**:

```
simplehealth-back-financeiro/
├── src/main/java/com/simplehealth/financeiro/
│   ├── SimpleHealthFinanceiroApplication.java
│   ├── domain/
│   │   └── entity/
│   │       ├── Pagamento.java
│   │       └── Cobranca.java
│   ├── application/
│   │   └── service/
│   │       └── PagamentoService.java
│   ├── infrastructure/
│   │   ├── repositories/
│   │   │   └── PagamentoRepository.java
│   │   └── redis/
│   │       └── subscribers/
│   │           └── FinanceiroSubscriber.java
│   └── web/
│       └── controllers/
│           └── PagamentoController.java
```

**2. Implementar subscriber Redis**:

```java
@Component
@RequiredArgsConstructor
public class FinanceiroSubscriber {
  
  private final PagamentoService pagamentoService;
  private final RedisTemplate<String, Object> redisTemplate;
  
  @RedisListener(topics = "historico.pagamento.request")
  public void processarSolicitacaoHistorico(HistoricoRequestEvent event) {
    List<PagamentoDTO> pagamentos = pagamentoService
        .buscarPagamentosPorPaciente(event.getCpf());
    
    redisTemplate.convertAndSend(
      "historico.pagamento.response",
      new HistoricoPagamentoResponseEvent(event.getCorrelationId(), pagamentos)
    );
  }
}
```

**3. Criar entidade `Pagamento`**:

```java
@Entity
public class Pagamento {
  @Id private UUID id;
  private String pacienteCpf;
  private String consultaId; // Referência ao MongoDB do Agendamento
  private BigDecimal valor;
  private LocalDateTime dataHora;
  private FormaPagamento formaPagamento; // DINHEIRO, CARTAO, PIX, CONVENIO
  private StatusPagamento status; // PENDENTE, PAGO, CANCELADO
}
```

### 8.3. Estimativa de Esforço

| Componente | Complexidade | Estimativa |
|------------|--------------|------------|
| **Subscriber Redis Estoque** | Baixa | 3-4 horas |
| **Buscar itens baixados por paciente** | Média | 4-6 horas |
| **Adicionar campo destino em Movimentacao** | Baixa | 2-3 horas |
| **Módulo Financeiro completo** | Alta | 40-60 horas |
| **Subscriber Redis Financeiro** | Baixa | 3-4 horas |
| **Entidades e repositórios Financeiro** | Média | 8-12 horas |
| **APIs REST Financeiro** | Média | 8-12 horas |
| **Integração com Convênios (faturamento)** | Alta | 16-24 horas |
| **Testes** | - | 12-16 horas |
| **TOTAL Estoque** | - | **9-13 horas** (1-2 dias) |
| **TOTAL Financeiro** | - | **87-128 horas** (2-3 semanas) |

---

## 9. Validação da Correção

### 9.1. Comandos de Verificação

**1. Verificar ausência de subscriber no Estoque**:
```bash
grep -r "@RedisListener\|RedisSubscriber\|historico" \
  simplehealth-back/simplehealth-back-estoque/src/main/java/
```
**Resultado esperado**: `No matches found`

**2. Verificar ausência do módulo Financeiro**:
```bash
ls -la simplehealth-back/simplehealth-back-financeiro/
```
**Resultado esperado**: `ls: cannot access ... No such file or directory`

**3. Verificar subscriber Agendamento (funciona)**:
```bash
grep -r "historico.consulta.request\|historico.exame.request\|historico.procedimento.request" \
  simplehealth-back/simplehealth-back-agendamento/src/main/java/
```
**Resultado esperado**: Encontra `AgendamentoSubscriber.java` e `RedisConfig.java` (3 matches)

**4. Verificar publisher Cadastro (envia 5 solicitações)**:
```bash
grep -r "solicitarConsultas\|solicitarExames\|solicitarProcedimentos\|solicitarEstoque\|solicitarPagamentos" \
  simplehealth-back/simplehealth-back-cadastro/src/main/java/
```
**Resultado esperado**: Encontra `HistoricoPublisher.java` (5 métodos)

**5. Verificar documentação atualizada**:
```bash
grep -r "UC08.*IMPLEMENTAÇÃO PARCIAL" \
  docs/documentos-finais-definitivos/
```
**Resultado esperado**: 4 arquivos com marcações de implementação parcial

### 9.2. Teste de Comportamento Atual

**Cenário**: Consultar histórico de paciente via API

**Setup**: 
- Paciente com CPF "12345678900" cadastrado
- 2 consultas realizadas
- 1 baixa de insumo (seringas) vinculada à consulta
- Nenhum pagamento registrado

**Request**:
```bash
curl -X GET http://localhost:8081/api/cadastro/pacientes/12345678900/historico
```

**Comportamento atual**:
1. `ConsultarHistoricoPacienteUseCase.execute("12345678900")` é chamado
2. `HistoricoPublisher` publica 5 solicitações no Redis
3. **Agendamento responde** com consultas, exames, procedimentos (✅)
4. **Estoque NÃO responde** - timeout após 5 segundos (❌)
5. **Financeiro NÃO responde** - timeout após 5 segundos (❌)
6. Retorna JSON com `itensBaixados: []` e `pagamentos: []`

**Response esperada** (parcial):
```json
{
  "dadosCadastrais": { ... },
  "consultas": [ ... ],         // ✅ Preenchido
  "exames": [ ... ],            // ✅ Preenchido
  "procedimentos": [ ... ],     // ✅ Preenchido
  "itensBaixados": [],          // ❌ Vazio (deveria ter 1 item)
  "pagamentos": []              // ❌ Vazio (ok, nenhum registrado)
}
```

---

## 10. Conclusão

### 10.1. Status da Discrepância

✅ **DOCUMENTADA**: A implementação parcial foi marcada em todos os arquivos relevantes da documentação.

### 10.2. Arquivos Sincronizados

- ✅ `3.2. Diagrama global de Casos de Uso.md` (2 alterações)
- ✅ `3.3. Descrição detalhada de cada Caso de Uso.md` (2 alterações)
- ✅ `3.5. Diagramas de Processos de Negócio (BPM).md` (1 alteração)
- ✅ `3.9. Modelagem de Interações.md` (1 alteração)

### 10.3. Backend vs. Documentação

| Componente | Documentação | Backend | Status |
|-----------|--------------|---------|--------|
| **UC08 completo (5 módulos)** | ✅ Documentado | 🟡 Parcial | ⚠️ **Implementação parcial marcada** |
| **Dados cadastrais** | ✅ Documentado | ✅ Implementado | ✅ Funcional |
| **Consultas (Agendamento)** | ✅ Documentado | ✅ Implementado | ✅ Funcional |
| **Exames (Agendamento)** | ✅ Documentado | ✅ Implementado | ✅ Funcional |
| **Procedimentos (Agendamento)** | ✅ Documentado | ✅ Implementado | ✅ Funcional |
| **Insumos baixados (Estoque)** | ✅ Documentado | ❌ **Não implementado** | ⚠️ **Implementação parcial marcada** |
| **Pagamentos (Financeiro)** | ✅ Documentado | ❌ **Módulo não existe** | ⚠️ **Implementação parcial marcada** |
| **Sincronização** | ✅ | ✅ | ✅ Completa |

### 10.4. Diferença entre UC07 e UC08

| Aspecto | UC07 (Gerar Alerta) | UC08 (Consultar Histórico) |
|---------|---------------------|----------------------------|
| **Status** | ❌ NÃO IMPLEMENTADO | 🟡 PARCIALMENTE IMPLEMENTADO |
| **Funcionalidade** | 0% funcional | 60% funcional (3 de 5 módulos) |
| **Publisher** | ✅ Existe mas inútil | ✅ Existe e funciona parcialmente |
| **Subscribers** | ❌ Nenhum | 🟡 1 de 3 módulos responde |
| **Tag na doc** | [REDUÇÃO DE ESCOPO] | [IMPLEMENTAÇÃO PARCIAL] |
| **Cor PlantUML** | #LightGray (cinza) | #LightYellow (amarelo) |

### 10.5. Recomendações

1. **Curto Prazo**: Manter documentação com marcações de implementação parcial (✅ **FEITO**)
2. **Médio Prazo**: Implementar subscriber Estoque para histórico de insumos (9-13h de esforço)
3. **Longo Prazo**: Avaliar necessidade de módulo Financeiro completo com stakeholders
4. **Alternativa**: Se Financeiro não for priorizado, documentar definitivamente como "não implementado"

---

**Documento criado em**: 2025-12-14  
**Discrepância**: 5.2 - UC08: Consultar Histórico do Paciente (Implementação Parcial)  
**Tipo de correção**: Documentação (marcação de implementação parcial)  
**Status**: ✅ Concluída
