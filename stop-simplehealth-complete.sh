#!/bin/bash

# Script para parar o SimpleHealth (Backend e Frontend)

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

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

# Função para verificar se um serviço está rodando em uma porta
check_port() {
    local port=$1
    if ss -tlnp | grep -q ":$port "; then
        return 0
    else
        return 1
    fi
}

print_info "Parando SimpleHealth..."

# Diretórios
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BACKEND_DIR="$SCRIPT_DIR/simplehealth-back"

# Parar backend usando PID salvo
if [ -f "$BACKEND_DIR/simplehealth.pid" ]; then
    PID=$(cat "$BACKEND_DIR/simplehealth.pid")
    if kill -0 $PID 2>/dev/null; then
        print_info "Parando backend (PID: $PID)..."
        kill $PID
        sleep 3
        
        # Verificar se parou
        if ! kill -0 $PID 2>/dev/null; then
            print_success "Backend parado com sucesso"
        else
            print_warning "Forçando parada do backend..."
            kill -9 $PID 2>/dev/null
        fi
    else
        print_info "Backend não está rodando"
    fi
    rm -f "$BACKEND_DIR/simplehealth.pid"
else
    print_info "Arquivo PID do backend não encontrado"
fi

# Verificar e parar processos na porta 8080
if check_port 8080; then
    print_warning "Ainda há processos na porta 8080, tentando parar..."
    PIDS=$(ss -tlnp | grep :8080 | awk '{print $6}' | grep -oP 'pid=\K\d+')
    for PID in $PIDS; do
        print_info "Parando processo PID: $PID"
        kill $PID 2>/dev/null || kill -9 $PID 2>/dev/null
    done
fi

# Parar processos JavaFX/Maven do frontend
JAVA_PIDS=$(pgrep -f "javafx:run\|moduloArmazenamento" || true)
if [ ! -z "$JAVA_PIDS" ]; then
    print_info "Parando processos do frontend..."
    for PID in $JAVA_PIDS; do
        print_info "Parando frontend (PID: $PID)"
        kill $PID 2>/dev/null || kill -9 $PID 2>/dev/null
    done
    print_success "Frontend parado"
else
    print_info "Frontend não está rodando"
fi

# Verificação final
sleep 2
if check_port 8080; then
    print_error "Ainda há processos na porta 8080"
    ss -tlnp | grep :8080
else
    print_success "Porta 8080 liberada"
fi

print_success "SimpleHealth parado completamente!"