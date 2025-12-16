# Relatório de Instalação e Execução do Projeto

## Sumário

- [1. Preparação do Ambiente](#1-preparação-do-ambiente)
- [2. Backend Cadastro](#2-backend-cadastro)
- [3. Backend Agendamento](#3-backend-agendamento)
- [4. Backend Estoque](#4-backend-estoque)
- [5. Frontend Cadastro](#5-frontend-cadastro)
- [6. Frontend Agendamento](#6-frontend-agendamento)
- [7. Frontend Estoque](#7-frontend-estoque)
- [8. Conclusão](#8-conclusão)

---


Este relatório descreve, de forma detalhada e passo a passo, todo o processo de instalação, configuração e execução do projeto, partindo do zero, conforme os testes realizados.

Todo o procedimento foi executado em uma **máquina virtual com Ubuntu 24.04.3 LTS**, garantindo um ambiente limpo e padronizado para validação.

---

## 1. Preparação do Ambiente

Antes de iniciar a execução do projeto, foram instaladas as seguintes ferramentas:

- **Visual Studio Code** – versão **1.107.0**
- **Apache Maven** – versão **3.8.7**
- **Docker** – versão **29.1.3**

Após a instalação das ferramentas, o **Visual Studio Code** foi aberto e o repositório do **Grupo 4** foi clonado a partir da branch **`review/code`**.

### Evidências

**Imagem 1 – Mudança de diretório**

![Imagem 1](imagens/1.png)

**Imagem 2 – Tentativa de inicialização do Docker**

![Imagem 2](imagens/2.png)

**Imagem 3 – Instalação do Docker**

![Imagem 3](imagens/3.png)

**Imagem 4 – Verificação da versão e inicialização do Docker**

![Imagem 4](imagens/4.png)

**Imagem 5 – Docker em execução com bancos de dados e backend**

![Imagem 5](imagens/5.png)

---

## 2. Backend Cadastro

Inicialmente, houve dificuldades para executar o **Backend de Cadastro**, e o serviço não avançou até a imagem 16. Após ajustes no ambiente e nos procedimentos, a execução foi concluída com sucesso.

### Ajustes Necessários (README)

- O comando correto para inicialização é:
  ```bash
  docker compose up -d
  ```
  e não:
  ```bash
  docker-compose up -d
  ```

- Instalação do **Java 17**:
  ```bash
  sudo apt install openjdk-17-jdk
  ```

- Configuração manual do Java:
  ```bash
  sudo update-alternatives --config java
  sudo update-alternatives --config javac
  ```

- Definição da variável de ambiente **JAVA_HOME**:
  ```bash
  export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
  ```

- Persistência da variável:
  ```bash
  echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
  ```
  Após este passo, foi necessário **reiniciar o VS Code**.

### Evidências

**Imagem 6 – Falha ao usar docker-compose up -d**

![Imagem 6](imagens/6.png)

**Imagem 7 – Instalação do Java 17**

![Imagem 7](imagens/7.png)

**Imagem 8 – Configuração do Java (update-alternatives)**

![Imagem 8](imagens/8.png)

**Imagem 9 – Configuração do javac em modo manual**

![Imagem 9](imagens/9.png)

**Imagem 10 – Definição do JAVA_HOME**

![Imagem 10](imagens/10.png)

**Imagem 11 – Persistência do JAVA_HOME no .bashrc**

![Imagem 11](imagens/11.png)

**Imagem 12 – Reinício e nova tentativa de execução**

![Imagem 12](imagens/12.png)

**Imagem 13 – Encerrando processos do Docker**

![Imagem 13](imagens/13.png)

**Imagem 14 – Reiniciando os containers Docker**

![Imagem 14](imagens/14.png)

**Imagem 15 – Compilação do Maven**

![Imagem 15](imagens/15.png)

**Imagem 23 – Reinício do Docker após falha**

![Imagem 23](imagens/23.png)

**Imagem 24 – Execução do backend com mvn spring-boot:run**

![Imagem 24](imagens/24.png)

**Imagem 25 – Backend Cadastro em execução com sucesso**

![Imagem 25](imagens/25.png)

---

## 3. Backend Agendamento

O **Backend de Agendamento** foi executado com sucesso sem intercorrências relevantes.

- Porta utilizada: **8082**
- Comando:
  ```bash
  mvn spring-boot:run
  ```

**Imagem 16 – Backend Agendamento rodando corretamente**

![Imagem 16](imagens/16.png)

---

## 4. Backend Estoque

O **Backend de Estoque** foi inicializado com sucesso após a subida dos containers Docker e a compilação do Maven.

### Evidências

**Imagem 17 – Inicialização do Docker no backend Estoque**

![Imagem 17](imagens/17.png)

**Imagem 18 – Compilação do Maven**

![Imagem 18](imagens/18.png)

**Imagem 19 – Backend Estoque executado com sucesso**

![Imagem 19](imagens/19.png)

---

## 5. Frontend Cadastro

O **Frontend de Cadastro** foi executado com sucesso, porém **não conseguiu se conectar ao backend**, uma vez que o backend de cadastro ainda apresentava instabilidades no momento do teste. Todavia apos inicializar o backend de Agendamento e Estoque o backend de Cadastro passou a funcionar.

- Comando utilizado:
  ```bash
  mvn javafx:run
  ```

**Imagem 20 – Frontend Cadastro executando**

![Imagem 20](imagens/20.png)

---

## 6. Frontend Agendamento

O **Frontend de Agendamento** foi executado com sucesso absoluto.

- Comando utilizado:
  ```bash
  mvn javafx:run
  ```

**Imagem 21 – Frontend Agendamento em execução**

![Imagem 21](imagens/21.png)

---

## 7. Frontend Estoque

O **Frontend de Estoque** também foi executado com sucesso absoluto.

- Comando utilizado:
  ```bash
  mvn javafx:run
  ```

**Imagem 22 – Frontend Estoque em execução**

![Imagem 22](imagens/22.png)

---

## 8. Conclusão

Após os ajustes necessários no ambiente, especialmente relacionados à versão do Java, configuração do Maven e uso correto do Docker Compose, foi possível executar com sucesso todos os backends e frontends do projeto, validando o processo completo de instalação e execução em um ambiente Ubuntu limpo.
