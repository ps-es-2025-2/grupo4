—

# Padrões de Projeto - SimpleHealth

Este documento evidencia os **10 padrões de projeto** implementados no SimpleHealth, com exemplos de código reais e links para as classes.

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

**Onde**: Backend - Lógica de negócio
**Propósito**: Define camada de serviços entre controllers e repositories.

### Implementação

**Backend Cadastro**: [`PacienteService.java`](simplehealth-back/simplehealth-back-cadastro/src/main/java/com/simplehealth/cadastro/application/service/PacienteService.java)

```java
@Service
public class PacienteService {
  
    @Autowired
    private PacienteRepository pacienteRepository;
  
    @Autowired
    private CadastrarNovoPacienteUseCase cadastrarUseCase;
  
    // Lógica de negócio
    public Paciente cadastrar(PacienteDTO dto) {
        // Validações
        if (pacienteRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new BusinessException("CPF já cadastrado");
        }
      
        // Regras de negócio
        return cadastrarUseCase.execute(dto);
    }
  
    public List<Paciente> buscarPorNome(String nome) { ... }
}
```

---

## 7. DTO (Data Transfer Object) Pattern

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