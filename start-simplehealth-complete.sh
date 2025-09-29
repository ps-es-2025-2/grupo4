#!/bin/bash

# Script de Inicialização Completo do SimpleHealth
# Este script inicia o backend e frontend do SimpleHealth de forma integrada

set -e  # Parar execução em caso de erro

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
PURPLE='\033[0;35m'
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

print_backend() {
    echo -e "${CYAN}[BACKEND]${NC} $1"
}

print_frontend() {
    echo -e "${PURPLE}[FRONTEND]${NC} $1"
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

# Função para aguardar uma porta ficar disponível
wait_for_port() {
    local port=$1
    local max_attempts=$2
    local attempt=0
    
    while [ $attempt -lt $max_attempts ]; do
        if check_port $port; then
            return 0
        fi
        sleep 2
        attempt=$((attempt + 1))
        echo -n "."
    done
    return 1
}

# Função para cleanup dos processos
cleanup() {
    print_info "Parando aplicações..."
    
    # Parar backend se estiver rodando
    if [ -f "$BACKEND_DIR/simplehealth.pid" ]; then
        BACKEND_PID=$(cat "$BACKEND_DIR/simplehealth.pid")
        if kill -0 $BACKEND_PID 2>/dev/null; then
            print_backend "Parando backend (PID: $BACKEND_PID)"
            kill $BACKEND_PID 2>/dev/null || true
            rm -f "$BACKEND_DIR/simplehealth.pid"
        fi
    fi
    
    # Parar frontend se estiver rodando
    if [ ! -z "$FRONTEND_PID" ] && kill -0 $FRONTEND_PID 2>/dev/null; then
        print_frontend "Parando frontend (PID: $FRONTEND_PID)"
        kill $FRONTEND_PID 2>/dev/null || true
    fi
    
    print_info "Aplicações paradas"
    exit 0
}

# Configurar trap para cleanup
trap cleanup INT TERM

print_info "========================================="
print_info "   SimpleHealth - Sistema Completo      "
print_info "========================================="
echo ""

# Verificar dependências
print_info "Verificando dependências do sistema..."

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

echo ""

# Definir diretórios
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BACKEND_DIR="$SCRIPT_DIR/simplehealth-back"
FRONTEND_DIR="$SCRIPT_DIR/SimpleHealth_Frontend/moduloArmazenamento"

# Verificar diretórios
if [ ! -d "$BACKEND_DIR" ]; then
    print_error "Diretório do backend não encontrado: $BACKEND_DIR"
    exit 1
fi

if [ ! -d "$FRONTEND_DIR" ]; then
    print_error "Diretório do frontend não encontrado: $FRONTEND_DIR"
    exit 1
fi

# ================================
# INICIALIZANDO BACKEND
# ================================

print_backend "Iniciando backend SpringBoot..."

# Verificar se PostgreSQL está rodando
print_backend "Verificando PostgreSQL..."
if check_port 5432; then
    print_success "PostgreSQL está rodando na porta 5432"
else
    print_warning "PostgreSQL não está rodando na porta 5432"
    print_backend "Tentando iniciar PostgreSQL..."
    
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
cd "$BACKEND_DIR"
print_backend "Navegando para: $BACKEND_DIR"

# Verificar se a porta 8080 está livre
if check_port 8080; then
    print_warning "Porta 8080 já está em uso. Tentando parar processo..."
    
    # Tentar encontrar e parar processos na porta 8080
    PID=$(ss -tlnp | grep :8080 | awk '{print $6}' | grep -oP 'pid=\K\d+' | head -1)
    if [ ! -z "$PID" ]; then
        print_backend "Parando processo PID: $PID"
        kill $PID 2>/dev/null || sudo kill $PID 2>/dev/null
        sleep 3
    fi
fi

# Compilar backend
print_backend "Compilando projeto..."
mvn clean compile -q
if [ $? -eq 0 ]; then
    print_success "Backend compilado com sucesso"
else
    print_error "Falha na compilação do backend"
    exit 1
fi

# Executar backend em background
print_backend "Iniciando aplicação backend..."
LOG_FILE="simplehealth.log"
rm -f "$LOG_FILE"

nohup mvn spring-boot:run > "$LOG_FILE" 2>&1 &
BACKEND_PID=$!
echo $BACKEND_PID > simplehealth.pid

print_backend "Backend iniciado com PID: $BACKEND_PID"
print_backend "Aguardando backend inicializar..."

# Aguardar backend inicializar (máximo 60 segundos)
echo -n "Aguardando"
if wait_for_port 8080 30; then
    echo ""
    print_success "Backend inicializado na porta 8080!"
    
    # Aguardar um pouco mais para garantir que todos os endpoints estejam prontos
    sleep 5
    
    # Testar endpoint básico
    if curl -s -f http://localhost:8080/swagger-ui/index.html > /dev/null; then
        print_success "Swagger UI disponível em: http://localhost:8080/swagger-ui/index.html"
    else
        print_warning "Swagger UI pode não estar disponível ainda"
    fi
else
    echo ""
    print_error "Timeout na inicialização do backend"
    print_backend "Últimas linhas do log:"
    tail -20 "$LOG_FILE"
    exit 1
fi

echo ""

# ================================
# INICIALIZANDO FRONTEND
# ================================

print_frontend "Preparando frontend JavaFX..."

# Navegar para diretório do frontend
cd "$FRONTEND_DIR"
print_frontend "Navegando para: $FRONTEND_DIR"

# Compilar frontend
print_frontend "Compilando projeto frontend..."
mvn clean compile -q
if [ $? -eq 0 ]; then
    print_success "Frontend compilado com sucesso"
else
    print_error "Falha na compilação do frontend"
    cleanup
    exit 1
fi

# Executar frontend
print_frontend "Iniciando aplicação frontend..."
print_frontend "A interface gráfica será aberta em uma nova janela..."

# Executar frontend em background e capturar PID
mvn javafx:run &
FRONTEND_PID=$!

print_frontend "Frontend iniciado com PID: $FRONTEND_PID"

echo ""
print_success "========================================="
print_success "   SimpleHealth - Sistema Iniciado!     "
print_success "========================================="
echo ""
print_info "🚀 BACKEND:"
print_info "   📊 Aplicação: http://localhost:8080"
print_info "   📚 Swagger UI: http://localhost:8080/swagger-ui/index.html"
print_info "   📄 Log: $BACKEND_DIR/$LOG_FILE"
print_info "   🔧 PID: $BACKEND_PID"
echo ""
print_info "🖥️  FRONTEND:"
print_info "   📱 Interface JavaFX: Janela aberta"
print_info "   🔧 PID: $FRONTEND_PID"
echo ""
print_info "💾 BANCO DE DADOS:"
print_info "   🐘 PostgreSQL: localhost:5432"
print_info "   🎯 DBeaver: Disponível para gerenciar o banco"
echo ""
print_info "⚡ INTEGRAÇÃO:"
print_info "   🔗 Frontend conectado ao backend via API REST"
print_info "   📡 Comunicação HTTP em localhost:8080"
echo ""
print_warning "Para parar o sistema:"
print_warning "   • Pressione Ctrl+C neste terminal, ou"
print_warning "   • Execute: ./stop-simplehealth.sh"
echo ""
print_info "📊 Para acompanhar logs do backend:"
print_info "   tail -f $BACKEND_DIR/$LOG_FILE"
echo ""

# Aguardar frontend terminar ou Ctrl+C
wait $FRONTEND_PID

print_info "Frontend finalizado"
cleanup