# SimpleHealth - Sistema Integrado

Sistema completo do SimpleHealth com backend Spring Boot e frontend JavaFX integrados via API REST.

## 🚀 Execução Rápida

### Iniciar Sistema Completo (Backend + Frontend)
```bash
./start-simplehealth-complete.sh
```

### Parar Sistema Completo
```bash
./stop-simplehealth-complete.sh
```

## 📋 Pré-requisitos

- **Java 17+**
- **Maven 3.6+**
- **PostgreSQL** (rodando na porta 5432)

## 🏗️ Estrutura do Projeto

```
grupo4/
├── simplehealth-back/              # Backend Spring Boot
├── SimpleHealth_Frontend/          # Frontend JavaFX
│   └── moduloArmazenamento/
├── start-simplehealth.sh          # Script do backend apenas
├── start-simplehealth-complete.sh # Script completo (recomendado)
└── stop-simplehealth-complete.sh  # Script de parada
```

## 🔧 Execução Individual

### Backend apenas
```bash
./start-simplehealth.sh
```

### Frontend apenas (requer backend rodando)
```bash
cd SimpleHealth_Frontend/moduloArmazenamento
./run.sh
```

## 📡 APIs Disponíveis

O sistema expõe as seguintes APIs REST:

| Endpoint | Descrição |
|----------|-----------|
| `/estoques` | Gerenciamento de estoques |
| `/fornecedores` | Gerenciamento de fornecedores |
| `/pedidos` | Gerenciamento de pedidos |
| `/itens` | Listagem de itens |
| `/medicamentos` | Gerenciamento de medicamentos |
| `/hospitalares` | Gerenciamento de itens hospitalares |
| `/alimentos` | Gerenciamento de alimentos |

## 🌐 Acessos

- **Backend API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Frontend**: Interface JavaFX (abre automaticamente)
- **PostgreSQL**: localhost:5432

## 🔗 Integração Frontend-Backend

O frontend JavaFX está completamente integrado com o backend via HTTP:

- **Services**: Fazem chamadas REST para o backend
- **Controllers**: Tratam respostas da API
- **Models**: Mapeados conforme DTOs do backend
- **Configuração**: URLs centralizadas em `AppConfig.java`

## 📊 Logs e Monitoramento

### Ver logs do backend em tempo real:
```bash
tail -f simplehealth-back/simplehealth.log
```

### Ver status das aplicações:
```bash
# Verificar porta do backend
ss -tlnp | grep :8080

# Verificar processos Java
ps aux | grep java
```

## 🛠️ Desenvolvimento

### Configurar URL da API (opcional):
```bash
export SIMPLEHEALTH_API_URL="http://localhost:8080"
```

### Executar em modo desenvolvimento:
```bash
# Backend
cd simplehealth-back
mvn spring-boot:run

# Frontend (em outro terminal)
cd SimpleHealth_Frontend/moduloArmazenamento  
mvn javafx:run
```

## 🧪 Testando a Integração

1. **Inicie o sistema completo**: `./start-simplehealth-complete.sh`
2. **Acesse o Swagger**: http://localhost:8080/swagger-ui/index.html
3. **Use o frontend**: Interface JavaFX aberta automaticamente
4. **Teste CRUD**: Crie/edite/delete dados via frontend
5. **Verifique API**: Confirme mudanças via Swagger UI

## ❗ Solução de Problemas

### Backend não inicia:
- Verifique se PostgreSQL está rodando: `systemctl status postgresql`
- Verifique se a porta 8080 está livre: `ss -tlnp | grep :8080`

### Frontend não conecta:
- Confirme que o backend está rodando
- Verifique logs do backend: `tail -f simplehealth-back/simplehealth.log`

### Erro de permissão:
```bash
chmod +x *.sh
```

## 📝 Funcionalidades Implementadas

### Backend (Spring Boot):
- ✅ API REST completa (CRUD)
- ✅ Integração PostgreSQL
- ✅ Documentação Swagger
- ✅ Validação de dados
- ✅ Tratamento de erros

### Frontend (JavaFX):
- ✅ Interface gráfica completa
- ✅ Integração HTTP com backend
- ✅ CRUD via API
- ✅ Tratamento de erros de conexão
- ✅ Formulários validados

### Integração:
- ✅ Comunicação HTTP/JSON
- ✅ URLs configuráveis
- ✅ Mapeamento de entidades
- ✅ Tratamento de exceções
- ✅ Scripts de automação