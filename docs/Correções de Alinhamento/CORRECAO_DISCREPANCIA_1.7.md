# Correção da Discrepância 1.7 - Relacionamento Item ↔ Estoque Faltante

**Data:** 14/12/2025  
**Responsável:** Equipe de Documentação  
**Status:** ⚠️ Análise de Impacto Realizada - Relacionamento NÃO Implementado

---

## 1. Discrepância Identificada

### Problema
A documentação mostra um relacionamento **`Item "*" --> "1" Estoque`** (cada Item está armazenado em um Estoque), mas a **implementação NO CASSANDRA não possui este relacionamento**.

### Diferenças Encontradas

#### Documentação (Relacionamento Explícito):
```plantuml
class Item {
    - idItem: Long {PK}
    - nome: String
    - quantidadeTotal: Integer
    - validade: Date
}

class Estoque {
    - idEstoque: Long {PK}
    - local: String
}

Item "*" --> "1" Estoque : armazenado em >
```

#### Implementação Real (SEM Relacionamento):
```java
// Item.java - Classe abstrata
@Data
public abstract class Item {
    @PrimaryKey
    private UUID idItem = UUID.randomUUID();
    
    @Column("nome")
    private String nome;
    
    @Column("quantidade_total")
    private Integer quantidadeTotal;
    
    @Column("validade")
    private Date validade;
    
    // ❌ NÃO HÁ: private UUID estoqueId;
}

// Estoque.java
@Table("estoque")
@Data
public class Estoque {
    @PrimaryKey
    private UUID idEstoque = UUID.randomUUID();
    
    @Column
    private String local;
    
    // ❌ NÃO HÁ: private List<UUID> itemIds;
}
```

---

## 2. Análise de Impacto

### 2.1. Por Que o Relacionamento Não Foi Implementado?

#### Decisão de Design NoSQL (Cassandra)

**Cassandra é um banco de dados NoSQL orientado a colunas**, não relacional. A ausência do relacionamento é uma **decisão arquitetural consciente** baseada nos seguintes princípios:

#### 1️⃣ **Cassandra Não Suporta Joins Nativamente**
```
❌ NÃO É POSSÍVEL no Cassandra:
SELECT i.*, e.local 
FROM item i 
JOIN estoque e ON i.estoque_id = e.id_estoque

✅ PADRÃO no Cassandra:
- Tabelas desnormalizadas
- Dados duplicados para performance
- Queries específicas por caso de uso
```

#### 2️⃣ **Modelo de Consulta do Sistema**

Analisando os casos de uso implementados:
```
UC05 - Dar Baixa em Item:
└─ Input: itemId, quantidade
└─ Query: SELECT * FROM medicamento WHERE id_item = ?
└─ ✅ NÃO PRECISA de estoqueId

UC06 - Processar Entrada NF:
└─ Input: lista de itens (nome, quantidade, validade)
└─ Query: INSERT INTO medicamento (...)
└─ ✅ NÃO PRECISA de estoqueId

UC07 - Alerta de Estoque Crítico:
└─ Query: SELECT * FROM medicamento WHERE quantidade_total < ?
└─ ✅ NÃO PRECISA de estoqueId

UC10 - Controlar Validade:
└─ Query: SELECT * FROM medicamento WHERE validade < ?
└─ ✅ NÃO PRECISA de estoqueId
```

**Conclusão:** Nenhum caso de uso implementado **REQUER** o relacionamento Item ↔ Estoque.

#### 3️⃣ **Escopo MVP Simplificado**

O sistema atual opera com um **estoque único** (implícito), sem necessidade de múltiplos locais:
```
Cenário Atual (MVP):
└─ 1 Hospital
    └─ 1 Estoque Central
        └─ Todos os itens (Medicamentos, Hospitalares, Alimentos)

Relacionamento Item-Estoque seria necessário para:
❌ Múltiplos estoques por hospital (Central, Farmácia, UTI)
❌ Transferências entre estoques
❌ Controle de localização física
```

---

### 2.2. Impacto de Implementar o Relacionamento

#### ❌ **ALTO IMPACTO** - Mudança Estrutural Significativa

##### 1. **Alteração no Schema Cassandra**

**Problema:** Cassandra não suporta `ALTER TABLE ADD COLUMN` com dados existentes de forma simples.

**Necessário:**
```java
// 1. Adicionar coluna em Item (abstrata)
@Data
public abstract class Item {
    @PrimaryKey
    private UUID idItem = UUID.randomUUID();
    
    // ... campos existentes ...
    
    @Column("estoque_id")  // ← NOVO
    private UUID estoqueId;
}

// 2. Atualizar todas as subclasses
// - Medicamento.java
// - Hospitalar.java
// - Alimento.java
```

**Impacto:**
- ⚠️ Migração de dados existentes (todos os itens precisam de estoqueId)
- ⚠️ Risco de inconsistência se não migrado corretamente
- ⚠️ Downtime durante migração

##### 2. **Refatoração de Repositories**

```java
// ItemRepository atual
public interface MedicamentoRepository extends CassandraRepository<Medicamento, UUID> {
    List<Medicamento> findByQuantidadeTotalLessThan(Integer quantidade);
    List<Medicamento> findByValidadeBefore(Date data);
}

// ItemRepository NOVO (com relacionamento)
public interface MedicamentoRepository extends CassandraRepository<Medicamento, UUID> {
    List<Medicamento> findByQuantidadeTotalLessThan(Integer quantidade);
    List<Medicamento> findByValidadeBefore(Date data);
    
    // ← NOVOS métodos
    List<Medicamento> findByEstoqueId(UUID estoqueId);
    List<Medicamento> findByEstoqueIdAndQuantidadeTotalLessThan(UUID estoqueId, Integer quantidade);
    List<Medicamento> findByEstoqueIdAndValidadeBefore(UUID estoqueId, Date data);
}
```

**Impacto:**
- 🔧 3 repositories alterados (Medicamento, Hospitalar, Alimento)
- 🔧 6+ novos métodos de consulta

##### 3. **Refatoração de Services**

```java
// EstoqueService atual
@Service
public class EstoqueService {
    public void darBaixa(UUID itemId, Integer quantidade) {
        // Lógica atual: apenas atualiza item
    }
    
    public List<Medicamento> verificarEstoqueCritico(Integer limiar) {
        return medicamentoRepository.findByQuantidadeTotalLessThan(limiar);
    }
}

// EstoqueService NOVO (com relacionamento)
@Service
public class EstoqueService {
    public void darBaixa(UUID itemId, Integer quantidade) {
        // ← PRECISA validar se item pertence ao estoque
        Item item = itemRepository.findById(itemId);
        if (item.getEstoqueId() == null) {
            throw new BusinessException("Item não vinculado a estoque");
        }
        // ... resto da lógica
    }
    
    public List<Medicamento> verificarEstoqueCritico(UUID estoqueId, Integer limiar) {
        // ← AGORA precisa filtrar por estoque
        return medicamentoRepository.findByEstoqueIdAndQuantidadeTotalLessThan(estoqueId, limiar);
    }
}
```

**Impacto:**
- 🔧 5+ métodos de serviço alterados
- ⚠️ Lógica de validação adicional
- ⚠️ Performance: queries mais complexas

##### 4. **Alteração de DTOs e Controllers**

```java
// EntradaItensDTO atual
@Data
public class EntradaItensDTO {
    private String nome;
    private Integer quantidade;
    private Date validade;
}

// EntradaItensDTO NOVO
@Data
public class EntradaItensDTO {
    private String nome;
    private Integer quantidade;
    private Date validade;
    private UUID estoqueId;  // ← NOVO (obrigatório)
}
```

**Impacto:**
- 🔧 4+ DTOs alterados
- 🔧 6+ endpoints REST alterados
- ⚠️ Breaking change na API (clientes precisam enviar estoqueId)

##### 5. **Testes Unitários e de Integração**

```java
// Testes atuais
@Test
void testDarBaixa() {
    medicamentoService.darBaixa(itemId, 10);
    // ...
}

// Testes NOVOS (com relacionamento)
@Test
void testDarBaixa_ItemVinculadoAEstoque() {
    Estoque estoque = criarEstoque();
    Medicamento med = criarMedicamento(estoque.getId());
    medicamentoService.darBaixa(med.getId(), 10);
    // ...
}

@Test
void testDarBaixa_ItemSemEstoque_DeveFalhar() {
    Medicamento med = criarMedicamentoSemEstoque();
    assertThrows(BusinessException.class, 
        () -> medicamentoService.darBaixa(med.getId(), 10));
}
```

**Impacto:**
- 🔧 20+ testes alterados
- 🔧 10+ novos testes de validação

##### 6. **Documentação OpenAPI**

```yaml
# API atual
/api/estoque/item/{id}/baixa:
  post:
    parameters:
      - name: id
        in: path
      - name: quantidade
        in: body

# API NOVA (com relacionamento)
/api/estoque/{estoqueId}/item/{id}/baixa:
  post:
    parameters:
      - name: estoqueId  # ← NOVO
        in: path
      - name: id
        in: path
      - name: quantidade
        in: body
```

**Impacto:**
- 🔧 Reescrita de 8+ endpoints
- ⚠️ Versionamento de API necessário

---

### 2.3. Análise Custo x Benefício

| Aspecto | Implementar Relacionamento | Manter Status Quo |
|---------|---------------------------|-------------------|
| **Desenvolvimento** | 40-60 horas | 0 horas |
| **Testes** | 20-30 horas | 0 horas |
| **Migração de Dados** | 8-12 horas | 0 horas |
| **Documentação** | 4-6 horas | 2 horas (ajustar docs) |
| **Risco de Bugs** | Alto (muitas mudanças) | Baixo |
| **Breaking Changes** | Sim (API, DTOs) | Não |
| **Benefício Imediato** | ❌ Nenhum (MVP não usa) | ✅ MVP funcional |
| **Benefício Futuro** | ✅ Múltiplos estoques | ⚠️ Requer refatoração futura |

---

### 2.4. Cenários que Justificariam a Implementação

O relacionamento Item ↔ Estoque seria **NECESSÁRIO** nos seguintes cenários:

#### 1️⃣ **Múltiplos Estoques Físicos**
```
Hospital com:
├─ Estoque Central (almoxarifado)
├─ Farmácia Ambulatorial
├─ Estoque UTI
├─ Estoque Centro Cirúrgico
└─ Estoque Pronto Socorro

Cada medicamento pode estar em múltiplos locais:
- Dipirona: 100 unidades no Central, 50 na Farmácia, 20 na UTI
```

#### 2️⃣ **Transferências Entre Estoques**
```
UC: Transferir Item Entre Estoques
├─ Input: itemId, estoqueOrigemId, estoqueDestinoId, quantidade
├─ Validação: item existe no estoque origem?
└─ Ação: debitar origem, creditar destino
```

#### 3️⃣ **Controle de Localização Física**
```
Consulta: "Onde está o Medicamento X?"
└─ Response: "100 unidades no Estoque Central (Prateleira A3)"
```

#### 4️⃣ **Restrições de Acesso por Estoque**
```
Regra: "Farmacêutico só pode dar baixa em itens do Estoque Farmácia"
└─ Validação: item.estoqueId == usuario.estoquePermitido
```

**Nenhum desses cenários está no escopo do MVP atual.**

---

## 3. Decisão Técnica

### ⚠️ Decisão: **NÃO IMPLEMENTAR** o Relacionamento

**Justificativa:**
1. ✅ **MVP funciona sem o relacionamento**
2. ✅ **Nenhum caso de uso implementado requer estoqueId**
3. ✅ **Cassandra não foi projetado para joins complexos**
4. ✅ **Custo de implementação é MUITO ALTO (80-100 horas)**
5. ✅ **Alto risco de bugs e breaking changes**
6. ⚠️ **Possível implementação futura se necessário**

### ✅ Ação: **Atualizar Documentação** para Refletir Realidade

**Mudança na Documentação:**
- ❌ Remover relacionamento `Item "*" --> "1" Estoque`
- ✅ Adicionar nota explicativa sobre decisão de design NoSQL
- ✅ Documentar que MVP opera com estoque único implícito
- ✅ Indicar possível expansão futura se necessário

---

## 4. Arquivos a Serem Corrigidos

### 4.1. Classes de Análise (3.4)

**Arquivo:** `docs/documentos-finais-definitivos/3.4. Classes de Análise/3.4. Classes de Análise_Diagrama de Classes.md`

**Alterações:**
- ❌ Remover linha: `Item "0..*" -- "1" Estoque : <<tem>>`
- ✅ Adicionar nota sobre decisão de não implementar relacionamento

### 4.2. Modelagem de Classes de Projeto (3.7)

**Arquivo:** `docs/documentos-finais-definitivos/3.7_3.9_3.10_Modelagens/3.7. Modelagem de Classes de Projeto/3.7. Modelagem de Classes de Projeto.md`

**Alterações:**
- ❌ Remover linha: `Item "*" --> "1" Estoque : armazenado em >`
- ✅ Adicionar nota sobre design NoSQL

---

## 5. Justificativa Técnica Detalhada

### 5.1. Princípios de Design NoSQL (Cassandra)

#### Cassandra vs SQL - Paradigmas Diferentes

| Aspecto | SQL (PostgreSQL) | NoSQL (Cassandra) |
|---------|------------------|-------------------|
| **Modelo** | Relacional | Orientado a Colunas |
| **Joins** | ✅ Suportado nativamente | ❌ Não suportado |
| **Normalização** | ✅ Recomendado (3NF) | ❌ Evitar (desnormalizar) |
| **Foreign Keys** | ✅ Constraints automáticos | ❌ Não existe |
| **Relacionamentos** | ✅ 1:N, N:M com tabelas | ⚠️ Desnormalização ou UDTs |
| **Transações** | ✅ ACID completo | ⚠️ Eventual consistency |
| **Consultas Ad-Hoc** | ✅ Qualquer join | ❌ Queries pré-definidas |

#### Padrão Correto para Cassandra

Se o relacionamento fosse **REALMENTE NECESSÁRIO**, a implementação correta seria:

##### Opção 1: Desnormalização Completa
```java
@Table("medicamento")
public class Medicamento extends Item {
    private UUID idItem;
    private String nome;
    private Integer quantidadeTotal;
    private Date validade;
    
    // Dados do Estoque DUPLICADOS
    private UUID estoqueId;
    private String estoqueLocal;  // ← Denormalizado!
}

// Consulta simples:
SELECT * FROM medicamento WHERE estoque_local = 'Central'
```

**Prós:**
- ✅ Query ultra-rápida (1 SELECT)
- ✅ Performance excelente

**Contras:**
- ❌ Dados duplicados (estoqueLocal repetido em todos os itens)
- ❌ Se mudar o local do estoque, precisa atualizar TODOS os itens

##### Opção 2: Tabela de Mapeamento
```java
@Table("estoque_item")
public class EstoqueItem {
    @PrimaryKey
    private UUID id;
    
    @Column("estoque_id")
    private UUID estoqueId;
    
    @Column("item_id")
    private UUID itemId;
    
    @Column("quantidade")
    private Integer quantidade;  // ← Por localização
}

// 2 Queries necessárias:
1. SELECT * FROM estoque_item WHERE estoque_id = ?
2. SELECT * FROM medicamento WHERE id_item IN (...)
```

**Prós:**
- ✅ Flexibilidade (item pode estar em múltiplos estoques)
- ✅ Quantidade por localização

**Contras:**
- ❌ 2 queries (não é o padrão Cassandra)
- ❌ Mais complexo

**Nenhuma dessas opções é necessária para o MVP atual.**

---

### 5.2. Análise de Casos de Uso Implementados

#### UC05 - Dar Baixa em Item

**Fluxo Atual:**
```
1. Usuario chama: POST /api/estoque/medicamento/{id}/baixa
2. Service valida: item existe? quantidade disponível?
3. Service atualiza: UPDATE medicamento SET quantidade_total = quantidade_total - X
4. Service publica evento Redis: "estoque.baixa"
```

**Se tivesse relacionamento com Estoque:**
```
1. Usuario chama: POST /api/estoque/{estoqueId}/medicamento/{id}/baixa
2. Service valida: item existe? item pertence ao estoque? quantidade disponível?
3. Service atualiza: UPDATE medicamento SET quantidade_total = quantidade_total - X
4. Service publica evento Redis: "estoque.baixa"
```

**Análise:**
- ⚠️ Validação extra: `item.estoqueId == estoqueId`
- ⚠️ API mais verbosa
- ✅ **Benefício:** Segurança (usuário não dá baixa em estoque errado)
- ❌ **Problema:** No MVP, só há 1 estoque (validação inútil)

#### UC07 - Verificar Estoque Crítico

**Query Atual (Cassandra):**
```cql
SELECT * FROM medicamento WHERE quantidade_total < 10;
```

**Query com Relacionamento:**
```cql
SELECT * FROM medicamento WHERE estoque_id = ? AND quantidade_total < 10;
```

**Análise:**
- ⚠️ Query mais restrita (boa prática Cassandra)
- ✅ Performance melhor com partition key (estoqueId)
- ❌ **Problema:** No MVP, sempre seria o mesmo estoqueId

#### UC10 - Controlar Validade

**Query Atual (Cassandra):**
```cql
SELECT * FROM medicamento WHERE validade < '2025-12-31';
```

**Query com Relacionamento:**
```cql
SELECT * FROM medicamento WHERE estoque_id = ? AND validade < '2025-12-31';
```

**Análise:**
- Idêntico ao UC07
- Benefício apenas com múltiplos estoques

**Conclusão Geral:** Relacionamento **NÃO AGREGA VALOR** ao MVP atual.

---

## 6. Recomendações Futuras

### Quando Implementar o Relacionamento?

✅ **Implementar SE:**
1. Sistema expandir para múltiplos hospitais/clínicas
2. Cada unidade tiver múltiplos estoques físicos
3. Necessidade de transferências entre estoques
4. Controle de localização física for requisito

### Como Implementar (Padrão Recomendado)?

```java
// 1. Adicionar coluna em Item (partition key secundária)
@Data
public abstract class Item {
    @PrimaryKeyColumn(name = "estoque_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID estoqueId;  // ← Nova partition key
    
    @PrimaryKeyColumn(name = "id_item", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private UUID idItem;
    
    // ... resto dos campos ...
}

// 2. Queries otimizadas por estoque
medicamentoRepository.findByEstoqueIdAndQuantidadeTotalLessThan(estoqueId, limiar);
```

**Benefícios deste design:**
- ✅ Performance: estoqueId como partition key
- ✅ Distribuição: itens de estoques diferentes em nós diferentes
- ✅ Escalabilidade: adicionar estoques não impacta performance

---

## 7. Documentação de Design Decisions

### ADR (Architecture Decision Record)

```markdown
# ADR-007: Não Implementar Relacionamento Item-Estoque no MVP

## Status
Aceito

## Contexto
Documentação mostra relacionamento Item "*" --> "1" Estoque, mas implementação
Cassandra não possui este relacionamento.

## Decisão
Não implementar relacionamento no MVP. Operar com estoque único implícito.

## Consequências
Positivas:
- Código mais simples e direto
- Performance ótima (queries diretas)
- Sem complexidade de joins no Cassandra
- API mais simples

Negativas:
- Sistema não suporta múltiplos estoques físicos
- Necessário refatoração se expansão futura
- Documentação diverge do design real (será corrigida)

## Alternativas Consideradas
1. Implementar relacionamento com coluna estoqueId
   - Descartado: custo alto (80-100h), sem benefício para MVP
2. Usar tabela de mapeamento EstoqueItem
   - Descartado: anti-pattern Cassandra, complexidade desnecessária
```

---

## 8. Checklist de Alterações

- [ ] Remover relacionamento `Item -- Estoque` de 3.4 (2 ocorrências)
- [ ] Remover relacionamento `Item --> Estoque` de 3.7
- [ ] Adicionar nota técnica em 3.4 explicando decisão
- [ ] Adicionar nota técnica em 3.7 explicando design NoSQL
- [ ] Criar este documento (CORRECAO_DISCREPANCIA_1.7.md)
- [ ] Documentar no README do módulo estoque
- [ ] Adicionar na seção de Design Patterns/Boas Práticas

---

## 9. Referências

### Código Backend (Implementação Real)
- `simplehealth-back-estoque/src/main/java/com/simplehealth/estoque/domain/entity/Item.java`
- `simplehealth-back-estoque/src/main/java/com/simplehealth/estoque/domain/entity/Estoque.java`
- `simplehealth-back-estoque/src/main/java/com/simplehealth/estoque/domain/entity/Medicamento.java`

### Documentação a Corrigir
- [Classes de Análise](./documentos-finais-definitivos/3.4.%20Classes%20de%20Análise/3.4.%20Classes%20de%20Análise_Diagrama%20de%20Classes.md)
- [Modelagem de Classes de Projeto](./documentos-finais-definitivos/3.7_3.9_3.10_Modelagens/3.7.%20Modelagem%20de%20Classes%20de%20Projeto/3.7.%20Modelagem%20de%20Classes%20de%20Projeto.md)

### Outras Discrepâncias
- [Discrepância 1.1 - Médico vs Usuario](./CORRECAO_DISCREPANCIA_1.1.md)
- [Discrepância 1.2 - EventoAuditoria com Cassandra](./CORRECAO_DISCREPANCIA_1.2.md)
- [Discrepância 1.3 - Redis para Cache](./CORRECAO_DISCREPANCIA_1.3.md)
- [Discrepância 1.4 - Atributos de Agendamento](./CORRECAO_DISCREPANCIA_1.4.md)
- [Discrepância 1.5 - Atributos de Rastreamento](./CORRECAO_DISCREPANCIA_1.5.md)

### Recursos Externos
- [Cassandra Data Modeling Best Practices](https://cassandra.apache.org/doc/latest/cassandra/data-modeling/intro.html)
- [NoSQL vs SQL: When to Use Which](https://www.mongodb.com/nosql-explained/nosql-vs-sql)
- [Denormalization in NoSQL Databases](https://university.scylladb.com/courses/data-modeling/lessons/advanced-data-modeling/topic/denormalization/)

---

**Assinatura Digital:**  
Análise de impacto realizada e documentada em 14/12/2025  
Decisão: Manter implementação atual (sem relacionamento)  
Documentação será atualizada para refletir design NoSQL do Cassandra  
Relacionamento Item-Estoque não é necessário para o escopo MVP atual
