#!/bin/bash

# ========================================
# SimpleHealth Front Cadastro
# Guia Rápido de Inicialização
# ========================================

echo "🏥 SimpleHealth Front Cadastro"
echo "======================================"
echo ""

# Função para verificar se um comando existe
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Função para verificar se uma porta está em uso
port_in_use() {
    lsof -i :"$1" >/dev/null 2>&1
}

echo "📋 Verificando pré-requisitos..."
echo ""

# Verificar Java
if command_exists java; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    echo "✅ Java instalado: versão $JAVA_VERSION"
    if [ "$JAVA_VERSION" -lt 17 ]; then
        echo "⚠️  Aviso: Java 17 ou superior é recomendado"
    fi
else
    echo "❌ Java NÃO instalado!"
    echo "   Por favor, instale Java 17 ou superior"
    exit 1
fi

# Verificar Maven
if command_exists mvn; then
    echo "✅ Maven instalado"
else
    echo "❌ Maven NÃO instalado!"
    echo "   Por favor, instale Maven 3.8 ou superior"
    exit 1
fi

# Verificar Docker (opcional)
if command_exists docker; then
    echo "✅ Docker instalado"
    DOCKER_AVAILABLE=true
else
    echo "ℹ️  Docker não instalado (opcional)"
    DOCKER_AVAILABLE=false
fi

echo ""
echo "======================================"
echo "🔌 Verificando Backend..."
echo "======================================"
echo ""

# Verificar se backend está rodando
if curl -s http://localhost:8081/cadastro/api/cadastro/medicos >/dev/null 2>&1; then
    echo "✅ Backend está rodando em http://localhost:8081/cadastro"
else
    echo "⚠️  Backend NÃO está rodando!"
    echo ""
    echo "Para iniciar o backend:"
    echo ""
    echo "Opção 1 - Docker:"
    echo "  cd ../../simplehealth-back/simplehealth-back-cadastro"
    echo "  docker-compose up -d"
    echo ""
    echo "Opção 2 - Maven:"
    echo "  cd ../../simplehealth-back/simplehealth-back-cadastro"
    echo "  ./mvnw spring-boot:run"
    echo ""
    
    read -p "Deseja continuar mesmo assim? (s/N): " CONTINUE
    if [ "$CONTINUE" != "s" ] && [ "$CONTINUE" != "S" ]; then
        exit 1
    fi
fi

echo ""
echo "======================================"
echo "🚀 Escolha o método de execução:"
echo "======================================"
echo ""
echo "1) Executar com Maven (Recomendado para desenvolvimento)"
echo "2) Executar com Docker (Inclui backend e banco)"
echo "3) Apenas compilar"
echo "4) Sair"
echo ""

read -p "Escolha uma opção (1-4): " OPTION

case $OPTION in
    1)
        echo ""
        echo "🔨 Compilando projeto..."
        mvn clean compile
        
        if [ $? -eq 0 ]; then
            echo ""
            echo "✅ Compilação bem-sucedida!"
            echo "🚀 Iniciando aplicação..."
            echo ""
            mvn javafx:run
        else
            echo "❌ Erro na compilação!"
            exit 1
        fi
        ;;
        
    2)
        if [ "$DOCKER_AVAILABLE" = true ]; then
            echo ""
            echo "🐳 Iniciando containers com Docker Compose..."
            docker-compose up -d
            
            if [ $? -eq 0 ]; then
                echo ""
                echo "✅ Containers iniciados com sucesso!"
                echo ""
                echo "📊 Status dos containers:"
                docker-compose ps
                echo ""
                echo "📋 Para ver logs:"
                echo "   docker-compose logs -f"
                echo ""
                echo "🛑 Para parar:"
                echo "   docker-compose down"
            else
                echo "❌ Erro ao iniciar containers!"
                exit 1
            fi
        else
            echo "❌ Docker não está disponível!"
            exit 1
        fi
        ;;
        
    3)
        echo ""
        echo "🔨 Compilando projeto..."
        mvn clean compile
        
        if [ $? -eq 0 ]; then
            echo ""
            echo "✅ Compilação bem-sucedida!"
            echo ""
            echo "Para executar:"
            echo "  mvn javafx:run"
        else
            echo "❌ Erro na compilação!"
            exit 1
        fi
        ;;
        
    4)
        echo "👋 Até logo!"
        exit 0
        ;;
        
    *)
        echo "❌ Opção inválida!"
        exit 1
        ;;
esac

echo ""
echo "======================================"
echo "📚 Documentação Disponível:"
echo "======================================"
echo ""
echo "  📄 README.md              - Visão geral"
echo "  📖 MANUAL_USO.md          - Manual completo"
echo "  📋 RESUMO_IMPLEMENTACAO.md - Detalhes técnicos"
echo ""
echo "======================================"
