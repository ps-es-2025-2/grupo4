#!/bin/bash

###############################################################################
# SimpleHealth - Script de Parada Completa
# 
# Este script para todos os serviços do SimpleHealth
###############################################################################

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Função para imprimir com cor
print_color() {
    color=$1
    shift
    echo -e "${color}$@${NC}"
}

# Função para imprimir cabeçalho
print_header() {
    echo ""
    print_color $CYAN "========================================================================"
    print_color $CYAN "$1"
    print_color $CYAN "========================================================================"
    echo ""
}

main() {
    print_header "🛑 SIMPLEHEALTH - PARANDO TODOS OS SERVIÇOS"
    
    # Para processos Spring Boot dos backends
    print_color $BLUE "🔧 Parando processos Spring Boot..."
    
    if [ -f /tmp/agendamento-backend.pid ]; then
        PID=$(cat /tmp/agendamento-backend.pid)
        print_color $YELLOW "   ├─ Parando Agendamento (PID: $PID)..."
        kill $PID 2>/dev/null || print_color $RED "   │  ⚠️  Processo já parado"
        rm /tmp/agendamento-backend.pid
    fi
    
    if [ -f /tmp/cadastro-backend.pid ]; then
        PID=$(cat /tmp/cadastro-backend.pid)
        print_color $YELLOW "   ├─ Parando Cadastro (PID: $PID)..."
        kill $PID 2>/dev/null || print_color $RED "   │  ⚠️  Processo já parado"
        rm /tmp/cadastro-backend.pid
    fi
    
    if [ -f /tmp/estoque-backend.pid ]; then
        PID=$(cat /tmp/estoque-backend.pid)
        print_color $YELLOW "   └─ Parando Estoque (PID: $PID)..."
        kill $PID 2>/dev/null || print_color $RED "      ⚠️  Processo já parado"
        rm /tmp/estoque-backend.pid
    fi
    
    # Para processos JavaFX dos frontends
    print_color $BLUE "🖥️  Parando processos JavaFX..."
    
    if [ -f /tmp/agendamento-frontend.pid ]; then
        PID=$(cat /tmp/agendamento-frontend.pid)
        print_color $YELLOW "   ├─ Parando Frontend Agendamento (PID: $PID)..."
        kill $PID 2>/dev/null || print_color $RED "   │  ⚠️  Processo já parado"
        rm /tmp/agendamento-frontend.pid
    fi
    
    if [ -f /tmp/cadastro-frontend.pid ]; then
        PID=$(cat /tmp/cadastro-frontend.pid)
        print_color $YELLOW "   ├─ Parando Frontend Cadastro (PID: $PID)..."
        kill $PID 2>/dev/null || print_color $RED "   │  ⚠️  Processo já parado"
        rm /tmp/cadastro-frontend.pid
    fi
    
    if [ -f /tmp/estoque-frontend.pid ]; then
        PID=$(cat /tmp/estoque-frontend.pid)
        print_color $YELLOW "   └─ Parando Frontend Estoque (PID: $PID)..."
        kill $PID 2>/dev/null || print_color $RED "      ⚠️  Processo já parado"
        rm /tmp/estoque-frontend.pid
    fi
    
    # Para bancos de dados dos backends
    print_color $BLUE "💾 Parando Bancos de Dados..."
    cd simplehealth-back/simplehealth-back-agendamento && docker-compose down 2>/dev/null; cd ../..
    cd simplehealth-back/simplehealth-back-cadastro && docker-compose down 2>/dev/null; cd ../..
    cd simplehealth-back/simplehealth-back-estoque && docker-compose down 2>/dev/null; cd ../..
    
    print_header "✅ TODOS OS SERVIÇOS FORAM PARADOS"
    
    print_color $YELLOW "💡 Para iniciar novamente, execute: ./start-all.sh"
    echo ""
}

main
