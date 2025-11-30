#!/bin/bash

###############################################################################
# SimpleHealth - Script de Status
# 
# Verifica o status de todos os serviços do SimpleHealth
###############################################################################

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
MAGENTA='\033[0;35m'
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

# Função para verificar porta
check_port() {
    local name=$1
    local port=$2
    
    if nc -z localhost $port 2>/dev/null; then
        print_color $GREEN "✅ $name (porta $port) - RODANDO"
        return 0
    else
        print_color $RED "❌ $name (porta $port) - PARADO"
        return 1
    fi
}

# Função para verificar container
check_container() {
    local name=$1
    
    if docker ps --filter "name=$name" --format "{{.Names}}" | grep -q "$name"; then
        local status=$(docker inspect --format='{{.State.Status}}' $name 2>/dev/null)
        if [ "$status" = "running" ]; then
            print_color $GREEN "✅ Container $name - RODANDO"
            return 0
        else
            print_color $YELLOW "⚠️  Container $name - STATUS: $status"
            return 1
        fi
    else
        print_color $RED "❌ Container $name - NÃO ENCONTRADO"
        return 1
    fi
}

main() {
    print_header "📊 SIMPLEHEALTH - STATUS DO SISTEMA"
    
    # Verificar se Docker está rodando
    if ! docker info > /dev/null 2>&1; then
        print_color $RED "❌ Docker não está rodando!"
        exit 1
    fi
    
    # Verificar se netcat está instalado
    if ! command -v nc &> /dev/null; then
        print_color $YELLOW "⚠️  netcat não está instalado. Algumas verificações podem falhar."
    fi
    
    # Backends
    print_header "🔧 BACKENDS"
    check_port "Backend Agendamento" 8082
    check_port "Backend Cadastro" 8081
    check_port "Backend Estoque" 8083
    
    # Bancos de Dados
    print_header "💾 BANCOS DE DADOS"
    check_port "MongoDB" 27017
    check_port "PostgreSQL" 5430
    check_port "Redis" 6379
    check_port "ImmuDB" 3322
    check_port "ImmuDB Gateway" 3323
    
    # Containers Frontend
    print_header "🖥️  FRONTENDS (CONTAINERS)"
    check_container "simplehealth-front-agendamento"
    check_container "simplehealth-front-cadastro"
    check_container "simplehealth-front-estoque"
    
    # Containers Backend
    print_header "🔧 BACKENDS (CONTAINERS)"
    check_container "simplehealth-back-agendamento"
    check_container "simplehealth-back-cadastro"
    check_container "simplehealth-back-estoque"
    
    # Lista completa de containers
    print_header "📦 TODOS OS CONTAINERS SIMPLEHEALTH"
    docker ps --filter "name=simplehealth" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | head -20
    
    # Containers de banco de dados
    echo ""
    print_color $MAGENTA "💾 CONTAINERS DE BANCO DE DADOS:"
    docker ps --filter "name=mongo" --filter "name=postgres" --filter "name=redis" --filter "name=immu" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | head -20
    
    # Resumo
    echo ""
    print_header "📝 COMANDOS ÚTEIS"
    print_color $CYAN "  • Iniciar tudo:       ./start-all.sh"
    print_color $CYAN "  • Parar tudo:         ./stop-all.sh"
    print_color $CYAN "  • Ver logs:           docker-compose logs -f [serviço]"
    print_color $CYAN "  • Logs backend:       cd simplehealth-back/[modulo] && docker-compose logs -f"
    print_color $CYAN "  • Logs frontend:      cd simplehealth-front/[modulo] && docker-compose logs -f"
    print_color $CYAN "  • Reiniciar serviço:  docker-compose restart [serviço]"
    echo ""
}

main
