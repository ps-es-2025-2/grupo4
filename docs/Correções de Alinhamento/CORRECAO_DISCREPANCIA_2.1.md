# Correção da Discrepância 2.1 - PostgreSQL + Cassandra no Módulo Cadastro (Auditoria)

**Data:** 14/12/2025  
**Responsável:** Equipe de Documentação  
**Status:** ✅ Concluída

---

## Resumo Executivo

**Problema**: Documentação mostrava Cassandra no módulo de Cadastro para auditoria, mas implementação usa apenas PostgreSQL + Redis.

**Solução**: Removidas todas as referências a Cassandra no módulo de Cadastro (mantido apenas no Estoque). Arquitetura física corrigida de 6 para 5 containers Docker.

**Arquivos Corrigidos**:
- ✅ `3.6. Arquitetura do Sistema - Lógica e Física.md` (4 alterações)
- ✅ `3.1. Documento de Visão.md` (já estava correto)
- ✅ `3.8. DESIGN_PATTERNS.md` (já estava correto)

**Impacto**: Documentação agora reflete corretamente a implementação real (PostgreSQL para Cadastro, Cassandra apenas para Estoque).

---

## 1. Discrepância Identificada

### Problema
A documentação original mostrava que o **módulo de Cadastro utilizava PostgreSQL + Cassandra** (para auditoria), mas a **implementação real utiliza APENAS PostgreSQL + Redis** (sem Cassandra).

### Diferenças Encontradas

#### Documentação Original (Incorreta):
```
Módulo Cadastro:
├─ PostgreSQL 16 (porta 5430) - Dados principais
├─ Cassandra 5 (porta 9042) - Auditoria (EventoAuditoria)
└─ Redis 7 (porta 6380) - Cache e Pub/Sub
```

#### Implementação Real (Correta):
```
Módulo Cadastro:
├─ PostgreSQL 16 (porta 5430) - Dados principais
└─ Redis 7 (porta 6380) - Pub/Sub APENAS (não cache)

❌ NÃO HÁ: Cassandra
❌ NÃO HÁ: EventoAuditoria
```

---

## 2. Análise da Situação

### 2.1. Verificação do Código Backend

#### pom.xml do Módulo Cadastro
```xml
<dependencies>
    <!-- PostgreSQL -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Redis -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    
    <!-- ❌ NÃO HÁ: spring-boot-starter-data-cassandra -->
</dependencies>
```

**Conclusão:** Nenhuma dependência do Cassandra no `pom.xml` do módulo Cadastro.

#### Entidades do Módulo Cadastro
```
simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/domain/entity/
├─ Pessoa.java (@MappedSuperclass)
├─ Paciente.java (@Entity, PostgreSQL)
├─ Medico.java (@Entity, PostgreSQL)
├─ Usuario.java (@Entity, PostgreSQL)
├─ Convenio.java (@Entity, PostgreSQL)
└─ ❌ NÃO EXISTE: EventoAuditoria.java
```

**Conclusão:** Todas as entidades usam JPA (`@Entity`) para PostgreSQL. Nenhuma usa Cassandra (`@Table`).

#### Docker Compose do Módulo Cadastro
```yaml
services:
  postgres:
    image: postgres:16
    ports:
      - "5430:5432"
  
  redis:
    image: redis:7
    ports:
      - "6380:6379"
  
  # ❌ NÃO HÁ: cassandra
```

**Conclusão:** Container Cassandra não está configurado para o módulo Cadastro.

---

### 2.2. Motivo da Discrepância

#### Redução de Escopo do MVP

A funcionalidade de **auditoria com Cassandra** foi **planejada inicialmente** mas **removida durante o desenvolvimento** por decisão de priorização do MVP.

**Razões da Remoção:**

1. **Prioridade Baixa para MVP**
   - Auditoria não é funcionalidade crítica para o funcionamento básico
   - CRUD de pacientes/médicos/convênios funciona sem auditoria
   - MVP foca em operações essenciais

2. **Complexidade Adicional**
   - Cassandra adiciona complexidade operacional (mais um banco)
   - Configuração, manutenção e monitoramento adicionais
   - Curva de aprendizado

3. **Alternativas Disponíveis**
   - PostgreSQL tem logging nativo (pg_audit se necessário)
   - Logs de aplicação (Logback/SLF4J) já capturam operações
   - Soluções centralizadas (ELK Stack) podem ser adicionadas futuramente

4. **Tempo de Desenvolvimento**
   - Foco em funcionalidades core
   - Auditoria pode ser implementada em versões futuras

---

## 3. Decisão Técnica

### ✅ Decisão: Documentar Corretamente a Arquitetura Real

**Ação:** Atualizar documentação para refletir que o **módulo Cadastro usa APENAS PostgreSQL + Redis**.

**Justificativa:**
1. ✅ Implementação está **correta e funcional**
2. ✅ MVP não requer auditoria avançada
3. ✅ Documentação deve refletir realidade
4. ⚠️ Possível implementação futura de auditoria (se necessário)

---

## 4. Arquivos Corrigidos

### 4.1. Arquitetura do Sistema - Lógica e Física (3.6)

**Arquivo:** `docs/documentos-finais-definitivos/3.6. Arquitetura do Sistema - Lógica e Física/3.6. Arquitetura do Sistema - Lógica e Física.md`

#### Alteração 1: Nota Técnica no Cabeçalho (JÁ EXISTIA)

✅ **Já estava correto:**
```markdown
> **⚠️ NOTA TÉCNICA - Discrepância 1.2 Resolvida:**
> 
> **Redução de Escopo:** Cassandra e EventoAuditoria foram **removidos** 
> do módulo de Cadastro por decisão de redução de escopo do projeto.
> 
> **Justificativa:** Funcionalidade de auditoria não é crítica para MVP.
```

#### Alteração 2: Diagrama de Arquitetura Física

**Antes:**
```plantuml
node "Containers: Bancos Cadastro" as DB_Cadastro_Node {
  database "PostgreSQL 16\n:5430" as DB_Postgres
  database "Cassandra 5\n:9042\n(Auditoria)" as DB_Cassandra_Cad
  database "Redis 7\n:6380\n(Cache)" as Redis_Cad
}

' Conexões
Cadastro_Comp ..> DB_Postgres : "<<JPA/JDBC>>"
Cadastro_Comp ..> DB_Cassandra_Cad : "<<Cassandra Driver>>"
Cadastro_Comp ..> Redis_Cad : "<<Redis Client>>"
```

**Depois:**
```plantuml
node "Containers: Bancos Cadastro" as DB_Cadastro_Node {
  database "PostgreSQL 16\n:5430" as DB_Postgres
  database "Redis 7\n:6380\n(Pub/Sub)" as Redis_Cad
}

note right of DB_Cadastro_Node
  ⚠️ Cassandra removido do Cadastro
  (Discrepância 1.2)
  Auditoria não implementada no MVP
end note

' Conexões
Cadastro_Comp ..> DB_Postgres : "<<JPA/JDBC>>"
Cadastro_Comp ..> Redis_Cad : "<<Redis Client (Pub/Sub)>>"
```

**Mudanças:**
- ❌ Removido: `database "Cassandra 5\n:9042\n(Auditoria)"`
- ❌ Removido: `Cadastro_Comp ..> DB_Cassandra_Cad`
- ✅ Adicionado: Nota explicativa sobre remoção
- ✅ Corrigido: Redis para "Pub/Sub" (não cache)

#### Alteração 3: Camada de Infraestrutura - Persistência (Linha 228)

**Antes:**
```markdown
- **Persistência (Repositories)**: Interfaces para acesso aos bancos de dados
  - Spring Data JPA (PostgreSQL)
  - Spring Data MongoDB
  - Spring Data Cassandra
- **Integração (Middleware)**: Redis Pub/Sub para comunicação assíncrona entre módulos
```

**Depois:**
```markdown
- **Persistência (Repositories)**: Interfaces para acesso aos bancos de dados
  - Spring Data JPA (PostgreSQL - Cadastro)
  - Spring Data MongoDB (Agendamento)
  - Spring Data Cassandra (Estoque apenas)
- **Integração (Middleware)**: Redis Pub/Sub para comunicação assíncrona entre módulos

**⚠️ Nota**: Cassandra utilizado apenas no módulo de Estoque (ver Discrepância 2.1).
```

**Mudanças:**
- ✅ Clarificado: Cada framework ORM/ODM com seu módulo específico
- ✅ Especificado: Spring Data Cassandra usado APENAS no módulo de Estoque
- ✅ Adicionado: Nota de referência cruzada para esta discrepância

#### Alteração 3: Contagem de Containers Docker

**Antes:**
```markdown
- **Problema**: Gerenciar 6 containers Docker 
  (PostgreSQL + 3 Redis + MongoDB + 2 Cassandra)
```

**Depois:**
```markdown
- **Problema**: Gerenciar 5 containers Docker 
  (PostgreSQL + 3 Redis + MongoDB + 1 Cassandra)
- **Nota**: Cassandra removido do módulo Cadastro (ver Discrepância 1.2)
```

**Mudança:**
- 6 containers → 5 containers
- 2 Cassandra → 1 Cassandra (apenas no Estoque)

---

### 4.2. Documento de Visão do Projeto (3.1)

**Arquivo:** `docs/documentos-finais-definitivos/3.1. Documento de Visão do Projeto/Documento de visão do projeto.md`

✅ **Já estava correto:**
```markdown
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  PostgreSQL +   │     │   MongoDB +     │     │  Cassandra +    │
│     Redis       │     │     Redis       │     │     Redis       │
└─────────────────┘     └─────────────────┘     └─────────────────┘

> **⚠️ NOTA - Redução de Escopo:** Cassandra foi removido do módulo 
> de Cadastro (Discrepância 1.2). O módulo usa apenas PostgreSQL + Redis.
```

Nenhuma alteração necessária (nota já existia).

---

### 4.3. Design Patterns (3.8)

**Arquivo:** `docs/documentos-finais-definitivos/3.8 Boas Práticas/DESIGN_PATTERNS.md`

✅ **Já estava correto:**
```markdown
**Spring Data Cassandra** (Módulo Estoque):

> **📝 Nota:** Cassandra foi removido do módulo de Cadastro (Discrepância 1.2). 
> Permanece apenas no módulo de Estoque.
```

Nenhuma alteração necessária (nota já existia).

---

## 5. Validação da Correção

### 5.1. Checklist de Arquivos

- [x] **3.6 - Arquitetura do Sistema**: Cassandra removido do diagrama físico
- [x] **3.6 - Arquitetura do Sistema**: Conexão Cadastro-Cassandra removida
- [x] **3.6 - Arquitetura do Sistema**: Contagem de containers atualizada (6→5)
- [x] **3.6 - Arquitetura do Sistema**: Nota explicativa adicionada no diagrama
- [x] **3.1 - Documento de Visão**: Já possuía nota correta
- [x] **3.8 - Design Patterns**: Já possuía nota correta
- [x] **pom.xml**: Verificado - sem dependências Cassandra
- [x] **docker-compose.yml**: Verificado - sem container Cassandra

### 5.2. Arquitetura Atual Validada

```
┌─────────────────────────────────────────────────────────────────┐
│                         SIMPLEHEALTH                           │
│                  Arquitetura de Microsserviços                 │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────────┐  ┌──────────────────────┐  ┌──────────────────────┐
│   MÓDULO CADASTRO    │  │  MÓDULO AGENDAMENTO  │  │   MÓDULO ESTOQUE     │
├──────────────────────┤  ├──────────────────────┤  ├──────────────────────┤
│ Spring Boot :8081    │  │ Spring Boot :8082    │  │ Spring Boot :8083    │
├──────────────────────┤  ├──────────────────────┤  ├──────────────────────┤
│ PostgreSQL 16 :5430  │  │ MongoDB 6.0 :27017   │  │ Cassandra 5 :9042    │
│ Redis 7 :6380        │  │ Redis 7 :6379        │  │ Redis 7 :6381        │
│   (Pub/Sub)          │  │   (Pub/Sub)          │  │   (Pub/Sub)          │
└──────────────────────┘  └──────────────────────┘  └──────────────────────┘
         │                         │                         │
         └─────────────────────────┼─────────────────────────┘
                                   │
                          ┌────────▼────────┐
                          │  Redis Central  │
                          │   (Pub/Sub)     │
                          └─────────────────┘
```

**Total de Tecnologias:**
- 1 PostgreSQL (Cadastro)
- 1 MongoDB (Agendamento)
- 1 Cassandra (Estoque apenas)
- 4 Redis (3 locais + 1 central)
- **Total:** 5 containers de banco de dados

---

## 6. Comparação: Planejado vs Implementado

| Aspecto | Documentação Original | Implementação Real | Status |
|---------|----------------------|-------------------|--------|
| **Cadastro - PostgreSQL** | ✅ Sim | ✅ Sim | ✅ Correto |
| **Cadastro - Redis** | ✅ Sim (Cache) | ✅ Sim (Pub/Sub) | ⚠️ Uso diferente* |
| **Cadastro - Cassandra** | ✅ Sim (Auditoria) | ❌ Não | ❌ Removido |
| **Cadastro - EventoAuditoria** | ✅ Sim | ❌ Não | ❌ Não implementado |
| **Agendamento - MongoDB** | ✅ Sim | ✅ Sim | ✅ Correto |
| **Agendamento - Redis** | ✅ Sim | ✅ Sim | ✅ Correto |
| **Estoque - Cassandra** | ✅ Sim | ✅ Sim | ✅ Correto |
| **Estoque - Redis** | ✅ Sim | ✅ Sim | ✅ Correto |

*Nota: Redis no Cadastro é usado apenas para Pub/Sub, não para cache (ver Discrepância 1.3).

---

## 7. Funcionalidades de Auditoria

### 7.1. Planejamento Original (Não Implementado)

```java
// EventoAuditoria.java (PLANEJADO, NÃO IMPLEMENTADO)
@Table("evento_auditoria")
public class EventoAuditoria {
    @PrimaryKey
    private UUID id;
    
    @Column("usuario_login")
    private String usuarioLogin;
    
    @Column("acao")
    private String acao;  // CREATE, UPDATE, DELETE
    
    @Column("entidade")
    private String entidade;  // Paciente, Medico, etc.
    
    @Column("entidade_id")
    private String entidadeId;
    
    @Column("data_hora")
    private LocalDateTime dataHora;
    
    @Column("dados_antes")
    private String dadosAntes;  // JSON
    
    @Column("dados_depois")
    private String dadosDepois;  // JSON
}
```

**Casos de Uso Planejados:**
- UC: Auditar criação de paciente
- UC: Auditar atualização de médico
- UC: Auditar exclusão de convênio
- UC: Consultar histórico de alterações

**Por que Cassandra era adequado:**
- ✅ Alta performance de escrita (logs constantes)
- ✅ Retenção de dados históricos (time-series)
- ✅ Escalabilidade horizontal
- ✅ Modelo append-only (imutável)

### 7.2. Alternativas Atuais (MVP)

Embora a auditoria avançada não esteja implementada, o sistema possui:

#### 1. Logs de Aplicação (Logback)
```java
@Service
public class PacienteService {
    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);
    
    public Paciente criar(PacienteDTO dto) {
        logger.info("Criando paciente: CPF={}", dto.getCpf());
        Paciente paciente = pacienteRepository.save(paciente);
        logger.info("Paciente criado: ID={}", paciente.getId());
        return paciente;
    }
}
```

**Vantagens:**
- ✅ Simples
- ✅ Já implementado
- ✅ Logs em arquivo (rotacionados)

**Limitações:**
- ❌ Não estruturado (texto livre)
- ❌ Difícil consultar logs antigos
- ❌ Sem dados "antes/depois"

#### 2. Histórico no PostgreSQL (Futuro)

Se auditoria for necessária, pode-se usar:

**Opção A: Tabela de Auditoria no PostgreSQL**
```sql
CREATE TABLE auditoria (
    id SERIAL PRIMARY KEY,
    usuario_login VARCHAR(50),
    acao VARCHAR(20),
    entidade VARCHAR(50),
    entidade_id VARCHAR(50),
    data_hora TIMESTAMP,
    dados_antes JSONB,
    dados_depois JSONB
);

CREATE INDEX idx_auditoria_entidade ON auditoria(entidade, entidade_id);
CREATE INDEX idx_auditoria_data ON auditoria(data_hora DESC);
```

**Vantagens:**
- ✅ Mesma tecnologia (PostgreSQL)
- ✅ Queries relacionais (JOINs)
- ✅ JSONB para flexibilidade

**Limitações:**
- ⚠️ Menor performance que Cassandra em alto volume
- ⚠️ Escalabilidade vertical

**Opção B: PostgreSQL + pg_audit**

Usar extensão nativa do PostgreSQL para auditoria:
```sql
-- Habilitar pg_audit
CREATE EXTENSION pg_audit;

-- Configurar auditoria
ALTER SYSTEM SET pgaudit.log = 'all';
```

**Vantagens:**
- ✅ Nativo do PostgreSQL
- ✅ Captura automaticamente DDL e DML
- ✅ Sem código adicional

**Limitações:**
- ⚠️ Logs verbosos
- ⚠️ Difícil filtrar/consultar

---

## 8. Impacto da Mudança

### 8.1. Impacto na Documentação
- ✅ **Positivo:** Documentação agora reflete realidade
- ✅ **Clareza:** Arquitetura simplificada (menos tecnologias)
- ✅ **Manutenção:** Menos complexidade para explicar

### 8.2. Impacto no Sistema
- ✅ **Nenhum:** Implementação já estava correta
- ✅ **Simplicidade:** Menos containers para gerenciar
- ✅ **Performance:** PostgreSQL suficiente para MVP

### 8.3. Impacto em Funcionalidades
- ⚠️ **Auditoria avançada não disponível**
- ✅ **Logs básicos disponíveis** (Logback)
- ✅ **Todas funcionalidades core funcionam**

---

## 9. Recomendações Futuras

### Quando Implementar Auditoria?

✅ **Implementar SE:**
1. Sistema entrar em produção com múltiplos usuários
2. Necessidade de conformidade regulatória (LGPD, HIPAA)
3. Investigação de incidentes (quem alterou o quê?)
4. Análise de padrões de uso

### Estratégias de Implementação

#### Estratégia 1: Auditoria com PostgreSQL (Mais Simples)
```java
@Service
public class AuditoriaService {
    @Autowired
    private AuditoriaRepository auditoriaRepository;
    
    public void registrar(String acao, String entidade, Object antes, Object depois) {
        Auditoria log = new Auditoria();
        log.setAcao(acao);
        log.setEntidade(entidade);
        log.setDadosAntes(toJson(antes));
        log.setDadosDepois(toJson(depois));
        log.setDataHora(LocalDateTime.now());
        auditoriaRepository.save(log);
    }
}
```

**Esforço:** Baixo (2-3 dias)

#### Estratégia 2: Auditoria com Cassandra (Mais Escalável)
```java
@Service
public class AuditoriaCassandraService {
    @Autowired
    private EventoAuditoriaRepository eventoRepository;
    
    public void registrar(EventoAuditoria evento) {
        // Cassandra é append-only, alta performance
        eventoRepository.save(evento);
    }
}
```

**Esforço:** Médio (5-7 dias) - adicionar dependências, containers, configuração

#### Estratégia 3: Solução Centralizada (ELK Stack)
```
Cadastro ──> Logstash ──> Elasticsearch ──> Kibana
Agendamento ──┘              │                 │
Estoque ─────────────────────┘                 │
                                               ▼
                                         Dashboards
```

**Esforço:** Alto (10-15 dias) - infraestrutura completa

---

## 10. Lições Aprendidas

### 10.1. Priorização de MVP

✅ **Positivo:**
- Foco em funcionalidades core
- Redução de complexidade
- Time-to-market mais rápido

⚠️ **Atenção:**
- Documentar redução de escopo claramente
- Manter backlog para versões futuras

### 10.2. Documentação vs Implementação

✅ **Importante:**
- Documentação deve refletir realidade
- Notas de discrepâncias são valiosas
- Histórico de decisões (ADRs)

### 10.3. Persistência Poliglota

✅ **Aprendizado:**
- Nem sempre mais tecnologias = melhor
- Simplicidade tem valor
- PostgreSQL é versátil (pode fazer muito)

---

## 11. Checklist Final

- [x] Código backend verificado (pom.xml, entidades, docker-compose)
- [x] Cassandra confirmado como ausente no módulo Cadastro
- [x] Diagrama de arquitetura física corrigido (3.6)
- [x] Conexão Cadastro-Cassandra removida
- [x] Contagem de containers atualizada (6→5)
- [x] Nota explicativa adicionada no diagrama
- [x] Documento de Visão verificado (3.1) - já estava correto
- [x] Design Patterns verificado (3.8) - já estava correto
- [x] Alternativas de auditoria documentadas
- [x] Recomendações futuras criadas

---

## 12. Referências

### Código Backend
- `simplehealth-back-cadastro/pom.xml` - Sem dependências Cassandra
- `simplehealth-back-cadastro/docker-compose.yml` - Sem container Cassandra
- `simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/domain/entity/` - Apenas entidades JPA

### Documentação Corrigida
- [Arquitetura do Sistema](./documentos-finais-definitivos/3.6.%20Arquitetura%20do%20Sistema%20-%20Lógica%20e%20Física/3.6.%20Arquitetura%20do%20Sistema%20-%20Lógica%20e%20Física.md)

### Documentação Já Correta
- [Documento de Visão](./documentos-finais-definitivos/3.1.%20Documento%20de%20Visão%20do%20Projeto/Documento%20de%20visão%20do%20projeto.md)
- [Design Patterns](./documentos-finais-definitivos/3.8%20Boas%20Práticas/DESIGN_PATTERNS.md)

### Outras Discrepâncias
- [Discrepância 1.1 - Médico vs Usuario](./CORRECAO_DISCREPANCIA_1.1.md)
- [Discrepância 1.2 - EventoAuditoria com Cassandra](./CORRECAO_DISCREPANCIA_1.2.md) - Relacionada!
- [Discrepância 1.3 - Redis para Cache](./CORRECAO_DISCREPANCIA_1.3.md)
- [Discrepância 1.4 - Atributos de Agendamento](./CORRECAO_DISCREPANCIA_1.4.md)
- [Discrepância 1.5 - Atributos de Rastreamento](./CORRECAO_DISCREPANCIA_1.5.md)
- [Discrepância 1.7 - Relacionamento Item-Estoque](./CORRECAO_DISCREPANCIA_1.7.md)

---

**Assinatura Digital:**  
Correção realizada e validada em 14/12/2025  
Documentação sincronizada com implementação real  
Cassandra removido do módulo Cadastro (apenas PostgreSQL + Redis)  
Auditoria não implementada no MVP - possível expansão futura
