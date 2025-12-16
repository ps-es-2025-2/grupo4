# Correção da Discrepância 1.2 - EventoAuditoria com Cassandra Não Implementado

**Data:** 14/12/2025  
**Responsável:** Equipe de Documentação  
**Status:** ✅ Concluída

---

## 1. Discrepância Identificada

### Problema
A documentação indicava que o **módulo de Cadastro** utilizaria **Cassandra 5** para armazenar dados de **EventoAuditoria**, mas esta funcionalidade **não foi implementada**.

### Evidências
- ✅ **Backend:** Nenhum código relacionado a Cassandra ou EventoAuditoria encontrado
- ✅ **pom.xml:** Sem dependência `spring-boot-starter-data-cassandra`
- ✅ **Estrutura:** Nenhuma classe `EventoAuditoria` no domínio
- ❌ **Documentação:** Múltiplas referências a Cassandra e auditoria

---

## 2. Análise da Situação

### Motivo da Não Implementação
**Redução de Escopo:** Funcionalidade de auditoria foi considerada **não crítica** para o MVP (Minimum Viable Product) do projeto acadêmico.

### Priorização
A equipe optou por focar nos requisitos funcionais principais:
1. ✅ Cadastro de pacientes, médicos e usuários
2. ✅ Agendamento de consultas
3. ✅ Controle de estoque

### Justificativa Técnica
- Auditoria é uma funcionalidade de **qualidade/observabilidade**, não funcional crítica
- Pode ser implementada posteriormente usando:
  - PostgreSQL (tabela de audit log)
  - Soluções de logging centralizadas (ELK Stack, Splunk)
  - Event Sourcing pattern
  - Spring Boot Actuator + Micrometer

---

## 3. Decisão Técnica

### ✅ Decisão: Documentar Redução de Escopo

**Ação:** Atualizar documentação para refletir que Cassandra/EventoAuditoria **não foram implementados** no módulo de Cadastro.

**Nota Importante:** Cassandra **permanece** no módulo de **Estoque**, onde foi efetivamente implementado.

---

## 4. Arquivos Corrigidos

### 4.1. Arquitetura do Sistema - Lógica e Física
**Arquivo:** `docs/documentos-finais-definitivos/3.6. Arquitetura do Sistema - Lógica e Física/3.6. Arquitetura do Sistema - Lógica e Física.md`

**Alterações:**
- ✅ Título corrigido: `Módulo Cadastro: PostgreSQL + Redis` (removido Cassandra)
- ✅ Seção completa de Cassandra removida
- ✅ Nota técnica adicionada explicando a redução de escopo
- ✅ Confirmado: Redis permanece para cache

**Antes:**
```markdown
### Módulo Cadastro: PostgreSQL + Cassandra + Redis

#### Cassandra 5 (Dados de Auditoria)
**Entidades**: EventoAuditoria
```

**Depois:**
```markdown
### Módulo Cadastro: PostgreSQL + Redis

> ⚠️ NOTA TÉCNICA - Discrepância 1.2 Resolvida:
> Cassandra e EventoAuditoria foram removidos por redução de escopo.
```

---

### 4.2. Documento de Visão do Projeto
**Arquivo:** `docs/documentos-finais-definitivos/3.1. Documento de Visão do Projeto/Documento de visão do projeto.md`

**Alterações:**
- ✅ Diagrama ASCII atualizado (removido Cassandra do Cadastro)
- ✅ Nota adicionada abaixo do diagrama
- ✅ Tabela de capacidades: removida linha "Auditoria completa"
- ✅ Tecnologias do módulo Cadastro: removida linha "Auditoria: Cassandra 5"
- ✅ Restrições de conformidade: removida "Auditoria"
- ✅ Requisitos de confiabilidade: removida "Auditoria completa em Cassandra"
- ✅ Tabela de Persistência Poliglota: removida linha de Auditoria
- ✅ Nota adicionada na tabela
- ✅ Tecnologias do Estoque: mantido Cassandra com nota explicativa
- ✅ Stakeholders: "Relatórios e auditoria" → "Relatórios gerenciais"

**Antes:**
```
│  PostgreSQL +   │
│  Cassandra +    │
│     Redis       │
```

**Depois:**
```
│  PostgreSQL +   │
│     Redis       │
│                 │

> ⚠️ NOTA: Cassandra foi removido do módulo de Cadastro (Discrepância 1.2).
```

---

### 4.3. Design Patterns (Boas Práticas)
**Arquivo:** `docs/documentos-finais-definitivos/3.8 Boas Práticas/DESIGN_PATTERNS.md`

**Alterações:**
- ✅ Mantido exemplo de Spring Data Cassandra (módulo Estoque)
- ✅ Nota adicionada clarificando que Cassandra permanece no Estoque

**Nota Adicionada:**
```markdown
> 📝 Nota: Cassandra foi removido do módulo de Cadastro (Discrepância 1.2). 
> Permanece apenas no módulo de Estoque.
```

---

## 5. Validação

### 5.1. Código Backend Revisado
- ✅ **Cadastro:** Sem referências a Cassandra
- ✅ **Cadastro:** Sem classe `EventoAuditoria`
- ✅ **Cadastro/pom.xml:** Sem dependência Cassandra
- ✅ **Estoque:** Cassandra implementado e funcional

### 5.2. Documentação Alinhada
- ✅ Arquitetura: PostgreSQL + Redis apenas
- ✅ Visão do Projeto: diagramas e tabelas atualizados
- ✅ Boas Práticas: nota explicativa adicionada
- ✅ Todas as referências a "auditoria em Cassandra" removidas do Cadastro

---

## 6. Arquitetura Atual (Corrigida)

### Módulo de Cadastro

```
┌─────────────────────────────┐
│   simplehealth-back-cadastro│
│      (Spring Boot 3.5.6)    │
└──────────┬──────────────────┘
           │
           ├─────────► PostgreSQL 16:5430
           │           (Paciente, Medico, Usuario, Convenio)
           │
           └─────────► Redis 7:6380
                       (Cache, Sessions)
```

### Módulo de Estoque (mantém Cassandra)

```
┌─────────────────────────────┐
│   simplehealth-back-estoque │
│      (Spring Boot 3.5.6)    │
└──────────┬──────────────────┘
           │
           ├─────────► Cassandra 5:9042
           │           (Medicamento, Movimentacao)
           │
           └─────────► Redis 7:6381
                       (Cache)
```

---

## 7. Impacto da Mudança

### Impacto na Documentação
- ✅ **Baixo:** Apenas remoção de seções não implementadas
- ✅ **Positivo:** Documentação agora reflete a realidade

### Impacto no Sistema
- ✅ **Nenhum:** Funcionalidade nunca foi implementada
- ✅ **Sem quebras:** Nenhum código dependia de auditoria

### Funcionalidades Afetadas
- ❌ **Auditoria de operações críticas:** Não implementada
- ✅ **Todos os requisitos funcionais:** Sem impacto

---

## 8. Próximos Passos

### Para Versões Futuras (se necessário)

Se a funcionalidade de auditoria for desejada no futuro, as opções são:

#### Opção 1: PostgreSQL (Mais Simples)
```sql
CREATE TABLE evento_auditoria (
    id BIGSERIAL PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    usuario_id BIGINT REFERENCES usuario(id),
    acao VARCHAR(50) NOT NULL,
    entidade VARCHAR(50) NOT NULL,
    entidade_id BIGINT,
    dados_anteriores JSONB,
    dados_novos JSONB
);
```

**Vantagens:**
- ✅ Mesma stack tecnológica
- ✅ ACID completo
- ✅ Fácil manutenção

**Desvantagens:**
- ❌ Pode impactar performance com alto volume

#### Opção 2: Cassandra (Mais Escalável)
```java
@Table("evento_auditoria")
public class EventoAuditoria {
    @PrimaryKey
    private UUID id;
    private LocalDateTime timestamp;
    private String usuarioId;
    private String acao;
    private String entidade;
    private String dadosAnteriores;
    private String dadosNovos;
}
```

**Vantagens:**
- ✅ Alta disponibilidade
- ✅ Write-heavy workload
- ✅ Escalabilidade horizontal

**Desvantagens:**
- ❌ Complexidade adicional
- ❌ Nova stack tecnológica

#### Opção 3: Spring Boot Actuator + ELK
- Logs centralizados com Elasticsearch
- Dashboards no Kibana
- Sem código adicional no domínio

---

## 9. Lições Aprendidas

### Gestão de Escopo
- ✅ Redução de escopo é normal em projetos acadêmicos
- ✅ Importante documentar decisões de forma clara
- ✅ Priorizar funcionalidades críticas para o MVP

### Documentação Técnica
- ⚠️ Manter sincronia entre código e documentação
- ⚠️ Revisar documentação quando há mudanças de escopo
- ✅ Usar notas técnicas para explicar discrepâncias

### Arquitetura
- ✅ Persistência poliglota traz benefícios, mas também complexidade
- ✅ Cada tecnologia adicional requer justificativa clara
- ✅ MVP deve focar no essencial

---

## 10. Checklist de Validação

- [x] Código backend verificado (sem Cassandra no Cadastro)
- [x] pom.xml verificado (sem dependência)
- [x] Arquitetura documentada corrigida
- [x] Visão do Projeto atualizada
- [x] Diagramas ASCII corrigidos
- [x] Tabelas atualizadas
- [x] Notas técnicas adicionadas
- [x] Design Patterns com nota explicativa
- [x] Estoque mantém Cassandra (correto)
- [x] Todas as referências incorretas removidas

---

## 11. Referências

### Arquivos Relacionados
- Nenhum arquivo de código (funcionalidade não implementada)

### Documentação Corrigida
- [Arquitetura do Sistema](./documentos-finais-definitivos/3.6.%20Arquitetura%20do%20Sistema%20-%20Lógica%20e%20Física/3.6.%20Arquitetura%20do%20Sistema%20-%20Lógica%20e%20Física.md)
- [Documento de Visão](./documentos-finais-definitivos/3.1.%20Documento%20de%20Visão%20do%20Projeto/Documento%20de%20visão%20do%20projeto.md)
- [Design Patterns](./documentos-finais-definitivos/3.8%20Boas%20Práticas/DESIGN_PATTERNS.md)

### Outras Discrepâncias
- [Discrepância 1.1 - Médico vs Usuario](./CORRECAO_DISCREPANCIA_1.1.md)

---

**Assinatura Digital:**  
Correção realizada e validada em 14/12/2025  
Documentação sincronizada com implementação real do backend  
Redução de escopo devidamente documentada
