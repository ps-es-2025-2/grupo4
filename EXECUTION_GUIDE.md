# SimpleHealth Backend - Guia de Execução

Este guia fornece instruções para executar o backend SimpleHealth com PostgreSQL e acessar a documentação Swagger.

## 🚀 Execução Rápida

### Opção 1: Script Automatizado (Recomendado)

```bash
# Executar o script de inicialização
./start-simplehealth.sh
```

### Opção 2: Parar a Aplicação

```bash
# Executar o script de parada
./stop-simplehealth.sh
```

## 📋 Pré-requisitos

### Dependências Necessárias
- **Java 17+** - Runtime para Spring Boot
- **Maven 3.6+** - Gerenciador de dependências
- **PostgreSQL** - Banco de dados
- **DBeaver** (opcional) - Interface gráfica para PostgreSQL

### Instalação das Dependências

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk maven postgresql postgresql-contrib

# DBeaver (opcional)
sudo snap install dbeaver-ce
```

## 🛠️ Configuração Manual

### 1. PostgreSQL
```bash
# Iniciar serviço PostgreSQL
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Verificar se está rodando na porta 5432
ss -tlnp | grep 5432
```

### 2. Configuração do Banco
O arquivo `simplehealth-back/src/main/resources/application.properties` deve conter:

```properties
spring.application.name=SimpleHealth

spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 3. Execução Manual
```bash
# Navegar para o diretório do backend
cd simplehealth-back

# Compilar o projeto
mvn clean compile

# Executar a aplicação
mvn spring-boot:run
```

## 🌐 Acesso às Interfaces

### Aplicação Principal
- **URL**: http://localhost:8080
- **Status**: Aplicação Spring Boot rodando

### Swagger UI - Documentação da API
- **URL**: http://localhost:8080/swagger-ui/index.html
- **Descrição**: Interface interativa da documentação da API
- **Funcionalidades**:
  - Visualizar todos os endpoints disponíveis
  - Testar APIs diretamente no navegador
  - Ver modelos de dados e parâmetros

### DBeaver - Gerenciamento do Banco
- **Aplicação**: DBeaver Community Edition
- **Comando**: `dbeaver-ce`
- **Configuração de Conexão**:
  - **Host**: localhost
  - **Porta**: 5432
  - **Database**: postgres
  - **Usuário**: postgres
  - **Senha**: postgres

## 📊 Endpoints da API

A aplicação SimpleHealth possui os seguintes controladores:

### 🍎 Alimento Controller
- Gerenciamento de alimentos
- Endpoints para CRUD de alimentos

### 📦 Estoque Controller  
- Controle de estoque
- Gerenciamento de quantidades e localização

### 🏭 Fornecedor Controller
- Cadastro e gestão de fornecedores
- Informações de contato e endereço

### 🏥 Hospitalar Controller
- Itens hospitalares
- Controle de descartabilidade e uso

### 📋 Item Controller
- Gestão geral de itens
- CRUD básico de itens

### 💊 Medicamento Controller
- Controle de medicamentos
- Bula, composição e prescrição

### 📝 Pedido Controller
- Gerenciamento de pedidos
- Status e histórico de pedidos

## 🔧 Resolução de Problemas

### Porta 8080 já está em uso
```bash
# Verificar qual processo está usando a porta
ss -tlnp | grep :8080

# Parar processo específico (substitua PID pelo número do processo)
kill <PID>

# Ou usar o script de parada
./stop-simplehealth.sh
```

### PostgreSQL não está rodando
```bash
# Iniciar PostgreSQL
sudo systemctl start postgresql

# Verificar status
sudo systemctl status postgresql

# Verificar se está ouvindo na porta correta
ss -tlnp | grep 5432
```

### Problemas de compilação Maven
```bash
# Limpar cache Maven
mvn clean

# Recompilar
mvn compile

# Verificar versão do Java
java --version

# Verificar versão do Maven
mvn --version
```

### Problemas de conexão com banco
1. Verificar se PostgreSQL está rodando
2. Confirmar credenciais no `application.properties`
3. Testar conexão manual:
   ```bash
   psql -h localhost -U postgres -d postgres
   ```

## 📝 Logs

### Logs da Aplicação
- **Localização**: `simplehealth-back/simplehealth.log`
- **Visualizar em tempo real**: `tail -f simplehealth-back/simplehealth.log`

### Logs do PostgreSQL
- **Ubuntu**: `/var/log/postgresql/postgresql-<version>-main.log`

## 🔄 Comandos Úteis

```bash
# Verificar processos Java rodando
jps -l

# Verificar processos Maven
pgrep -f "mvn.*spring-boot:run"

# Verificar portas em uso
ss -tlnp | grep -E ":(8080|5432)"

# Monitorar logs da aplicação
tail -f simplehealth-back/simplehealth.log

# Testar conectividade da API
curl http://localhost:8080/swagger-ui/index.html
```

## 📱 Estrutura do Projeto

```
grupo4/
├── start-simplehealth.sh          # Script de inicialização
├── stop-simplehealth.sh           # Script de parada  
├── EXECUTION_GUIDE.md             # Este arquivo
├── simplehealth-back/             # Backend Spring Boot
│   ├── src/main/java/             # Código fonte Java
│   ├── src/main/resources/        # Arquivos de configuração
│   ├── pom.xml                    # Configuração Maven
│   ├── simplehealth.log           # Log da aplicação
│   └── simplehealth.pid           # PID da aplicação
└── simplehealth/                  # Frontend (se aplicável)
```

## ✅ Verificação de Saúde do Sistema

Para verificar se tudo está funcionando:

1. ✅ **PostgreSQL**: `ss -tlnp | grep 5432`
2. ✅ **Aplicação**: `curl -s http://localhost:8080/swagger-ui/index.html`
3. ✅ **Swagger UI**: Acessar http://localhost:8080/swagger-ui/index.html no navegador
4. ✅ **DBeaver**: Conectar com as credenciais configuradas

---

## 📞 Suporte

Se encontrar problemas:

1. Verifique os logs: `tail -f simplehealth-back/simplehealth.log`
2. Execute o script de parada e reinicialização
3. Verifique se todas as dependências estão instaladas
4. Confirme que as portas 8080 e 5432 estão livres

**Desenvolvido para o Projeto SimpleHealth - Grupo 4**