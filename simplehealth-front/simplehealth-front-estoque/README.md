# SimpleHealth - Módulo de Estoque (Frontend)

## 📋 Visão Geral

O **Módulo de Estoque** é uma aplicação JavaFX responsável pela gestão completa do estoque hospitalar do sistema SimpleHealth. Este módulo permite o gerenciamento de medicamentos, alimentos, materiais hospitalares, fornecedores, localizações de estoque, pedidos e itens.

**Versão**: 1.0.0  
**Framework**: JavaFX 17  
**Build Tool**: Maven 3.9.x  
**Java Version**: 17

---

## 🎯 Casos de Uso Implementados

### UC10 - Cadastrar Medicamento
**Descrição**: Permite cadastrar medicamentos no estoque  
**Atores**: Farmacêutico, Administrador  
**Fluxo Principal**:
1. Usuário seleciona "Medicamentos"
2. Preenche dados (nome, princípio ativo, tarja, prescrição, etc.)
3. Sistema valida dados
4. Confirma cadastro

### UC11 - Cadastrar Alimento
**Descrição**: Permite cadastrar alimentos no estoque  
**Atores**: Nutricionista, Administrador  
**Fluxo Principal**:
1. Usuário seleciona "Alimentos"
2. Preenche dados (nome, alérgenos, armazenamento, etc.)
3. Sistema valida dados
4. Confirma cadastro

### UC12 - Cadastrar Material Hospitalar
**Descrição**: Permite cadastrar materiais hospitalares  
**Atores**: Administrador  
**Fluxo Principal**:
1. Usuário seleciona "Hospitalares"
2. Preenche dados (nome, uso, descartável, etc.)
3. Sistema valida dados
4. Confirma cadastro

### UC13 - Cadastrar Fornecedor
**Descrição**: Permite cadastrar fornecedores  
**Atores**: Administrador  
**Fluxo Principal**:
1. Usuário seleciona "Fornecedores"
2. Preenche dados (CNPJ, nome, contato, etc.)
3. Sistema valida CNPJ
4. Confirma cadastro

### UC14 - Gerenciar Localizações de Estoque
**Descrição**: Permite gerenciar localizações físicas no estoque  
**Atores**: Administrador, Estoquista  
**Fluxo Principal**:
1. Usuário seleciona "Estoques"
2. Define setor, corredor, prateleira
3. Sistema registra localização

### UC15 - Gerenciar Pedidos
**Descrição**: Permite criar e gerenciar pedidos a fornecedores  
**Atores**: Administrador, Estoquista  
**Fluxo Principal**:
1. Usuário seleciona "Pedidos"
2. Seleciona fornecedor e itens
3. Define quantidades e valores
4. Sistema calcula total e registra pedido

### UC16 - Visualizar Itens
**Descrição**: Permite visualizar todos os itens cadastrados  
**Atores**: Todos  
**Fluxo Principal**:
1. Usuário seleciona "Itens"
2. Sistema exibe listagem consolidada de medicamentos, alimentos e hospitalares

---

## 🏗️ Arquitetura do Sistema

### Arquitetura Lógica

```
┌────────────────────────────────────────────────────────────────────────┐
│                       CAMADA DE APRESENTAÇÃO                            │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐   │
│  │Medicamen │ │ Alimento │ │Hospitalar│ │Fornecedor│ │ Estoque  │   │
│  │Controller│ │Controller│ │Controller│ │Controller│ │Controller│   │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘ └──────────┘   │
│  ┌──────────┐ ┌──────────┐                                           │
│  │  Pedido  │ │   Item   │                                           │
│  │Controller│ │Controller│                                           │
│  └──────────┘ └──────────┘                                           │
└────────────────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────────────────────┐
│                       CAMADA DE NEGÓCIO                                 │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐   │
│  │Medicamen │ │ Alimento │ │Hospitalar│ │Fornecedor│ │ Estoque  │   │
│  │ Service  │ │ Service  │ │ Service  │ │ Service  │ │ Service  │   │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘ └──────────┘   │
│  ┌──────────┐ ┌──────────┐                                           │
│  │  Pedido  │ │   Item   │                                           │
│  │ Service  │ │ Service  │                                           │
│  └──────────┘ └──────────┘                                           │
└────────────────────────────────────────────────────────────────────────┘
                            │
                            ▼ HTTP/REST
┌────────────────────────────────────────────────────────────────────────┐
│                BACKEND - Spring Boot (Porta 8083)                       │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │  API REST (/estoque/medicamentos, /alimentos, etc)              │  │
│  └──────────────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────────────┘
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
│   Porta: 8083           │
└───────────┬─────────────┘
            │
    ┌───────┴───────┐
    ▼               ▼
┌─────────┐   ┌─────────┐
│ ImmuDB  │   │  Redis  │
│ :3322   │   │ :6379   │
└─────────┘   └─────────┘
```

---

## 📦 Modelagem de Classes

### Classes de Domínio

#### Medicamento
```java
- id: Long
- nome: String
- principioAtivo: String
- composicao: String
- tarja: String (Vermelha, Preta, Amarela, Sem Tarja)
- prescrição: Boolean
- bula: String
```

#### Alimento
```java
- id: Long
- nome: String
- alergenos: String
- armazenamento: String (Refrigerado, Congelado, Temperatura Ambiente)
- validade: LocalDate
```

#### Hospitalar
```java
- id: Long
- nome: String
- uso: String
- descartavel: Boolean
```

#### Fornecedor
```java
- id: Long
- cnpj: String
- nome: String
- contato: String
- email: String
- endereco: String
```

#### Estoque
```java
- id: Long
- setor: String
- corredor: String
- prateleira: String
- descricao: String
```

#### Pedido
```java
- id: Long
- fornecedor: Fornecedor
- dataPedido: LocalDate
- dataEntrega: LocalDate
- status: String
- valorTotal: BigDecimal
- observacoes: String
```

#### Item
```java
- id: Long
- nome: String
- tipo: String (Medicamento, Alimento, Hospitalar)
- quantidade: Integer
- estoque: Estoque
```

### Padrões de Projeto Aplicados

#### 1. **MVC (Model-View-Controller)**
- **Model**: Classes de domínio (Medicamento, Alimento, Hospitalar, Fornecedor, Estoque, Pedido, Item)
- **View**: Arquivos FXML (medicamento.fxml, alimento.fxml, hospitalar.fxml, fornecedor.fxml, estoque.fxml, pedido.fxml, item.fxml)
- **Controller**: Classes Controller (MedicamentoController, AlimentoController, etc.)

#### 2. **Service Layer**
- Isolamento da lógica de comunicação HTTP
- Classes: MedicamentoService, AlimentoService, HospitalarService, FornecedorService, EstoqueService, PedidoService, ItemService

#### 3. **Template Method**
- `AbstractCrudController`: Define template para operações CRUD
- Métodos abstratos implementados pelas subclasses

#### 4. **Singleton**
- `RefreshManager`: Gerencia atualização de dados entre controllers
- `AppConfig`: Configurações da aplicação

#### 5. **Observer**
- `RefreshManager`: Notifica controllers sobre mudanças nos dados

#### 6. **Strategy**
- Diferentes estratégias de armazenamento (Refrigerado, Congelado, Temperatura Ambiente)

---

## 🔄 Diagramas de Processo (BPM)

### Processo de Criação de Pedido

```
[Início] → [Selecionar Fornecedor] → [Adicionar Itens] → [Definir Quantidades]
    ↓
[Calcular Total]
    ↓
[Definir Data Entrega]
    ↓
[Validar Pedido]
    ↓
[Válido?] --Não--> [Mostrar Erros] → [Fim]
    ↓ Sim
[Salvar Pedido]
    ↓
[Atualizar Estoque] → [Fim]
```

---

## 🚀 Como Executar

### Pré-requisitos

- **Java 17** ou superior
- **Maven 3.9.x** ou superior
- **Backend do Estoque** rodando na porta 8083

### Opção 1: Via Maven (Recomendado)

```bash
cd simplehealth-front/simplehealth-front-estoque
mvn javafx:run
```

### Opção 2: Via Script

**Linux/Mac:**
```bash
cd simplehealth-front/simplehealth-front-estoque
./run.sh
```

**Windows:**
```cmd
cd simplehealth-front\simplehealth-front-estoque
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

✅ **Validação de CNPJ**
- Validação matemática dos dígitos verificadores
- Aceita formato com ou sem pontuação

✅ **Validação de Campos Obrigatórios**
- Nome, Princípio Ativo, Tarja (Medicamento)
- Nome, Alérgenos, Armazenamento (Alimento)
- Nome, Uso (Hospitalar)
- CNPJ, Nome, Contato (Fornecedor)

✅ **Validação de Data**
- Validade de alimentos não pode ser passada
- Data de entrega não pode ser anterior à data do pedido

✅ **Validação de Valores**
- Quantidade deve ser positiva
- Valor total calculado automaticamente

### Testes de Integração

✅ **Comunicação com Backend**
- Teste de criação de registros
- Teste de listagem
- Teste de atualização
- Teste de exclusão

---

## 📁 Estrutura do Projeto

```
simplehealth-front-estoque/
├── src/
│   ├── main/
│   │   ├── java/br/com/simplehealth/estoque/
│   │   │   ├── client/
│   │   │   │   └── MainApp.java          # Classe principal
│   │   │   ├── config/
│   │   │   │   └── AppConfig.java         # Configurações
│   │   │   ├── controller/
│   │   │   │   ├── AbstractCrudController.java
│   │   │   │   ├── MedicamentoController.java
│   │   │   │   ├── AlimentoController.java
│   │   │   │   ├── HospitalarController.java
│   │   │   │   ├── FornecedorController.java
│   │   │   │   ├── EstoqueController.java
│   │   │   │   ├── PedidoController.java
│   │   │   │   └── ItemController.java
│   │   │   ├── model/
│   │   │   │   ├── Medicamento.java
│   │   │   │   ├── Alimento.java
│   │   │   │   ├── Hospitalar.java
│   │   │   │   ├── Fornecedor.java
│   │   │   │   ├── Estoque.java
│   │   │   │   ├── Pedido.java
│   │   │   │   └── Item.java
│   │   │   ├── service/
│   │   │   │   ├── MedicamentoService.java
│   │   │   │   ├── AlimentoService.java
│   │   │   │   ├── HospitalarService.java
│   │   │   │   ├── FornecedorService.java
│   │   │   │   ├── EstoqueService.java
│   │   │   │   ├── PedidoService.java
│   │   │   │   └── ItemService.java
│   │   │   └── util/
│   │   │       ├── RefreshManager.java
│   │   │       └── ValidationUtils.java
│   │   └── resources/
│   │       ├── view/
│   │       │   ├── medicamento.fxml
│   │       │   ├── alimento.fxml
│   │       │   ├── hospitalar.fxml
│   │       │   ├── fornecedor.fxml
│   │       │   ├── estoque.fxml
│   │       │   ├── pedido.fxml
│   │       │   └── item.fxml
│   │       └── logback.xml
├── pom.xml
└── README.md
```

---

## 🔌 Endpoints da API (Backend)

### Medicamentos
- `GET /estoque/medicamentos` - Listar todos os medicamentos
- `GET /estoque/medicamentos/{id}` - Buscar medicamento por ID
- `POST /estoque/medicamentos` - Criar novo medicamento
- `PUT /estoque/medicamentos/{id}` - Atualizar medicamento
- `DELETE /estoque/medicamentos/{id}` - Excluir medicamento

### Alimentos
- `GET /estoque/alimentos` - Listar todos os alimentos
- `GET /estoque/alimentos/{id}` - Buscar alimento por ID
- `POST /estoque/alimentos` - Criar novo alimento
- `PUT /estoque/alimentos/{id}` - Atualizar alimento
- `DELETE /estoque/alimentos/{id}` - Excluir alimento

### Hospitalares
- `GET /estoque/hospitalares` - Listar todos os materiais hospitalares
- `GET /estoque/hospitalares/{id}` - Buscar hospitalar por ID
- `POST /estoque/hospitalares` - Criar novo hospitalar
- `PUT /estoque/hospitalares/{id}` - Atualizar hospitalar
- `DELETE /estoque/hospitalares/{id}` - Excluir hospitalar

### Fornecedores
- `GET /estoque/fornecedores` - Listar todos os fornecedores
- `GET /estoque/fornecedores/{id}` - Buscar fornecedor por ID
- `POST /estoque/fornecedores` - Criar novo fornecedor
- `PUT /estoque/fornecedores/{id}` - Atualizar fornecedor
- `DELETE /estoque/fornecedores/{id}` - Excluir fornecedor

### Estoques
- `GET /estoque/estoques` - Listar todas as localizações
- `GET /estoque/estoques/{id}` - Buscar estoque por ID
- `POST /estoque/estoques` - Criar nova localização
- `PUT /estoque/estoques/{id}` - Atualizar localização
- `DELETE /estoque/estoques/{id}` - Excluir localização

### Pedidos
- `GET /estoque/pedidos` - Listar todos os pedidos
- `GET /estoque/pedidos/{id}` - Buscar pedido por ID
- `POST /estoque/pedidos` - Criar novo pedido
- `PUT /estoque/pedidos/{id}` - Atualizar pedido
- `DELETE /estoque/pedidos/{id}` - Excluir pedido

### Itens
- `GET /estoque/itens` - Listar todos os itens

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

### Estados de Pedido

```
[Criado] → [Enviado] → [Em Trânsito] → [Entregue]
    ↓           ↓            ↓
[Cancelado] ←───┴────────────┘
```

**Transições:**
- `Criado → Enviado`: Quando pedido é enviado ao fornecedor
- `Enviado → Em Trânsito`: Quando fornecedor confirma envio
- `Em Trânsito → Entregue`: Quando pedido é recebido
- `Criado/Enviado/Em Trânsito → Cancelado`: Cancelamento do pedido

### Estados de Item no Estoque

```
[Disponível] → [Reservado] → [Utilizado]
      ↓
[Vencido/Descartado]
```

---

## 🔍 Funcionalidades Principais

### 1. Gestão de Medicamentos
- ✅ Listar medicamentos cadastrados
- ✅ Criar novo medicamento
- ✅ Editar medicamento existente
- ✅ Excluir medicamento
- ✅ Controle de tarja e prescrição
- ✅ Registro de bula e composição

### 2. Gestão de Alimentos
- ✅ Listar alimentos cadastrados
- ✅ Criar novo alimento
- ✅ Editar alimento existente
- ✅ Excluir alimento
- ✅ Controle de alérgenos
- ✅ Definição de armazenamento

### 3. Gestão de Materiais Hospitalares
- ✅ Listar materiais cadastrados
- ✅ Criar novo material
- ✅ Editar material existente
- ✅ Excluir material
- ✅ Marcação de descartabilidade

### 4. Gestão de Fornecedores
- ✅ Listar fornecedores
- ✅ Criar novo fornecedor
- ✅ Editar fornecedor existente
- ✅ Excluir fornecedor
- ✅ Validação de CNPJ

### 5. Gestão de Localizações
- ✅ Listar localizações de estoque
- ✅ Criar nova localização
- ✅ Organização por setor/corredor/prateleira

### 6. Gestão de Pedidos
- ✅ Listar pedidos
- ✅ Criar novo pedido
- ✅ Associar fornecedor
- ✅ Cálculo automático de total
- ✅ Controle de status

### 7. Visualização de Itens
- ✅ Visualização consolidada de todos os itens
- ✅ Filtro por tipo (Medicamento, Alimento, Hospitalar)

---

## 📝 Logs e Debugging

Os logs são armazenados em:
- **Console**: Nível DEBUG durante desenvolvimento
- **Arquivo**: `/tmp/estoque-frontend.log` (quando executado via script)

Para ativar logs detalhados, edite `src/main/resources/logback.xml`:
```xml
<logger name="br.com.simplehealth.estoque" level="DEBUG"/>
```

---

## 🤝 Integração com Outros Módulos

Este módulo se integra com:

1. **Backend de Estoque** (porta 8083)
   - Comunicação via HTTP REST
   - Formato de dados: JSON

2. **Módulo de Cadastro** (indireto)
   - Dados de médicos podem prescrever medicamentos
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
1. Verifique os logs em `/tmp/estoque-frontend.log`
2. Confirme se o backend está rodando na porta 8083
3. Consulte a documentação técnica em `docs/documentos-finais-definitivos/`

---

**Última atualização**: 30 de novembro de 2025
- **API Docs**: Ver `api_docs.json` no backend

## 🏗️ Arquitetura

```
src/main/java/br/com/simplehealth/estoque/
├── client/          # MainApp (entrada da aplicação)
├── config/          # Configurações (URLs, endpoints)
├── controller/      # Controllers JavaFX (7 CRUDs)
├── model/           # Modelos de dados (7 entidades)
├── service/         # Services HTTP (integração REST)
└── util/            # Utilitários (RefreshManager)

src/main/resources/
├── logback.xml      # Configuração de logs
└── view/            # Arquivos FXML (7 interfaces)
```

## 📊 Modelos de Dados

### Hierarquia de Item
- **Item** (abstrato)
  - Medicamento
  - Alimento
  - Hospitalar

### Entidades Independentes
- Fornecedor
- Estoque
- Pedido

## 🎨 Interface

Aplicação com 7 abas:
- 🏥 Medicamentos (vermelho)
- 🍎 Alimentos (verde)
- 🏥 Hospitalares (azul)
- 📦 Fornecedores (laranja)
- 📍 Estoques (roxo)
- 🛒 Pedidos (ciano)
- 📋 Todos os Itens (cinza)

## 📝 Licença

Projeto acadêmico - Universidade XYZ
