# SimpleHealth - Módulo de Agendamento (Frontend)

## 📋 Visão Geral

O **Módulo de Agendamento** é uma aplicação JavaFX responsável pela gestão de agendamentos médicos do sistema SimpleHealth. Este módulo permite o gerenciamento de consultas, exames, procedimentos e bloqueios de agenda.

**Versão**: 1.0.0  
**Framework**: JavaFX 17  
**Build Tool**: Maven 3.9.x  
**Java Version**: 17

---

## 🎯 Casos de Uso Implementados

### UC06 - Agendar Consulta
**Descrição**: Permite agendar uma nova consulta médica  
**Atores**: Recepcionista, Médico  
**Fluxo Principal**:
1. Usuário seleciona "Consultas"
2. Preenche dados da consulta (paciente, médico, data/hora)
3. Sistema valida disponibilidade
4. Confirma agendamento

### UC07 - Agendar Exame
**Descrição**: Permite agendar exames médicos  
**Atores**: Recepcionista, Médico  
**Fluxo Principal**:
1. Usuário seleciona "Exames"
2. Preenche dados do exame (paciente, tipo, data/hora)
3. Sistema valida disponibilidade
4. Confirma agendamento

### UC08 - Agendar Procedimento
**Descrição**: Permite agendar procedimentos médicos  
**Atores**: Recepcionista, Médico  
**Fluxo Principal**:
1. Usuário seleciona "Procedimentos"
2. Preenche dados do procedimento (paciente, tipo, data/hora)
3. Sistema valida disponibilidade
4. Confirma agendamento

### UC09 - Gerenciar Bloqueios de Agenda
**Descrição**: Permite bloquear horários na agenda médica  
**Atores**: Recepcionista, Médico  
**Fluxo Principal**:
1. Usuário seleciona "Bloqueios de Agenda"
2. Define período de bloqueio
3. Informa motivo
4. Sistema registra bloqueio

---

## 🏗️ Arquitetura do Sistema

### Arquitetura Lógica

```
┌─────────────────────────────────────────────────────────────┐
│                    CAMADA DE APRESENTAÇÃO                    │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │ Consulta │  │  Exame   │  │Procedimen│  │ Bloqueio │   │
│  │Controller│  │Controller│  │Controller│  │Controller│   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    CAMADA DE NEGÓCIO                         │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │Consulta  │  │  Exame   │  │Procedimen│  │ Bloqueio │   │
│  │ Service  │  │ Service  │  │ Service  │  │ Service  │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼ HTTP/REST
┌─────────────────────────────────────────────────────────────┐
│                BACKEND - Spring Boot (Porta 8082)            │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  API REST (/agendamento/consultas, /exames, etc)    │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Arquitetura Física

```
┌─────────────────────────┐
│   Frontend JavaFX       │
│   (Este Módulo)         │
│   Porta: N/A            │
└───────────┬─────────────┘
            │ HTTP
            ▼
┌─────────────────────────┐
│   Backend Spring Boot   │
│   Porta: 8082           │
└───────────┬─────────────┘
            │
    ┌───────┴───────┐
    ▼               ▼
┌─────────┐   ┌─────────┐
│ MongoDB │   │  Redis  │
│ :27017  │   │ :6379   │
└─────────┘   └─────────┘
```

---

## 📦 Modelagem de Classes

### Classes de Domínio

#### Consulta
```java
- idAgendamento: Long
- idPaciente: Long
- idMedico: Long
- dataHora: LocalDateTime
- status: String
- observacoes: String
```

#### Exame
```java
- idAgendamento: Long
- idPaciente: Long
- idMedico: Long
- tipoExame: String
- dataHora: LocalDateTime
- status: String
- observacoes: String
```

#### Procedimento
```java
- idAgendamento: Long
- idPaciente: Long
- idMedico: Long
- tipoProcedimento: String
- dataHora: LocalDateTime
- status: String
- observacoes: String
```

#### BloqueioAgenda
```java
- id: Long
- dataInicio: LocalDateTime
- dataFim: LocalDateTime
- motivo: String
- idMedico: Long
```

### Padrões de Projeto Aplicados

#### 1. **MVC (Model-View-Controller)**
- **Model**: Classes de domínio (Consulta, Exame, Procedimento, BloqueioAgenda)
- **View**: Arquivos FXML (consulta.fxml, exame.fxml, procedimento.fxml, bloqueio.fxml)
- **Controller**: Classes Controller (ConsultaController, ExameController, etc.)

#### 2. **Service Layer**
- Isolamento da lógica de comunicação HTTP
- Classes: ConsultaService, ExameService, ProcedimentoService, BloqueioAgendaService

#### 3. **Template Method**
- `AbstractCrudController`: Define template para operações CRUD
- Métodos abstratos implementados pelas subclasses

#### 4. **Singleton**
- `RefreshManager`: Gerencia atualização de dados entre controllers
- `AppConfig`: Configurações da aplicação

#### 5. **Observer**
- `RefreshManager`: Notifica controllers sobre mudanças nos dados

---

## 🔄 Diagramas de Processo (BPM)

### Processo de Agendamento de Consulta

```
[Início] → [Selecionar Paciente] → [Selecionar Médico] → [Escolher Data/Hora]
    ↓
[Validar Disponibilidade]
    ↓
[Disponível?] --Não--> [Mostrar Erro] → [Fim]
    ↓ Sim
[Criar Agendamento]
    ↓
[Confirmar] → [Fim]
```

---

## 🚀 Como Executar

### Pré-requisitos

- **Java 17** ou superior
- **Maven 3.9.x** ou superior
- **Backend do Agendamento** rodando na porta 8082

### Opção 1: Via Maven (Recomendado)

```bash
cd simplehealth-front/simplehealth-front-agendamento
mvn javafx:run
```

### Opção 2: Via Script

**Linux/Mac:**
```bash
cd simplehealth-front/simplehealth-front-agendamento
./run.sh
```

**Windows:**
```cmd
cd simplehealth-front\simplehealth-front-agendamento
run.bat
```

### Opção 3: Via Sistema Completo

```bash
# Na raiz do projeto
./start-all.sh
```

---

## 🧪 Testes Implementados

### Validações de Interface

✅ **Validação de Campos Obrigatórios**
- Paciente deve ser selecionado
- Médico deve ser selecionado
- Data/Hora deve ser preenchida

✅ **Validação de Data**
- Data não pode ser anterior à atual
- Horário deve estar em horário comercial (8h-18h)

✅ **Validação de Disponibilidade**
- Verifica conflitos de horário
- Valida bloqueios de agenda

### Testes de Integração

✅ **Comunicação com Backend**
- Teste de criação de agendamento
- Teste de listagem de agendamentos
- Teste de atualização de agendamento
- Teste de cancelamento

---

## 📁 Estrutura do Projeto

```
simplehealth-front-agendamento/
├── src/
│   ├── main/
│   │   ├── java/br/com/simplehealth/agendamento/
│   │   │   ├── client/
│   │   │   │   └── MainApp.java          # Classe principal
│   │   │   ├── config/
│   │   │   │   └── AppConfig.java         # Configurações
│   │   │   ├── controller/
│   │   │   │   ├── AbstractCrudController.java
│   │   │   │   ├── ConsultaController.java
│   │   │   │   ├── ExameController.java
│   │   │   │   ├── ProcedimentoController.java
│   │   │   │   └── BloqueioAgendaController.java
│   │   │   ├── model/
│   │   │   │   ├── Consulta.java
│   │   │   │   ├── Exame.java
│   │   │   │   ├── Procedimento.java
│   │   │   │   └── BloqueioAgenda.java
│   │   │   ├── service/
│   │   │   │   ├── ConsultaService.java
│   │   │   │   ├── ExameService.java
│   │   │   │   ├── ProcedimentoService.java
│   │   │   │   └── BloqueioAgendaService.java
│   │   │   └── util/
│   │   │       ├── RefreshManager.java
│   │   │       └── ValidationUtils.java
│   │   └── resources/
│   │       ├── view/
│   │       │   ├── consulta.fxml
│   │       │   ├── exame.fxml
│   │       │   ├── procedimento.fxml
│   │       │   └── bloqueio.fxml
│   │       └── logback.xml
├── pom.xml
└── README.md
```

---

## 🔌 Endpoints da API (Backend)

### Consultas
- `GET /agendamento/consultas` - Listar todas as consultas
- `GET /agendamento/consultas/{id}` - Buscar consulta por ID
- `POST /agendamento/consultas` - Criar nova consulta
- `PUT /agendamento/consultas/{id}` - Atualizar consulta
- `DELETE /agendamento/consultas/{id}` - Cancelar consulta

### Exames
- `GET /agendamento/exames` - Listar todos os exames
- `GET /agendamento/exames/{id}` - Buscar exame por ID
- `POST /agendamento/exames` - Criar novo exame
- `PUT /agendamento/exames/{id}` - Atualizar exame
- `DELETE /agendamento/exames/{id}` - Cancelar exame

### Procedimentos
- `GET /agendamento/procedimentos` - Listar todos os procedimentos
- `GET /agendamento/procedimentos/{id}` - Buscar procedimento por ID
- `POST /agendamento/procedimentos` - Criar novo procedimento
- `PUT /agendamento/procedimentos/{id}` - Atualizar procedimento
- `DELETE /agendamento/procedimentos/{id}` - Cancelar procedimento

### Bloqueios
- `POST /agendamento/bloqueios` - Criar bloqueio de agenda
- `GET /agendamento/bloqueios` - Listar bloqueios

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Propósito |
|-----------|--------|-----------|
| JavaFX | 17.0.2 | Framework de interface gráfica |
| Apache HttpClient | 5.2.1 | Comunicação HTTP com backend |
| Jackson | 2.15.2 | Serialização/Deserialização JSON |
| SLF4J + Logback | 2.0.7 / 1.4.8 | Logging |
| Maven | 3.9.x | Gerenciamento de dependências |

---

## 📊 Modelagem de Estados

### Estados de Agendamento

```
[Criado] → [Confirmado] → [Realizado]
    ↓           ↓
[Cancelado] ←───┘
```

**Transições:**
- `Criado → Confirmado`: Quando confirmado pela recepção
- `Confirmado → Realizado`: Quando consulta/exame/procedimento é realizado
- `Criado → Cancelado`: Cancelamento antes da confirmação
- `Confirmado → Cancelado`: Cancelamento após confirmação

---

## � Funcionalidades Principais

### 1. Gestão de Consultas
- ✅ Listar consultas agendadas
- ✅ Criar nova consulta
- ✅ Editar consulta existente
- ✅ Cancelar consulta
- ✅ Buscar consulta por paciente/médico/data

### 2. Gestão de Exames
- ✅ Listar exames agendados
- ✅ Criar novo exame
- ✅ Editar exame existente
- ✅ Cancelar exame
- ✅ Filtrar por tipo de exame

### 3. Gestão de Procedimentos
- ✅ Listar procedimentos agendados
- ✅ Criar novo procedimento
- ✅ Editar procedimento existente
- ✅ Cancelar procedimento

### 4. Bloqueios de Agenda
- ✅ Criar bloqueio de período
- ✅ Listar bloqueios ativos
- ✅ Visualizar motivo do bloqueio

---

## 📝 Logs e Debugging

Os logs são armazenados em:
- **Console**: Nível DEBUG durante desenvolvimento
- **Arquivo**: `/tmp/agendamento-frontend.log` (quando executado via script)

Para ativar logs detalhados, edite `src/main/resources/logback.xml`:
```xml
<logger name="br.com.simplehealth.agendamento" level="DEBUG"/>
```

---

## 🤝 Integração com Outros Módulos

Este módulo se integra com:

1. **Backend de Agendamento** (porta 8082)
   - Comunicação via HTTP REST
   - Formato de dados: JSON

2. **Módulo de Cadastro** (indireto)
   - Dados de pacientes e médicos vêm do backend
   - Sincronização via API

---

## 📄 Licença

Este projeto faz parte do sistema SimpleHealth desenvolvido para fins acadêmicos.

---

## 👥 Equipe de Desenvolvimento

**Grupo 4 - Engenharia de Software 2025**

---

## 📞 Suporte

Para questões técnicas ou problemas:
1. Verifique os logs em `/tmp/agendamento-frontend.log`
2. Confirme se o backend está rodando na porta 8082
3. Consulte a documentação técnica em `docs/documentos-finais-definitivos/`

---

**Última atualização**: 30 de novembro de 2025

```bash
# Linux/Mac
chmod +x start.sh
./start.sh

# Windows
start.bat
```

### Opção 3: Docker Compose (Stack Completa)

```bash
# Inicia todo o stack (frontend, backend, MongoDB, Redis)
docker-compose up -d

# Para visualizar logs
docker-compose logs -f

# Para parar
docker-compose down
```

### Opção 4: Build Manual

```bash
# Compilar
mvn clean package

# Executar
mvn javafx:run
```

## 📡 Configuração da API

O frontend está configurado para conectar ao backend em:
- **URL Base**: `http://localhost:8082/agendamento`

Para alterar a URL, edite o arquivo:
```
src/main/java/br/com/simplehealth/agendamento/config/AppConfig.java
```

## 🗂️ Estrutura do Projeto

```
simplehealth-front-agendamento/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/simplehealth/agendamento/
│   │   │       ├── client/          # Classe principal
│   │   │       ├── config/          # Configurações
│   │   │       ├── controller/      # Controllers JavaFX
│   │   │       ├── model/           # Modelos de dados
│   │   │       ├── service/         # Serviços de API
│   │   │       └── util/            # Utilitários
│   │   └── resources/
│   │       ├── view/                # Arquivos FXML
│   │       └── logback.xml          # Configuração de logs
├── pom.xml                          # Configuração Maven
├── Dockerfile                       # Container Docker
├── docker-compose.yml               # Orquestração Docker
├── run.sh / run.bat                 # Scripts de execução
└── README.md                        # Este arquivo
```

## 🎯 Endpoints da API Utilizados

### Agendamentos (Consultas, Exames, Procedimentos)
- **POST** `/agendamentos` - Criar agendamento
- **GET** `/agendamentos/{id}` - Buscar por ID
- **PUT** `/agendamentos/{id}` - Atualizar agendamento
- **DELETE** `/agendamentos/{id}` - Deletar agendamento
- **POST** `/agendamentos/cancelar` - Cancelar agendamento

### Bloqueios de Agenda
- **POST** `/bloqueio-agenda` - Criar bloqueio

## 🎨 Interface

A aplicação possui 4 abas principais:

1. **Consultas** (Azul): Gerenciamento de consultas médicas
2. **Exames** (Verde): Gerenciamento de exames
3. **Procedimentos** (Laranja): Gerenciamento de procedimentos
4. **Bloqueios** (Roxo): Gerenciamento de bloqueios de agenda

Cada aba possui:
- Tabela de listagem com os registros
- **Campo de busca avançada** com múltiplos critérios
- Formulário de cadastro/edição
- Botões de ação (Novo, Salvar, Cancelar, Excluir, Buscar)

## 🔍 Busca Avançada

A aplicação possui busca inteligente multi-campo:

### Consultas
- CPF do paciente (formatado ou não)
- CRM do médico
- Especialidade médica
- Convênio
- Tipo de consulta
- Status
- ID

### Exames
- CPF do paciente (formatado ou não)
- CRM do médico
- Nome do exame
- Status
- ID

### Procedimentos
- CPF do paciente (formatado ou não)
- CRM do médico
- Descrição do procedimento
- Convênio
- Modalidade
- Status
- ID

**Características:**
- ✅ Busca case-insensitive
- ✅ Aceita CPF com ou sem formatação
- ✅ Busca parcial (não precisa digitar completo)
- ✅ Limpa filtros automaticamente com campo vazio

Para mais detalhes, veja: [BUSCA_AVANCADA.md](BUSCA_AVANCADA.md)

## 📝 Validações

A aplicação implementa validações rigorosas através da classe `ValidationUtils`:

- **CPF**: Validação matemática completa dos dígitos verificadores
- **CRM**: Validação de formato (4-7 dígitos numéricos)
- **Data/Hora**: Formato yyyy-MM-dd HH:mm com validação de valores válidos
- **Período**: Validação que data/hora início é anterior ao fim
- **Campos Obrigatórios**: Marcados com asterisco (*) e validados
- **Confirmações**: Diálogos de confirmação detalhados antes de exclusões

Para mais detalhes, veja: [VALIDACOES_IMPLEMENTADAS.md](VALIDACOES_IMPLEMENTADAS.md)

## 🔍 Logs

Os logs da aplicação são exibidos no console e incluem:
- Operações de CRUD
- Chamadas à API
- Erros e exceções

Configuração de logs em: `src/main/resources/logback.xml`

## 🐛 Troubleshooting

### Backend não acessível
```
Verifique se o backend está rodando:
curl http://localhost:8082/agendamento/actuator/health
```

### Erro de conexão
- Verifique se MongoDB está rodando na porta 27017
- Verifique se Redis está rodando na porta 6379

### Erro ao executar com Docker
```bash
# Permitir acesso ao X11 (Linux)
xhost +local:docker
```

## 👥 Autores

Equipe SimpleHealth - Grupo 4

## 📄 Licença

Este projeto é parte do trabalho acadêmico da disciplina de Projeto de Software.

## �📞 Suporte

Para dúvidas ou problemas, entre em contato com a equipe de desenvolvimento.
