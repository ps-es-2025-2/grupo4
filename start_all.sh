#!/usr/bin/env bash
# Script para iniciar todo o sistema SimpleHealth (Backends + Frontends)
# Autor: Sistema SimpleHealth
# Data: 15/12/2025
# Compatível com Linux e macOS

set -e

echo "============================================"
echo "  SimpleHealth - Iniciando Sistema Completo"
echo "============================================"
echo ""

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 1. Iniciar todos os backends
echo ">>> INICIANDO BACKENDS <<<"
echo ""
bash "$ROOT_DIR/start_back.sh"

echo ""
echo ">>> INICIANDO FRONTENDS <<<"
echo ""

# 2. Iniciar Frontend Cadastro
echo "[1/3] Iniciando Frontend Cadastro..."
cd "$ROOT_DIR/simplehealth-front/simplehealth-front-cadastro"
bash run.sh &
echo "✅ Frontend Cadastro iniciando em background!"
sleep 3

# 3. Iniciar Frontend Agendamento
echo "[2/3] Iniciando Frontend Agendamento..."
cd "$ROOT_DIR/simplehealth-front/simplehealth-front-agendamento"
bash run.sh &
echo "✅ Frontend Agendamento iniciando em background!"
sleep 3

# 4. Iniciar Frontend Estoque
echo "[3/3] Iniciando Frontend Estoque..."
cd "$ROOT_DIR/simplehealth-front/simplehealth-front-estoque"
bash run.sh &
echo "✅ Frontend Estoque iniciando em background!"

cd "$ROOT_DIR"

echo ""
echo "============================================"
echo "  ✅ Sistema SimpleHealth Iniciado!"
echo "============================================"
echo ""
echo "📋 Serviços Backend:"
echo "  • Backend Cadastro:    http://localhost:8081"
echo "  • Backend Agendamento: http://localhost:8082"
echo "  • Backend Estoque:     http://localhost:8083"
echo ""
echo "🖥️  Frontends JavaFX iniciados em background"
echo ""
echo "Para parar todo o sistema, execute: ./stop_all.sh"
echo "Para verificar status, execute: ./status.sh"
echo ""
