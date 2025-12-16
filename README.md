# 📘 SimpleHealth – Documento Consolidado (v1.0.0)

## Sumário

- [📘 SimpleHealth – Documento Consolidado (v1.0.0)](#-simplehealth--documento-consolidado-v100)
  - [Sumário](#sumário)
  - [1. Visão Geral](#1-visão-geral)
    - [📚 Documentação Completa](#-documentação-completa)
  - [2. Arquitetura](#2-arquitetura)
    - [2.1 Arquitetura Lógica](#21-arquitetura-lógica)
    - [2.2 Arquitetura Física (execução local via Docker)](#22-arquitetura-física-execução-local-via-docker)
  - [3. Módulos](#3-módulos)
    - [3.1 Cadastro (8081)](#31-cadastro-8081)
    - [3.2 Agendamento (8082)](#32-agendamento-8082)
    - [3.3 Estoque (8083)](#33-estoque-8083)
- [4. Modos de Execução](#4-modos-de-execução)
  - [4.1 🚀 Modo Completo via Scripts (Recomendado)](#41--modo-completo-via-scripts-recomendado)
  - [4.2 🐳 Apenas Backends via Docker (Banco + Aplicação)](#42--apenas-backends-via-docker-banco--aplicação)
  - [4.3 💻 Modo Híbrido (Banco no Docker + App Local)](#43--modo-híbrido-banco-no-docker--app-local)
  - [4.4 📊 Verificar Status](#44--verificar-status)
- [⚙️ 5. Configuração e Pré-requisitos](#️-5-configuração-e-pré-requisitos)
  - [5.1 Pré-requisitos](#51-pré-requisitos)
    - [Instalação do Java 17](#instalação-do-java-17)
    - [Configuração do Java](#configuração-do-java)
    - [Definição da variável JAVA\_HOME](#definição-da-variável-java_home)
  - [5.2 Estrutura dos Arquivos Docker Compose](#52-estrutura-dos-arquivos-docker-compose)
    - [`docker-compose.yml` (Modo Híbrido)](#docker-composeyml-modo-híbrido)
    - [`docker-compose_all.yml` (Modo Completo)](#docker-compose_allyml-modo-completo)
  - [5.3 Portas Utilizadas](#53-portas-utilizadas)
  - [5.4 Backends - Detalhamento](#54-backends---detalhamento)
    - [**Backend Cadastro – Porta 8081**](#backend-cadastro--porta-8081)
    - [**Backend Agendamento – Porta 8082**](#backend-agendamento--porta-8082)
    - [**Backend Estoque – Porta 8083**](#backend-estoque--porta-8083)
  - [5.5 Frontends](#55-frontends)
  - [6. Endpoints](#6-endpoints)
    - [Cadastro – `http://localhost:8081/cadastro`](#cadastro--httplocalhost8081cadastro)
    - [Agendamento – `http://localhost:8082/agendamento`](#agendamento--httplocalhost8082agendamento)
    - [Estoque – `http://localhost:8083/estoque`](#estoque--httplocalhost8083estoque)
  - [7. Validações e Testes](#7-validações-e-testes)
    - [Validações](#validações)
    - [Testes REST](#testes-rest)
  - [8. Estrutura do Projeto (versão definitiva, sem repetições)](#8-estrutura-do-projeto-versão-definitiva-sem-repetições)
  - [9. Métricas](#9-métricas)
  - [10. Logs](#10-logs)

---

## 1. Visão Geral

O SimpleHealth é um sistema hospitalar modular baseado em microsserviços, composto por:

* **3 backends Spring Boot** (Cadastro, Agendamento, Estoque)
* **3 frontends JavaFX** independentes
* **3 bancos de dados distintos** adequados ao tipo de informação
* **Comunicação via REST**

**Tecnologias principais:**
Java 17, Spring Boot 3.5.6, JavaFX 17, PostgreSQL, MongoDB, Cassandra, Redis, Docker e Maven.

**Casos de Uso:** 16 (completos).
**Status:** Finalizado – v1.0.0.

### 📚 Documentação Completa

Para informações detalhadas sobre arquitetura, modelagens, casos de uso, diagramas e boas práticas, consulte a **[Documentação Final Consolidada](docs/DOCUMENTACAO_FINAL.md)**.

A documentação inclui:

* Documento de Visão do Projeto
* Casos de Uso detalhados
* Diagramas de Classes, BPM, Arquitetura, Interações e Estados
* Especificações técnicas de implementação
* Design Patterns aplicados
* Boas práticas de desenvolvimento

---

## 2. Arquitetura

### 2.1 Arquitetura Lógica

Cada módulo possui frontend JavaFX + backend Spring Boot + banco dedicado:

* **Cadastro (8081):** PostgreSQL + Cassandra + Redis
* **Agendamento (8082):** MongoDB + Redis
* **Estoque (8083):** Cassandra + Redis

Comunicação exclusivamente via **HTTP/REST**.

### 2.2 Arquitetura Física (execução local via Docker)

* Todos os bancos rodam em containers Docker.
* Os backends são executados localmente via Maven ou Docker.
* Os frontends JavaFX podem ser executados localmente ou via containers com X11.

---

## 3. Módulos

### 3.1 Cadastro (8081)

**Backend:** Spring Boot, PostgreSQL 15, Cassandra 5, Redis.
**Entidades:** Paciente, Médico, Usuário, Convênio.
**Endpoint base:** `/cadastro`.

### 3.2 Agendamento (8082)

**Backend:** Spring Boot, MongoDB, Redis.
**Entidades:** Consulta, Exame, Procedimento, BloqueioAgenda.
**Endpoint base:** `/agendamento`.

### 3.3 Estoque (8083)

**Backend:** Spring Boot, Cassandra 5, Redis.
**Entidades:** Medicamento, Alimento, Material Hospitalar, Fornecedor, Estoque, Pedido, Item.
**Endpoint base:** `/estoque`.

---

# 4. Modos de Execução

O sistema SimpleHealth pode ser executado de **3 formas diferentes**, dependendo da necessidade:

## 4.1 🚀 Modo Completo via Scripts (Recomendado)

**Execute todo o sistema (backends + frontends) com um único comando:**

```bash
# Linux/Mac/Git Bash
sh ./start_all.sh

# Para parar tudo
sh ./stop_all.sh
```

**O que o script faz:**
- Inicia o Redis compartilhado
- Sobe todos os bancos de dados (PostgreSQL, MongoDB, Cassandra)
- Compila e inicia os 3 backends Spring Boot em Docker
- Inicia os 3 frontends JavaFX

## 4.2 🐳 Apenas Backends via Docker (Banco + Aplicação)

**Para rodar cada backend com seu banco de dados em containers Docker:**

```bash
# Na raiz do projeto, primeiro suba o Redis compartilhado
docker compose -f docker-compose_all.yml up -d

# Depois, em cada módulo:
cd simplehealth-back/simplehealth-back-cadastro
docker compose -f docker-compose_all.yml up -d --build

cd ../simplehealth-back-agendamento
docker compose -f docker-compose_all.yml up -d --build

cd ../simplehealth-back-estoque
docker compose -f docker-compose_all.yml up -d --build
```

**Ou use o script auxiliar:**
```bash
sh ./start_back.sh    # Inicia apenas os backends
sh ./stop_back.sh     # Para apenas os backends
```

## 4.3 💻 Modo Híbrido (Banco no Docker + App Local)

**Para desenvolvimento: rode apenas os bancos no Docker e as aplicações localmente com Maven.**

**1. Suba apenas os bancos de dados:**

```bash
# Cadastro - PostgreSQL e Cassandra
cd simplehealth-back/simplehealth-back-cadastro
docker compose up -d

# Agendamento - MongoDB
cd ../simplehealth-back-agendamento
docker compose up -d

# Estoque - Cassandra
cd ../simplehealth-back-estoque
docker compose up -d

# Redis compartilhado (na raiz)
cd ../../
docker compose up -d
```

**2. Execute as aplicações localmente:**

```bash
# Cadastro
cd simplehealth-back/simplehealth-back-cadastro
mvn spring-boot:run

# Agendamento
cd ../simplehealth-back-agendamento
mvn spring-boot:run

# Estoque
cd ../simplehealth-back-estoque
mvn spring-boot:run
```

## 4.4 📊 Verificar Status

```bash
sh ./status.sh    # Mostra o status de todos os containers e processos
```
---

# ⚙️ 5. Configuração e Pré-requisitos

> **⚠️ IMPORTANTE:** Recomendamos usar os scripts automatizados (Seção 4). Em caso de erros, consulte o **[Relatório de Instalação e Execução](docs/Teste%20de%20Instalacao/relatorio_de_instalacao_e_execucao_do_projeto%20(1).md)** para soluções detalhadas.

## 5.1 Pré-requisitos

* **Java 17** (JDK)
* **Maven 3.8+**
* **Docker** e **Docker Compose**
* **Git Bash** (Windows) ou terminal Unix

### Instalação do Java 17

**Linux/WSL:**
```bash
sudo apt install openjdk-17-jdk
```

**Windows:**
Baixe e instale o [OpenJDK 17](https://adoptium.net/)

### Configuração do Java

**Linux:**
```bash
sudo update-alternatives --config java
sudo update-alternatives --config javac
```

### Definição da variável JAVA_HOME

**Linux/Mac:**
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
```

**Windows:**
```powershell
setx JAVA_HOME "C:\Program Files\Eclipse Adoptium\jdk-17.x.x"
```

Após configurar, reinicie o terminal ou IDE.

## 5.2 Estrutura dos Arquivos Docker Compose

O projeto possui 2 tipos de arquivos Docker Compose:

### `docker-compose.yml` (Modo Híbrido)
- Sobe **apenas os bancos de dados**
- Cada módulo tem seu próprio Redis local
- Use quando for rodar a aplicação Spring Boot localmente com Maven

### `docker-compose_all.yml` (Modo Completo)
- **Na raiz:** Sobe apenas o Redis compartilhado
- **Nos módulos:** Sobe banco + aplicação em containers
- Usa o Redis compartilhado da raiz
- Use para execução completa em Docker

## 5.3 Portas Utilizadas

| Serviço | Porta | Descrição |
|---------|-------|-----------|
| Backend Cadastro | 8081 | API REST |
| Backend Agendamento | 8082 | API REST |
| Backend Estoque | 8083 | API REST |
| PostgreSQL | 5432 | Banco Cadastro |
| MongoDB | 27017 | Banco Agendamento |
| Cassandra | 9042 | Banco Estoque |
| Redis Compartilhado | 6379 | Cache e mensageria |

## 5.4 Backends - Detalhamento

### **Backend Cadastro – Porta 8081**

**Banco de dados:** PostgreSQL + Cassandra
**Entidades:** Paciente, Médico, Usuário, Convênio

### **Backend Agendamento – Porta 8082**

**Banco de dados:** MongoDB
**Entidades:** Consulta, Exame, Procedimento, BloqueioAgenda

### **Backend Estoque – Porta 8083**

**Banco de dados:** Cassandra
**Entidades:** Medicamento, Alimento, Material, Fornecedor, Estoque, Pedido

## 5.5 Frontends

Todos os frontends usam **JavaFX 17 + Maven**.
As conexões REST estão pré-configuradas via `AppConfig.java`.

**Execução manual:**

```bash
# Frontend Cadastro
cd simplehealth-front/simplehealth-front-cadastro
mvn javafx:run

# Frontend Agendamento
cd simplehealth-front/simplehealth-front-agendamento
mvn javafx:run

# Frontend Estoque
cd simplehealth-front/simplehealth-front-estoque
mvn javafx:run
```

> **⚠️ Importante:** Os backends devem estar rodando antes de iniciar os frontends.

---

## 6. Endpoints

### Cadastro – `http://localhost:8081/cadastro`

* `/auth/login`
* `/pacientes`
* `/medicos`
* `/usuarios`
* `/convenios`

### Agendamento – `http://localhost:8082/agendamento`

* `/consultas`
* `/exames`
* `/procedimentos`
* `/bloqueios`

### Estoque – `http://localhost:8083/estoque`

* `/medicamentos`
* `/alimentos`
* `/hospitalares`
* `/fornecedores`
* `/estoques`
* `/pedidos`
* `/itens`

---

## 7. Validações e Testes

### Validações

* **Cadastro:** CPF, CRM, emails, obrigatórios
* **Agendamento:** datas válidas, conflitos, bloqueios, disponibilidade
* **Estoque:** CNPJ, quantidades, validade, estoque mínimo

### Testes REST

POST, GET, PUT, DELETE com verificação de integração entre módulos.

---

## 8. Estrutura do Projeto (versão definitiva, sem repetições)

```
grupo4/
├── docs/                         # Documentação formal completa
├── simplehealth-back/            # Backends (3)
├── simplehealth-front/           # Frontends (3)
├── start-all.sh
├── stop-all.sh
├── status.sh
└── README.md
```

---

## 9. Métricas

* **Total estimado:** ~17 mil linhas de código.
* ~64 endpoints REST.
* 6 aplicativos executáveis (3 front + 3 back).
* 3 bancos diferentes integrados.

---

## 10. Logs

Backends: configurados via `application.properties`.

---