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
- [⚙️ 5. Execução Individual dos Módulos](#️-5-execução-individual-dos-módulos)
  - [5.1 Backends](#51-backends)
    - [**Backend Cadastro – Porta 8081**](#backend-cadastro--porta-8081)
    - [**Backend Agendamento – Porta 8082**](#backend-agendamento--porta-8082)
    - [**Backend Estoque – Porta 8083**](#backend-estoque--porta-8083)
  - [5.2 Frontends](#52-frontends)
    - [**Frontend Cadastro**](#frontend-cadastro)
    - [**Frontend Agendamento**](#frontend-agendamento)
    - [**Frontend Estoque**](#frontend-estoque)
  - [5.3 Observações Importantes](#53-observações-importantes)
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

---

# ⚙️ 5. Execução Individual dos Módulos

## 5.1 Backends

### **Backend Cadastro – Porta 8081**

**Requisitos:** PostgreSQL, Cassandra e Redis via Docker.

```bash
cd simplehealth-back/simplehealth-back-cadastro
docker-compose up -d        # Sobe bancos (Postgres, Cassandra, Redis)
mvn spring-boot:run         # Sobe o backend
```

### **Backend Agendamento – Porta 8082**

**Requisitos:** MongoDB e Redis via Docker.

```bash
cd simplehealth-back/simplehealth-back-agendamento
docker-compose up -d        # Sobe MongoDB + Redis
mvn spring-boot:run
```

### **Backend Estoque – Porta 8083**

**Requisitos:** Cassandra e Redis via Docker.

```bash
cd simplehealth-back/simplehealth-back-estoque
docker-compose up -d        # Sobe Cassandra + Redis
mvn spring-boot:run
```

---

## 5.2 Frontends

Todos os frontends usam JavaFX 17 + Maven.
Conexões REST já configuradas via `AppConfig.java`.

### **Frontend Cadastro**

```bash
cd simplehealth-front/simplehealth-front-cadastro
mvn javafx:run
```

### **Frontend Agendamento**

```bash
cd simplehealth-front/simplehealth-front-agendamento
mvn javafx:run
```

### **Frontend Estoque**

```bash
cd simplehealth-front/simplehealth-front-estoque
mvn javafx:run
```

---

## 5.3 Observações Importantes

* **Backends devem estar rodando antes dos frontends**, ou as telas não carregam listagens/consultas.
* Se mudar **portas**, atualize `AppConfig.java` no frontend correspondente.

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