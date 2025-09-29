# SimpleHealth - Módulo de Armazenamento (Frontend)

Este é o frontend JavaFX para o módulo de armazenamento do sistema SimpleHealth. O projeto implementa um sistema completo de CRUDs (Create, Read, Update, Delete) consumindo APIs REST desenvolvidas em Spring Boot para gerenciar itens de estoque, fornecedores, pedidos e estoques.

## Estrutura do Projeto

O projeto segue o padrão MVC (Model-View-Controller) e está organizado da seguinte forma:

```
src/main/java/br/com/simplehealth/armazenamento/
├── client/           # Classe principal da aplicação
├── controller/       # Controladores JavaFX
├── model/           # Modelos de dados (DTOs)
├── service/         # Serviços para consumir APIs
└── view/            # ViewModels para JavaFX

src/main/resources/
├── view/            # Arquivos FXML das interfaces
└── logback.xml      # Configuração de logging
```

## Funcionalidades dos CRUDs

### 1. CRUD de Estoque
**Funcionalidades:**
- Criar, atualizar, deletar e listar estoques
- Cada estoque possui um local e uma coleção de itens
- Interface permite adicionar/remover itens do estoque

**Campos:**
- Local* (obrigatório)
- Lista de itens associados

### 2. CRUD de Item
**Funcionalidades:**
- Criar, atualizar, deletar e listar itens
- Suporte a 3 tipos específicos: Medicamento, Hospitalar e Alimento
- Campos específicos aparecem dinamicamente baseados no tipo selecionado

**Campos Comuns:**
- Nome* (obrigatório)
- Descrição
- Tipo* (obrigatório) - MEDICAMENTO, HOSPITALAR, ALIMENTO
- Unidade de Medida* (obrigatório)
- Quantidade Total
- Validade
- Lote

**Campos Específicos por Tipo:**

**Medicamento:**
- Prescrição
- Composição
- Bula
- Tarja (LIVRE, VERMELHA, PRETA)
- Modo de Consumo

**Hospitalar:**
- Descartável (checkbox)
- Uso (GERAL, CIRÚRGICO, CURATIVO)

**Alimento:**
- Alérgenos
- Tipo de Armazenamento (PERECÍVEL, NÃO PERECÍVEL, REFRIGERADO)

### 3. CRUD de Pedido
**Funcionalidades:**
- Criar, atualizar, deletar e listar pedidos
- Selecionar vários itens para um fornecedor
- Controle de status do pedido

**Campos:**
- Data do Pedido* (obrigatório)
- Status* (obrigatório) - PENDENTE, PROCESSANDO, ENVIADO, ENTREGUE, CANCELADO
- Nota Fiscal
- Fornecedor* (obrigatório)
- Lista de itens do pedido

### 4. CRUD de Fornecedor
**Funcionalidades:**
- Criar, atualizar, deletar e listar fornecedores
- Gerenciamento de dados de contato e endereço

**Campos:**
- Nome* (obrigatório)
- CNPJ* (obrigatório)
- Contato
- Endereço

## Fluxo de Operações

Todos os CRUDs seguem o mesmo padrão de operação:

### Operação "Novo"
1. Clique no botão "Novo"
2. Formulário é exibido com campos limpos
3. Preencha os campos obrigatórios (marcados com *)
4. Para campos específicos (Item), selecione o tipo primeiro
5. Clique em "Confirmar" para salvar ou "Cancelar" para descartar

### Operação "Atualizar"
1. Selecione um registro na tabela
2. Clique no botão "Atualizar"
3. Formulário é preenchido com os dados atuais
4. Modifique os campos desejados
5. Clique em "Confirmar" para salvar ou "Cancelar" para descartar

### Operação "Deletar"
1. Selecione um registro na tabela
2. Clique no botão "Deletar"
3. Confirme a exclusão na janela de diálogo
4. O registro será removido do sistema

### Funcionalidades Especiais

#### Gerenciamento de Itens em Estoque/Pedido
- Use o ComboBox "Adicionar Item" para selecionar itens disponíveis
- Clique em "Adicionar Item" para incluir na lista
- Selecione um item na lista e clique em "Remover Item" para excluir

#### Seleção Dinâmica de Tipos (Item)
- Ao selecionar um tipo no ComboBox, os campos específicos aparecem automaticamente
- Medicamento: campos relacionados a prescrição e farmacologia
- Hospitalar: campos para uso médico e descarte
- Alimento: campos para alérgenos e armazenamento

## Arquitetura do Sistema

### Camadas
1. **View (FXML)**: Interface gráfica JavaFX
2. **Controller**: Lógica de apresentação e controle
3. **Service**: Comunicação com APIs REST
4. **Model**: Entidades de domínio

### Padrões Utilizados
- **MVC (Model-View-Controller)**: Separação de responsabilidades
- **Abstract Factory**: `AbstractCrudController` para operações comuns
- **Observer**: Listeners para eventos da interface
- **ViewModel**: Classes de view para binding com JavaFX

### Estrutura de Classes

#### Herança de Itens
```
Item (classe base)
├── Medicamento
├── Hospitalar
└── Alimento
```

#### Padrão CRUD
Todos os controladores herdam de `AbstractCrudController` que fornece:
- Operações CRUD via API
- Validação de campos
- Gerenciamento de estado da interface
- Notificações entre telas

#### Modelos
- **Item**: Classe base para todos os itens
- **Medicamento**, **Hospitalar**, **Alimento**: Especializações de Item
- **Fornecedor**: Dados dos fornecedores
- **Pedido**: Pedidos de compra
- **Estoque**: Locais de armazenamento

#### Serviços
- **ItemService**: Consumo de APIs de itens
- **FornecedorService**: Consumo de APIs de fornecedores
- **PedidoService**: Consumo de APIs de pedidos
- **EstoqueService**: Consumo de APIs de estoque

#### Controladores
- **AbstractCrudController**: Controlador base com funcionalidades CRUD
- **ItemController**: Controlador específico para itens
- **FornecedorController**: Controlador específico para fornecedores
- **PedidoController**: Controlador específico para pedidos
- **EstoqueController**: Controlador específico para estoques

### APIs Consumidas
O sistema consome APIs REST nos seguintes endpoints:
- `http://localhost:8080/api/estoques`
- `http://localhost:8080/api/itens`
- `http://localhost:8080/api/pedidos`
- `http://localhost:8080/api/fornecedores`

## Tecnologias Utilizadas

- **JavaFX 17**: Interface gráfica
- **Jackson**: Serialização/deserialização JSON
- **Apache HttpClient 5**: Cliente HTTP para consumo de APIs
- **SLF4J + Logback**: Logging
- **Maven**: Gerenciamento de dependências

## Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- Backend Spring Boot rodando na porta 8080

## Como Executar

1. **Clone o projeto**:
   ```bash
   git clone <url-do-repositorio>
   cd SimpleHealth_Frontend/moduloArmazenamento
   ```

2. **Compile o projeto**:
   ```bash
   mvn clean compile
   ```

3. **Execute a aplicação**:
   ```bash
   mvn javafx:run
   ```

   Ou usando o plugin Maven:
   ```bash
   mvn clean javafx:run
   ```

### Via Scripts
- **Windows**: Execute `run.bat`
- **Linux/Mac**: Execute `run.sh`

### Via IDE (Desenvolvimento)
1. Execute a classe `MainApp.java`
2. A aplicação abrirá com abas para cada CRUD

## Validações

### Campos Obrigatórios
- Estoque: Local
- Item: Nome, Tipo, Unidade de Medida
- Pedido: Data, Status, Fornecedor
- Fornecedor: Nome, CNPJ

### Validações de Formato
- Datas: Formato brasileiro (dd/MM/yyyy)
- CNPJ: Validação de formato
- Campos numéricos: Apenas números

## Tratamento de Erros

O sistema possui tratamento abrangente de erros:
- **Erro de Comunicação**: Problemas com API REST
- **Erro de Validação**: Campos obrigatórios não preenchidos
- **Erro de Formato**: Dados em formato inválido
- **Erro de Negócio**: Regras de domínio violadas

## Configuração da API

Por padrão, a aplicação espera que as APIs estejam rodando em `http://localhost:8080`.

## Interface do Usuário

### Tela de Itens
- **Tabela**: Lista todos os itens com informações básicas
- **Formulário**: Campos para criar/editar itens
- **Campos Dinâmicos**: Mostrados baseados no tipo de item selecionado
- **Botões**: Novo, Atualizar, Deletar, Confirmar, Cancelar

### Tela de Fornecedores
- **Tabela**: Lista todos os fornecedores
- **Formulário**: Campos para dados do fornecedor
- **Busca**: Funcionalidade de busca por nome
- **Botões**: CRUD padrão + busca e filtros

### Tela de Pedidos
- **Tabela**: Lista todos os pedidos com status
- **Formulário**: Campos para dados do pedido
- **Seleção de Itens**: Múltiplos itens por pedido
- **Controle de Status**: Estados do pedido

### Tela de Estoques
- **Tabela**: Lista todos os estoques por local
- **Formulário**: Campos para dados do estoque
- **Gerenciamento de Itens**: Adicionar/remover itens
- **Localização**: Organização por local

## Logging

Os logs são salvos em:
- Console: Para desenvolvimento
- Arquivo: `logs/simplehealth-frontend.log`

Configuração em `src/main/resources/logback.xml`

## Estrutura de Classes

### Herança de Itens
```
Item (classe base)
├── Medicamento
├── Hospitalar
└── Alimento
```

### Padrão CRUD
Todos os controladores herdam de `AbstractCrudController` que fornece:
- Operações CRUD via API
- Validação de campos
- Gerenciamento de estado da interface
- Notificações entre telas

## Desenvolvimento

### Estrutura de Arquivos

```
src/main/java/br/com/simplehealth/armazenamento/
├── client/          # Aplicações principais
├── controller/      # Controladores JavaFX
├── model/          # Entidades de domínio
├── service/        # Serviços para API REST
└── view/           # ViewModels para JavaFX

src/main/resources/view/  # Arquivos FXML
├── estoque.fxml
├── item.fxml
├── pedido.fxml
└── fornecedor.fxml
```

### Adicionando Novos Módulos

1. **Criar o Modelo**: Adicionar em `model/`
2. **Criar o ViewModel**: Adicionar em `view/`
3. **Criar o Serviço**: Adicionar em `service/`
4. **Criar o Controlador**: Estender `AbstractCrudController`
5. **Criar o FXML**: Adicionar em `resources/view/`
6. **Adicionar Tab**: Modificar `MainApp`

### Padrões de Código

- Usar SLF4J para logging
- Validar entrada do usuário
- Tratar exceções de I/O
- Seguir convenções JavaFX para binding de propriedades
- Usar Jackson annotations para serialização JSON

## Troubleshooting

### Problemas Comuns

1. **Erro de conexão com API**:
   - Verificar se backend está rodando
   - Verificar URL base nos serviços

2. **Campos não aparecem**:
   - Verificar IDs no FXML
   - Verificar binding no controlador

3. **Erro de serialização JSON**:
   - Verificar annotations Jackson
   - Verificar formato de data/hora

## Contribuição

1. Seguir padrões de código existentes
2. Adicionar testes para novas funcionalidades
3. Documentar mudanças no README
4. Usar mensagens de commit descritivas

## Licença

Este projeto é parte do trabalho acadêmico para o curso de Ciência da Computação.