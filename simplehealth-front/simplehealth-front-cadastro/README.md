# SimpleHealth - Módulo de Cadastro (Frontend)

## 📋 Visão Geral

O **Módulo de Cadastro** é uma aplicação JavaFX responsável pela gestão de cadastros do sistema SimpleHealth. Este módulo permite o gerenciamento de pacientes, médicos, usuários do sistema e convênios médicos.

**Versão**: 1.0.0  
**Framework**: JavaFX 17  
**Build Tool**: Maven 3.9.x  
**Java Version**: 17

---

## 🎯 Casos de Uso Implementados

### UC01 - Autenticação de Usuário (Login)
**Descrição**: Permite o acesso seguro ao sistema  
**Atores**: Recepcionista, Médico, Administrador  
**Fluxo Principal**:
1. Usuário informa credenciais (login/senha)
2. Sistema valida credenciais
3. Sistema autentica e redireciona para tela principal

### UC02 - Cadastrar Paciente
**Descrição**: Permite cadastrar novos pacientes no sistema  
**Atores**: Recepcionista  
**Fluxo Principal**:
1. Usuário seleciona "Pacientes"
2. Preenche dados obrigatórios (CPF, nome, data nascimento, etc.)
3. Sistema valida CPF e dados
4. Confirma cadastro

### UC03 - Cadastrar Médico
**Descrição**: Permite cadastrar médicos no sistema  
**Atores**: Administrador  
**Fluxo Principal**:
1. Usuário seleciona "Médicos"
2. Preenche dados (CRM, nome, especialidade, etc.)
3. Sistema valida CRM e dados
4. Confirma cadastro

### UC04 - Cadastrar Usuário
**Descrição**: Permite cadastrar usuários do sistema  
**Atores**: Administrador  
**Fluxo Principal**:
1. Usuário seleciona "Usuários"
2. Define perfil de acesso (Recepcionista, Médico, Admin)
3. Preenche dados de login
4. Sistema cria credenciais

### UC05 - Cadastrar Convênio
**Descrição**: Permite cadastrar convênios médicos  
**Atores**: Administrador  
**Fluxo Principal**:
1. Usuário seleciona "Convênios"
2. Preenche dados do convênio
3. Sistema valida e cadastra

---

## 🏗️ Arquitetura do Sistema

### Arquitetura Lógica

```
┌─────────────────────────────────────────────────────────────┐
│                    CAMADA DE APRESENTAÇÃO                    │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │ Paciente │  │  Médico  │  │ Usuário  │  │ Convênio │   │
│  │Controller│  │Controller│  │Controller│  │Controller│   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │              Login Controller                        │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    CAMADA DE NEGÓCIO                         │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │ Paciente │  │  Médico  │  │ Usuário  │  │ Convênio │   │
│  │ Service  │  │ Service  │  │ Service  │  │ Service  │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼ HTTP/REST
┌─────────────────────────────────────────────────────────────┐
│                BACKEND - Spring Boot (Porta 8081)            │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  API REST (/cadastro/pacientes, /medicos, etc)      │   │
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
│   Porta: 8081           │
└───────────┬─────────────┘
            │
    ┌───────┴───────┐
    ▼               ▼
┌─────────┐   ┌─────────┐
│PostgreSQL│  │ ImmuDB  │
│  :5432   │  │ :3322   │
└─────────┘   └─────────┘
```

---

## 📦 Modelagem de Classes

### Classes de Domínio

#### Paciente
```java
- id: Long
- cpf: String
- nome: String
- dataNascimento: LocalDate
- telefone: String
- email: String
- endereco: String
- convenio: Convenio
```

#### Medico
```java
- id: Long
- crm: String
- nome: String
- especialidade: String
- telefone: String
- email: String
```

#### Usuario
```java
- id: Long
- login: String
- senha: String
- perfil: String (ADMIN, MEDICO, RECEPCIONISTA)
- nome: String
- ativo: Boolean
```

#### Convenio
```java
- id: Long
- nome: String
- cnpj: String
- telefone: String
- ativo: Boolean
```

### Padrões de Projeto Aplicados

#### 1. **MVC (Model-View-Controller)**
- **Model**: Classes de domínio (Paciente, Medico, Usuario, Convenio)
- **View**: Arquivos FXML (paciente.fxml, medico.fxml, usuario.fxml, convenio.fxml, login.fxml)
- **Controller**: Classes Controller (PacienteController, MedicoController, etc.)

#### 2. **Service Layer**
- Isolamento da lógica de comunicação HTTP
- Classes: PacienteService, MedicoService, UsuarioService, ConvenioService

#### 3. **Template Method**
- `AbstractCrudController`: Define template para operações CRUD
- Métodos abstratos implementados pelas subclasses

#### 4. **Singleton**
- `RefreshManager`: Gerencia atualização de dados entre controllers
- `AppConfig`: Configurações da aplicação

#### 5. **Observer**
- `RefreshManager`: Notifica controllers sobre mudanças nos dados

#### 6. **Facade**
- Services fazem fachada para comunicação com backend REST

---

## 🔄 Diagramas de Processo (BPM)

### Processo de Cadastro de Paciente

```
[Início] → [Informar CPF] → [CPF Válido?] --Não--> [Mostrar Erro] → [Fim]
                                  ↓ Sim
                          [Preencher Dados]
                                  ↓
                          [Validar Campos]
                                  ↓
                          [Dados Válidos?] --Não--> [Mostrar Erros] → [Fim]
                                  ↓ Sim
                          [Salvar no Backend]
                                  ↓
                          [Atualizar Tabela] → [Fim]
```

---

## 🚀 Como Executar

### Pré-requisitos

- **Java 17** ou superior
- **Maven 3.9.x** ou superior
- **Backend do Cadastro** rodando na porta 8081

### Opção 1: Via Maven (Recomendado)

```bash
cd simplehealth-front/simplehealth-front-cadastro
mvn javafx:run
```

### Opção 2: Via Script

**Linux/Mac:**
```bash
cd simplehealth-front/simplehealth-front-cadastro
./run.sh
```

**Windows:**
```cmd
cd simplehealth-front\simplehealth-front-cadastro
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

✅ **Validação de CPF**
- Validação matemática dos dígitos verificadores
- Aceita formato com ou sem pontuação
- Verifica CPFs conhecidos como inválidos

✅ **Validação de CRM**
- Formato: 4-7 dígitos numéricos
- Validação de caracteres

✅ **Validação de Campos Obrigatórios**
- Nome, CPF, Data de Nascimento (Paciente)
- Nome, CRM, Especialidade (Médico)
- Login, Senha, Perfil (Usuário)
- Nome (Convênio)

✅ **Validação de Data**
- Data de nascimento não pode ser futura
- Formato válido (dd/MM/yyyy)

✅ **Validação de Email**
- Formato válido de email
- Domínio presente

### Testes de Integração

✅ **Comunicação com Backend**
- Teste de autenticação (login)
- Teste de criação de registros
- Teste de listagem
- Teste de atualização
- Teste de exclusão

---

## 📁 Estrutura do Projeto

```
simplehealth-front-cadastro/
├── src/
│   ├── main/
│   │   ├── java/br/com/simplehealth/cadastro/
│   │   │   ├── client/
│   │   │   │   └── MainApp.java          # Classe principal
│   │   │   ├── config/
│   │   │   │   └── AppConfig.java         # Configurações
│   │   │   ├── controller/
│   │   │   │   ├── AbstractCrudController.java
│   │   │   │   ├── LoginController.java
│   │   │   │   ├── PacienteController.java
│   │   │   │   ├── MedicoController.java
│   │   │   │   ├── UsuarioController.java
│   │   │   │   └── ConvenioController.java
│   │   │   ├── model/
│   │   │   │   ├── Paciente.java
│   │   │   │   ├── Medico.java
│   │   │   │   ├── Usuario.java
│   │   │   │   └── Convenio.java
│   │   │   ├── service/
│   │   │   │   ├── PacienteService.java
│   │   │   │   ├── MedicoService.java
│   │   │   │   ├── UsuarioService.java
│   │   │   │   └── ConvenioService.java
│   │   │   └── util/
│   │   │       ├── RefreshManager.java
│   │   │       └── ValidationUtils.java
│   │   └── resources/
│   │       ├── view/
│   │       │   ├── login.fxml
│   │       │   ├── paciente.fxml
│   │       │   ├── medico.fxml
│   │       │   ├── usuario.fxml
│   │       │   └── convenio.fxml
│   │       └── logback.xml
├── pom.xml
└── README.md
```

---

## 🔌 Endpoints da API (Backend)

### Autenticação
- `POST /cadastro/auth/login` - Autenticar usuário

### Pacientes
- `GET /cadastro/pacientes` - Listar todos os pacientes
- `GET /cadastro/pacientes/{id}` - Buscar paciente por ID
- `POST /cadastro/pacientes` - Criar novo paciente
- `PUT /cadastro/pacientes/{id}` - Atualizar paciente
- `DELETE /cadastro/pacientes/{id}` - Excluir paciente

### Médicos
- `GET /cadastro/medicos` - Listar todos os médicos
- `GET /cadastro/medicos/{id}` - Buscar médico por ID
- `POST /cadastro/medicos` - Criar novo médico
- `PUT /cadastro/medicos/{id}` - Atualizar médico
- `DELETE /cadastro/medicos/{id}` - Excluir médico

### Usuários
- `GET /cadastro/usuarios` - Listar todos os usuários
- `GET /cadastro/usuarios/{id}` - Buscar usuário por ID
- `POST /cadastro/usuarios` - Criar novo usuário
- `PUT /cadastro/usuarios/{id}` - Atualizar usuário
- `DELETE /cadastro/usuarios/{id}` - Excluir usuário

### Convênios
- `GET /cadastro/convenios` - Listar todos os convênios
- `GET /cadastro/convenios/{id}` - Buscar convênio por ID
- `POST /cadastro/convenios` - Criar novo convênio
- `PUT /cadastro/convenios/{id}` - Atualizar convênio
- `DELETE /cadastro/convenios/{id}` - Excluir convênio

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

### Estados de Cadastro (CRUD)

```
[Novo] → [Editando] → [Validando]
                          ↓
                    [Válido?] --Não--> [Editando]
                          ↓ Sim
                    [Salvando]
                          ↓
                    [Salvo] → [Visualizando]
```

### Estados de Usuário

```
[Criado] → [Ativo] ⟷ [Inativo]
```

**Transições:**
- `Criado → Ativo`: Quando usuário é criado no sistema
- `Ativo ⟷ Inativo`: Administrador pode ativar/desativar usuário

---

## 🔍 Funcionalidades Principais

### 1. Autenticação
- ✅ Login com usuário e senha
- ✅ Validação de credenciais
- ✅ Controle de perfis de acesso

### 2. Gestão de Pacientes
- ✅ Listar pacientes cadastrados
- ✅ Criar novo paciente
- ✅ Editar paciente existente
- ✅ Excluir paciente
- ✅ Buscar paciente por CPF/nome
- ✅ Validação de CPF

### 3. Gestão de Médicos
- ✅ Listar médicos cadastrados
- ✅ Criar novo médico
- ✅ Editar médico existente
- ✅ Excluir médico
- ✅ Buscar por CRM/especialidade
- ✅ Validação de CRM

### 4. Gestão de Usuários
- ✅ Listar usuários do sistema
- ✅ Criar novo usuário
- ✅ Definir perfil de acesso
- ✅ Ativar/Desativar usuário
- ✅ Alterar senha

### 5. Gestão de Convênios
- ✅ Listar convênios
- ✅ Criar novo convênio
- ✅ Editar convênio existente
- ✅ Ativar/Desativar convênio

---

## 📝 Logs e Debugging

Os logs são armazenados em:
- **Console**: Nível DEBUG durante desenvolvimento
- **Arquivo**: `/tmp/cadastro-frontend.log` (quando executado via script)

Para ativar logs detalhados, edite `src/main/resources/logback.xml`:
```xml
<logger name="br.com.simplehealth.cadastro" level="DEBUG"/>
```

---

## 🤝 Integração com Outros Módulos

Este módulo se integra com:

1. **Backend de Cadastro** (porta 8081)
   - Comunicação via HTTP REST
   - Formato de dados: JSON

2. **Módulo de Agendamento** (indireto)
   - Dados de pacientes e médicos são consumidos via backend
   - Sincronização via API

3. **Módulo de Estoque** (indireto)
   - Dados de médicos são compartilhados

---

## 📄 Licença

Este projeto faz parte do sistema SimpleHealth desenvolvido para fins acadêmicos.

---

## 👥 Equipe de Desenvolvimento

**Grupo 4 - Engenharia de Software 2025**

---

## 📞 Suporte

Para questões técnicas ou problemas:
1. Verifique os logs em `/tmp/cadastro-frontend.log`
2. Confirme se o backend está rodando na porta 8081
3. Consulte a documentação técnica em `docs/documentos-finais-definitivos/`

---

**Última atualização**: 30 de novembro de 2025
│       └── java/                    # Testes unitários
├── pom.xml                          # Configuração Maven
├── run.sh                           # Script de execução (Linux/Mac)
└── run.bat                          # Script de execução (Windows)
```

## 📦 Funcionalidades

### Gerenciamento de Pacientes
- ✅ Listar todos os pacientes
- ✅ Cadastrar novo paciente
- ✅ Atualizar dados do paciente
- ✅ Deletar paciente
- ✅ Campos: Nome, Data de Nascimento, CPF, Telefone, Email

### Gerenciamento de Médicos
- ✅ Listar todos os médicos
- ✅ Cadastrar novo médico
- ✅ Atualizar dados do médico
- ✅ Deletar médico
- ✅ Campos: Nome, CRM, Especialidade, Telefone, Email

### Gerenciamento de Convênios
- ✅ Listar todos os convênios
- ✅ Cadastrar novo convênio
- ✅ Atualizar dados do convênio
- ✅ Deletar convênio
- ✅ Campos: Nome, Plano, Status (Ativo/Inativo)

## 🔌 Configuração da API

A URL base da API está configurada em:
```
src/main/java/br/com/simplehealth/cadastro/config/AppConfig.java
```

Por padrão, aponta para: `http://localhost:8081/cadastro`

### Endpoints utilizados:
- **Pacientes**: `/pacientes`
- **Médicos**: `/api/cadastro/medicos`
- **Convênios**: `/api/cadastro/convenios`

## 🎨 Interface

A aplicação possui uma interface com abas (tabs) organizadas por funcionalidade:

1. **Aba Pacientes** - Gestão de pacientes
2. **Aba Médicos** - Gestão de médicos
3. **Aba Convênios** - Gestão de convênios

Cada aba contém:
- Tabela com listagem dos registros
- Formulário para inclusão/edição
- Botões de ação (Salvar, Atualizar, Deletar, Limpar)

## 🐛 Troubleshooting

### Backend não está rodando
Se você receber erros de conexão, verifique se o backend está rodando:
```bash
cd ../../simplehealth-back/simplehealth-back-cadastro
docker-compose up -d
./mvnw spring-boot:run
```

### Porta já está em uso
O backend usa a porta 8081. Verifique se não há outro serviço usando esta porta.

### Problemas com JavaFX
Certifique-se de estar usando Java 17 com suporte a JavaFX.

## 📄 Licença

Este projeto faz parte do sistema SimpleHealth.
