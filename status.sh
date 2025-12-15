#!/usr/bin/env bash
# Script para verificar status do sistema SimpleHealth
# Autor: Sistema SimpleHealth
# Data: 15/12/2025
# Compatível com Linux e macOS

echo "============================================"
echo "  SimpleHealth - Status do Sistema"
echo "============================================"
echo ""

# 1. Status dos Backends (Docker)
echo ">>> BACKENDS (Docker Containers) <<<"
echo ""

if docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | grep -E "simplehealth|redis_shared|postgres|mongodb|cassandra" > /dev/null 2>&1; then
    docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | grep -E "NAMES|simplehealth|redis_shared|postgres|mongodb|cassandra"
    echo ""
    
    # Contar containers rodando
    backendCount=$(docker ps --filter "name=simplehealth" --format "{{.Names}}" | wc -l)
    dbCount=$(($(docker ps --filter "name=postgres_cadastro" --format "{{.Names}}" | wc -l) + \
               $(docker ps --filter "name=mongodb_agendamento" --format "{{.Names}}" | wc -l) + \
               $(docker ps --filter "name=cassandra_estoque" --format "{{.Names}}" | wc -l)))
    redisCount=$(docker ps --filter "name=redis_shared" --format "{{.Names}}" | wc -l)
    
    echo "📊 Resumo Backends:"
    echo "  • Aplicações Backend: $backendCount/3"
    echo "  • Bancos de Dados:    $dbCount/3"
    echo "  • Redis Compartilhado: $redisCount/1"
else
    echo "❌ Nenhum container backend está rodando!"
    echo "   Execute: ./start_back.sh para iniciar os backends"
fi

echo ""
echo ">>> FRONTENDS (JavaFX) <<<"
echo ""

# 2. Status dos Frontends (Processos Java)
# Compatível com Windows (Git Bash) e Linux/Mac
if command -v tasklist.exe &> /dev/null; then
    # Windows
    javaCount=$(tasklist.exe //FI "IMAGENAME eq java.exe" 2>/dev/null | grep -c "java.exe" || echo "0")
    if [ "$javaCount" -gt 0 ]; then
        echo "Processos Java encontrados:"
        tasklist.exe //FI "IMAGENAME eq java.exe" //FO TABLE | grep "java.exe" | head -10
        echo ""
        echo "📊 Total de processos JavaFX: $javaCount"
    else
        echo "❌ Nenhum processo Frontend JavaFX está rodando!"
        echo "   Execute: ./start_all.sh para iniciar todo o sistema"
    fi
else
    # Linux/Mac
    javaCount=$(pgrep -f "simplehealth-front|javafx|java" 2>/dev/null | wc -l)
    if [ $javaCount -gt 0 ]; then
        echo "Processos Java encontrados:"
        ps aux | grep -E "java" | grep -v grep | head -10 | awk '{print "  • PID: " $2 " | CPU: " $3 "% | Mem: " $4 "%"}'
        echo ""
        echo "📊 Total de processos Java: $javaCount"
    else
        echo "❌ Nenhum processo Frontend JavaFX está rodando!"
        echo "   Execute: ./start_all.sh para iniciar todo o sistema"
    fi
fi

echo ""
echo ">>> CONECTIVIDADE <<<"
echo ""

# 3. Testar conectividade com backends
for endpoint in "Backend Cadastro:8081" "Backend Agendamento:8082" "Backend Estoque:8083"; do
    name=$(echo $endpoint | cut -d: -f1)
    port=$(echo $endpoint | cut -d: -f2)
    
    if curl -s --max-time 2 "http://localhost:$port" > /dev/null 2>&1; then
        echo "  ✅ $name: ONLINE"
    else
        echo "  ❌ $name: OFFLINE ou sem resposta"
    fi
done

echo ""
echo "============================================"
echo "  Comandos Úteis"
echo "============================================"
echo "  ./start_all.sh  - Iniciar sistema completo"
echo "  ./start_back.sh - Iniciar apenas backends"
echo "  ./stop_all.sh   - Parar sistema completo"
echo "  ./stop_back.sh  - Parar apenas backends"
echo "  ./status.sh     - Ver este status"
echo ""
