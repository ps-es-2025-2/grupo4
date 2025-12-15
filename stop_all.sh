#!/usr/bin/env bash
# Script para parar todo o sistema SimpleHealth (Backends + Frontends)
# Autor: Sistema SimpleHealth
# Data: 15/12/2025
# Compatível com Linux e macOS

set -e

echo "============================================"
echo "  SimpleHealth - Parando Sistema Completo"
echo "============================================"
echo ""

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 1. Parar processos Java (Frontends JavaFX)
echo "[1/2] Parando Frontends JavaFX..."
# Compatível com Windows (Git Bash) e Linux/Mac
if command -v taskkill.exe &> /dev/null; then
    # Windows
    taskkill.exe //F //IM java.exe //T 2>/dev/null || true
    echo "✅ Frontends JavaFX parados (Windows)!"
else
    # Linux/Mac
    pkill -f "simplehealth-front" 2>/dev/null || true
    pkill -f "javafx" 2>/dev/null || true
    pkill -f "java" 2>/dev/null || true
    echo "✅ Frontends JavaFX parados (Linux/Mac)!"
fi
echo ""

# 2. Parar todos os backends
echo "[2/2] Parando Backends..."
bash "$ROOT_DIR/stop_back.sh"

echo ""
echo "============================================"
echo "  ✅ Sistema SimpleHealth Parado!"
echo "============================================"
echo ""
