# 📘 SimpleHealth – Documento Consolidado (v1.0.0)

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

## 4. Padrões de Projeto

Aplicados de forma consistente:

* MVC (Frontend JavaFX)
* Service Layer (front e back)
* Repository Pattern (Spring Data)
* DTOs
* Template Method (`AbstractCrudController`)
* Singleton (`RefreshManager`, `AppConfig`)
* Observer (`RefreshManager`)
* Facade (camada de serviços HTTP)
* Dependency Injection (Spring)
* REST API (Spring Web)

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
