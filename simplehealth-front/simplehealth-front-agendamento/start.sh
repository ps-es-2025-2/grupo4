#!/bin/bash

echo "======================================"
echo "SimpleHealth Front - Agendamento"
echo "======================================"
echo ""

# Verifica se o backend está rodando
echo "Verificando conexão com backend..."
if curl -s http://localhost:8082/agendamento/actuator/health > /dev/null 2>&1; then
    echo "✓ Backend acessível em http://localhost:8082/agendamento"
else
    echo "⚠ ATENÇÃO: Backend não está acessível!"
    echo "Certifique-se de que o backend está rodando na porta 8082"
    echo ""
    read -p "Deseja continuar mesmo assim? (s/n) " -n 1 -r
    echo ""
    if [[ ! $REPLY =~ ^[Ss]$ ]]; then
        exit 1
    fi
fi

echo ""
echo "Iniciando aplicação JavaFX de Agendamento..."
echo ""

mvn clean javafx:run
