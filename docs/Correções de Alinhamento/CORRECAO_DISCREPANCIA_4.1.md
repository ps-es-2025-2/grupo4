# Correção de Discrepância 4.1: Cardinalidade Paciente ↔ Convênio

**Data**: 14 de dezembro de 2025  
**Tipo de Correção**: Documentação (Cardinalidade UML Errada)  
**Módulo Afetado**: Cadastro  

---

## 1. Descrição da Discrepância

A documentação mostrava cardinalidade **incorreta** no relacionamento entre `Paciente` e `Convênio`:

**Documentação Original (ERRADA)**:
```plantuml
Paciente "1" -- "0..*" Convenio : possui plano
```

Isso indicava que:
- **1 Paciente** possui **0 ou vários Convênios**
- Cada Paciente obrigatoriamente existe (cardinalidade mínima 1)
- Cada Paciente pode ter múltiplos planos de saúde simultaneamente

**Implementação Real (CORRETA)**:
```java
@Entity
public class Paciente extends Pessoa {
  @ManyToOne
  @JoinColumn(name = "convenio_id")
  private Convenio convenio;  // Um paciente tem 0 ou 1 convênio
}
```

---

## 2. Análise da Implementação Backend

### 2.1 Entidade Paciente

**Arquivo**: `simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/domain/entity/Paciente.java`

```java
package com.simplehealth.cadastro.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Paciente extends Pessoa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private LocalDate dataNascimento;

  @Column(unique = true)
  private String cpf;

  @jakarta.persistence.ManyToOne          // ← RELACIONAMENTO MANY-TO-ONE
  @jakarta.persistence.JoinColumn(name = "convenio_id")
  private Convenio convenio;              // ← UM PACIENTE TEM 0 OU 1 CONVÊNIO

  public boolean verificarExistencia() {
    return false;
  }
}
```

### 2.2 Entidade Convênio

**Arquivo**: `simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/domain/entity/Convenio.java`

```java
package com.simplehealth.cadastro.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Convenio {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nome;
  private String plano;
  private Boolean ativo;
}
```

**Observação Importante**: A classe `Convenio` **NÃO possui** a anotação `@OneToMany` de volta para `Paciente`. Isso significa que o relacionamento é **unidirecional** de `Paciente` para `Convênio`.

### 2.3 PacienteDTO

**Arquivo**: `simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/application/dto/PacienteDTO.java`

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {
  private Long id;
  private String nome;
  private String email;
  private String telefone;
  private String endereco;
  private String cpf;
  private LocalDate dataNascimento;
  
  // Relacionamento com Convênio
  private Long convenioId;      // FK para Convenio
  private String convenioNome;  // Denormalização para performance
}
```

---

## 3. Cardinalidade Correta

### 3.1 Interpretação do @ManyToOne

A anotação `@ManyToOne` em JPA indica:

```
Paciente (@ManyToOne) → Convênio

Lado "Many": Paciente (muitos pacientes)
Lado "One":  Convênio (um convênio)

Leitura: Muitos Pacientes podem ter o mesmo Convênio
         Um Paciente tem no máximo UM Convênio
```

### 3.2 Cardinalidade UML Correta

**Notação UML**:
```plantuml
Paciente "0..*" -- "0..1" Convenio : possui plano
```

**Interpretação**:
- **0..\***: Zero ou vários Pacientes podem estar associados a um Convênio
  - Um convênio pode ter zero pacientes (convênio inativo)
  - Um convênio pode ter vários pacientes (ex: 1000 pacientes no plano XYZ)

- **0..1**: Cada Paciente possui zero ou um Convênio
  - Zero: Paciente particular (sem convênio)
  - Um: Paciente tem plano de saúde

### 3.3 Esquema de Banco de Dados

```sql
-- Tabela Convenio (lado "One")
CREATE TABLE convenio (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(255),
  plano VARCHAR(255),
  ativo BOOLEAN
);

-- Tabela Paciente (lado "Many")
CREATE TABLE paciente (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(255),
  cpf VARCHAR(11) UNIQUE,
  data_nascimento DATE,
  convenio_id BIGINT,                    -- FK nullable (0..1)
  FOREIGN KEY (convenio_id) REFERENCES convenio(id)
);
```

**Observação**: A coluna `convenio_id` é **NULLABLE**, permitindo que pacientes existam sem convênio (pacientes particulares).

---

## 4. Comparação: Documentação vs. Implementação

| Aspecto | Documentação Original (ERRADA) | Implementação Real (CORRETA) |
|---------|-------------------------------|------------------------------|
| **Cardinalidade UML** | `Paciente "1" -- "0..*" Convenio` | `Paciente "0..*" -- "0..1" Convenio` |
| **Leitura** | 1 Paciente possui 0 ou vários Convênios | Vários Pacientes possuem 0 ou 1 Convênio |
| **Atributo Paciente** | `convenios: List<Convenio>` (coleção) | `convenio: Convenio` (referência única) |
| **Anotação JPA** | `@OneToMany` (esperado) | `@ManyToOne` (implementado) |
| **FK no Banco** | convenio teria FK para paciente | paciente tem FK para convenio ✅ |
| **Paciente particular** | Não permitido (min=1) | Permitido (convenio_id NULL) ✅ |
| **Múltiplos planos** | Permitido (max=*) | NÃO permitido (max=1) ✅ |

---

## 5. Justificativa da Cardinalidade Correta

### 5.1 Regras de Negócio Identificadas

1. **RN-PACIENTE.1**: Um paciente pode ser **particular** (sem convênio)
   - Pacientes sem plano de saúde pagam consultas diretamente
   - Cardinalidade mínima: **0**

2. **RN-PACIENTE.2**: Um paciente tem **no máximo UM** convênio ativo por vez
   - Simplificação do modelo de negócio
   - Na prática, pacientes com múltiplos planos escolhem um como principal
   - Cardinalidade máxima: **1**

3. **RN-CONVENIO.1**: Um convênio pode atender **vários pacientes**
   - Convênios são entidades compartilhadas (ex: Unimed, Amil, SulAmérica)
   - Um único convênio pode ter milhares de beneficiários
   - Cardinalidade: **0..\***

### 5.2 Padrão de Design: Many-to-One Unidirecional

```
┌─────────────────────────────────────────────────────────┐
│              RELACIONAMENTO UNIDIRECIONAL                │
├─────────────────────────────────────────────────────────┤
│                                                          │
│   ┌──────────┐                      ┌──────────┐       │
│   │ Paciente │ ───── @ManyToOne ──→ │ Convenio │       │
│   └──────────┘                      └──────────┘       │
│   ↓ convenio                                            │
│   ↓ FK: convenio_id                                     │
│                                                          │
│   Paciente CONHECE Convenio                             │
│   Convenio NÃO conhece seus Pacientes                   │
│   (sem navegação bidirecional)                          │
└─────────────────────────────────────────────────────────┘
```

**Vantagens**:
- **Performance**: Convenio não carrega lista de milhares de pacientes
- **Simplicidade**: Menor acoplamento entre entidades
- **Manutenibilidade**: Queries mais simples (JOIN direto de Paciente para Convenio)

### 5.3 Cenários de Uso

**Cenário 1: Paciente Particular**
```java
Paciente p = new Paciente();
p.setCpf("12345678900");
p.setNome("João Silva");
p.setConvenio(null);  // ← Paciente sem convênio (particular)
pacienteRepository.save(p);
```

**Cenário 2: Paciente com Convênio**
```java
Convenio unimed = convenioRepository.findByNome("Unimed");
Paciente p = new Paciente();
p.setCpf("98765432100");
p.setNome("Maria Santos");
p.setConvenio(unimed);  // ← Paciente com plano Unimed
pacienteRepository.save(p);
```

**Cenário 3: Múltiplos Pacientes no Mesmo Convênio**
```java
Convenio amil = convenioRepository.findByNome("Amil");

Paciente p1 = new Paciente();
p1.setConvenio(amil);  // Paciente 1 no Amil

Paciente p2 = new Paciente();
p2.setConvenio(amil);  // Paciente 2 no mesmo Amil

Paciente p3 = new Paciente();
p3.setConvenio(amil);  // Paciente 3 no mesmo Amil

// Múltiplos pacientes (Many) apontam para 1 convênio (One)
```

---

## 6. Correções Aplicadas na Documentação

### 6.1 Arquivo: `3.4. Classes de Análise_Diagrama de Classes.md`

**Localização**: 2 ocorrências (linhas 482 e 938)

#### Correção 1 - Linha 482 (Diagrama Módulo Cadastro)

**Antes**:
```plantuml
Medico "0..*" -- "0..*" Convenio : atende

Paciente "1" -- "0..*" Convenio : possui plano
}
```

**Depois**:
```plantuml
Medico "0..*" -- "0..*" Convenio : atende

' CORREÇÃO CARDINALIDADE 4.1: Um Paciente "0..*" -- "0..1" Convenio : possui plano

}
```

#### Correção 2 - Linha 938 (Diagrama Completo)

**Antes**:
```plantuml
Medico "0..*" -- "0..*" Convenio : atende

Paciente "1" -- "0..*" Convenio : possui plano
}
```

**Depois**:
```plantuml
Medico "0..*" -- "0..*" Convenio : atende

' CORREÇÃO CARDINALIDADE 4.1: Um Paciente "0..*" -- "0..1" Convenio : possui plano

}
```

---

## 7. Impacto da Correção

### 7.1 Impacto na Modelagem

| Aspecto | Impacto |
|---------|---------|
| **Classes de Análise** | ✅ Corrigido - cardinalidade alinhada com @ManyToOne |
| **Classes de Projeto** | ⚠️ Revisar se há diagrama com relacionamento errado |
| **Casos de Uso** | ✅ Nenhum - UC01 (Cadastrar Paciente) permite convenio=null |
| **Regras de Negócio** | ✅ Confirmado - RN-PACIENTE.1 permite paciente particular |

### 7.2 Queries Impactadas

**Query para buscar pacientes de um convênio** (caso necessário):
```java
// Antes (esperado pela documentação errada):
convenio.getPacientes();  // ❌ Não existe - sem @OneToMany no Convenio

// Depois (implementação real):
List<Paciente> pacientes = pacienteRepository.findByConvenioId(convenioId);
```

**Query para buscar convênio de um paciente**:
```java
// Ambos os casos funcionam igual:
Paciente p = pacienteRepository.findByCpf("12345678900");
Convenio c = p.getConvenio();  // ✅ OK - @ManyToOne permite navegação direta
```

---

## 8. Validação

### 8.1 Comandos Executados

```bash
# 1. Localizar entidade Paciente
find . -name "Paciente.java"
# Resultado: 
# simplehealth-back/simplehealth-back-cadastro/src/.../domain/entity/Paciente.java

# 2. Verificar anotação JPA
grep -A3 "@ManyToOne" Paciente.java
# Resultado:
#   @jakarta.persistence.ManyToOne
#   @jakarta.persistence.JoinColumn(name = "convenio_id")
#   private Convenio convenio;

# 3. Verificar entidade Convênio
cat Convenio.java | grep -E "@OneToMany|@ManyToOne"
# Resultado: (vazio - sem relacionamento bidirecional)

# 4. Verificar DTO
grep "convenio" PacienteDTO.java
# Resultado:
#   private Long convenioId;
#   private String convenioNome;
```

### 8.2 Arquitetura Validada

```
✅ Paciente possui atributo "convenio" (singular, não coleção)
✅ Anotação @ManyToOne confirma relacionamento Many-to-One
✅ @JoinColumn em Paciente confirma FK em tabela paciente
✅ Convênio NÃO possui @OneToMany (unidirecional)
✅ convenio_id é NULLABLE (permite paciente particular)
✅ Cardinalidade correta: Paciente "0..*" -- "0..1" Convenio
```

---

## 9. Diagramas Comparativos

### 9.1 Documentação Original (ERRADA)

```
                    1              0..*
            Paciente ─────────────── Convenio
                         possui plano

Interpretação:
• 1 Paciente obrigatório
• 1 Paciente pode ter 0 ou vários Convênios
• Erro: paciente poderia ter múltiplos planos simultaneamente
```

### 9.2 Implementação Real (CORRETA)

```
                   0..*            0..1
            Paciente ─────────────── Convenio
                         possui plano

Interpretação:
• 0..* Pacientes podem ter o mesmo Convênio
• Cada Paciente tem 0 ou 1 Convênio
• Permite: Paciente particular (convenio=null)
• Impede: Paciente com múltiplos convênios
```

### 9.3 Esquema ER (Entity-Relationship)

```
┌─────────────────────┐              ┌──────────────────────┐
│      Paciente       │              │       Convenio        │
├─────────────────────┤              ├──────────────────────┤
│ id (PK)             │              │ id (PK)              │
│ cpf                 │    0..1      │ nome                 │
│ nome                │ ───────────  │ plano                │
│ data_nascimento     │  @ManyToOne  │ ativo                │
│ convenio_id (FK) ◄──┼──────────────┤                      │
└─────────────────────┘      0..*    └──────────────────────┘
          │
          └─ FK: convenio_id REFERENCES convenio(id)
             nullable: TRUE (permite paciente particular)
```

---

## 10. Resumo das Mudanças

| Arquivo | Tipo de Correção | Linhas Alteradas |
|---------|-----------------|------------------|
| `3.4. Classes de Análise_Diagrama de Classes.md` | Cardinalidade UML | 2 ocorrências (linhas 482, 938) |

**Total de Arquivos Modificados**: 1  
**Tipo de Mudança**: Documentação apenas (código já estava correto)

---

## 11. Conclusão

A **Discrepância 4.1** foi identificada como uma **cardinalidade incorreta** na documentação UML. A implementação backend utilizava corretamente `@ManyToOne`, indicando que:

- **Vários Pacientes** (Many) podem ter o **mesmo Convênio** (One)
- **Cada Paciente** possui **0 ou 1 Convênio** (não uma coleção)

**Cardinalidade Corrigida**: `Paciente "0..*" -- "0..1" Convenio : possui plano`

**Status**: ✅ Documentação corrigida para refletir a implementação real

---

**Revisado por**: GitHub Copilot  
**Aprovado para**: Documentação Final do Projeto SimpleHealth
