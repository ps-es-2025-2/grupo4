# 🏥 SimpleHealth - Sistema de Gestão Hospitalar Integrada

## 📋 Visão Geral do Projeto

O **SimpleHealth** é um sistema completo de gestão hospitalar desenvolvido com arquitetura de microsserviços. O projeto é composto por **3 módulos independentes** (Cadastro, Agendamento e Estoque), cada um com seu próprio frontend JavaFX e backend Spring Boot, comunicando-se via APIs REST.

**Versão**: 1.0.0  
**Arquitetura**: Microsserviços  
**Tecnologias**: Java 17, JavaFX 17, Spring Boot 3.5.6, Maven 3.9.x  
**Containerização**: Docker & Docker Compose

---

## 🎯 Casos de Uso Implementados (16 Total)

### Módulo Cadastro (UC01-UC05) - Porta 8081
- **UC01**: Autenticação de Usuário (Login)
- **UC02**: Cadastrar Paciente
- **UC03**: Cadastrar Médico
- **UC04**: Cadastrar Usuário do Sistema
- **UC05**: Cadastrar Convênio Médico

### Módulo Agendamento (UC06-UC09) - Porta 8082
- **UC06**: Agendar Consulta
- **UC07**: Agendar Exame
- **UC08**: Agendar Procedimento
- **UC09**: Gerenciar Bloqueios de Agenda

### Módulo Estoque (UC10-UC16) - Porta 8083
- **UC10**: Cadastrar Medicamento
- **UC11**: Cadastrar Alimento
- **UC12**: Cadastrar Material Hospitalar
- **UC13**: Cadastrar Fornecedor
- **UC14**: Gerenciar Localizações de Estoque
- **UC15**: Gerenciar Pedidos
- **UC16**: Visualizar Itens Consolidados

---

## 🏗️ Arquitetura do Sistema

### Arquitetura Lógica Completa

```
┌──────────────────────────────────────────────────────────────────────────┐
│                        CAMADA DE APRESENTAÇÃO                             │
│                         (3 Frontends JavaFX)                              │
├────────────────────┬────────────────────┬─────────────────────────────────┤
│  Frontend Cadastro │ Frontend Agendamento│    Frontend Estoque           │
│                    │                    │                                │
│ • LoginController  │ • ConsultaController│ • MedicamentoController       │
│ • PacienteCtrl     │ • ExameController  │ • AlimentoController          │
│ • MedicoCtrl       │ • ProcedimentoCtrl │ • HospitalarController        │
│ • UsuarioCtrl      │ • BloqueioCtrl     │ • FornecedorController        │
│ • ConvenioCtrl     │                    │ • EstoqueController           │
│                    │                    │ • PedidoController            │
│                    │                    │ • ItemController              │
└────────────────────┴────────────────────┴─────────────────────────────────┘
                                  │
                                  ▼ HTTP/REST
┌──────────────────────────────────────────────────────────────────────────┐
│                         CAMADA DE NEGÓCIO                                 │
│                    (3 Backends Spring Boot)                               │
├────────────────────┬────────────────────┬─────────────────────────────────┤
│  Backend Cadastro  │ Backend Agendamento│    Backend Estoque            │
│   Porta: 8081      │   Porta: 8082      │    Porta: 8083               │
│                    │                    │                                │
│ • PacienteService  │ • ConsultaService  │ • MedicamentoService          │
│ • MedicoService    │ • ExameService     │ • AlimentoService             │
│ • UsuarioService   │ • ProcedimentoSvc  │ • HospitalarService           │
│ • ConvenioService  │ • BloqueioService  │ • FornecedorService           │
│                    │                    │ • EstoqueService              │
│                    │                    │ • PedidoService               │
│                    │                    │ • ItemService                 │
└────────────────────┴────────────────────┴─────────────────────────────────┘
                                  │
                                  ▼
┌──────────────────────────────────────────────────────────────────────────┐
│                        CAMADA DE DADOS                                    │
├────────────────────┬────────────────────┬─────────────────────────────────┤
│ PostgreSQL + ImmuDB│  MongoDB + Redis   │   ImmuDB + Redis              │
│  :5432     :3322   │  :27017   :6379    │   :3322    :6379              │
└────────────────────┴────────────────────┴─────────────────────────────────┘
```

### Arquitetura Física

```
┌────────────────────────────────────────────────────────────────┐
│                     MÁQUINA DO DESENVOLVEDOR                    │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              Docker Desktop / Docker Engine              │  │
│  │                                                          │  │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐    │  │
│  │  │  Container  │  │  Container  │  │  Container  │    │  │
│  │  │  Frontend   │  │  Frontend   │  │  Frontend   │    │  │
│  │  │  Cadastro   │  │ Agendamento │  │  Estoque    │    │  │
│  │  │  (JavaFX)   │  │  (JavaFX)   │  │  (JavaFX)   │    │  │
│  │  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘    │  │
│  │         │ HTTP           │ HTTP           │ HTTP       │  │
│  │         ▼                ▼                ▼            │  │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐    │  │
│  │  │  Container  │  │  Container  │  │  Container  │    │  │
│  │  │  Backend    │  │  Backend    │  │  Backend    │    │  │
│  │  │  Cadastro   │  │ Agendamento │  │  Estoque    │    │  │
│  │  │  :8081      │  │  :8082      │  │  :8083      │    │  │
│  │  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘    │  │
│  │         │                │                │            │  │
│  │    ┌────┴─────┐     ┌────┴─────┐     ┌────┴─────┐     │  │
│  │    ▼          ▼     ▼          ▼     ▼          ▼     │  │
│  │  ┌────┐   ┌────┐ ┌────┐   ┌────┐ ┌────┐   ┌────┐    │  │
│  │  │Post│   │Immu│ │Mongo│  │Redis│ │Immu│   │Redis│    │  │
│  │  │greSQL  │DB  │ │DB  │  │     │ │DB  │   │     │    │  │
│  │  │5432│   │3322│ │27017  │6379 │ │3322│   │6379 │    │  │
│  │  └────┘   └────┘ └────┘   └────┘ └────┘   └────┘    │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────┘
```

---

## 📦 Estrutura de Módulos e Tecnologias

### 1. Módulo de Cadastro

**Backend**:
- Framework: Spring Boot 3.5.6
- Porta: 8081
- Banco de Dados: PostgreSQL 15 + ImmuDB 1.5.0
- Cache: Redis 7
- API: REST (JSON)

**Frontend**:
- Framework: JavaFX 17
- Comunicação: Apache HttpClient 5.2.1
- Execução: Maven (`mvn javafx:run`)

**Entidades**: Paciente, Médico, Usuário, Convênio

### 2. Módulo de Agendamento

**Backend**:
- Framework: Spring Boot 3.5.6
- Porta: 8082
- Banco de Dados: MongoDB 6.0
- Cache: Redis 7
- API: REST (JSON)

**Frontend**:
- Framework: JavaFX 17
- Comunicação: Apache HttpClient 5.2.1
- Execução: Maven (`mvn javafx:run`)

**Entidades**: Consulta, Exame, Procedimento, BloqueioAgenda

### 3. Módulo de Estoque

**Backend**:
- Framework: Spring Boot 3.5.6
- Porta: 8083
- Banco de Dados: ImmuDB 1.5.0
- Cache: Redis 7
- API: REST (JSON)

**Frontend**:
- Framework: JavaFX 17
- Comunicação: Apache HttpClient 5.2.1
- Execução: Maven (`mvn javafx:run`)

**Entidades**: Medicamento, Alimento, Hospitalar, Fornecedor, Estoque, Pedido, Item

---

## 🔄 Diagramas de Processos de Negócio (BPM)

### Processo Completo de Atendimento

```
[Paciente chega ao hospital]
          ↓
[Recepcionista faz login] ← UC01
          ↓
[Busca/Cadastra Paciente] ← UC02
          ↓
[Seleciona Médico disponível] ← UC03
          ↓
[Agenda Consulta] ← UC06
          ↓
[Médico atende paciente]
          ↓
┌─────────┴─────────┐
│                   │
▼                   ▼
[Prescreve        [Solicita
 Medicamento]      Exame] ← UC07
↓                   ↓
[Farmácia verifica [Agenda Exame]
 estoque] ← UC16    ↓
↓                  [Realiza Exame]
[Disponível?]
│
├─ Sim → [Dispensa Medicamento]
│         ↓
│        [Fim]
│
└─ Não → [Cria Pedido] ← UC15
          ↓
         [Aguarda Entrega]
          ↓
         [Dispensa Medicamento]
          ↓
         [Fim]
```

---

## 📦 Padrões de Projeto Aplicados

### Padrões Comuns (Todos os Módulos)

#### 1. **MVC (Model-View-Controller)**
- **Localização**: Frontend
- **Model**: Classes de domínio
- **View**: Arquivos FXML
- **Controller**: Controllers JavaFX

#### 2. **Service Layer**
- **Localização**: Frontend e Backend
- **Propósito**: Isolamento de lógica de negócio

#### 3. **Repository Pattern**
- **Localização**: Backend
- **Frameworks**: Spring Data JPA, Spring Data MongoDB

#### 4. **DTO (Data Transfer Object)**
- **Localização**: Backend
- **Propósito**: Transferência de dados entre camadas

#### 5. **Template Method**
- **Localização**: Frontend
- **Classe**: `AbstractCrudController`

#### 6. **Singleton**
- **Localização**: Frontend
- **Classes**: `RefreshManager`, `AppConfig`

#### 7. **Observer**
- **Localização**: Frontend
- **Classe**: `RefreshManager`

#### 8. **Facade**
- **Localização**: Frontend
- **Classes**: Services HTTP

#### 9. **Dependency Injection**
- **Localização**: Backend
- **Framework**: Spring Framework

#### 10. **REST API**
- **Localização**: Backend
- **Framework**: Spring Web MVC

---

## 🚀 Como Executar o Sistema

### Pré-requisitos

- **Java 17** ou superior
- **Maven 3.9.x** ou superior
- **Docker** e **Docker Compose** (para bancos de dados)

### Opção 1: Execução Completa com Script

```bash
# Na raiz do projeto
chmod +x start-all.sh
./start-all.sh
```

Este script irá:
1. Iniciar todos os 3 backends (portas 8081, 8082, 8083)
2. Aguardar backends estarem prontos
3. Iniciar todos os 3 frontends JavaFX

### Opção 2: Execução Individual por Módulo

**Backend Cadastro:**
```bash
cd simplehealth-back/simplehealth-back-cadastro
docker-compose up -d
mvn spring-boot:run
```

**Frontend Cadastro:**
```bash
cd simplehealth-front/simplehealth-front-cadastro
mvn javafx:run
```

Repita o processo para **Agendamento** (porta 8082) e **Estoque** (porta 8083).

### Opção 3: Verificar Status

```bash
./status.sh
```

### Opção 4: Parar o Sistema

```bash
./stop-all.sh
```

---

## 🧪 Testes e Validações

### Validações Implementadas

✅ **Cadastro**:
- Validação de CPF (matemática)
- Validação de CRM (formato)
- Validação de email
- Validação de campos obrigatórios

✅ **Agendamento**:
- Validação de datas (não pode agendar no passado)
- Validação de conflitos de horário
- Validação de bloqueios de agenda
- Validação de disponibilidade de médico

✅ **Estoque**:
- Validação de CNPJ (matemática)
- Validação de quantidades (positivas)
- Validação de datas de validade
- Validação de estoque mínimo

### Testes de Integração

✅ **Comunicação REST**:
- Testes de criação (POST)
- Testes de listagem (GET)
- Testes de atualização (PUT)
- Testes de exclusão (DELETE)

---

## 📁 Estrutura Completa do Projeto

```
grupo4/
├── docs/
│   ├── documentos-finais-definitivos/
│   │   ├── 3.1. Documento de Visão do Projeto/
│   │   ├── 3.2_3.3_Casos de uso/
│   │   │   ├── 3.2. Diagrama global de Casos de Uso/
│   │   │   └── 3.3. Descrição detalhada de cada Caso de Uso/
│   │   ├── 3.4. Classes de Análise/
│   │   ├── 3.5. Diagramas de Processos de Negócio (BPM)/
│   │   │   ├── agendamento_completo.bpmn
│   │   │   ├── cadastro_consulta.bpmn
│   │   │   └── estoque_completo.bpmn
│   │   ├── 3.6. Arquitetura do Sistema - Lógica e Física/
│   │   ├── 3.7_3.9_3.10_Modelagens/
│   │   │   ├── 3.7. Modelagem de Classes de Projeto/
│   │   │   ├── 3.9. Modelagem de Interações/
│   │   │   └── 3.10. Modelagem de Estados/
│   │   └── 3.8. Documentação de boas práticas de uso de padrões do projeto/
│   └── relatorios/
│       ├── RELATORIO_CONFORMIDADE_AGENDAMENTO.md
│       ├── RELATORIO_CONFORMIDADE_CADASTRO.md
│       └── RELATORIO_CONFORMIDADE_ESTOQUE.md
│
├── simplehealth-back/
│   ├── simplehealth-back-cadastro/
│   │   ├── src/main/java/br/com/simplehealth/cadastro/
│   │   ├── pom.xml
│   │   └── docker-compose.yml
│   ├── simplehealth-back-agendamento/
│   │   ├── src/main/java/br/com/simplehealth/agendamento/
│   │   ├── pom.xml
│   │   └── docker-compose.yml
│   └── simplehealth-back-estoque/
│       ├── src/main/java/br/com/simplehealth/estoque/
│       ├── pom.xml
│       └── docker-compose.yml
│
├── simplehealth-front/
│   ├── README.md (Visão geral dos frontends)
│   ├── simplehealth-front-cadastro/
│   │   ├── src/main/java/br/com/simplehealth/cadastro/
│   │   ├── src/main/resources/view/
│   │   ├── pom.xml
│   │   └── README.md
│   ├── simplehealth-front-agendamento/
│   │   ├── src/main/java/br/com/simplehealth/agendamento/
│   │   ├── src/main/resources/view/
│   │   ├── pom.xml
│   │   └── README.md
│   └── simplehealth-front-estoque/
│       ├── src/main/java/br/com/simplehealth/estoque/
│       ├── src/main/resources/view/
│       ├── pom.xml
│       └── README.md
│
├── start-all.sh              # Script para iniciar todo o sistema
├── stop-all.sh               # Script para parar todo o sistema
├── status.sh                 # Script para verificar status
└── README.md                 # Este arquivo
```

---

## 🔌 Endpoints das APIs

### Backend Cadastro (http://localhost:8081/cadastro)
- `POST /auth/login` - Login
- `GET/POST/PUT/DELETE /pacientes` - CRUD Pacientes
- `GET/POST/PUT/DELETE /medicos` - CRUD Médicos
- `GET/POST/PUT/DELETE /usuarios` - CRUD Usuários
- `GET/POST/PUT/DELETE /convenios` - CRUD Convênios

### Backend Agendamento (http://localhost:8082/agendamento)
- `GET/POST/PUT/DELETE /consultas` - CRUD Consultas
- `GET/POST/PUT/DELETE /exames` - CRUD Exames
- `GET/POST/PUT/DELETE /procedimentos` - CRUD Procedimentos
- `POST/GET /bloqueios` - Bloqueios de Agenda

### Backend Estoque (http://localhost:8083/estoque)
- `GET/POST/PUT/DELETE /medicamentos` - CRUD Medicamentos
- `GET/POST/PUT/DELETE /alimentos` - CRUD Alimentos
- `GET/POST/PUT/DELETE /hospitalares` - CRUD Hospitalares
- `GET/POST/PUT/DELETE /fornecedores` - CRUD Fornecedores
- `GET/POST/PUT/DELETE /estoques` - CRUD Localizações
- `GET/POST/PUT/DELETE /pedidos` - CRUD Pedidos
- `GET /itens` - Visualização consolidada

---

## 🛠️ Stack Tecnológico

### Backend
| Tecnologia | Versão | Propósito |
|-----------|--------|-----------|
| Java | 17 | Linguagem de programação |
| Spring Boot | 3.5.6 | Framework backend |
| Spring Data JPA | 3.5.6 | Persistência (PostgreSQL) |
| Spring Data MongoDB | 3.5.6 | Persistência (MongoDB) |
| PostgreSQL | 15 | Banco relacional (Cadastro) |
| MongoDB | 6.0 | Banco NoSQL (Agendamento) |
| ImmuDB | 1.5.0 | Banco imutável (Cadastro/Estoque) |
| Redis | 7 | Cache |
| Maven | 3.9.x | Build tool |

### Frontend
| Tecnologia | Versão | Propósito |
|-----------|--------|-----------|
| Java | 17 | Linguagem de programação |
| JavaFX | 17.0.2 | Framework de UI |
| Apache HttpClient | 5.2.1 | Cliente REST |
| Jackson | 2.15.2 | Serialização JSON |
| SLF4J + Logback | 2.0.7 / 1.4.8 | Logging |
| Maven | 3.9.x | Build tool |

---

## 📊 Estatísticas do Projeto

### Métricas Gerais

| Métrica | Cadastro | Agendamento | Estoque | **Total** |
|---------|----------|-------------|---------|-----------|
| **Casos de Uso** | 5 | 4 | 7 | **16** |
| **Controllers (Frontend)** | 5 | 5 | 8 | **18** |
| **Services (Frontend)** | 4 | 4 | 7 | **15** |
| **Models (Frontend)** | 4 | 5 | 7 | **16** |
| **Controllers (Backend)** | 5 | 4 | 7 | **16** |
| **Services (Backend)** | 4 | 4 | 7 | **15** |
| **Repositories (Backend)** | 4 | 4 | 7 | **15** |
| **FXML Views** | 5 | 4 | 7 | **16** |
| **Endpoints REST** | ~20 | ~16 | ~28 | **~64** |

### Linhas de Código (Aproximado)

| Componente | LOC (Aprox.) |
|-----------|--------------|
| Backend Cadastro | ~3.000 |
| Backend Agendamento | ~2.500 |
| Backend Estoque | ~4.500 |
| Frontend Cadastro | ~2.000 |
| Frontend Agendamento | ~2.000 |
| Frontend Estoque | ~3.000 |
| **Total** | **~17.000** |

---

## 📝 Logs e Debugging

### Logs dos Backends

Configurados via `application.properties`:
```properties
logging.level.br.com.simplehealth=DEBUG
logging.level.org.springframework.web=INFO
```

### Logs dos Frontends

Configurados via `logback.xml`:
```xml
<logger name="br.com.simplehealth" level="DEBUG"/>
```

**Localização**:
- Cadastro: `/tmp/cadastro-frontend.log`
- Agendamento: `/tmp/agendamento-frontend.log`
- Estoque: `/tmp/estoque-frontend.log`

---

## 🤝 Integração entre Módulos

Os módulos se integram através de fluxos de negócio:

```
Fluxo 1: Cadastro → Agendamento
  Recepcionista cadastra paciente (Cadastro)
       ↓
  Recepcionista agenda consulta (Agendamento)

Fluxo 2: Agendamento → Estoque
  Médico prescreve medicamento (Agendamento)
       ↓
  Farmacêutico verifica estoque (Estoque)

Fluxo 3: Estoque → Cadastro
  Pedido necessita aprovação de fornecedor cadastrado (Cadastro)
       ↓
  Sistema cria pedido (Estoque)
```

---

## 📚 Documentação Detalhada

### Documentação de Projeto (docs/documentos-finais-definitivos/)

1. **3.1. Documento de Visão do Projeto** - Visão geral, objetivos e escopo
2. **3.2. Diagrama Global de Casos de Uso** - Visão geral dos 16 casos de uso
3. **3.3. Descrição Detalhada dos Casos de Uso** - Fluxos, pré-condições, pós-condições
4. **3.4. Classes de Análise** - Diagramas de classes conceituais
5. **3.5. Diagramas de Processos (BPM)** - Modelagem de processos de negócio
6. **3.6. Arquitetura Lógica e Física** - Diagramas de arquitetura
7. **3.7. Modelagem de Classes de Projeto** - Classes detalhadas
8. **3.8. Boas Práticas e Padrões** - Documentação de padrões aplicados
9. **3.9. Modelagem de Interações** - Diagramas de sequência
10. **3.10. Modelagem de Estados** - Diagramas de estados
11. **3.11. Implementação e Testes** - Cobertura de testes

### Documentação de Módulos

- **Frontend Geral**: `simplehealth-front/README.md`
- **Frontend Cadastro**: `simplehealth-front/simplehealth-front-cadastro/README.md`
- **Frontend Agendamento**: `simplehealth-front/simplehealth-front-agendamento/README.md`
- **Frontend Estoque**: `simplehealth-front/simplehealth-front-estoque/README.md`

### Relatórios de Conformidade

- `docs/relatorios/RELATORIO_CONFORMIDADE_CADASTRO.md`
- `docs/relatorios/RELATORIO_CONFORMIDADE_AGENDAMENTO.md`
- `docs/relatorios/RELATORIO_CONFORMIDADE_ESTOQUE.md`

---

## 📄 Licença

Este projeto faz parte do sistema SimpleHealth desenvolvido para fins acadêmicos.

**Instituição**: Universidade  
**Curso**: Engenharia de Software  
**Disciplina**: Projeto de Software  
**Ano**: 2025

---

## 👥 Equipe de Desenvolvimento

**Grupo 4 - Engenharia de Software 2025/2**

---

## 📞 Suporte e Troubleshooting

### Problemas Comuns

1. **Backend não inicia**:
   - Verifique se as portas 8081, 8082, 8083 estão disponíveis
   - Verifique se os bancos de dados estão rodando (Docker)
   - Confira os logs: `tail -f simplehealth-back/*/target/*.log`

2. **Frontend não conecta ao backend**:
   - Confirme que o backend está rodando: `curl http://localhost:8081/actuator/health`
   - Verifique a URL em `AppConfig.java`

3. **Banco de dados não conecta**:
   - Verifique containers Docker: `docker ps`
   - Reinicie containers: `cd simplehealth-back/* && docker-compose restart`

4. **Erro de compilação Maven**:
   - Limpe o cache: `mvn clean`
   - Atualize dependências: `mvn clean install`

### Comandos Úteis

```bash
# Verificar status de todos os backends
./status.sh

# Ver logs de um backend específico
cd simplehealth-back/simplehealth-back-cadastro
tail -f target/spring-boot.log

# Verificar containers Docker
docker ps | grep simplehealth

# Reiniciar um banco de dados
docker restart simplehealth-postgres
```

---

## 🎯 Roadmap Futuro

- [ ] Implementar autenticação JWT
- [ ] Adicionar testes unitários automatizados
- [ ] Implementar CI/CD com GitHub Actions
- [ ] Criar dashboard administrativo
- [ ] Adicionar notificações por email
- [ ] Implementar relatórios em PDF

---

**Última atualização**: 30 de novembro de 2025

**Status do Projeto**: ✅ Versão 1.0.0 - Completo e Funcional

---

## 🚀 Início Rápido

### Pré-requisitos
- Docker e Docker Compose instalados
- Sistema Linux (recomendado Ubuntu/Debian)
- X11 configurado para interfaces gráficas (já vem na maioria das distribuições Linux)

### Iniciar Todo o Sistema

```bash
# Dar permissão de execução aos scripts (apenas uma vez)
chmod +x start-all.sh stop-all.sh status.sh

# Iniciar todos os módulos (backends + frontends)
./start-all.sh
```

O script `start-all.sh` irá:
1. ✅ Verificar se Docker está rodando
2. 🧹 Opcionalmente limpar containers antigos
3. 🔧 Inicializar os 3 backends (Agendamento, Cadastro, Estoque)
4. ⏳ Aguardar cada backend estar pronto
5. 🖥️ Inicializar os 3 frontends JavaFX
6. 📊 Exibir resumo com URLs e portas

### Parar Todo o Sistema

```bash
./stop-all.sh
```

### Verificar Status

```bash
./status.sh
```

---

## 📦 Arquitetura do Sistema

### Módulos Backend

| Módulo | Porta | Banco de Dados | URL |
|--------|-------|----------------|-----|
| **Agendamento** | 8082 | MongoDB (27017) | http://localhost:8082/agendamento |
| **Cadastro** | 8081 | PostgreSQL (5430) + Redis (6379) | http://localhost:8081/cadastro |
| **Estoque** | 8083 | ImmuDB (3322) + Redis | http://localhost:8083/estoque |

### Módulos Frontend

Todos os frontends são aplicações JavaFX 17 executadas em containers Docker com suporte X11 para interface gráfica.

| Módulo | Tecnologia | Conecta ao Backend |
|--------|------------|-------------------|
| **Agendamento** | JavaFX 17 | localhost:8082 |
| **Cadastro** | JavaFX 17 | localhost:8081 |
| **Estoque** | JavaFX 17 | localhost:8083 |

---

## 🛠️ Uso Individual dos Módulos

### Backend Agendamento
```bash
cd simplehealth-back/simplehealth-back-agendamento
docker-compose up -d
```

### Backend Cadastro
```bash
cd simplehealth-back/simplehealth-back-cadastro
docker-compose up -d
```

### Backend Estoque
```bash
cd simplehealth-back/simplehealth-back-estoque
docker-compose up -d
```

### Frontend Agendamento
```bash
cd simplehealth-front/simplehealth-front-agendamento
docker-compose up -d
```

### Frontend Cadastro
```bash
cd simplehealth-front/simplehealth-front-cadastro
docker-compose up -d
```

### Frontend Estoque
```bash
cd simplehealth-front/simplehealth-front-estoque
docker-compose up -d
```

---

## 📊 Comandos Úteis

### Ver Logs

```bash
# Logs de todos os containers
docker-compose logs -f

# Logs de um módulo específico (dentro da pasta do módulo)
cd simplehealth-back/simplehealth-back-agendamento
docker-compose logs -f

# Logs de um serviço específico
docker logs -f simplehealth-back-estoque
```

### Verificar Containers Rodando

```bash
# Todos os containers
docker ps

# Apenas containers do SimpleHealth
docker ps | grep simplehealth
```

### Reiniciar um Módulo

```bash
cd simplehealth-back/simplehealth-back-cadastro
docker-compose restart
```

### Reconstruir Imagens

```bash
# Dentro da pasta do módulo
docker-compose up -d --build
```

### Acessar Shell do Container

```bash
docker exec -it simplehealth-back-estoque bash
```

---

## 🔧 Troubleshooting

### Erro: "Cannot connect to Docker daemon"
```bash
# Inicie o Docker
sudo systemctl start docker
```

### Interface JavaFX não abre
```bash
# Configure X11 para aceitar conexões do Docker
xhost +local:docker

# Verifique se DISPLAY está configurado
echo $DISPLAY
```

### Porta já em uso
```bash
# Identifique o processo usando a porta
sudo lsof -i :8081

# Pare o processo ou mude a porta no docker-compose.yml
```

### Backend não conecta ao banco
```bash
# Verifique se o banco está rodando
./status.sh

# Reinicie o container do banco
cd simplehealth-back/simplehealth-back-cadastro
docker-compose restart postgres
```

### Erro de permissão nos scripts
```bash
chmod +x start-all.sh stop-all.sh status.sh
```

---

## 📁 Estrutura do Projeto

```
grupo4/
├── start-all.sh              # 🚀 Inicia todos os módulos
├── stop-all.sh               # 🛑 Para todos os módulos
├── status.sh                 # 📊 Verifica status do sistema
├── README.md                 # 📖 Este arquivo
│
├── simplehealth-back/        # Backends
│   ├── simplehealth-back-agendamento/
│   │   ├── docker-compose.yml
│   │   ├── Dockerfile
│   │   └── src/
│   ├── simplehealth-back-cadastro/
│   │   ├── docker-compose.yml
│   │   ├── Dockerfile
│   │   └── src/
│   └── simplehealth-back-estoque/
│       ├── docker-compose.yml
│       ├── Dockerfile
│       └── src/
│
└── simplehealth-front/       # Frontends
    ├── simplehealth-front-agendamento/
    │   ├── docker-compose.yml
    │   ├── Dockerfile
    │   └── src/
    ├── simplehealth-front-cadastro/
    │   ├── docker-compose.yml
    │   ├── Dockerfile
    │   └── src/
    └── simplehealth-front-estoque/
        ├── docker-compose.yml
        ├── Dockerfile
        └── src/
```

---

## 🎯 Fluxo de Desenvolvimento

### 1. Desenvolver
```bash
# Edite o código fonte em src/
vim simplehealth-back/simplehealth-back-estoque/src/main/java/...
```

### 2. Reconstruir
```bash
cd simplehealth-back/simplehealth-back-estoque
docker-compose up -d --build
```

### 3. Testar
```bash
# Via frontend JavaFX ou via API
curl http://localhost:8083/estoque/medicamentos
```

### 4. Ver Logs
```bash
docker-compose logs -f
```

---

## 🌐 URLs e Endpoints

### APIs REST Backend

**Agendamento (MongoDB):**
- Base URL: `http://localhost:8082/agendamento`
- Swagger: `http://localhost:8082/swagger-ui.html`

**Cadastro (PostgreSQL + Redis):**
- Base URL: `http://localhost:8081/cadastro`
- Swagger: `http://localhost:8081/swagger-ui.html`
- Endpoints: `/pacientes`, `/medicos`, `/funcionarios`, `/usuarios`

**Estoque (ImmuDB + Redis):**
- Base URL: `http://localhost:8083/estoque`
- Swagger: `http://localhost:8083/swagger-ui.html`
- Endpoints: `/medicamentos`, `/alimentos`, `/hospitalares`, `/fornecedores`, `/estoques`, `/pedidos`, `/itens`

---

## 📚 Documentação Adicional

- **Módulo Agendamento:** `simplehealth-front/simplehealth-front-agendamento/README.md`
- **Módulo Cadastro:** `simplehealth-front/simplehealth-front-cadastro/README.md`
- **Módulo Estoque:** `simplehealth-front/simplehealth-front-estoque/README.md`
- **Relatórios de Conformidade:** `docs/relatorios/`
- **Documentação Técnica:** `docs/documentos-finais-definitivos/`

---

## 👥 Equipe

Projeto desenvolvido para a disciplina de Projeto de Software - ES 2025/2

---

## 📝 Licença

Este projeto é acadêmico e destina-se apenas para fins educacionais.

---

## 🆘 Suporte

Para problemas ou dúvidas:
1. Verifique o status: `./status.sh`
2. Consulte os logs: `docker-compose logs -f`
3. Consulte a seção Troubleshooting acima
4. Consulte a documentação específica de cada módulo

---

**Última atualização:** 30/11/2025
