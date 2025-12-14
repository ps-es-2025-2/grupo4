—

# Padrões de Projeto - SimpleHealth

Este documento evidencia os **10 padrões de projeto** implementados no SimpleHealth, com exemplos de código reais e links para as classes.

---

## Notas Técnicas - Correções de Discrepâncias

Os padrões de projeto implementados no sistema refletem decisões arquiteturais importantes.

### Discrepância 3.1: Padrão DAO vs Repository (Spring Data)

**Discrepância:** Documentação original usava termo "DAO Pattern", mas o sistema implementa Spring Data Repository Pattern.

**Mudança Feita:** Terminologia atualizada para "Repository Pattern" em todo o documento. JpaRepository, MongoRepository, CassandraRepository são os padrões reais.

**Justificativa:** Spring Data Repository é uma abstração mais moderna que DAO tradicional, com suporte a queries derivadas de métodos, @Query, e menor boilerplate.

**Documento Detalhado:** [📄 CORRECAO_DISCREPANCIA_3.1.md](../../Correções%20de%20Alinhamento/CORRECAO_DISCREPANCIA_3.1.md)

### Discrepância 3.2: Camada de Serviço - Service vs UseCase

**Discrepância:** Sistema implementa DOIS padrões distintos: Service Layer (13 classes) + Use Case Pattern (47 classes), mas documentação não distinguia.

**Mudança Feita:** Documentado padrão Use Case separadamente, explicando diferença:
- **Services**: Lógica de negócio básica, operações CRUD simples (ex: PacienteService, MedicoService)
- **UseCases**: Orquestração complexa envolvendo múltiplos Services (ex: CadastrarNovoPacienteUseCase, AgendarConsultaUseCase)

**Justificativa:** Separação de responsabilidades clara. Services encapsulam lógica de entidade única, UseCases coordenam processos de negócio que envolvem múltiplas entidades e validações complexas.

**Documento Detalhado:** [📄 CORRECAO_DISCREPANCIA_3.2.md](../../Correções%20de%20Alinhamento/CORRECAO_DISCREPANCIA_3.2.md)

---

Para consultar todas as correções de discrepâncias do projeto, acesse o [📑 Sumário de Correções](../../Correções%20de%20Alinhamento/SUMARIO_CORRECAO_DISCREPANCIA.md).

---

## 1.  Template Method Pattern

**Onde**: Frontend - Controllers CRUD  

**Propósito**: Define o esqueleto de um algoritmo, permitindo que subclasses sobrescrevam passos específicos.

### Implementação

**Classe Base**: [`AbstractCrudController.java`](simplehealth-front/simplehealth-front-cadastro/src/main/java/br/com/simplehealth/cadastro/controller/AbstractCrudController.java)

```java

public abstract class AbstractCrudController<T> {

    

    // Controle de modo de edição

    protected String modoEdicao = null; // "CRIAR", "ALTERAR" ou null

    protected T itemSelecionado = null;

    

    // Template method - define o fluxo

    protected void configurarCoresBotoes() {

        if (btnCriar != null) {

            btnCriar.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

        }

        // ... outros botões

    }

    

    // Métodos auxiliares compartilhados

    protected void mostrarErro(String titulo, String mensagem) { ... }

    protected void mostrarSucesso(String titulo, String mensagem) { ... }

    protected boolean mostrarConfirmacao(String titulo, String mensagem) { ... }

}
```

Uso em Subclasses:

PacienteController.java
MedicoController.java
MedicamentoController.java


## 2. Observer Pattern

**Onde**: Frontend - Sistema de Refresh
**Propósito**: Define dependência um-para-muitos, onde mudança em um objeto notifica seus dependentes.

### Implementação

**Subject**: [`RefreshManager.java`](simplehealth-front/simplehealth-front-cadastro/src/main/java/br/com/simplehealth/cadastro/util/RefreshManager.java)

```java
public class RefreshManager {
    private static final List<Runnable> refreshListeners = new ArrayList<>();

    // Adiciona observador
    public static void addRefreshListener(Runnable listener) {
        refreshListeners.add(listener);
    }

    // Remove observador
    public static void removeRefreshListener(Runnable listener) {
        refreshListeners.remove(listener);
    }

    // Notifica todos os observadores
    public static void notifyRefresh() {
        Platform.runLater(() -> {
            for (Runnable listener : refreshListeners) {
                listener.run();
            }
        });
    }
}
```

**Observers (Controllers)**: Qualquer controller que registra um listener para atualizar automaticamente quando dados mudam.

---

## 3. Singleton Pattern

**Onde**: Frontend - Configuração da Aplicação
**Propósito**: Garante que uma classe tenha apenas uma instância e fornece ponto de acesso global.

### Implementação

**Classe**: [`AppConfig.java`](simplehealth-front/simplehealth-front-cadastro/src/main/java/br/com/simplehealth/cadastro/config/AppConfig.java)

```java
public class AppConfig {
    // Instância única (Singleton)
    private static final AppConfig instance = new AppConfig();
  
    // URLs dos backends
    private String baseUrlCadastro = "http://localhost:8081";
    private String baseUrlAgendamento = "http://localhost:8082";
    private String baseUrlEstoque = "http://localhost:8083";
  
    // Construtor privado
    private AppConfig() {}
  
    // Ponto de acesso global
    public static AppConfig getInstance() {
        return instance;
    }
  
    // Getters
    public String getBaseUrlCadastro() { return baseUrlCadastro; }
    public String getBaseUrlAgendamento() { return baseUrlAgendamento; }
    public String getBaseUrlEstoque() { return baseUrlEstoque; }
}
```

**Uso**:

```java
String url = AppConfig.getInstance().getBaseUrlCadastro() + "/pacientes";
```

---

## 4. Repository Pattern

**Onde**: Backend - Acesso a dados
**Propósito**: Encapsula lógica de acesso a dados, centralizando consultas.

### Implementação

**Spring Data JPA** (Módulo Cadastro):

```java
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByCpf(String cpf);
    List<Paciente> findByNomeContainingIgnoreCase(String nome);
}
```

**Spring Data MongoDB** (Módulo Agendamento):

```java
@Repository
public interface ConsultaRepository extends MongoRepository<Consulta, String> {
    List<Consulta> findByMedicoIdAndDataBetween(Long medicoId, LocalDateTime inicio, LocalDateTime fim);
}
```

**Spring Data Cassandra** (Módulo Estoque):

```java
@Repository
public interface MedicamentoRepository extends CassandraRepository<Medicamento, UUID> {
    List<Medicamento> findByNomeContainingIgnoreCase(String nome);
}
```

> **📝 Nota:** Cassandra foi removido do módulo de Cadastro (Discrepância 1.2). Permanece apenas no módulo de Estoque.

---

## 5. Facade Pattern

**Onde**: Frontend - Services HTTP
**Propósito**: Fornece interface simplificada para subsistema complexo (APIs REST).

### Implementação

**Exemplo**: [`PacienteService.java`](simplehealth-front/simplehealth-front-cadastro/src/main/java/br/com/simplehealth/cadastro/service/PacienteService.java)

```java
public class PacienteService {
    private final String BASE_URL = AppConfig.getInstance().getBaseUrlCadastro() + "/pacientes";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Facade: esconde complexidade de HTTP, JSON, exceções
    public List<Paciente> listarTodos() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL))
            .GET()
            .build();
      
        HttpResponse<String> response = httpClient.send(request, 
            HttpResponse.BodyHandlers.ofString());
      
        return objectMapper.readValue(response.body(), 
            new TypeReference<List<Paciente>>() {});
    }
  
    public Paciente salvar(Paciente paciente) throws Exception { ... }
    public void deletar(Long id) throws Exception { ... }
}
```

**Uso no Controller**:

```java
List<Paciente> pacientes = pacienteService.listarTodos(); // Interface simples!
```

---

## 6. Service Layer Pattern

**Onde**: Backend - Lógica de negócio básica
**Propósito**: Encapsula operações CRUD e lógica de negócio relacionada a uma única entidade.

### Implementação

**Backend Cadastro**: [`PacienteService.java`](simplehealth-back/simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/application/service/PacienteService.java)

```java
@Service
public class PacienteService {
  
    private final PacienteRepository pacienteRepository;
  
    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }
  
    // Operações CRUD básicas
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
  
    public List<Paciente> findAll() {
        return pacienteRepository.findAll();
    }
  
    public boolean existsByCpf(String cpf) {
        return pacienteRepository.existsByCpf(cpf);
    }
}
```

**Outros Services do Sistema**:
- `MedicoService`, `UsuarioService`, `ConvenioService` (Cadastro)
- `ConsultaService`, `ExameService`, `ProcedimentoService` (Agendamento)
- `EstoqueService`, `ItemService`, `MedicamentoService` (Estoque)

**Total**: 13 classes Service no backend

---

## 7. UseCase Pattern (Command Pattern)

**Onde**: Backend - Orquestração de processos complexos
**Propósito**: Encapsula casos de uso específicos que coordenam múltiplos Services e implementam regras de negócio complexas.

### Implementação

**Cadastro**: [`CadastrarNovoPacienteUseCase.java`](simplehealth-back/simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/application/usecases/CadastrarNovoPacienteUseCase.java)

```java
@Component
@RequiredArgsConstructor
public class CadastrarNovoPacienteUseCase {

    private final PacienteService pacienteService;
    private final ConvenioService convenioService;

    @Transactional
    public PacienteDTO execute(PacienteDTO dto) throws Exception {
        // Validações complexas
        if (pacienteService.existsByCpf(dto.getCpf())) {
            var existingPaciente = pacienteService.findAll().stream()
                .filter(p -> p.getCpf().equals(dto.getCpf()))
                .findFirst()
                .orElse(null);
            throw new Exception("CPF já cadastrado...");
        }

        // Orquestração de múltiplos services
        Paciente paciente = convertToEntity(dto);
        
        if (dto.getConvenioId() != null) {
            Convenio convenio = convenioService.findById(dto.getConvenioId());
            paciente.setConvenio(convenio);
        }

        // Execução
        Paciente savedPaciente = pacienteService.save(paciente);
        return convertToDTO(savedPaciente);
    }
}
```

**Agendamento**: [`AgendarConsultaUseCase.java`](simplehealth-back/simplehealth-back-agendamento/src/main/java/com/simplehealth/agendamento/application/usecases/AgendarConsultaUseCase.java)

```java
@Component
@RequiredArgsConstructor
public class AgendarConsultaUseCase {

    private final ConsultaService consultaService;
    private final BloqueioAgendaService bloqueioService;
    private final HistoricoPublisher historicoPublisher; // Integração Redis

    @Transactional
    public ConsultaDTO execute(ConsultaDTO dto) throws Exception {
        // Validação de conflitos
        if (bloqueioService.existeConflito(dto.getMedicoCrm(), dto.getDataHora())) {
            throw new BusinessException("Horário bloqueado");
        }

        // Validação de disponibilidade
        if (consultaService.existeConflitoAgenda(dto)) {
            throw new BusinessException("Horário já ocupado");
        }

        // Persistência
        Consulta consulta = consultaService.save(convertToEntity(dto));

        // Publicação de evento (integração com outros módulos)
        historicoPublisher.publicarConsulta(consulta);

        return convertToDTO(consulta);
    }
}
```

**Outros UseCases do Sistema**:
- Cadastro: 12 UseCases (incluindo `ConsultarHistoricoPacienteUseCase`, `GerenciarMedicoUseCase`)
- Agendamento: 22 UseCases (incluindo `SolicitarEncaixeUseCase`, `CancelarAgendamentoUseCase`)
- Estoque: 13 UseCases (incluindo `DarBaixaInsumosUseCase`, `ControlarValidadeUseCase`)

**Total**: 47 classes UseCase no backend

### Diferença: Service vs UseCase

| Aspecto | Service | UseCase |
|---------|---------|---------|
| **Escopo** | Entidade única | Processo completo |
| **Complexidade** | CRUD simples | Orquestração complexa |
| **Dependências** | Repository apenas | Múltiplos Services |
| **Transação** | Operação única | Transação coordenada |
| **Exemplo** | `save(paciente)` | `cadastrarNovoPaciente(dto)` |

---

## 8. DTO (Data Transfer Object) Pattern

**Onde**: Backend - Transferência entre camadas
**Propósito**: Carregar dados entre processos, reduzindo número de chamadas.

### Implementação

**Exemplo**: `PacienteDTO.java`

```java
public class PacienteDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private LocalDate dataNascimento;
  
    // Getters e Setters
}
```

**Conversão** (Mapper):

```java
public class PacienteMapper {
    public static PacienteDTO toDTO(Paciente entity) {
        PacienteDTO dto = new PacienteDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        // ...
        return dto;
    }
  
    public static Paciente toEntity(PacienteDTO dto) { ... }
}
```

---

## 8. Dependency Injection Pattern

**Onde**: Backend - Todo o sistema Spring Boot
**Propósito**: Inverte controle de criação de dependências.

### Implementação

**Spring Framework** gerencia automaticamente:

```java
@RestController
@RequestMapping("/pacientes")
public class PacienteController {
  
    // Injeção automática via Spring
    @Autowired
    private PacienteService pacienteService;
  
    @GetMapping
    public List<PacienteDTO> listar() {
        return pacienteService.listarTodos();
    }
}
```

**Configuração**:

```java
@Service
public class PacienteService { ... }

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> { ... }
```

---

## 9. MVC (Model-View-Controller) Pattern

**Onde**: Frontend JavaFX
**Propósito**: Separa dados (Model), interface (View) e lógica (Controller).

### Implementação

**Model**:

```java
public class Paciente {
    private Long id;
    private String nome;
    private String cpf;
    // ...
}
```

**View** (FXML): `paciente-view.fxml`

```xml
<VBox>
    <TextField fx:id="txtNome" />
    <TextField fx:id="txtCpf" />
    <Button text="Salvar" onAction="#salvar" />
</VBox>
```

**Controller**:

```java
public class PacienteController extends AbstractCrudController<Paciente> {
  
    @FXML private TextField txtNome;
    @FXML private TextField txtCpf;
  
    @FXML
    private void salvar() {
        Paciente paciente = new Paciente();
        paciente.setNome(txtNome.getText());
        paciente.setCpf(txtCpf.getText());
      
        pacienteService.salvar(paciente);
    }
}
```

---

## 10. REST API Pattern

**Onde**: Backend - Comunicação entre serviços
**Propósito**: Arquitetura stateless para comunicação via HTTP.

### Implementação

**Backend**: `PacienteController.java`

```java
@RestController
@RequestMapping("/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {
  
    @Autowired
    private PacienteService service;
  
    @GetMapping
    public ResponseEntity<List<PacienteDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }
  
    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
  
    @PostMapping
    public ResponseEntity<PacienteDTO> criar(@RequestBody @Valid PacienteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.cadastrar(dto));
    }
  
    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> atualizar(
            @PathVariable Long id, 
            @RequestBody @Valid PacienteDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }
  
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## Resumo dos Padrões

| Padrão              | Categoria      | Localização        | Evidência                 |
| -------------------- | -------------- | -------------------- | -------------------------- |
| Template Method      | Comportamental | Frontend Controllers | `AbstractCrudController` |
| Observer             | Comportamental | Frontend Refresh     | `RefreshManager`         |
| Singleton            | Criacional     | Frontend Config      | `AppConfig`              |
| Repository           | Estrutural     | Backend Data         | Spring Data repos          |
| Facade               | Estrutural     | Frontend Services    | HTTP Services              |
| Service Layer        | Arquitetural   | Backend Business     | `@Service` classes       |
| DTO                  | Estrutural     | Backend Transfer     | DTO classes                |
| Dependency Injection | Criacional     | Backend              | Spring Framework           |
| MVC                  | Arquitetural   | Frontend             | JavaFX structure           |
| REST API             | Arquitetural   | Backend              | Spring Web MVC             |

---