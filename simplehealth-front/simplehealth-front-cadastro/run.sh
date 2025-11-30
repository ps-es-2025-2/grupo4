#!/bin/bash

# Script para executar o SimpleHealth Front Cadastro

echo "========================================"
echo "SimpleHealth Front Cadastro"
echo "========================================"
echo ""

# Verificar se o Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "ERRO: Maven não está instalado!"
    echo "Por favor, instale o Maven antes de continuar."
    exit 1
fi

# Compilar o projeto
echo "Compilando o projeto..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "ERRO: Falha na compilação!"
    exit 1
fi

echo ""
echo "Iniciando aplicação..."
echo ""

# Executar a aplicação
mvn javafx:run
