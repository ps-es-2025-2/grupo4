#!/usr/bin/env bash
# Script para parar todos os backends do SimpleHealth
# Autor: Sistema SimpleHealth
# Data: 15/12/2025
# Compatível com Linux e macOS

set -e  # Parar em caso de erro

echo "========================================"
echo "  SimpleHealth - Parando Backends"
echo "========================================"
echo ""

# Diretório raiz do projeto
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 1. Parar Backend Estoque
echo "[1/4] Parando Backend Estoque..."
cd "$ROOT_DIR/simplehealth-back/simplehealth-back-estoque"
docker compose -f docker-compose_all.yml down
echo "✅ Backend Estoque parado!"
echo ""

# 2. Parar Backend Agendamento
echo "[2/4] Parando Backend Agendamento..."
cd "$ROOT_DIR/simplehealth-back/simplehealth-back-agendamento"
docker compose -f docker-compose_all.yml down
echo "✅ Backend Agendamento parado!"
echo ""

# 3. Parar Backend Cadastro
echo "[3/4] Parando Backend Cadastro..."
cd "$ROOT_DIR/simplehealth-back/simplehealth-back-cadastro"
docker compose -f docker-compose_all.yml down
echo "✅ Backend Cadastro parado!"
echo ""

# 4. Parar Redis compartilhado
echo "[4/4] Parando Redis compartilhado..."
cd "$ROOT_DIR"
docker compose -f docker-compose_all.yml down
echo "✅ Redis compartilhado parado!"
echo ""

echo "========================================"
echo "  ✅ Todos os backends foram parados!"
echo "========================================"
echo ""
