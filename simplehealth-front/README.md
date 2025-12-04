# SimpleHealth - Frontend (JavaFX)

## 📋 Visão Geral

O **Frontend do SimpleHealth** é um conjunto de aplicações JavaFX que compõem a interface gráfica do sistema de gestão hospitalar. O sistema é dividido em **3 módulos independentes**, cada um responsável por uma área específica de gestão hospitalar: **Cadastro**, **Agendamento** e **Estoque**.

**Versão**: 1.0.0  
**Framework**: JavaFX 17  
**Build Tool**: Maven 3.9.x  
**Java Version**: 17  
**Arquitetura**: Multi-módulo independente

---

## 🎯 Casos de Uso Implementados

### Módulo Cadastro (UC01-UC05)
- **UC01**: Autenticação de Usuário (Login)
- **UC02**: Cadastrar Paciente
- **UC03**: Cadastrar Médico
- **UC04**: Cadastrar Usuário do Sistema
- **UC05**: Cadastrar Convênio Médico

### Módulo Agendamento (UC06-UC09)
- **UC06**: Agendar Consulta
- **UC07**: Agendar Exame
- **UC08**: Agendar Procedimento
- **UC09**: Gerenciar Bloqueios de Agenda

### Módulo Estoque (UC10-UC16)
- **UC10**: Cadastrar Medicamento
- **UC11**: Cadastrar Alimento
- **UC12**: Cadastrar Material Hospitalar
- **UC13**: Cadastrar Fornecedor
- **UC14**: Gerenciar Localizações de Estoque
- **UC15**: Gerenciar Pedidos
- **UC16**: Visualizar Itens Consolidados

**Total**: 16 Casos de Uso implementados

---

## 🏗️ Arquitetura do Sistema

### Arquitetura Lógica Geral

```
┌────────────────────────────────────────────────────────────────────────┐
│                         CAMADA DE APRESENTAÇÃO                          │
│                            (JavaFX Frontend)                            │
├─────────────────────┬─────────────────────┬────────────────────────────┤
│   Módulo Cadastro   │  Módulo Agendamento │    Módulo Estoque         │
│                     │                     │                            │
│ • LoginController   │ • ConsultaController│ • MedicamentoController   │
│ • PacienteController│ • ExameController   │ • AlimentoController      │
│ • MedicoController  │ • ProcedimentoCtrler│ • HospitalarController    │
│ • UsuarioController │ • BloqueioController│ • FornecedorController    │
│ • ConvenioController│                     │ • EstoqueController       │
│                     │                     │ • PedidoController        │
│                     │                     │ • ItemController          │
└─────────────────────┴─────────────────────┴────────────────────────────┘
                                  │
                                  ▼
┌────────────────────────────────────────────────────────────────────────┐
│                         CAMADA DE NEGÓCIO                               │
│                           (Service Layer)                               │
├─────────────────────┬─────────────────────┬────────────────────────────┤
│  Cadastro Services  │ Agendamento Services│    Estoque Services        │
│                     │                     │                            │
│ • PacienteService   │ • ConsultaService   │ • MedicamentoService      │
│ • MedicoService     │ • ExameService      │ • AlimentoService         │
│ • UsuarioService    │ • ProcedimentoSvc   │ • HospitalarService       │
│ • ConvenioService   │ • BloqueioService   │ • FornecedorService       │
│                     │                     │ • EstoqueService          │
│                     │                     │ • PedidoService           │
│                     │                     │ • ItemService             │
└─────────────────────┴─────────────────────┴────────────────────────────┘
                                  │
                                  ▼ HTTP/REST
┌────────────────────────────────────────────────────────────────────────┐
│                    BACKENDS - Spring Boot                               │
├─────────────────────┬─────────────────────┬────────────────────────────┤
│   Backend Cadastro  │ Backend Agendamento │    Backend Estoque         │
│   Porta: 8081       │   Porta: 8082       │    Porta: 8083            │
│                     │                     │                            │
│PostgreSQL+Cassandra│  MongoDB + Redis    │ Cassandra + Redis         │
│   :5432    :3322    │  :27017   :6379     │   :3322    :6379          │
└─────────────────────┴─────────────────────┴────────────────────────────┘
```

### Arquitetura Física

```
┌─────────────────────────────────────────────────────────────┐
│                    CAMADA DE APRESENTAÇÃO                    │
│                                                              │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐           │
│  │  Frontend  │  │  Frontend  │  │  Frontend  │           │
│  │  Cadastro  │  │ Agendamento│  │  Estoque   │           │
│  │            │  │            │  │            │           │
│  │  JavaFX    │  │  JavaFX    │  │  JavaFX    │           │
│  └─────┬──────┘  └─────┬──────┘  └─────┬──────┘           │
└────────┼───────────────┼───────────────┼───────────────────┘
         │ HTTP          │ HTTP          │ HTTP
         ▼               ▼               ▼
┌────────────────────────────────────────────────────────────┐
│                    CAMADA DE BACKEND                        │
│                                                             │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐            │
│  │ Backend  │    │ Backend  │    │ Backend  │            │
│  │ Cadastro │    │Agendamento│   │ Estoque  │            │
│  │  :8081   │    │  :8082   │    │  :8083   │            │
│  └────┬─────┘    └────┬─────┘    └────┬─────┘            │
└───────┼───────────────┼───────────────┼────────────────────┘
        │               │               │
   ┌────┴────┐     ┌────┴────┐     ┌────┴────┐
   ▼         ▼     ▼         ▼     ▼         ▼
┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐
│PostgreSQL Cassandra│ MongoDB│ Redis │Cassandra│ Redis │
│:5432 │ │:3322 │ │:27017│ │:6379 │ │:3322 │ │:6379 │
└──────┘ └──────┘ └──────┘ └──────┘ └──────┘ └──────┘
```

---

## 📦 Estrutura de Módulos

### 1. Módulo de Cadastro
**Localização**: `simplehealth-front-cadastro/`  
**Backend**: `http://localhost:8081/cadastro`  
**Banco de Dados**: PostgreSQL + Cassandra  
**CRUDs**: 4 entidades

**Funcionalidades**:
- Login e autenticação
- Gestão de pacientes
- Gestão de médicos
- Gestão de usuários do sistema
- Gestão de convênios

### 2. Módulo de Agendamento
**Localização**: `simplehealth-front-agendamento/`  
**Backend**: `http://localhost:8082/agendamento`  
**Banco de Dados**: MongoDB + Redis  
**CRUDs**: 4 entidades

**Funcionalidades**:
- Agendamento de consultas
- Agendamento de exames
- Agendamento de procedimentos
- Bloqueios de agenda médica

### 3. Módulo de Estoque
**Localização**: `simplehealth-front-estoque/`  
**Backend**: `http://localhost:8083/estoque`  
**Banco de Dados**: Cassandra + Redis  
**CRUDs**: 7 entidades

**Funcionalidades**:
- Gestão de medicamentos
- Gestão de alimentos
- Gestão de materiais hospitalares
- Gestão de fornecedores
- Controle de localizações no estoque
- Gestão de pedidos
- Visualização consolidada de itens

---

## 📦 Modelagem de Classes

### Padrões de Projeto Aplicados (Todos os Módulos)

#### 1. **MVC (Model-View-Controller)**
- **Model**: Classes de domínio específicas de cada módulo
- **View**: Arquivos FXML para interface gráfica
- **Controller**: Controllers JavaFX para lógica de apresentação

#### 2. **Service Layer**
- Isolamento da lógica de comunicação HTTP
- Um service para cada entidade de domínio

#### 3. **Template Method**
- `AbstractCrudController`: Define template para operações CRUD
- Implementado em todos os 3 módulos

#### 4. **Singleton**
- `RefreshManager`: Gerencia atualização de dados entre controllers
- `AppConfig`: Configurações da aplicação

#### 5. **Observer**
- `RefreshManager`: Notifica controllers sobre mudanças

#### 6. **Facade**
- Services fazem fachada para comunicação REST

#### 7. **Strategy** (Módulo Estoque)
- Estratégias de armazenamento de alimentos

---

## 🔄 Diagramas de Processo (BPM)

### Processo Global de Atendimento

```
[Paciente chega] → [Login Recepcionista] → [Cadastro/Busca Paciente]
                                                      ↓
                                            [Agendar Consulta]
                                                      ↓
                                            [Médico atende]
                                                      ↓
                                    [Prescreve Medicamentos/Exames]
                                                      ↓
                                            [Verifica Estoque]
                                                      ↓
                            [Disponível?] --Não--> [Criar Pedido]
                                  ↓ Sim
                            [Dispensar Medicamento] → [Fim]
```

---

## 🚀 Como Executar

### Pré-requisitos Globais

- **Java 17** ou superior
- **Maven 3.9.x** ou superior
- **Backends rodando** nas portas 8081, 8082 e 8083

### Opção 1: Executar Módulo Individual

**Cadastro:**
```bash
cd simplehealth-front-cadastro
mvn javafx:run
```

**Agendamento:**
```bash
cd simplehealth-front-agendamento
mvn javafx:run
```

**Estoque:**
```bash
cd simplehealth-front-estoque
mvn javafx:run
```

### Opção 2: Via Scripts (Linux/Mac)

```bash
# Cadastro
cd simplehealth-front-cadastro && ./run.sh

# Agendamento
cd simplehealth-front-agendamento && ./run.sh

# Estoque
cd simplehealth-front-estoque && ./run.sh
```

### Opção 3: Sistema Completo

```bash
# Na raiz do projeto
cd ..
./start-all.sh
```

Este script iniciará:
1. Todos os 3 backends (portas 8081, 8082, 8083)
2. Todos os bancos de dados via Docker
3. Todos os 3 frontends JavaFX

---

## 🧪 Testes Implementados

### Validações Comuns (Todos os Módulos)

✅ **Validação de Campos Obrigatórios**
- Campos marcados com asterisco (*)
- Mensagens de erro claras

✅ **Validação de Formatos**
- CPF (cadastro)
- CRM (cadastro)
- CNPJ (estoque)
- Email
- Datas

✅ **Validação de Regras de Negócio**
- Disponibilidade de agenda (agendamento)
- Estoque suficiente (estoque)
- Dados duplicados

### Testes de Integração

✅ **Comunicação com Backend**
- Autenticação
- CRUD completo (Create, Read, Update, Delete)
- Tratamento de erros HTTP

---

## 📁 Estrutura do Projeto

```
simplehealth-front/
├── simplehealth-front-cadastro/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/br/com/simplehealth/cadastro/
│   │   │   │   ├── client/          # MainApp
│   │   │   │   ├── config/          # AppConfig
│   │   │   │   ├── controller/      # 5 Controllers
│   │   │   │   ├── model/           # 4 Models
│   │   │   │   ├── service/         # 4 Services
│   │   │   │   └── util/            # RefreshManager, ValidationUtils
│   │   │   └── resources/view/      # 5 FXML files
│   │   └── test/
│   ├── pom.xml
│   └── README.md
│
├── simplehealth-front-agendamento/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/br/com/simplehealth/agendamento/
│   │   │   │   ├── client/          # MainApp
│   │   │   │   ├── config/          # AppConfig
│   │   │   │   ├── controller/      # 5 Controllers
│   │   │   │   ├── model/           # 5 Models
│   │   │   │   ├── service/         # 4 Services
│   │   │   │   └── util/            # RefreshManager, ValidationUtils
│   │   │   └── resources/view/      # 4 FXML files
│   │   └── test/
│   ├── pom.xml
│   └── README.md
│
├── simplehealth-front-estoque/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/br/com/simplehealth/estoque/
│   │   │   │   ├── client/          # MainApp
│   │   │   │   ├── config/          # AppConfig
│   │   │   │   ├── controller/      # 8 Controllers
│   │   │   │   ├── model/           # 7 Models
│   │   │   │   ├── service/         # 7 Services
│   │   │   │   └── util/            # RefreshManager, ValidationUtils
│   │   │   └── resources/view/      # 7 FXML files
│   │   └── test/
│   ├── pom.xml
│   └── README.md
│
└── README.md (este arquivo)
```

---

## � Endpoints das APIs

### Backend Cadastro (8081)
- `POST /cadastro/auth/login` - Login
- `GET/POST/PUT/DELETE /cadastro/pacientes` - Pacientes
- `GET/POST/PUT/DELETE /cadastro/medicos` - Médicos
- `GET/POST/PUT/DELETE /cadastro/usuarios` - Usuários
- `GET/POST/PUT/DELETE /cadastro/convenios` - Convênios

### Backend Agendamento (8082)
- `GET/POST/PUT/DELETE /agendamento/consultas` - Consultas
- `GET/POST/PUT/DELETE /agendamento/exames` - Exames
- `GET/POST/PUT/DELETE /agendamento/procedimentos` - Procedimentos
- `POST/GET /agendamento/bloqueios` - Bloqueios de Agenda

### Backend Estoque (8083)
- `GET/POST/PUT/DELETE /estoque/medicamentos` - Medicamentos
- `GET/POST/PUT/DELETE /estoque/alimentos` - Alimentos
- `GET/POST/PUT/DELETE /estoque/hospitalares` - Hospitalares
- `GET/POST/PUT/DELETE /estoque/fornecedores` - Fornecedores
- `GET/POST/PUT/DELETE /estoque/estoques` - Localizações
- `GET/POST/PUT/DELETE /estoque/pedidos` - Pedidos
- `GET /estoque/itens` - Itens (consolidado)

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Propósito |
|-----------|--------|-----------|
| JavaFX | 17.0.2 | Framework de interface gráfica |
| Java | 17 | Linguagem de programação |
| Maven | 3.9.x | Gerenciamento de dependências e build |
| Apache HttpClient | 5.2.1 | Comunicação HTTP com backends |
| Jackson | 2.15.2 | Serialização/Deserialização JSON |
| SLF4J + Logback | 2.0.7 / 1.4.8 | Sistema de logging |

---

## 📊 Estatísticas do Projeto

| Métrica | Cadastro | Agendamento | Estoque | **Total** |
|---------|----------|-------------|---------|-----------|
| **Casos de Uso** | 5 | 4 | 7 | **16** |
| **Controllers** | 5 | 5 | 8 | **18** |
| **Models** | 4 | 5 | 7 | **16** |
| **Services** | 4 | 4 | 7 | **15** |
| **FXML Views** | 5 | 4 | 7 | **16** |
| **Endpoints API** | ~20 | ~16 | ~28 | **~64** |

---

## � Logs e Debugging

Os logs de cada módulo são armazenados em:
- **Cadastro**: `/tmp/cadastro-frontend.log`
- **Agendamento**: `/tmp/agendamento-frontend.log`
- **Estoque**: `/tmp/estoque-frontend.log`

Para ativar logs detalhados, edite o arquivo `logback.xml` de cada módulo:
```xml
<logger name="br.com.simplehealth.[modulo]" level="DEBUG"/>
```

---

## 🤝 Integração entre Módulos

Os módulos se comunicam **indiretamente** através dos backends:

```
Frontend Cadastro → Backend Cadastro → Dados de Pacientes/Médicos
                                              ↓
Frontend Agendamento → Backend Agendamento → Usa Pacientes/Médicos
                                              ↓
Frontend Estoque → Backend Estoque → Usa dados de Médicos (prescrições)
```

**Exemplo de fluxo integrado**:
1. Recepcionista cadastra paciente (Módulo Cadastro)
2. Recepcionista agenda consulta para o paciente (Módulo Agendamento)
3. Médico prescreve medicamento (Módulo Agendamento)
4. Farmacêutico verifica estoque (Módulo Estoque)
5. Se falta medicamento, cria pedido (Módulo Estoque)

---

## 📄 Licença

Este projeto faz parte do sistema SimpleHealth desenvolvido para fins acadêmicos.

---

## 👥 Equipe de Desenvolvimento

**Grupo 4 - Engenharia de Software 2025**

---

## 📞 Suporte

Para questões técnicas ou problemas:

1. **Problemas de execução**:
   - Verifique se os backends estão rodando
   - Confirme as portas 8081, 8082, 8083
   - Verifique os logs em `/tmp/*-frontend.log`

2. **Documentação detalhada**:
   - Cadastro: `simplehealth-front-cadastro/README.md`
   - Agendamento: `simplehealth-front-agendamento/README.md`
   - Estoque: `simplehealth-front-estoque/README.md`

3. **Documentação do projeto**:
   - `docs/documentos-finais-definitivos/`

---

**Última atualização**: 30 de novembro de 2025

Cada módulo possui documentação completa:
- `README.md` - Guia de uso
- `MANUAL_USO.md` - Manual do usuário
- `RESUMO_IMPLEMENTACAO.md` - Detalhes técnicos
- `SUMARIO_COMPLETO.md` - Sumário executivo
- `INDEX.md` - Índice de arquivos
- `ESTRUTURA.txt` - Árvore de diretórios

---

## 🎯 Padrões de Projeto

- **MVC** - Model-View-Controller
- **Observer** - RefreshManager para sincronização
- **Singleton** - Gerenciadores globais
- **Template Method** - Services e Controllers
- **Herança** - Hierarquias de domínio

---

## 🔗 Links Úteis

- Backend Cadastro: http://localhost:8081/cadastro
- Backend Agendamento: http://localhost:8082/agendamento
- Backend Estoque: http://localhost:8083/estoque

---

## ✅ Status do Projeto

**Data**: 30/11/2025  
**Status**: ✅ **TODOS OS 3 MÓDULOS COMPLETOS**

- [x] Módulo de Cadastro
- [x] Módulo de Agendamento
- [x] Módulo de Estoque

---

## 📝 Licença

Projeto Acadêmico - Universidade XYZ  
Curso: Engenharia de Software
