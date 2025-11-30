@echo off

REM Script para executar o SimpleHealth Front Cadastro

echo ========================================
echo SimpleHealth Front Cadastro
echo ========================================
echo.

REM Verificar se o Maven está instalado
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERRO: Maven não está instalado!
    echo Por favor, instale o Maven antes de continuar.
    pause
    exit /b 1
)

REM Compilar o projeto
echo Compilando o projeto...
call mvn clean compile

if %ERRORLEVEL% NEQ 0 (
    echo ERRO: Falha na compilação!
    pause
    exit /b 1
)

echo.
echo Iniciando aplicação...
echo.

REM Executar a aplicação
call mvn javafx:run

pause
