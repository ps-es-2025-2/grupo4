# CORREÇÃO DA DISCREPÂNCIA 3.3 - INTEGRAÇÃO ENTRE MICROSERVIÇOS

**Data da Correção:** 2025-01-17  
**Tipo de Correção:** Documentação (código já estava correto)  
**Sistema:** SimpleHealth  

---

## 1. RESUMO EXECUTIVO

### Discrepância Identificada
A documentação indicava dependências diretas entre serviços de diferentes módulos (ex: `AgendamentoService → CadastroService`, `AgendamentoService → EstoqueService`), sugerindo chamadas síncronas entre microserviços. Porém, a implementação **NÃO possui** tais dependências diretas.

### Arquitetura Real
- **Padrão:** Microservices com Database per Service
- **Integração:** Redis Pub/Sub (eventos assíncronos)
- **Desacoplamento:** Zero imports entre pacotes de módulos diferentes
- **Armazenamento:** Strings identificadoras (pacienteCpf, medicoCrm) em vez de referências a objetos

---

## 2. VERIFICAÇÃO DA IMPLEMENTAÇÃO

### 2.1. Backend: Ausência de Dependências Diretas

**Comando de Verificação:**
```bash
grep -r "import com.simplehealth.cadastro" simplehealth-back/simplehealth-back-agendamento/src/
```

**Resultado:**
```
No matches found
```

**Análise:** Módulo Agendamento **NÃO importa** nenhuma classe do módulo Cadastro, confirmando isolamento arquitetural.

### 2.2. Código-Fonte: AgendarConsultaUseCase

**Arquivo:** `AgendarConsultaUseCase.java`

**Dependências Injetadas:**
```java
@Service
public class AgendarConsultaUseCase {
    private final AgendamentoService agendamentoService;
    private final ConsultaService consultaService;
    
    // NÃO há injeção de CadastroService
}
```

**Modelo de Dados:**
```java
consulta.setPacienteCpf(dto.getPacienteCpf());       // String, não Paciente
consulta.setMedicoCrm(dto.getMedicoCrm());           // String, não Medico
consulta.setConvenioNome(dto.getConvenioNome());     // String, não Convenio
```

### 2.3. Padrão de Integração Real

**Redis Pub/Sub:**
- **4 instâncias Redis:** 3 específicas por módulo + 1 central
- **Publicação de Eventos:**
  ```
  Agendamento → "consulta-agendada"
  Agendamento → "consulta-finalizada"
  Cadastro → "paciente-atualizado"
  Estoque → "insumo-baixado"
  ```
- **Consumo Assíncrono:** Cada módulo consome eventos relevantes sem conhecer o produtor

---

## 3. ARQUIVOS CORRIGIDOS

### 3.1. Classes de Análise (Diagramas UML)

**Arquivo:** `3.4. Classes de Análise_Diagrama de Classes.md`

#### Correção 1: Relacionamento Falso (linha 240-250)
**ANTES:**
```
AgendamentoService o--> CadastroService : usa para validar Paciente/Medico
' Dependência/Integração: AgendamentoService precisa de regras do CadastroService
```

**DEPOIS:**
```
' NOTA ARQUITETURAL: NÃO há dependência direta entre módulos
' Agendamento armazena pacienteCpf e medicoCrm como Strings
' Integração com Cadastro é via Redis Pub/Sub (desacoplada)
```

#### Correção 2: Integração Síncrona Falsa (linha 310-320)
**ANTES:**
```
AgendamentoService --> EstoqueService : usa (ex: para dar baixa em material)
```

**DEPOIS:**
```
' NOTA: Integração entre módulos é via Redis Pub/Sub (desacoplada)
' NÃO há chamadas diretas entre AgendamentoService e EstoqueService
```

#### Correção 3: Seção de Integração (linha 755-770)
**ANTES:**
```
' O AgendamentoService integra a lógica chamando outros serviços:
AgendamentoService --> CadastroService : <<integra>> (valida paciente/médico)
AgendamentoService --> EstoqueService : <<integra>> (dá baixa em insumos)
```

**DEPOIS:**
```
' INTEGRAÇÃO ENTRE MÓDULOS VIA REDIS PUB/SUB
' ARQUITETURA DESACOPLADA: Módulos NÃO se chamam diretamente
' Integração via Redis Pub/Sub para eventos:
' - Agendamento publica: 'consulta-agendada', 'consulta-finalizada'
' - Cadastro pode consumir: atualizar histórico de paciente
' - Estoque pode consumir: reservar/dar baixa em insumos
' Agendamento armazena APENAS identificação (não objetos):
' - pacienteCpf : String (não objeto Paciente)
' - medicoCrm : String (não objeto Medico)
' - convenioNome : String (não objeto Convenio)
```

### 3.2. Modelagem de Interações (Diagramas de Sequência)

**Arquivo:** `3.9. Modelagem de Interações.md`

#### Correção Global: DAO → Repository
**Comando sed aplicado:**
```bash
sed -i 's/:AgendamentoDAO/:AgendamentoRepository/g; 
        s/AgDAO/AgRepository/g; 
        s/persistir/save/g; 
        s/buscarPorPeriodo/findByPeriodo/g; 
        s/buscarAgendamentoPorId/findById/g; 
        s/buscarAgendamentosPorPaciente/findByPacienteCpf/g'
```

**Efeito:** Alinhamento com Spring Data Repository pattern.

#### UC02 - Agendar Consulta

**ANTES:**
```plantuml
participant ":CadastroService" as CadService <<Service>>
...
AgService -> CadService: paciente := buscarPaciente(dadosAgendamento.pacienteId)
```

**DEPOIS:**
```plantuml
' NOTA: NÃO há chamada direta AgendamentoService -> CadastroService
' Agendamento armazena apenas String (pacienteCpf, medicoCrm, convenioNome)
' Validação de dados (pacienteCpf, medicoCrm) ocorre na camada controller/frontend
```

**Nota Arquitetural Adicionada:**
```
NOTA ARQUITETURAL: Na implementação real, NÃO existe dependência direta 
entre AgendamentoService e CadastroService. Os módulos são desacoplados 
e integram-se via Redis Pub/Sub (eventos). Consultas armazenam apenas 
pacienteCpf (String), não objetos Paciente.
```

#### UC03 - Solicitar Encaixe

**ANTES:**
```plantuml
participant ":CadastroService" as CadService <<Service>>
...
AgService -> CadService: temPermissao := temPermissaoEncaixe(actorId)
```

**DEPOIS:**
```plantuml
' NOTA: Validação de permissão é feita internamente ou na camada controller
' NÃO há chamada direta entre AgendamentoService e CadastroService
AgService -> AgService: temPermissao := verificarPermissaoEncaixe(actorId)
```

#### UC08 - Consultar Histórico do Paciente

**ANTES:**
```plantuml
participant ":CadastroService" as CadService <<Service>>
participant ":EstoqueDAO" as EstDAO <<Repository>>
...
AgService -> CadService: paciente := buscarPacientePorCpf(pacienteCpf)
AgService -> EstDAO: insumos := buscarInsumosPorAgendamento(agendamento.id)
```

**DEPOIS:**
```plantuml
' NOTA: Integração entre módulos via Redis Pub/Sub (não há chamadas diretas)
participant ":AgendamentoRepository" as AgRepository <<Repository>>
participant ":EstoqueRepository" as EstRepository <<Repository>>
...
' Validação de pacienteCpf ocorre na camada controller/frontend
' NOTA: Agendamento já possui pacienteCpf (String)
' Histórico completo seria agregado via eventos Redis ou API Gateway
```

---

## 4. PADRÕES ARQUITETURAIS DOCUMENTADOS

### 4.1. Database per Service
- **Agendamento:** MongoDB (documentos JSON)
- **Cadastro:** PostgreSQL (dados relacionais)
- **Estoque:** Cassandra (séries temporais)

### 4.2. Event-Driven Architecture
```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│ Agendamento │       │  Cadastro   │       │  Estoque    │
│   Module    │       │   Module    │       │   Module    │
└──────┬──────┘       └──────┬──────┘       └──────┬──────┘
       │                     │                     │
       │  publish('consulta-agendada')              │
       ├─────────────────────┼─────────────────────┤
       │                     │                     │
       │                     │  consume            │  consume
       │                     ▼                     ▼
       │              [atualizar histórico]  [reservar insumos]
       │
       └───────────────► Redis Pub/Sub ◄──────────────┘
```

### 4.3. String-Based References
```java
// NÃO é feito assim (acoplamento):
consulta.setPaciente(pacienteObjeto);

// É feito assim (desacoplamento):
consulta.setPacienteCpf("123.456.789-00");
```

**Vantagens:**
- Nenhuma dependência entre módulos
- Cada módulo gerencia seu próprio domínio
- Mudanças em Paciente não afetam Agendamento
- Escalabilidade independente

---

## 5. IMPACTO DA CORREÇÃO

### 5.1. Documentação Afetada
| Documento | Linhas Alteradas | Tipo de Correção |
|-----------|------------------|------------------|
| 3.4 Classes de Análise | 240-250, 310-320, 755-770 | Relacionamentos UML |
| 3.9 Modelagem de Interações | UC02, UC03, UC08 | Diagramas de Sequência |

### 5.2. Mudanças Conceituais
- **DE:** Arquitetura monolítica com serviços acoplados
- **PARA:** Microservices com integração assíncrona via eventos

### 5.3. Código-Fonte
**Nenhuma alteração necessária** - implementação já estava correta.

---

## 6. VALIDAÇÃO DA CORREÇÃO

### 6.1. Checklist de Verificação
- [x] Zero imports entre módulos diferentes (grep confirmado)
- [x] Diagramas UML sem setas diretas entre serviços cross-module
- [x] Notas arquiteturais adicionadas sobre Redis Pub/Sub
- [x] Modelo de dados documentado (String-based references)
- [x] Diagramas de sequência atualizados (UC02, UC03, UC08)
- [x] Padrão Repository aplicado (DAO → Repository)

### 6.2. Consistência Documentação ↔ Código
| Aspecto | Documentação Antes | Implementação | Documentação Depois |
|---------|-------------------|---------------|---------------------|
| Integração Agendamento-Cadastro | Chamada direta | Redis Pub/Sub | Redis Pub/Sub ✅ |
| Integração Agendamento-Estoque | Chamada direta | Redis Pub/Sub | Redis Pub/Sub ✅ |
| Armazenamento de referências | Objetos | Strings | Strings ✅ |
| Padrão de acesso a dados | DAO | Repository | Repository ✅ |

---

## 7. LIÇÕES APRENDIDAS

### 7.1. Arquitetura de Microservices
- **Isolamento Rigoroso:** Nenhum import cross-module é permitido
- **Comunicação Assíncrona:** Redis Pub/Sub para todos os eventos
- **Eventual Consistency:** Aceitável para agregação de dados

### 7.2. Documentação Técnica
- **Sincronização:** Diagramas devem refletir implementação real
- **Notas Arquiteturais:** Essenciais quando diagramas simplificam
- **Verificação Periódica:** Código e documentação divergem ao longo do tempo

### 7.3. Padrões Utilizados
- **Database per Service** (mantém autonomia dos módulos)
- **Event-Driven Architecture** (desacoplamento temporal)
- **API Composition** (agregação de dados via gateway/BFF)
- **String-Based References** (evita acoplamento de domínio)

---

## 8. PRÓXIMOS PASSOS (SE HOUVER)

### 8.1. Documentação Adicional
- [ ] Criar diagrama de arquitetura mostrando Redis Pub/Sub
- [ ] Documentar contratos de eventos (schemas de mensagens)
- [ ] Adicionar exemplo de fluxo completo com eventos

### 8.2. Validação Complementar
- [ ] Revisar outros UCs (UC04-UC07, UC09-UC10) para padrão similar
- [ ] Verificar se há outras referências a chamadas diretas cross-module
- [ ] Atualizar glossário com definição de "integração via eventos"

---

## 9. REFERÊNCIAS

### 9.1. Arquivos Analisados
- `simplehealth-back/simplehealth-back-agendamento/src/main/java/com/simplehealth/agendamento/application/usecases/AgendarConsultaUseCase.java`
- `simplehealth-back/simplehealth-back-agendamento/src/main/java/com/simplehealth/agendamento/domain/model/Consulta.java`
- `simplehealth-back/simplehealth-back-agendamento/src/main/java/com/simplehealth/agendamento/application/services/`

### 9.2. Comandos de Verificação
```bash
# Verificar imports cross-module
grep -r "import com.simplehealth.cadastro" simplehealth-back/simplehealth-back-agendamento/src/
grep -r "import com.simplehealth.estoque" simplehealth-back/simplehealth-back-agendamento/src/

# Buscar Redis Pub/Sub
grep -r "RedisTemplate" simplehealth-back/simplehealth-back-agendamento/src/
grep -r "@EventListener" simplehealth-back/simplehealth-back-agendamento/src/
```

### 9.3. Padrões Arquiteturais
- **Microservices Pattern:** Database per Service
- **Integration Pattern:** Event-Driven Architecture (EDA)
- **Data Pattern:** String-Based References
- **Repository Pattern:** Spring Data JPA/MongoDB/Cassandra

---

**Correção Finalizada:** 2025-01-17  
**Status:** ✅ Documentação Alinhada com Implementação  
**Impacto no Código:** Nenhum (código já estava correto)
