#!/bin/bash

# Script para parar o SimpleHealth Backend
# Este script para a aplicação SimpleHealth de forma segura

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

print_info "=== Parando SimpleHealth Backend ==="

# Navegar para diretório do backend
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BACKEND_DIR="$SCRIPT_DIR/simplehealth-back"

if [ -d "$BACKEND_DIR" ]; then
    cd "$BACKEND_DIR"
    print_info "Navegando para: $BACKEND_DIR"
fi

# Verificar se há PID salvo
PID_FILE="simplehealth.pid"
if [ -f "$PID_FILE" ]; then
    SAVED_PID=$(cat "$PID_FILE")
    print_info "PID encontrado no arquivo: $SAVED_PID"
    
    # Verificar se o processo ainda está rodando
    if kill -0 $SAVED_PID 2>/dev/null; then
        print_info "Parando processo PID: $SAVED_PID"
        kill $SAVED_PID
        
        # Aguardar processo terminar
        COUNTER=0
        while [ $COUNTER -lt 10 ]; do
            if ! kill -0 $SAVED_PID 2>/dev/null; then
                print_success "Processo parado com sucesso"
                break
            fi
            sleep 1
            COUNTER=$((COUNTER + 1))
            echo -n "."
        done
        
        echo ""
        
        # Se ainda estiver rodando, force kill
        if kill -0 $SAVED_PID 2>/dev/null; then
            print_warning "Forçando parada do processo..."
            kill -9 $SAVED_PID 2>/dev/null
        fi
    else
        print_warning "Processo PID $SAVED_PID não está mais rodando"
    fi
    
    # Remover arquivo PID
    rm -f "$PID_FILE"
    print_info "Arquivo PID removido"
else
    print_warning "Arquivo PID não encontrado"
fi

# Verificar se ainda há processos na porta 8080
if check_port 8080; then
    print_info "Ainda há processo na porta 8080, tentando parar..."
    
    # Encontrar e parar processos na porta 8080
    PIDS=$(ss -tlnp | grep :8080 | awk '{print $6}' | grep -oP 'pid=\K\d+')
    
    if [ ! -z "$PIDS" ]; then
        for PID in $PIDS; do
            print_info "Parando processo PID: $PID"
            kill $PID 2>/dev/null || sudo kill $PID 2>/dev/null
        done
        
        sleep 3
        
        # Verificar novamente
        if check_port 8080; then
            print_warning "Forçando parada de processos restantes..."
            for PID in $PIDS; do
                kill -9 $PID 2>/dev/null || sudo kill -9 $PID 2>/dev/null
            done
        fi
    fi
fi

# Verificação final
sleep 2
if check_port 8080; then
    print_error "Ainda há processos rodando na porta 8080"
    print_info "Processos encontrados:"
    ss -tlnp | grep :8080
else
    print_success "Porta 8080 está livre"
fi

# Listar processos Maven que podem estar rodando
MAVEN_PIDS=$(pgrep -f "mvn.*spring-boot:run" || true)
if [ ! -z "$MAVEN_PIDS" ]; then
    print_warning "Processos Maven ainda rodando:"
    echo "$MAVEN_PIDS"
    print_info "Para parar manualmente: kill $MAVEN_PIDS"
fi

print_success "=== Script de parada concluído! ==="