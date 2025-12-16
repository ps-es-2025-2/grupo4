#!/usr/bin/env bash
# Script para iniciar todos os backends do SimpleHealth
# Autor: Sistema SimpleHealth
# Data: 15/12/2025
# Compatível com Linux e macOS

set -e  # Parar em caso de erro

echo "========================================"
echo "  SimpleHealth - Iniciando Backends"
echo "========================================"
echo ""

# Diretório raiz do projeto
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 1. Iniciar Redis compartilhado
echo "[1/4] Iniciando Redis compartilhado..."
cd "$ROOT_DIR"
docker compose -f docker-compose_all.yml up -d
if [ $? -ne 0 ]; then
    echo "❌ Erro ao iniciar Redis!"
    exit 1
fi
echo "✅ Redis compartilhado iniciado!"
echo ""
sleep 5

# 2. Iniciar Backend Cadastro
echo "[2/4] Iniciando Backend Cadastro (PostgreSQL + App)..."
cd "$ROOT_DIR/simplehealth-back/simplehealth-back-cadastro"
docker compose -f docker-compose_all.yml up -d --build
if [ $? -ne 0 ]; then
    echo "❌ Erro ao iniciar Backend Cadastro!"
    exit 1
fi
echo "✅ Backend Cadastro iniciado!"
echo ""
sleep 5

# 3. Iniciar Backend Agendamento
echo "[3/4] Iniciando Backend Agendamento (MongoDB + App)..."
cd "$ROOT_DIR/simplehealth-back/simplehealth-back-agendamento"
docker compose -f docker-compose_all.yml up -d --build
if [ $? -ne 0 ]; then
    echo "❌ Erro ao iniciar Backend Agendamento!"
    exit 1
fi
echo "✅ Backend Agendamento iniciado!"
echo ""
sleep 5

# 4. Iniciar Backend Estoque
echo "[4/4] Iniciando Backend Estoque (Cassandra + App)..."
cd "$ROOT_DIR/simplehealth-back/simplehealth-back-estoque"
docker compose -f docker-compose_all.yml up -d --build
if [ $? -ne 0 ]; then
    echo "❌ Erro ao iniciar Backend Estoque!"
    exit 1
fi
echo "✅ Backend Estoque iniciado!"
echo ""

# Voltar ao diretório raiz
cd "$ROOT_DIR"

# Aguardar inicialização completa
echo "⏳ Aguardando inicialização completa dos serviços..."
sleep 10

# Verificar status
echo ""
echo "========================================"
echo "  Status dos Serviços"
echo "========================================"
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | grep -E "simplehealth|redis|postgres|mongodb|cassandra"

echo ""
echo "========================================"
echo "  ✅ Todos os backends iniciados!"
echo "========================================"
echo ""
echo "📋 Serviços disponíveis:"
echo "  • Backend Cadastro:    http://localhost:8081"
echo "  • Backend Agendamento: http://localhost:8082"
echo "  • Backend Estoque:     http://localhost:8083"
echo "  • Redis Compartilhado: localhost:6379"
echo ""
echo "Para parar todos os serviços, execute: ./stop_back.sh"
echo ""
