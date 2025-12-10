# 🏗️ Arquitetura do Sistema - SimpleHealth

## 📖 Decisões Arquiteturais

### Por que Microsserviços?

**Decisão**: Dividir o sistema em 3 módulos independentes (Cadastro, Agendamento, Estoque)

**Justificativa**:
1. **Escalabilidade Independente**: Cada módulo pode escalar conforme demanda
   - Cadastro: baixa frequência, alta consistência
   - Agendamento: alta frequência em horários específicos
   - Estoque: média frequência, picos em horários de dispensação

2. **Tecnologias Específicas**: Cada módulo usa o banco mais adequado para seu domínio

3. **Resiliência**: Falha em um módulo não derruba o sistema todo

4. **Desenvolvimento Paralelo**: Times diferentes podem trabalhar simultaneamente

---

## 🗄️ Decisões de Banco de Dados

### Módulo Cadastro: PostgreSQL + Cassandra + Redis

#### PostgreSQL 16 (Banco Principal)
**Porta**: 5430

**Por quê?**
1. **Consistência ACID**: Dados de pacientes/médicos requerem transações confiáveis
2. **Relacionamentos Complexos**: JOINs entre Paciente ↔ Convênio ↔ Médico
3. **Queries Complexas**: Busca por CPF, nome, especialidade médica
4. **Maturidade**: Banco robusto, bem documentado

**Trade-offs**:
- ✅ Consistência forte
- ✅ Queries relacionais
- ❌ Escalabilidade horizontal limitada
- ❌ Menor performance em altíssimo volume

**Entidades**: Paciente, Médico, Usuário, Convênio

#### Cassandra (Dados de Auditoria)
**Porta**: 9042

**Por quê?**
1. **Alta Disponibilidade**: Logs de auditoria não podem ser perdidos
2. **Write-Heavy**: Milhares de eventos de auditoria por dia
3. **Time Series**: Dados ordenados por timestamp
4. **Escalabilidade**: Preparado para crescimento futuro

**Trade-offs**:
- ✅ Alta disponibilidade (sem single point of failure)
- ✅ Write performance excelente
- ✅ Escalabilidade horizontal fácil
- ❌ Queries limitadas (sem JOINs)
- ❌ Eventual consistency

**Entidades**: EventoAuditoria

#### Redis 7 (Cache)
**Porta**: 6380

**Por quê?**
1. **Performance**: Cache de listas de médicos disponíveis (consulta frequente)
2. **Session Storage**: Sessões de usuários logados
3. **Pub/Sub**: Comunicação entre módulos (alertas de estoque crítico)

**Trade-offs**:
- ✅ Performance altíssima (in-memory)
- ✅ Estruturas de dados ricas (Lists, Sets, Hashes)
- ❌ Volatilidade (não é banco primário)
- ❌ Limitado pela RAM

---

### Módulo Agendamento: MongoDB + Redis

#### MongoDB 6.0
**Porta**: 27017

**Por quê?**
1. **Flexibilidade**: Consultas, Exames e Procedimentos têm estruturas diferentes
2. **Schema-less**: Facilita evolução dos tipos de agendamento
3. **Embedded Documents**: Agenda médica pode ter disponibilidades embutidas
4. **Queries Geoespaciais**: Futuro - agendar por localização

**Trade-offs**:
- ✅ Flexibilidade de schema
- ✅ Performance boa para reads/writes
- ✅ Escalabilidade horizontal (sharding)
- ❌ Sem transações ACID completas (até versão 4.0)
- ❌ Queries relacionais menos eficientes

**Entidades**: Consulta, Exame, Procedimento, BloqueioAgenda

#### Redis 7 (Cache)
**Porta**: 6379

**Por quê?**
1. **Cache de Agendas**: Disponibilidade de médicos (consulta a cada clique)
2. **Locks Distribuídos**: Evitar double-booking de horários

---

### Módulo Estoque: Cassandra + Redis

#### Cassandra 5
**Porta**: 9042

**Por quê?**
1. **Alta Disponibilidade**: Estoque crítico não pode ficar offline
2. **Write-Heavy**: Movimentações constantes de entrada/saída
3. **Particionamento Natural**: Dados por localizacao/setor/tipo
4. **Time Series**: Histórico de movimentações ordenado por tempo

**Trade-offs**:
- ✅ Alta disponibilidade
- ✅ Escalabilidade horizontal
- ✅ Performance em writes
- ❌ Queries complexas difíceis
- ❌ Modelagem exige planejamento (denormalização)

**Entidades**: Medicamento, Alimento, Hospitalar, Fornecedor, Estoque, Pedido, Item

#### Redis 7 (Cache + Pub/Sub)
**Porta**: 6381

**Por quê?**
1. **Cache de Estoque Crítico**: Lista de itens abaixo do mínimo
2. **Pub/Sub**: Alertas para módulo Cadastro quando estoque crítico

---

## 🔄 Integração entre Módulos

### Redis Pub/Sub

**Exemplo**: Alerta de Estoque Crítico

```
Módulo Estoque                Redis                 Módulo Cadastro
     │                          │                          │
     │  1. PUBLISH              │                          │
     │  "estoque:alerta"        │                          │
     │  { "medicamento": "X" }  │                          │
     ├─────────────────────────>│                          │
     │                          │  2. SUBSCRIBE            │
     │                          │  "estoque:alerta"        │
     │                          │<─────────────────────────┤
     │                          │  3. Mensagem recebida    │
     │                          ├─────────────────────────>│
     │                          │                          │
     │                          │  4. Notifica médicos     │
     │                          │     responsáveis         │
```

**Implementação**:
- Publisher: `EstoqueAlertaPublisher.java` (Estoque)
- Subscriber: `EstoqueAlertaSubscriber.java` (Cadastro)

---

## 📊 Comparação de Bancos

| Banco | Tipo | Consistência | Escalabilidade | Casos de Uso |
|-------|------|--------------|----------------|--------------|
| **PostgreSQL** | Relacional | ACID (Forte) | Vertical | Cadastros, transações |
| **MongoDB** | Documento | Eventual | Horizontal | Agendamentos flexíveis |
| **Cassandra** | Wide-Column | Eventual | Horizontal | Auditoria, logs, estoque |
| **Redis** | Key-Value | Forte (single-node) | Vertical | Cache, pub/sub, sessions |

---

## 🎯 Benefícios da Arquitetura Poliglota

1. **Cada problema com sua solução ideal**
   - Cadastro: ACID com PostgreSQL
   - Agendamento: Flexibilidade com MongoDB
   - Estoque: Alta disponibilidade com Cassandra

2. **Aprendizado Tecnológico**
   - Experiência com 4 bancos de dados diferentes
   - Compreensão de trade-offs

3. **Preparação para Mundo Real**
   - Empresas usam múltiplos bancos (ex: Netflix, Uber)
   - Arquitetura reflete cenários reais

---

## Desafios e Complexidade

### Complexidade Operacional
- **Problema**: Gerenciar 6 containers Docker (3 Cassandra + 2 Redis + 1 PostgreSQL + 1 MongoDB)
- **Solução**: Scripts `start-all.sh`, `stop-all.sh`, `status.sh`

### Consistência Entre Módulos
- **Problema**: Eventual consistency entre módulos
- **Solução**: Design aceita inconsistências temporárias (não crítico)

### Aprendizado
- **Problema**: Curva de aprendizado de 4 tecnologias
- **Solução**: Documentação extensa, exemplos práticos

---