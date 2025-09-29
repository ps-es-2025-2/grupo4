@echo off
echo ================================
echo SimpleHealth - Modulo Armazenamento
echo ================================
echo.

echo Verificando se o Maven esta instalado...
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Maven nao encontrado. Instale o Maven primeiro.
    pause
    exit /b 1
)

echo Maven encontrado!
echo.

echo Compilando o projeto...
call mvn clean compile
if %errorlevel% neq 0 (
    echo ERRO: Falha na compilacao.
    pause
    exit /b 1
)

echo.
echo Executando a aplicacao...
echo IMPORTANTE: Certifique-se de que o backend Spring Boot esteja rodando na porta 8080
echo.
pause

call mvn javafx:run

echo.
echo Aplicacao finalizada.
pause