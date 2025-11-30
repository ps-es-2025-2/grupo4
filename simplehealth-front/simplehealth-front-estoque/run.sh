#!/bin/bash

echo "=========================================="
echo "  SimpleHealth - Módulo de Estoque"
echo "=========================================="
echo ""

# Verificar se o backend está rodando
echo "Verificando backend em localhost:8083..."
if curl -s http://localhost:8083/estoque/medicamentos > /dev/null 2>&1; then
    echo "✓ Backend está respondendo"
else
    echo "⚠ ATENÇÃO: Backend não está respondendo em localhost:8083"
    echo "  Certifique-se de iniciar o backend antes de usar o frontend"
    echo ""
fi

echo "Compilando e executando aplicação..."
mvn clean javafx:run
