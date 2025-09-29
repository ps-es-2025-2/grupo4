#!/bin/bash

# Script de Inicialização do SimpleHealth
# Este script automatiza a inicialização do backend SimpleHealth com PostgreSQL e Swagger

set -e  # Parar execução em caso de erro

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para imprimir mensagens coloridas
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Função para verificar se um comando existe
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Função para verificar se um serviço está rodando em uma porta
check_port() {
    local port=$1
    if ss -tlnp | grep -q ":$port "; then
        return 0
    else
        return 1
    fi
}

print_info "=== Iniciando SimpleHealth Backend ==="

# Verificar dependências
print_info "Verificando dependências..."

# Verificar Java
if ! command_exists java; then
    print_error "Java não encontrado. Instale Java 17+"
    exit 1
else
    JAVA_VERSION=$(java -version 2>&1 | grep version | cut -d'"' -f2 | cut -d'.' -f1-2)
    print_success "Java encontrado: $JAVA_VERSION"
fi

# Verificar Maven
if ! command_exists mvn; then
    print_error "Maven não encontrado. Instale Maven 3.6+"
    exit 1
else
    MVN_VERSION=$(mvn --version | head -1 | cut -d' ' -f3)
    print_success "Maven encontrado: $MVN_VERSION"
fi

# Verificar PostgreSQL
if ! command_exists psql; then
    print_error "PostgreSQL não encontrado. Instale PostgreSQL"
    exit 1
else
    PSQL_VERSION=$(psql --version | cut -d' ' -f3)
    print_success "PostgreSQL encontrado: $PSQL_VERSION"
fi

# Verificar se PostgreSQL está rodando
print_info "Verificando serviço PostgreSQL..."
if check_port 5432; then
    print_success "PostgreSQL está rodando na porta 5432"
else
    print_warning "PostgreSQL não está rodando na porta 5432"
    print_info "Tentando iniciar PostgreSQL..."
    
    if command_exists systemctl; then
        sudo systemctl start postgresql
        sleep 3
        if check_port 5432; then
            print_success "PostgreSQL iniciado com sucesso"
        else
            print_error "Falha ao iniciar PostgreSQL"
            exit 1
        fi
    else
        print_error "Não foi possível iniciar PostgreSQL automaticamente"
        exit 1
    fi
fi

# Navegar para diretório do backend
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BACKEND_DIR="$SCRIPT_DIR/simplehealth-back"

if [ ! -d "$BACKEND_DIR" ]; then
    print_error "Diretório do backend não encontrado: $BACKEND_DIR"
    exit 1
fi

cd "$BACKEND_DIR"
print_info "Navegando para: $BACKEND_DIR"

# Verificar se a porta 8080 está livre
if check_port 8080; then
    print_warning "Porta 8080 já está em uso. Tentando parar processo..."
    
    # Tentar encontrar e parar processos na porta 8080
    PID=$(ss -tlnp | grep :8080 | awk '{print $6}' | grep -oP 'pid=\K\d+' | head -1)
    if [ ! -z "$PID" ]; then
        print_info "Parando processo PID: $PID"
        kill $PID 2>/dev/null || sudo kill $PID 2>/dev/null
        sleep 3
    fi
fi

# Verificar configuração do banco
print_info "Verificando configuração do banco de dados..."
if [ -f "src/main/resources/application.properties" ]; then
    if grep -q "localhost:5432" src/main/resources/application.properties; then
        print_success "Configuração do banco está correta"
    else
        print_warning "Ajustando configuração do banco para porta 5432"
        sed -i 's/localhost:5430/localhost:5432/g' src/main/resources/application.properties
    fi
else
    print_error "Arquivo application.properties não encontrado"
    exit 1
fi

# Compilar projeto
print_info "Compilando projeto..."
mvn clean compile -q
if [ $? -eq 0 ]; then
    print_success "Projeto compilado com sucesso"
else
    print_error "Falha na compilação do projeto"
    exit 1
fi

# Executar aplicação
print_info "Iniciando aplicação SimpleHealth..."
print_info "A aplicação será executada em background..."

# Criar log file
LOG_FILE="simplehealth.log"
rm -f "$LOG_FILE"

# Executar aplicação em background
nohup mvn spring-boot:run > "$LOG_FILE" 2>&1 &
APP_PID=$!

print_info "Aplicação iniciada com PID: $APP_PID"
print_info "Aguardando inicialização..."

# Aguardar aplicação inicializar (máximo 60 segundos)
TIMEOUT=60
COUNTER=0
while [ $COUNTER -lt $TIMEOUT ]; do
    if check_port 8080; then
        print_success "Aplicação inicializada na porta 8080!"
        break
    fi
    
    # Verificar se o processo ainda está rodando
    if ! kill -0 $APP_PID 2>/dev/null; then
        print_error "Aplicação falhou ao inicializar. Verifique o log:"
        tail -20 "$LOG_FILE"
        exit 1
    fi
    
    sleep 2
    COUNTER=$((COUNTER + 2))
    echo -n "."
done

echo ""

if [ $COUNTER -ge $TIMEOUT ]; then
    print_error "Timeout na inicialização da aplicação"
    print_info "Últimas linhas do log:"
    tail -20 "$LOG_FILE"
    exit 1
fi

# Verificar endpoints
print_info "Verificando endpoints da API..."

# Aguardar um pouco mais para garantir que todos os endpoints estejam prontos
sleep 5

# Testar endpoint básico
if curl -s -f http://localhost:8080/swagger-ui/index.html > /dev/null; then
    print_success "Swagger UI disponível em: http://localhost:8080/swagger-ui/index.html"
else
    print_warning "Swagger UI pode não estar disponível ainda"
fi

# Exibir informações finais
echo ""
print_success "=== SimpleHealth Backend iniciado com sucesso! ==="
echo ""
print_info "📊 Aplicação: http://localhost:8080"
print_info "📚 Swagger UI: http://localhost:8080/swagger-ui/index.html"
print_info "📄 Log da aplicação: $BACKEND_DIR/$LOG_FILE"
print_info "🔧 PID da aplicação: $APP_PID"
echo ""
print_info "💾 PostgreSQL: Rodando na porta 5432"
print_info "🎯 DBeaver: Disponível para gerenciar o banco"
echo ""
print_info "Para parar a aplicação: kill $APP_PID"
print_info "Para ver logs em tempo real: tail -f $BACKEND_DIR/$LOG_FILE"
echo ""

# Salvar PID para referência
echo $APP_PID > simplehealth.pid
print_info "PID salvo em: $BACKEND_DIR/simplehealth.pid"

print_success "Script concluído! ✅"