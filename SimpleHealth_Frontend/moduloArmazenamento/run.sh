#!/bin/bash

echo "========================================"
echo "SimpleHealth - Módulo Armazenamento"
echo "========================================"
echo

# Navegar para o diretório correto
cd "$(dirname "$0")"

echo "Verificando se o Maven está instalado..."
if ! command -v mvn &> /dev/null; then
    echo "ERRO: Maven não encontrado. Instale o Maven primeiro."
    exit 1
fi

echo "Maven encontrado!"
echo

echo "Compilando e executando o projeto..."
echo "IMPORTANTE: Certifique-se de que o backend Spring Boot esteja rodando na porta 8080"
echo

# Compilar e executar em um comando
mvn clean compile javafx:run

echo
echo "Aplicação finalizada."