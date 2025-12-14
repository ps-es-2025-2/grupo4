# Correção da Discrepância 3.2 - Service vs UseCase Pattern

**Data:** 14/12/2025  
**Responsável:** Equipe de Documentação  
**Status:** ✅ Concluída

---

## Resumo Executivo

**Problema**: Documentação mencionava apenas "Service Layer Pattern", mas a implementação real utiliza **Service + UseCase Pattern** combinados em uma arquitetura em camadas.

**Solução**: Documentados ambos os padrões, explicando suas diferenças, responsabilidades e como trabalham juntos no sistema.

**Arquivos Corrigidos**:
- ✅ `3.4. Classes de Análise_Diagrama de Classes.md` (nota técnica adicionada)
- ✅ `3.8 Boas Práticas/DESIGN_PATTERNS.md` (seção UseCase Pattern criada)
- ✅ `3.8 Boas Práticas/3.8 Boas praticas.md` (Service Layer adicionado)

**Impacto**: Documentação agora reflete corretamente a arquitetura de 60 classes (13 Services + 47 UseCases) no backend.

---

## 1. Discrepância Identificada

### Problema
A documentação original mencionava apenas **Service Layer Pattern**, mas a implementação real possui uma arquitetura mais sofisticada com **Service + UseCase Pattern** trabalhando em conjunto.

### Diferenças Encontradas

#### Documentação Original (Incompleta):
```
Camada de Aplicação:
└─ Service Layer Pattern
   ├─ PacienteService
   ├─ MedicoService
   └─ AgendamentoService
```

#### Implementação Real (Correta):
```
Camada de Aplicação (2 subcamadas):
├─ Service Layer (13 classes)
│  ├─ PacienteService (CRUD básico)
│  ├─ MedicoService (CRUD básico)
│  └─ ConsultaService (CRUD básico)
│
└─ UseCase Layer (47 classes)
   ├─ CadastrarNovoPacienteUseCase (orquestra PacienteService + ConvenioService)
   ├─ AgendarConsultaUseCase (orquestra ConsultaService + BloqueioService + Redis)
   └─ DarBaixaInsumosUseCase (orquestra ItemService + EstoqueService + Validações)
```

---

## 2. Análise da Situação

### 2.1. Verificação do Código Backend

#### Estrutura de Diretórios
```bash
simplehealth-back/
├─ simplehealth-back-cadastro/
│  └─ src/main/java/.../application/
│     ├─ service/          # 5 Services
│     │  ├─ PacienteService.java
│     │  ├─ MedicoService.java
│     │  ├─ UsuarioService.java
│     │  ├─ ConvenioService.java
│     │  └─ PessoaService.java
│     │
│     └─ usecases/         # 12 UseCases
│        ├─ CadastrarNovoPacienteUseCase.java
│        ├─ AtualizarPacienteUseCase.java
│        ├─ BuscarPacienteUseCase.java
│        ├─ ConsultarHistoricoPacienteUseCase.java
│        └─ ...
│
├─ simplehealth-back-agendamento/
│  └─ src/main/java/.../application/
│     ├─ service/          # 5 Services
│     │  ├─ ConsultaService.java
│     │  ├─ ExameService.java
│     │  ├─ ProcedimentoService.java
│     │  ├─ BloqueioAgendaService.java
│     │  └─ AgendamentoService.java
│     │
│     └─ usecases/         # 22 UseCases
│        ├─ AgendarConsultaUseCase.java
│        ├─ SolicitarEncaixeUseCase.java
│        ├─ CancelarAgendamentoUseCase.java
│        └─ ...
│
└─ simplehealth-back-estoque/
   └─ src/main/java/.../application/
      ├─ service/          # 3 Services
      │  ├─ EstoqueService.java
      │  ├─ ItemService.java
      │  └─ MedicamentoService.java
      │
      └─ usecases/         # 13 UseCases
         ├─ DarBaixaInsumosUseCase.java
         ├─ EntradaItensUseCase.java
         ├─ ControlarValidadeUseCase.java
         └─ ...
```

#### Contagem Total
```bash
# Services
$ find simplehealth-back -name "*Service.java" -path "*/application/service/*" | wc -l
13

# UseCases
$ find simplehealth-back -name "*UseCase.java" | wc -l
47

# Total de classes na camada de aplicação
60 classes
```

### 2.2. Exemplo Real: PacienteService

**Arquivo**: `simplehealth-back-cadastro/src/main/java/.../service/PacienteService.java`

```java
@Service
public class PacienteService {

  private final PacienteRepository pacienteRepository;

  public PacienteService(PacienteRepository pacienteRepository) {
    this.pacienteRepository = pacienteRepository;
  }

  // CRUD básico - Responsabilidade: Entidade Paciente
  
  @Transactional
  public Paciente save(Paciente paciente) {
    if (pacienteRepository.existsByCpf(paciente.getCpf())) {
      throw new IllegalArgumentException("CPF já cadastrado");
    }
    return pacienteRepository.save(paciente);
  }

  @Transactional(readOnly = true)
  public Paciente findById(Long id) {
    return pacienteRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
  }

  @Transactional(readOnly = true)
  public List<Paciente> findAll() {
    return pacienteRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Optional<Paciente> findByCpf(String cpf) {
    return pacienteRepository.findByCpf(cpf);
  }

  public boolean existsByCpf(String cpf) {
    return pacienteRepository.existsByCpf(cpf);
  }

  @Transactional
  public void deleteById(Long id) {
    if (!pacienteRepository.existsById(id)) {
      throw new ResourceNotFoundException("Paciente não encontrado");
    }
    pacienteRepository.deleteById(id);
  }
}
```

**Características**:
- ✅ Foco em **uma única entidade** (Paciente)
- ✅ Operações **CRUD** básicas
- ✅ Validações **simples** (CPF duplicado, existência)
- ✅ Dependência de **1 Repository** apenas
- ✅ Transações **unitárias**

### 2.3. Exemplo Real: CadastrarNovoPacienteUseCase

**Arquivo**: `simplehealth-back-cadastro/src/main/java/.../usecases/CadastrarNovoPacienteUseCase.java`

```java
@Component
@RequiredArgsConstructor
public class CadastrarNovoPacienteUseCase {

  private final PacienteService pacienteService;
  private final ConvenioService convenioService;

  // Orquestração complexa - Responsabilidade: Caso de Uso Completo
  
  @Transactional
  public PacienteDTO execute(PacienteDTO dto) throws Exception {
    // 1. Validações complexas (mensagens personalizadas)
    if (pacienteService.existsByCpf(dto.getCpf())) {
      var existingPaciente = pacienteService.findAll().stream()
          .filter(p -> p.getCpf().equals(dto.getCpf()))
          .findFirst()
          .orElse(null);

      String nomePaciente = existingPaciente != null 
          ? existingPaciente.getNomeCompleto() 
          : "paciente existente";
      throw new Exception("CPF já cadastrado. Verifique o paciente " + nomePaciente + ".");
    }

    // 2. Conversão DTO → Entidade
    Paciente paciente = new Paciente();
    paciente.setNomeCompleto(dto.getNome());
    paciente.setCpf(dto.getCpf());
    paciente.setEmail(dto.getEmail());
    paciente.setTelefone(dto.getTelefone());
    paciente.setDataNascimento(dto.getDataNascimento());
    paciente.setGenero(dto.getGenero());
    paciente.setEndereco(dto.getEndereco());

    // 3. Orquestração de múltiplos Services
    if (dto.getConvenioId() != null) {
      Convenio convenio = convenioService.findById(dto.getConvenioId());
      paciente.setConvenio(convenio);
    }

    // 4. Persistência coordenada
    Paciente savedPaciente = pacienteService.save(paciente);

    // 5. Conversão Entidade → DTO (resposta)
    PacienteDTO responseDTO = new PacienteDTO();
    responseDTO.setId(savedPaciente.getId());
    responseDTO.setNome(savedPaciente.getNomeCompleto());
    responseDTO.setCpf(savedPaciente.getCpf());
    // ... outros campos

    return responseDTO;
  }
}
```

**Características**:
- ✅ Orquestra **múltiplos Services** (PacienteService + ConvenioService)
- ✅ Validações **complexas** com mensagens personalizadas
- ✅ Conversão **DTO ↔ Entidade**
- ✅ Transação **coordenada** entre múltiplas entidades
- ✅ Implementa **caso de uso completo** de ponta a ponta

---

## 3. Service vs UseCase: Diferenças Fundamentais

| Aspecto | Service | UseCase |
|---------|---------|---------|
| **Responsabilidade** | Entidade única | Processo de negócio completo |
| **Complexidade** | CRUD básico | Orquestração complexa |
| **Dependências** | 1 Repository | Múltiplos Services |
| **Transação** | Operação única | Transação coordenada |
| **Validações** | Simples (CPF duplicado) | Complexas (regras de negócio) |
| **Conversão DTO** | Não faz | DTO ↔ Entidade |
| **Integração** | Não integra | Pode integrar (Redis, etc) |
| **Exemplo** | `save(paciente)` | `cadastrarNovoPaciente(dto)` |
| **Anotação** | `@Service` | `@Component` |
| **Quantidade** | 13 classes | 47 classes |

### 3.1. Quando Usar Service?

✅ **Use Service quando**:
- Operação afeta **uma única entidade**
- Lógica é **CRUD básico** (Create, Read, Update, Delete)
- Validações são **simples** (existência, unicidade)
- Não precisa coordenar **múltiplas entidades**
- Não tem **conversão DTO complexa**

**Exemplos**:
- `pacienteService.findById(id)`
- `medicoService.existsByCrm(crm)`
- `estoqueService.save(estoque)`

### 3.2. Quando Usar UseCase?

✅ **Use UseCase quando**:
- Implementa **caso de uso** completo da regra de negócio
- Orquestra **múltiplos Services**
- Tem **validações complexas** (regras de negócio)
- Precisa fazer **conversão DTO ↔ Entidade**
- Envolve **transação coordenada**
- Integra com **sistemas externos** (Redis, APIs)

**Exemplos**:
- `cadastrarNovoPacienteUseCase.execute(dto)` → coordena PacienteService + ConvenioService
- `agendarConsultaUseCase.execute(dto)` → coordena ConsultaService + BloqueioService + Redis
- `darBaixaInsumosUseCase.execute(dto)` → coordena ItemService + EstoqueService + Validações

---

## 4. Arquitetura em Camadas Implementada

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                    │
│                       (Controllers)                       │
│  - PacienteController                                    │
│  - AgendamentoController                                 │
│  - EstoqueController                                     │
└───────────────────────────┬─────────────────────────────┘
                            │ Recebe HTTP requests
                            │ Chama UseCases
                            ▼
┌─────────────────────────────────────────────────────────┐
│              APPLICATION LAYER - UseCase Layer           │
│                    (Business Logic)                      │
│  - CadastrarNovoPacienteUseCase (47 classes)            │
│  - AgendarConsultaUseCase                                │
│  - DarBaixaInsumosUseCase                                │
│                                                          │
│  Responsabilidade:                                       │
│  ✓ Orquestrar múltiplos Services                        │
│  ✓ Implementar regras de negócio complexas              │
│  ✓ Converter DTO ↔ Entidade                             │
│  ✓ Coordenar transações                                 │
└───────────────────────────┬─────────────────────────────┘
                            │ Usa Services
                            ▼
┌─────────────────────────────────────────────────────────┐
│            APPLICATION LAYER - Service Layer             │
│                  (Entity Operations)                     │
│  - PacienteService (13 classes)                          │
│  - MedicoService                                         │
│  - ConsultaService                                       │
│                                                          │
│  Responsabilidade:                                       │
│  ✓ CRUD básico de entidades                             │
│  ✓ Validações simples                                   │
│  ✓ Encapsular acesso ao Repository                      │
└───────────────────────────┬─────────────────────────────┘
                            │ Usa Repositories
                            ▼
┌─────────────────────────────────────────────────────────┐
│                   INFRASTRUCTURE LAYER                   │
│                 (Data Access - Repository)               │
│  - PacienteRepository (Spring Data)                      │
│  - MedicoRepository                                      │
│  - ConsultaRepository                                    │
└───────────────────────────┬─────────────────────────────┘
                            │ Acessa Database
                            ▼
┌─────────────────────────────────────────────────────────┐
│                      DATABASE LAYER                      │
│  - PostgreSQL (Cadastro)                                 │
│  - MongoDB (Agendamento)                                 │
│  - Cassandra (Estoque)                                   │
└─────────────────────────────────────────────────────────┘
```

---

## 5. Correções Aplicadas na Documentação

### 5.1. Documento 3.4 - Classes de Análise

**Arquivo**: `docs/documentos-finais-definitivos/3.4. Classes de Análise/3.4. Classes de Análise_Diagrama de Classes.md`

**Alteração**: Adicionada nota técnica explicando Service + UseCase

**Antes**:
```markdown
# Diagrama de Classes de Análise

> **⚠️ NOTA TÉCNICA - Discrepância 3.1 Resolvida:**
> ...
```

**Depois**:
```markdown
# Diagrama de Classes de Análise

> **⚠️ NOTA TÉCNICA - Discrepância 3.1 Resolvida:**
> ...

> **⚠️ NOTA TÉCNICA - Discrepância 3.2 Resolvida:**
> 
> **Arquitetura em Camadas**: O sistema implementa **Service Layer + UseCase Pattern** combinados:
> - **Services** (13 classes): Lógica de negócio básica e operações CRUD 
>   (ex: PacienteService, MedicoService)
> - **UseCases** (47 classes): Casos de uso específicos que orquestram múltiplos Services 
>   (ex: CadastrarNovoPacienteUseCase, AgendarConsultaUseCase)
> 
> **Justificativa**: Separação de responsabilidades clara - Services encapsulam lógica de 
> entidade única, UseCases coordenam processos complexos envolvendo múltiplas entidades.
> 
> **Nomenclatura nos diagramas**: Classes de serviço são representadas como <<Service>> para 
> simplificação conceitual, mas na implementação há distinção entre Service e UseCase.
> 
> Data da correção: 14/12/2025
```

### 5.2. Documento 3.8 - DESIGN_PATTERNS.md

**Arquivo**: `docs/documentos-finais-definitivos/3.8 Boas Práticas/DESIGN_PATTERNS.md`

**Alteração 1**: Seção "6. Service Layer Pattern" expandida com exemplo real

**Antes**:
```markdown
## 6. Service Layer Pattern

**Onde**: Backend - Lógica de negócio
**Propósito**: Define camada de serviços entre controllers e repositories.

[código simplificado]
```

**Depois**:
```markdown
## 6. Service Layer Pattern

**Onde**: Backend - Lógica de negócio básica
**Propósito**: Encapsula operações CRUD e lógica de negócio relacionada a uma única entidade.

[código completo com PacienteService.java real]

**Outros Services do Sistema**:
- MedicoService, UsuarioService, ConvenioService (Cadastro)
- ConsultaService, ExameService, ProcedimentoService (Agendamento)
- EstoqueService, ItemService, MedicamentoService (Estoque)

**Total**: 13 classes Service no backend
```

**Alteração 2**: Nova seção "7. UseCase Pattern" criada

**Adicionado**:
```markdown
## 7. UseCase Pattern (Command Pattern)

**Onde**: Backend - Orquestração de processos complexos
**Propósito**: Encapsula casos de uso específicos que coordenam múltiplos Services e implementam 
regras de negócio complexas.

### Implementação

[Exemplos reais de CadastrarNovoPacienteUseCase e AgendarConsultaUseCase]

**Outros UseCases do Sistema**:
- Cadastro: 12 UseCases
- Agendamento: 22 UseCases
- Estoque: 13 UseCases

**Total**: 47 classes UseCase no backend

### Diferença: Service vs UseCase

| Aspecto | Service | UseCase |
|---------|---------|---------|
| **Escopo** | Entidade única | Processo completo |
| **Complexidade** | CRUD simples | Orquestração complexa |
| **Dependências** | Repository apenas | Múltiplos Services |
| **Transação** | Operação única | Transação coordenada |
| **Exemplo** | `save(paciente)` | `cadastrarNovoPaciente(dto)` |
```

**Impacto**: Seção "7. DTO Pattern" renumerada para "8. DTO Pattern"

### 5.3. Documento 3.8 - Boas praticas.md

**Arquivo**: `docs/documentos-finais-definitivos/3.8 Boas Práticas/3.8 Boas praticas.md`

**Alteração**: Seções renumeradas e Service Layer adicionado

**Antes**:
```markdown
### 3.1. Repository Pattern
...

### 3.2. Use Case Pattern
Classes específicas (AgendarConsultaUseCase, CadastrarNovoPacienteUseCase)
encapsulam fluxos de negócio completos...

### 3.3. DTO Pattern
...
```

**Depois**:
```markdown
### 3.1. Repository Pattern
...

### 3.2. Service Layer Pattern
Classes de serviço (PacienteService, MedicoService, ConsultaService) encapsulam lógica de 
negócio básica e operações CRUD relacionadas a uma única entidade.

**Implementado**: 13 classes Service no backend (Cadastro, Agendamento, Estoque)

### 3.3. Use Case Pattern (Command Pattern)
Classes específicas (AgendarConsultaUseCase, CadastrarNovoPacienteUseCase) encapsulam casos 
de uso completos que orquestram múltiplos Services.

**Implementado**: 47 classes UseCase no backend
- Cadastro: 12 UseCases
- Agendamento: 22 UseCases
- Estoque: 13 UseCases

### 3.4. DTO Pattern
...
```

---

## 6. Benefícios da Arquitetura Service + UseCase

### 6.1. Separação de Responsabilidades (SRP)

✅ **Service**: Responsável apenas pela entidade
✅ **UseCase**: Responsável pelo caso de uso completo

**Exemplo**:
```java
// Service: Foco na entidade Paciente
public class PacienteService {
    public Paciente save(Paciente p) { ... }        // CRUD básico
    public Paciente findById(Long id) { ... }       // CRUD básico
}

// UseCase: Foco no caso de uso "Cadastrar Novo Paciente"
public class CadastrarNovoPacienteUseCase {
    public PacienteDTO execute(PacienteDTO dto) {
        // Orquestra PacienteService + ConvenioService
        // Implementa regras complexas
        // Faz conversões DTO
    }
}
```

### 6.2. Testabilidade

✅ **Services são fáceis de testar** (testes unitários simples)
✅ **UseCases testam fluxo completo** (testes de integração)

```java
// Teste unitário de Service (simples)
@Test
void testSavePaciente() {
    Paciente paciente = new Paciente();
    when(repository.save(any())).thenReturn(paciente);
    
    Paciente saved = pacienteService.save(paciente);
    
    assertNotNull(saved);
}

// Teste de UseCase (fluxo completo)
@Test
void testCadastrarNovoPacienteComConvenio() {
    PacienteDTO dto = createDTOWithConvenio();
    when(pacienteService.existsByCpf(any())).thenReturn(false);
    when(convenioService.findById(any())).thenReturn(convenio);
    
    PacienteDTO result = useCase.execute(dto);
    
    assertNotNull(result.getId());
    assertEquals(convenio.getId(), result.getConvenioId());
}
```

### 6.3. Reusabilidade

✅ **Services são reutilizados** por múltiplos UseCases

```java
// PacienteService usado por:
- CadastrarNovoPacienteUseCase
- AtualizarPacienteUseCase
- BuscarPacienteUseCase
- ConsultarHistoricoPacienteUseCase
- DeletarPacienteUseCase
```

### 6.4. Manutenibilidade

✅ **Mudança em regra de negócio** afeta apenas UseCase
✅ **Mudança em CRUD** afeta apenas Service

```java
// Exemplo: Adicionar validação de idade mínima
// Alteração: APENAS no UseCase (regra de negócio)
public class CadastrarNovoPacienteUseCase {
    public PacienteDTO execute(PacienteDTO dto) {
        // Nova validação
        if (calcularIdade(dto.getDataNascimento()) < 18) {
            throw new BusinessException("Paciente deve ter 18 anos ou mais");
        }
        // ... resto do código inalterado
    }
}

// PacienteService NÃO precisa mudar (CRUD básico permanece o mesmo)
```

---

## 7. Exemplos de UseCases por Módulo

### 7.1. Módulo Cadastro (12 UseCases)

```
cadastro/application/usecases/
├── AtualizarPacienteUseCase.java
├── BuscarPacienteUseCase.java
├── CadastrarNovoPacienteUseCase.java          ← Orquestra Paciente + Convenio
├── ConsultarHistoricoPacienteUseCase.java     ← Integra com Redis (histórico)
├── DeletarPacienteUseCase.java
├── GerarAlertaEstoqueCriticoUseCase.java      ← Integra com Estoque via Redis
├── GerenciarConvenioUseCase.java
├── GerenciarMedicoUseCase.java                ← Orquestra Medico + Usuario
├── GerenciarUsuarioUseCase.java
└── ...
```

### 7.2. Módulo Agendamento (22 UseCases)

```
agendamento/application/usecases/
├── AgendarConsultaUseCase.java                ← Orquestra Consulta + Bloqueio + Redis
├── AgendarExameUseCase.java
├── AgendarProcedimentoUseCase.java
├── AtualizarConsultaUseCase.java
├── BuscarAgendamentosPorPacienteUseCase.java
├── BuscarBloqueiosPorMedicoUseCase.java
├── BuscarConsultaPorIdUseCase.java
├── CancelarAgendamentoUseCase.java            ← Atualiza status + notifica
├── CriarBloqueioAgendaUseCase.java
├── DesativarBloqueioAgendaUseCase.java
├── FinalizarConsultaUseCase.java              ← Atualiza timestamps + Redis
├── FinalizarExameUseCase.java
├── FinalizarProcedimentoUseCase.java
├── IniciarConsultaUseCase.java
├── IniciarExameUseCase.java
├── IniciarProcedimentoUseCase.java
├── SolicitarEncaixeUseCase.java               ← Validações complexas de horário
└── ...
```

### 7.3. Módulo Estoque (13 UseCases)

```
estoque/application/usecases/
├── ControlarValidadeUseCase.java              ← Valida itens + Gera alertas
├── DarBaixaInsumosUseCase.java                ← Atualiza Item + Estoque + Redis
├── EntradaItensUseCase.java                   ← Atualiza Estoque + Lotes
└── ...
```

---

## 8. Fluxo de Chamadas no Sistema

### 8.1. Exemplo: Cadastrar Novo Paciente

```
HTTP POST /api/pacientes
    │
    ▼
┌─────────────────────────────────────────────────────┐
│  PacienteController                                 │
│  @PostMapping                                       │
│  public ResponseEntity cadastrar(@RequestBody dto)  │
└──────────────────┬──────────────────────────────────┘
                   │ 1. Chama UseCase
                   ▼
┌─────────────────────────────────────────────────────┐
│  CadastrarNovoPacienteUseCase                       │
│  @Component                                         │
│  public PacienteDTO execute(PacienteDTO dto)        │
│                                                     │
│  ├─ Validações complexas                           │
│  ├─ Conversão DTO → Entidade                       │
│  ├─ 2. Chama PacienteService.existsByCpf()         │
│  ├─ 3. Chama ConvenioService.findById()            │
│  ├─ 4. Chama PacienteService.save()                │
│  └─ Conversão Entidade → DTO                       │
└───────────┬────────────────┬────────────────────────┘
            │                │
            │ 5. CRUD        │ 6. CRUD
            ▼                ▼
┌──────────────────┐  ┌──────────────────┐
│ PacienteService  │  │ ConvenioService  │
│ @Service         │  │ @Service         │
│                  │  │                  │
│ save()           │  │ findById()       │
│ existsByCpf()    │  │                  │
└────────┬─────────┘  └────────┬─────────┘
         │                     │
         │ 7. SQL              │ 8. SQL
         ▼                     ▼
┌──────────────────┐  ┌──────────────────┐
│PacienteRepository│  │ConvenioRepository│
│ (Spring Data)    │  │ (Spring Data)    │
└────────┬─────────┘  └────────┬─────────┘
         │                     │
         │ 9. Query            │ 10. Query
         ▼                     ▼
┌─────────────────────────────────────────┐
│         PostgreSQL Database             │
│   Tabela: paciente | Tabela: convenio   │
└─────────────────────────────────────────┘
```

---

## 9. Recomendações e Boas Práticas

### 9.1. Quando Criar um Novo Service

✅ **Crie Service quando**:
- Adicionar nova entidade ao domínio
- Precisa encapsular CRUD básico
- Validações são simples (duplicatas, existência)

**Exemplo**: Criar `EspecialidadeService` para entidade `Especialidade`

### 9.2. Quando Criar um Novo UseCase

✅ **Crie UseCase quando**:
- Implementar novo caso de uso de negócio
- Precisa coordenar múltiplos Services
- Tem regras de negócio complexas
- Precisa integração (Redis, APIs externas)

**Exemplo**: Criar `TransferirPacienteEntreConveniosUseCase`

### 9.3. Nomenclatura Consistente

✅ **Services**: `{Entidade}Service`
- `PacienteService`
- `MedicoService`
- `ConsultaService`

✅ **UseCases**: `{Verbo}{Substantivo}UseCase`
- `CadastrarNovoPacienteUseCase`
- `AgendarConsultaUseCase`
- `DarBaixaInsumosUseCase`

### 9.4. Injeção de Dependências

✅ **Service**: Injeta apenas Repository
```java
@Service
public class PacienteService {
    private final PacienteRepository repository;
    
    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }
}
```

✅ **UseCase**: Injeta múltiplos Services
```java
@Component
@RequiredArgsConstructor  // Lombok
public class CadastrarNovoPacienteUseCase {
    private final PacienteService pacienteService;
    private final ConvenioService convenioService;
    // Mais services conforme necessário
}
```

---

## 10. Checklist de Validação

### 10.1. Verificação de Implementação

- [x] ✅ 13 Services implementados no backend
- [x] ✅ 47 UseCases implementados no backend
- [x] ✅ Services usam apenas 1 Repository
- [x] ✅ UseCases orquestram múltiplos Services
- [x] ✅ Anotação `@Service` em Services
- [x] ✅ Anotação `@Component` em UseCases
- [x] ✅ Nomenclatura consistente (Service/UseCase)

### 10.2. Verificação de Documentação

- [x] ✅ Nota técnica em 3.4 Classes de Análise
- [x] ✅ Seção Service Layer em DESIGN_PATTERNS.md
- [x] ✅ Seção UseCase Pattern em DESIGN_PATTERNS.md
- [x] ✅ Tabela comparativa Service vs UseCase
- [x] ✅ Atualização em 3.8 Boas praticas.md
- [x] ✅ Exemplos reais de código documentados
- [x] ✅ Contagem de classes (13 + 47) documentada

---

## 11. Conclusão

### Arquitetura Implementada

```
SimpleHealth Backend Architecture

60 Classes na Camada de Aplicação:
├─ 13 Services (Entity Operations)
│  ├─ Cadastro: 5 Services
│  ├─ Agendamento: 5 Services
│  └─ Estoque: 3 Services
│
└─ 47 UseCases (Business Logic)
   ├─ Cadastro: 12 UseCases
   ├─ Agendamento: 22 UseCases
   └─ Estoque: 13 UseCases
```

### Benefícios da Correção

| Antes | Depois |
|-------|--------|
| ❌ Documentação mencionava apenas Service | ✅ Documentação completa: Service + UseCase |
| ❌ Não explicava responsabilidades | ✅ Responsabilidades claras e diferenciadas |
| ❌ Sem exemplos reais | ✅ Exemplos reais do código |
| ❌ Sem contagem de classes | ✅ Contagem documentada (13 + 47) |
| ❌ Arquitetura incompleta | ✅ Arquitetura em camadas completa |

### Impacto na Qualidade

✅ **Clareza**: Desenvolvedores entendem quando usar Service vs UseCase  
✅ **Manutenibilidade**: Mudanças em regras de negócio afetam apenas UseCases  
✅ **Testabilidade**: Testes unitários (Service) e integração (UseCase) separados  
✅ **Reusabilidade**: Services reutilizados por múltiplos UseCases  
✅ **Escalabilidade**: Fácil adicionar novos casos de uso sem impactar CRUD básico  

**Documentação agora reflete fielmente a arquitetura sofisticada implementada no backend! 🎉**

---

**Próximas Discrepâncias**: Verificar se há outras inconsistências entre documentação e implementação nos módulos.
