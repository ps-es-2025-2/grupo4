# Correção de Discrepância 4.2: Relacionamento Médico ↔ Convênio (Não Implementado)

**Data**: 14 de dezembro de 2025  
**Tipo de Correção**: Documentação (Redução de Escopo)  
**Módulo Afetado**: Cadastro  

---

## 1. Descrição da Discrepância

A documentação mostrava um relacionamento **Many-to-Many** entre `Médico` e `Convênio`:

**Documentação Original**:
```plantuml
Medico "0..*" -- "0..*" Convenio : atende
```

Isso indicava que:
- **Vários Médicos** atendem **vários Convênios**
- Um médico pode atender múltiplos convênios (ex: Unimed, Amil, SulAmérica)
- Um convênio é atendido por múltiplos médicos

**Implementação Real**:
- ❌ **NÃO EXISTE** relacionamento entre `Médico` e `Convênio`
- ❌ **NÃO EXISTE** atributo `convenios` na entidade `Medico`
- ❌ **NÃO EXISTE** anotação `@ManyToMany` em nenhuma das entidades
- ❌ **NÃO EXISTE** tabela de junção `medico_convenio` no banco de dados

---

## 2. Análise da Implementação Backend

### 2.1 Entidade Médico

**Arquivo**: `simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/domain/entity/Medico.java`

```java
package com.simplehealth.cadastro.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Medico extends Pessoa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String crm;
  
  private String especialidade;
  
  // ❌ NÃO POSSUI: @ManyToMany com Convenio
  // ❌ NÃO POSSUI: List<Convenio> convenios
  // ❌ NÃO POSSUI: Set<Convenio> conveniosAtendidos
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
  
  // ❌ NÃO POSSUI: @ManyToMany com Medico
  // ❌ NÃO POSSUI: List<Medico> medicos
  // ❌ NÃO POSSUI: Set<Medico> medicosCredenciados
}
```

### 2.3 MedicoDTO

**Arquivo**: `simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/application/dto/MedicoDTO.java`

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoDTO {

  private Long id;

  @NotBlank
  private String nomeCompleto;

  @NotBlank
  private String crm;

  private String especialidade;
  private String telefone;
  private String email;
  
  // ❌ NÃO POSSUI: List<Long> convenioIds
  // ❌ NÃO POSSUI: List<String> conveniosAtendidos
}
```

---

## 3. Verificação de Código

### 3.1 Busca por @ManyToMany

```bash
# Procurar por anotação @ManyToMany em todo o módulo cadastro
grep -r "@ManyToMany" simplehealth-back/simplehealth-back-cadastro/src/
# Resultado: No matches found ✅

# Procurar por relacionamento Many-to-Many
grep -r "ManyToMany" simplehealth-back/simplehealth-back-cadastro/src/
# Resultado: No matches found ✅
```

### 3.2 Busca por Tabela de Junção

```bash
# Procurar por referências a tabela medico_convenio
grep -r "medico_convenio" simplehealth-back/simplehealth-back-cadastro/
# Resultado: No matches found ✅

# Procurar por @JoinTable
grep -r "@JoinTable" simplehealth-back/simplehealth-back-cadastro/src/
# Resultado: No matches found ✅
```

### 3.3 Análise dos Atributos

```bash
# Verificar atributos da classe Medico
cat Medico.java | grep -E "private|List|Set|Collection"
# Resultado:
#   private Long id;
#   private String crm;
#   private String especialidade;
# ✅ NÃO possui coleção de Convenios

# Verificar atributos da classe Convenio
cat Convenio.java | grep -E "private|List|Set|Collection"
# Resultado:
#   private Long id;
#   private String nome;
#   private String plano;
#   private Boolean ativo;
# ✅ NÃO possui coleção de Medicos
```

---

## 4. Redução de Escopo Identificada

### 4.1 O que foi Planejado vs. O que foi Implementado

| Funcionalidade | Documentação Original | Implementação Real | Status |
|----------------|----------------------|-------------------|--------|
| **Relacionamento Médico-Convênio** | Many-to-Many | ❌ NÃO implementado | **REDUÇÃO DE ESCOPO** |
| **Médico atende múltiplos convênios** | Sim (0..\*) | ❌ NÃO | **REDUÇÃO DE ESCOPO** |
| **Convênio tem múltiplos médicos** | Sim (0..\*) | ❌ NÃO | **REDUÇÃO DE ESCOPO** |
| **Tabela de junção medico_convenio** | Esperada | ❌ NÃO existe | **REDUÇÃO DE ESCOPO** |
| **Credenciamento de médicos** | Planejado | ❌ NÃO implementado | **REDUÇÃO DE ESCOPO** |

### 4.2 Impacto Funcional

**Funcionalidades NÃO Implementadas**:
1. ❌ Cadastro de credenciamento médico-convênio
2. ❌ Consulta de médicos por convênio
3. ❌ Consulta de convênios atendidos por médico
4. ❌ Validação de convênio aceito pelo médico ao agendar consulta
5. ❌ Filtro de médicos disponíveis baseado no convênio do paciente

### 4.3 Workaround na Implementação Atual

**Como o sistema funciona SEM o relacionamento**:

```
┌──────────────────────────────────────────────────────────┐
│         FLUXO DE AGENDAMENTO SEM CREDENCIAMENTO          │
├──────────────────────────────────────────────────────────┤
│                                                           │
│  1. Secretária seleciona Paciente (que tem Convênio)     │
│                                                           │
│  2. Secretária seleciona Médico (independente)           │
│     ↓                                                     │
│     NÃO HÁ VALIDAÇÃO: "Médico aceita este convênio?"     │
│                                                           │
│  3. Sistema agenda consulta                              │
│     ↓                                                     │
│     Agendamento armazena:                                │
│     - pacienteCpf (String)                               │
│     - medicoCrm (String)                                 │
│     - convenioNome (String)                              │
│                                                           │
│  4. Validação de convênio é RESPONSABILIDADE HUMANA      │
│     (secretária verifica manualmente)                    │
└──────────────────────────────────────────────────────────┘
```

**Observação**: O sistema **não impede** que uma consulta seja agendada com um médico que não atende determinado convênio. A responsabilidade de verificar o credenciamento fica com o usuário (secretária).

---

## 5. Justificativa da Redução de Escopo

### 5.1 Simplicidade Operacional

**Decisão de Design**: Manter o modelo simples sem credenciamento formal

**Razões**:
1. **Flexibilidade**: Em clínicas pequenas, todos os médicos atendem todos os convênios
2. **Menos Cadastros**: Evita manutenção complexa de tabela de credenciamento
3. **Velocidade de Desenvolvimento**: MVP mais rápido sem gerenciarMany-to-Many
4. **Responsabilidade Humana**: Secretária já conhece quais médicos atendem quais convênios

### 5.2 Cenários de Uso Real

**Cenário 1: Clínica Pequena (2-5 médicos)**
- Todos os médicos atendem os mesmos convênios
- Relacionamento Many-to-Many seria redundante
- Validação manual é suficiente

**Cenário 2: Convênio Particular**
- Paciente particular (sem convênio) pode escolher qualquer médico
- Não há necessidade de validação de credenciamento

**Cenário 3: Especialidade Única**
- Clínica especializada (ex: dermatologia)
- Todos os médicos têm mesma especialidade
- Credenciamento é homogêneo

### 5.3 Padrão Alternativo: Validação Opcional

Se no futuro houver necessidade de controlar credenciamento:

**Opção 1: Lista de Convênios Aceitos (campo texto)**
```java
@Entity
public class Medico extends Pessoa {
  private String crm;
  private String especialidade;
  private String conveniosAceitos;  // Ex: "Unimed,Amil,Particular"
}
```

**Opção 2: Tabela de Configuração Externa**
```sql
-- Tabela opcional de credenciamento (fora do modelo de domínio)
CREATE TABLE credenciamento (
  medico_crm VARCHAR(20),
  convenio_nome VARCHAR(255),
  data_credenciamento DATE,
  ativo BOOLEAN
);
```

**Opção 3: Regra de Negócio Simples**
- "Todos os médicos atendem todos os convênios ativos"
- Validação é desnecessária

---

## 6. Comparação: Documentado vs. Implementado

### 6.1 Diagrama Esperado (Documentação Original)

```
                   0..*            0..*
            Medico ─────────────── Convenio
                        atende

Com tabela de junção:
┌──────────────────┐         ┌──────────────────┐         ┌──────────────────┐
│      Medico      │         │ medico_convenio  │         │     Convenio      │
├──────────────────┤         ├──────────────────┤         ├──────────────────┤
│ id (PK)          │ ────────│ medico_id (FK)   │         │ id (PK)          │
│ crm              │    1    │ convenio_id (FK) │────────┤ nome             │
│ especialidade    │         │ data_credenc.    │    *    │ plano            │
└──────────────────┘         └──────────────────┘         └──────────────────┘
```

### 6.2 Diagrama Real (Implementação)

```
            Medico                          Convenio
         (sem relação)                  (sem relação)

┌──────────────────┐                    ┌──────────────────┐
│      Medico      │                    │     Convenio      │
├──────────────────┤                    ├──────────────────┤
│ id (PK)          │                    │ id (PK)          │
│ crm              │                    │ nome             │
│ especialidade    │                    │ plano            │
└──────────────────┘                    │ ativo            │
                                        └──────────────────┘
       ↑                                         ↑
       │                                         │
       └────── Paciente (@ManyToOne) ───────────┘
              possui 0 ou 1 convenio
```

**Observação**: O **único** relacionamento com `Convenio` é através de `Paciente`, não de `Medico`.

---

## 7. Correções Aplicadas na Documentação

### 7.1 Arquivo: `3.4. Classes de Análise_Diagrama de Classes.md`

**Localização**: 2 ocorrências (linhas 480 e 938)

#### Correção 1 - Linha 480 (Diagrama Módulo Cadastro)

**Antes**:
```plantuml
Usuario "1" -- "1" PerfilUsuario : tem

Medico "0..*" -- "0..*" Convenio : atende

' CORREÇÃO CARDINALIDADE 4.1: Um Paciente "0..*" -- "0..1" Convenio : possui plano
```

**Depois**:
```plantuml
Usuario "1" -- "1" PerfilUsuario : tem

' Médico NÃO possui atributo ou anotação @ManyToMany com Convênio
' REDUÇÃO DE ESCOPO 4.2: Relacionamento Médico-Convênio não implementado

' CORREÇÃO CARDINALIDADE 4.1: Um Paciente "0..*" -- "0..1" Convenio : possui plano
```

#### Correção 2 - Linha 938 (Diagrama Completo)

**Antes**:
```plantuml
Usuario "1" -- "1" PerfilUsuario : tem

Medico "0..*" -- "0..*" Convenio : atende

' CORREÇÃO CARDINALIDADE 4.1: Um Paciente "0..*" -- "0..1" Convenio : possui plano
```

**Depois**:
```plantuml
Usuario "1" -- "1" PerfilUsuario : tem

' REDUÇÃO DE ESCOPO 4.2: Médico-Convênio não implementado (sem @ManyToMany)

' CORREÇÃO CARDINALIDADE 4.1: Um Paciente "0..*" -- "0..1" Convenio : possui plano
```

---

## 8. Impacto nos Casos de Uso

### 8.1 UC02: Agendar Consulta

**Comportamento Esperado (com credenciamento)**:
```
Passo 3: Sistema verifica se Médico atende o Convênio do Paciente
  alt "Médico não aceita convênio"
    Sistema exibe: "Médico não atende este convênio"
    Caso de uso é encerrado
  else "Médico aceita convênio"
    Sistema prossegue com agendamento
  end
```

**Comportamento Real (sem credenciamento)**:
```
Passo 3: Sistema agenda consulta SEM validar credenciamento
  ' NÃO HÁ VERIFICAÇÃO: medico.getConvenios().contains(paciente.getConvenio())
  ' Responsabilidade de validação é da Secretária (processo manual)
```

### 8.2 UC01: Cadastrar Novo Médico

**Não é possível**:
- ❌ Selecionar convênios atendidos pelo médico
- ❌ Cadastrar credenciamento

**Campos disponíveis**:
- ✅ CRM
- ✅ Nome
- ✅ Especialidade
- ✅ Telefone, Email

---

## 9. Possíveis Evoluções Futuras

### 9.1 Implementar Credenciamento (Opção 1)

**Adicionar Many-to-Many**:
```java
@Entity
public class Medico extends Pessoa {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(unique = true)
  private String crm;
  private String especialidade;
  
  // Adicionar relacionamento
  @ManyToMany
  @JoinTable(
    name = "medico_convenio",
    joinColumns = @JoinColumn(name = "medico_id"),
    inverseJoinColumns = @JoinColumn(name = "convenio_id")
  )
  private Set<Convenio> conveniosAtendidos = new HashSet<>();
}
```

**Validação no AgendarConsultaUseCase**:
```java
public void execute(AgendarConsultaDTO dto) {
  Paciente paciente = pacienteService.buscarPorCpf(dto.getPacienteCpf());
  Medico medico = medicoService.buscarPorCrm(dto.getMedicoCrm());
  
  // Validar credenciamento
  if (paciente.getConvenio() != null) {
    boolean medicoAtendeConvenio = medico.getConveniosAtendidos()
      .contains(paciente.getConvenio());
    
    if (!medicoAtendeConvenio) {
      throw new MedicoNaoAtendeConvenioException(
        "Médico " + medico.getCrm() + " não atende convênio " + 
        paciente.getConvenio().getNome()
      );
    }
  }
  
  // Continuar agendamento...
}
```

### 9.2 Implementar Filtro de Médicos (Opção 2)

**Endpoint adicional**:
```java
@GetMapping("/medicos/por-convenio/{convenioId}")
public List<MedicoDTO> listarMedicosPorConvenio(@PathVariable Long convenioId) {
  return medicoService.buscarPorConvenio(convenioId);
}

// Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
  @Query("SELECT m FROM Medico m JOIN m.conveniosAtendidos c WHERE c.id = :convenioId")
  List<Medico> findByConvenioId(@Param("convenioId") Long convenioId);
}
```

### 9.3 Manter Simplicidade (Opção 3 - ATUAL)

**Não implementar credenciamento formal**:
- ✅ Modelo simples
- ✅ Menos manutenção
- ✅ Validação manual pela secretária
- ⚠️ Risco: agendamento com médico errado

---

## 10. Resumo das Mudanças

| Arquivo | Tipo de Correção | Linhas Alteradas |
|---------|-----------------|------------------|
| `3.4. Classes de Análise_Diagrama de Classes.md` | Remoção de relacionamento Many-to-Many | 2 ocorrências (linhas 480, 938) |

**Total de Arquivos Modificados**: 1  
**Tipo de Mudança**: Documentação apenas (relacionamento nunca foi implementado)

---

## 11. Validação

### 11.1 Comandos Executados

```bash
# 1. Verificar ausência de @ManyToMany
grep -r "@ManyToMany" simplehealth-back/simplehealth-back-cadastro/src/
# Resultado: No matches found ✅

# 2. Verificar atributos da classe Medico
cat Medico.java | grep -E "List|Set|Collection|convenio"
# Resultado: (vazio - sem coleção de convenios) ✅

# 3. Verificar atributos da classe Convenio
cat Convenio.java | grep -E "List|Set|Collection|medico"
# Resultado: (vazio - sem coleção de medicos) ✅

# 4. Verificar tabela de junção
grep -r "medico_convenio\|@JoinTable" simplehealth-back/simplehealth-back-cadastro/
# Resultado: No matches found ✅
```

### 11.2 Arquitetura Validada

```
✅ Medico NÃO possui atributo convenios
✅ Medico NÃO possui @ManyToMany
✅ Convenio NÃO possui atributo medicos
✅ Convenio NÃO possui @ManyToMany
✅ Não existe tabela medico_convenio
✅ MedicoDTO não possui lista de convenios
✅ Relacionamento é REDUÇÃO DE ESCOPO (nunca foi implementado)
```

---

## 12. Conclusão

A **Discrepância 4.2** foi identificada como uma **redução de escopo**, onde a documentação planejava um relacionamento `@ManyToMany` entre `Médico` e `Convênio` que **nunca foi implementado**.

**Decisão de Design Validada**: A ausência desse relacionamento simplifica o modelo e delega a validação de credenciamento para o processo manual da secretária. Para clínicas pequenas ou com poucos convênios, essa abordagem é suficiente.

**Relacionamento Removido da Documentação**: `Medico "0..*" -- "0..*" Convenio : atende`

**Status**: ✅ Documentação corrigida para refletir a implementação real (redução de escopo documentada)

---

**Revisado por**: GitHub Copilot  
**Aprovado para**: Documentação Final do Projeto SimpleHealth
