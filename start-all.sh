#!/bin/bash

###############################################################################
# SimpleHealth - Script de Inicialização Completa
# 
# Este script inicializa todo o sistema SimpleHealth na seguinte ordem:
# 1. Backends (Agendamento, Cadastro, Estoque) - Bancos de dados via Docker
#                                               - Aplicações Spring Boot via Maven
# 2. Frontends (Agendamento, Cadastro, Estoque) - Aplicações JavaFX via Maven
#
# Apenas os bancos de dados são dockerizados
###############################################################################

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
MAGENTA='\033[0;35m'
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

# Função para verificar se um serviço está rodando
check_service() {
    local service_name=$1
    local port=$2
    local max_attempts=30
    local attempt=1
    
    print_color $YELLOW "⏳ Aguardando $service_name na porta $port..."
    
    while [ $attempt -le $max_attempts ]; do
        if nc -z localhost $port 2>/dev/null; then
            print_color $GREEN "✅ $service_name está rodando!"
            return 0
        fi
        sleep 2
        attempt=$((attempt + 1))
    done
    
    print_color $RED "❌ Timeout: $service_name não iniciou na porta $port"
    return 1
}

# Função para verificar se Docker está rodando
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        print_color $RED "❌ Docker não está rodando. Por favor, inicie o Docker primeiro."
        exit 1
    fi
    print_color $GREEN "✅ Docker está rodando"
}

# Função para verificar se netcat está instalado
check_netcat() {
    if ! command -v nc &> /dev/null; then
        print_color $YELLOW "⚠️  netcat não encontrado. Instalando..."
        sudo apt-get update && sudo apt-get install -y netcat
    fi
}

# Função para limpar containers antigos (sempre executa)
cleanup() {
    print_header "🧹 LIMPEZA DE CONTAINERS ANTIGOS"
    
    print_color $YELLOW "🗑️  Parando e removendo containers antigos dos backends..."
    
    # Para e remove containers dos backends (bancos de dados)
    cd simplehealth-back/simplehealth-back-agendamento && docker-compose down 2>/dev/null; cd ../..
    cd simplehealth-back/simplehealth-back-cadastro && docker-compose down 2>/dev/null; cd ../..
    cd simplehealth-back/simplehealth-back-estoque && docker-compose down 2>/dev/null; cd ../..
    
    print_color $GREEN "✅ Limpeza concluída"
}

# Função principal
main() {
    print_header "🏥 SIMPLEHEALTH - INICIALIZAÇÃO COMPLETA DO SISTEMA"
    
    # Verificações preliminares
    check_docker
    check_netcat
    
    # Limpeza opcional
    cleanup
    
    #==========================================================================
    # FASE 1: BACKENDS (Bancos de Dados + Spring Boot)
    #==========================================================================
    print_header "🔧 FASE 1: INICIALIZANDO BACKENDS"
    
    # Backend Agendamento (MongoDB + Spring Boot porta 8082)
    print_color $BLUE "📦 1.1. Iniciando Backend de Agendamento..."
    cd simplehealth-back/simplehealth-back-agendamento
    
    # Inicia apenas o banco de dados
    docker-compose up -d
    print_color $YELLOW "   ├─ MongoDB iniciado"
    check_service "MongoDB" 27017
    
    # Inicia o Spring Boot em background
    print_color $YELLOW "   └─ Iniciando aplicação Spring Boot..."
    ./mvnw spring-boot:run > /tmp/agendamento-backend.log 2>&1 &
    AGENDAMENTO_PID=$!
    echo $AGENDAMENTO_PID > /tmp/agendamento-backend.pid
    check_service "Backend Agendamento" 8082
    cd ../..
    
    # Backend Cadastro (PostgreSQL + Redis + Spring Boot porta 8081)
    print_color $BLUE "📦 1.2. Iniciando Backend de Cadastro..."
    cd simplehealth-back/simplehealth-back-cadastro
    
    # Inicia bancos de dados
    docker-compose up -d
    print_color $YELLOW "   ├─ PostgreSQL iniciado"
    check_service "PostgreSQL" 5430
    print_color $YELLOW "   ├─ Redis (porta 6380) iniciado"
    check_service "Redis Cadastro" 6380
    
    # Inicia o Spring Boot em background
    print_color $YELLOW "   └─ Iniciando aplicação Spring Boot..."
    ./mvnw spring-boot:run > /tmp/cadastro-backend.log 2>&1 &
    CADASTRO_PID=$!
    echo $CADASTRO_PID > /tmp/cadastro-backend.pid
    check_service "Backend Cadastro" 8081
    cd ../..
    
    # Backend Estoque (Cassandra + Redis + Spring Boot porta 8083)
    print_color $BLUE "📦 1.3. Iniciando Backend de Estoque..."
    cd simplehealth-back/simplehealth-back-estoque
    
    # Inicia bancos de dados
    docker-compose up -d
    print_color $YELLOW "   ├─ Cassandra iniciado"
    check_service "Cassandra" 9042
    print_color $YELLOW "   ├─ Redis (porta 6381) iniciado"
    check_service "Redis Estoque" 6381
    
    # Inicia o Spring Boot em background
    print_color $YELLOW "   └─ Iniciando aplicação Spring Boot..."
    ./mvnw spring-boot:run > /tmp/estoque-backend.log 2>&1 &
    ESTOQUE_PID=$!
    echo $ESTOQUE_PID > /tmp/estoque-backend.pid
    check_service "Backend Estoque" 8083
    cd ../..
    
    print_color $GREEN "🎉 Todos os backends estão rodando!"
    print_color $CYAN "📄 Logs disponíveis em:"
    print_color $CYAN "   • Agendamento: /tmp/agendamento-backend.log (PID: $AGENDAMENTO_PID)"
    print_color $CYAN "   • Cadastro:    /tmp/cadastro-backend.log (PID: $CADASTRO_PID)"
    print_color $CYAN "   • Estoque:     /tmp/estoque-backend.log (PID: $ESTOQUE_PID)"
    
    #==========================================================================
    # FASE 2: FRONTENDS
    #==========================================================================
    # FASE 2: FRONTENDS (JavaFX via Maven)
    #==========================================================================
    print_header "🖥️  FASE 2: INICIALIZANDO FRONTENDS JAVAFX"
    
    print_color $YELLOW "💡 Verificando compilação dos frontends com mvn clean compile..."
    echo ""
    
    # Frontend Agendamento - Compilação
    print_color $BLUE "🔧 2.1. Compilando Frontend de Agendamento..."
    cd simplehealth-front/simplehealth-front-agendamento
    if mvn clean compile > /tmp/agendamento-frontend-compile.log 2>&1; then
        print_color $GREEN "✅ Compilação do Frontend Agendamento OK"
    else
        print_color $RED "❌ Erro na compilação do Frontend Agendamento. Verifique /tmp/agendamento-frontend-compile.log"
        exit 1
    fi
    cd ../..
    
    # Frontend Cadastro - Compilação
    print_color $BLUE "🔧 2.2. Compilando Frontend de Cadastro..."
    cd simplehealth-front/simplehealth-front-cadastro
    if mvn clean compile > /tmp/cadastro-frontend-compile.log 2>&1; then
        print_color $GREEN "✅ Compilação do Frontend Cadastro OK"
    else
        print_color $RED "❌ Erro na compilação do Frontend Cadastro. Verifique /tmp/cadastro-frontend-compile.log"
        exit 1
    fi
    cd ../..
    
    # Frontend Estoque - Compilação
    print_color $BLUE "🔧 2.3. Compilando Frontend de Estoque..."
    cd simplehealth-front/simplehealth-front-estoque
    if mvn clean compile > /tmp/estoque-frontend-compile.log 2>&1; then
        print_color $GREEN "✅ Compilação do Frontend Estoque OK"
    else
        print_color $RED "❌ Erro na compilação do Frontend Estoque. Verifique /tmp/estoque-frontend-compile.log"
        exit 1
    fi
    cd ../..
    
    echo ""
    print_color $GREEN "🎉 Todos os frontends compilados com sucesso!"
    echo ""
    print_color $YELLOW "💡 Agora iniciando os frontends JavaFX em background"
    print_color $YELLOW "   As janelas gráficas abrirão automaticamente"
    echo ""
    
    # Frontend Agendamento
    print_color $BLUE "🖥️  2.4. Iniciando Frontend de Agendamento..."
    cd simplehealth-front/simplehealth-front-agendamento
    mvn javafx:run > /tmp/agendamento-frontend.log 2>&1 &
    AGENDAMENTO_FRONT_PID=$!
    echo $AGENDAMENTO_FRONT_PID > /tmp/agendamento-frontend.pid
    print_color $GREEN "✅ Frontend Agendamento iniciado (PID: $AGENDAMENTO_FRONT_PID)"
    cd ../..
    
    sleep 2
    
    # Frontend Cadastro
    print_color $BLUE "🖥️  2.5. Iniciando Frontend de Cadastro..."
    cd simplehealth-front/simplehealth-front-cadastro
    mvn javafx:run > /tmp/cadastro-frontend.log 2>&1 &
    CADASTRO_FRONT_PID=$!
    echo $CADASTRO_FRONT_PID > /tmp/cadastro-frontend.pid
    print_color $GREEN "✅ Frontend Cadastro iniciado (PID: $CADASTRO_FRONT_PID)"
    cd ../..
    
    sleep 2
    
    # Frontend Estoque
    print_color $BLUE "🖥️  2.6. Iniciando Frontend de Estoque..."
    cd simplehealth-front/simplehealth-front-estoque
    mvn javafx:run > /tmp/estoque-frontend.log 2>&1 &
    ESTOQUE_FRONT_PID=$!
    echo $ESTOQUE_FRONT_PID > /tmp/estoque-frontend.pid
    print_color $GREEN "✅ Frontend Estoque iniciado (PID: $ESTOQUE_FRONT_PID)"
    cd ../..
    
    print_color $GREEN "🎉 Todos os frontends foram iniciados!"
    print_color $CYAN "� Logs dos frontends em:"
    print_color $CYAN "   • Agendamento: /tmp/agendamento-frontend.log (PID: $AGENDAMENTO_FRONT_PID)"
    print_color $CYAN "   • Cadastro:    /tmp/cadastro-frontend.log (PID: $CADASTRO_FRONT_PID)"
    print_color $CYAN "   • Estoque:     /tmp/estoque-frontend.log (PID: $ESTOQUE_FRONT_PID)"
    print_color $YELLOW "⏳ Aguarde alguns segundos para as janelas JavaFX aparecerem..."
    
    #==========================================================================
    # RESUMO FINAL
    #==========================================================================
    print_header "📊 RESUMO DO SISTEMA"
    
    echo ""
    print_color $MAGENTA "🔧 BACKENDS:"
    print_color $CYAN "  • Agendamento: http://localhost:8082/agendamento"
    print_color $CYAN "  • Cadastro:    http://localhost:8081/cadastro"
    print_color $CYAN "  • Estoque:     http://localhost:8083/estoque"
    
    echo ""
    print_color $MAGENTA "💾 BANCOS DE DADOS:"
    print_color $CYAN "  • MongoDB:     localhost:27017"
    print_color $CYAN "  • PostgreSQL:  localhost:5430"
    print_color $CYAN "  • Redis (Cadastro): localhost:6380"
    print_color $CYAN "  • Redis (Estoque):  localhost:6381"
    print_color $CYAN "  • Cassandra:   localhost:9042"
    
    echo ""
    print_color $MAGENTA "🖥️  FRONTENDS (JavaFX via Maven):"
    print_color $CYAN "  • Agendamento: Aplicação JavaFX (executando via Maven)"
    print_color $CYAN "  • Cadastro:    Aplicação JavaFX (executando via Maven)"
    print_color $CYAN "  • Estoque:     Aplicação JavaFX (executando via Maven)"
    
    echo ""
    print_color $MAGENTA "📝 COMANDOS ÚTEIS:"
    print_color $CYAN "  • Ver logs:           docker-compose logs -f"
    print_color $CYAN "  • Parar tudo:         ./stop-all.sh"
    print_color $CYAN "  • Ver containers:     docker ps"
    print_color $CYAN "  • Verificar serviços: docker-compose ps"
    
    echo ""
    print_header "✅ SISTEMA SIMPLEHEALTH INICIADO COM SUCESSO!"
    
    print_color $YELLOW "💡 Dica: As interfaces JavaFX devem abrir automaticamente."
    print_color $YELLOW "   Se não abrirem, verifique os logs com: docker-compose logs -f"
    echo ""
}

# Executar script
main
