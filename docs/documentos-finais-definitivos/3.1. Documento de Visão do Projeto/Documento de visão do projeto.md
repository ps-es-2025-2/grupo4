# Documento de Visão do Projeto - SimpleHealth

> Sistema Integrado de Gestão Hospitalar

---

## 📋 Sumário

0. [Notas Técnicas - Correções de Discrepâncias](#notas-técnicas---correções-de-discrepâncias)
1. [Introdução](#1-introdução)
   - 1.1 [Propósito do Documento](#11-propósito-do-documento)
   - 1.2 [Escopo do Sistema](#12-escopo-do-sistema)
   - 1.3 [Contexto do Projeto](#13-contexto-do-projeto)
2. [Posicionamento](#2-posicionamento)
   - 2.1 [Oportunidade de Negócio](#21-oportunidade-de-negócio)
   - 2.2 [Descrição do Problema](#22-descrição-do-problema)
3. [Descrição dos Stakeholders e Usuários](#3-descrição-dos-stakeholders-e-usuários)
4. [Visão Geral do Produto](#4-visão-geral-do-produto)
5. [Recursos do Produto](#5-recursos-do-produto)
6. [Restrições](#6-restrições)
7. [Requisitos de Qualidade](#7-requisitos-de-qualidade)
8. [Modelo de Inovação](#8-modelo-de-inovação)

---

## Notas Técnicas - Correções de Discrepâncias

Este documento foi atualizado para corrigir discrepâncias identificadas entre a documentação e a implementação real do sistema. Abaixo estão as correções aplicadas:

### Discrepância 4.3: Usuario ↔ PerfilUsuario (Enum Incompleto)

**Discrepância:** A documentação mostrava enum `EPerfilUsuario` com apenas 3-4 valores (MEDICO, SECRETARIA, GESTOR), mas o backend possui 5 valores incluindo FINANCEIRO e TESOURARIA.

**Mudança Feita:** Adicionados os perfis FINANCEIRO e TESOURARIA no enum de perfis de usuário em todas as ocorrências deste documento.

**Justificativa:** Sincronizar documentação com implementação real do backend (EPerfilUsuario.java tem 5 valores).

**Documento Detalhado:** [📄 CORRECAO_DISCREPANCIA_4.3.md](../../Correções%20de%20Alinhamento/CORRECAO_DISCREPANCIA_4.3.md)

---

Para consultar todas as correções de discrepâncias do projeto, acesse o [📑 Sumário de Correções](../../Correções%20de%20Alinhamento/SUMARIO_CORRECAO_DISCREPANCIA.md).

---

## 1. Introdução

### 1.1 Propósito do Documento

Este documento apresenta a visão geral do sistema SimpleHealth, definindo o problema a ser resolvido, os stakeholders envolvidos, as funcionalidades principais e as restrições do projeto.

### 1.2 Escopo do Sistema

O SimpleHealth é um sistema de gestão hospitalar dividido em **três módulos independentes**:

1. **Módulo de Cadastro**: Gerenciamento de pacientes, médicos, usuários e convênios
2. **Módulo de Agendamento**: Gestão de consultas, exames, procedimentos e bloqueios de agenda
3. **Módulo de Estoque**: Controle de medicamentos, alimentos, materiais hospitalares, fornecedores e pedidos

### 1.3 Contexto do Projeto

**Natureza**: Projeto acadêmico para a disciplina de Projeto de Software, inspirado em problemas reais de clínicas e hospitais de pequeno porte no Brasil.

**Objetivos Acadêmicos**:
- Aplicar arquitetura de microsserviços
- Implementar persistência poliglota (múltiplos bancos de dados)
- Utilizar padrões de projeto consolidados
- Desenvolver sistema completo (frontend + backend + banco de dados) 
---

## 2. Posicionamento

### 2.1 Oportunidade de Negócio

Clínicas e hospitais de pequeno porte no Brasil frequentemente utilizam sistemas fragmentados ou processos manuais (planilhas, agendas físicas) para gestão de cadastros, agendamentos e estoque. Esta descentralização gera:

- **Ineficiência operacional**: Tempo perdido buscando informações em múltiplos sistemas
- **Erros humanos**: Double-booking de consultas, estoque negativo não detectado
- **Falta de integração**: Impossibilidade de cruzar dados entre módulos
- **Custos elevados**: Desperdício de medicamentos vencidos, compras emergenciais

### 2.2 Descrição do Problema

#### 2.2.1 Módulo de Cadastro

**Problema**: Dados de pacientes, médicos e convênios dispersos em planilhas ou sistemas não integrados.

**Impacto**:
- Dados duplicados ou inconsistentes
- Dificuldade em localizar informações rapidamente
- Falta de histórico consolidado de pacientes

#### 2.2.2 Módulo de Agendamento

**Problema**: Agendamento manual de consultas, exames e procedimentos via telefone ou agendas compartilhadas.

**Impacto**:
- Agendamentos conflitantes (double-booking)
- Alta taxa de não comparecimento (sem lembretes automatizados)
- Má utilização de recursos (salas, equipamentos, profissionais)
- Dificuldade em gerenciar bloqueios de agenda

#### 2.2.3 Módulo de Estoque

**Problema**: Controle manual de medicamentos, materiais hospitalares e alimentos.

**Impacto**:
- Estoque negativo descoberto apenas na dispensação
- Perda de itens por validade vencida
- Falta de rastreabilidade (lote, fornecedor, data de entrada)
- Dificuldade em gerar alertas de estoque mínimo

> **⚠️ NOTA SOBRE ESCOPO**: O projeto original contemplava um quarto módulo (Gestão Financeira) que **não foi implementado devido à redução de escopo** para viabilizar a entrega no prazo acadêmico. O MVP atual foca nos três módulos principais: Cadastro, Agendamento e Estoque.

---

## 3. Descrição dos Stakeholders e Usuários

### 3.1 Stakeholders Primários

| Stakeholder | Interesse | Influência |
|-------------|-----------|------------|
| **Recepcionistas/Secretárias** | Agilizar cadastros e agendamentos | Alta |
| **Médicos** | Visualizar agenda e histórico de pacientes | Média |
| **Farmacêuticos/Técnicos** | Controlar estoque de forma precisa | Alta |
| **Gestores Administrativos** | Relatórios gerenciais | Média |

### 3.2 Perfis de Usuário

#### 3.2.1 Recepcionista/Secretária

**Responsabilidades**:
- Cadastrar pacientes, médicos e convênios
- Agendar consultas, exames e procedimentos
- Gerenciar bloqueios de agenda
- Confirmar e remarcar agendamentos

**Características**:
- Usuário frequente do sistema (uso diário)
- Necessita de interface intuitiva e rápida
- Perfil: `SECRETARIA`

#### 3.2.2 Médico

**Responsabilidades**:
- Visualizar sua própria agenda
- Registrar bloqueios de agenda (férias, folgas)
- Consultar histórico de pacientes

**Características**:
- Usuário ocasional do sistema
- Prioriza rapidez e facilidade de uso
- Perfil: `MEDICO`

#### 3.2.3 Farmacêutico/Técnico de Estoque

**Responsabilidades**:
- Cadastrar medicamentos, alimentos e materiais hospitalares
- Registrar entradas (pedidos) e saídas de estoque
- Gerenciar fornecedores
- Monitorar alertas de estoque mínimo e validade

**Características**:
- Usuário frequente do módulo de estoque
- Necessita de controle rigoroso de lotes e validades
- Perfil: `FARMACEUTICO`

#### 3.2.4 Gestor Administrativo

**Responsabilidades**:
- Criar usuários do sistema
- Visualizar relatórios consolidados
- Auditar operações

**Características**:
- Usuário ocasional
- Acesso privilegiado a todos os módulos
- Perfil: `ADMINISTRADOR`

---

## 4. Visão Geral do Produto

### 4.1 Perspectiva do Produto

O SimpleHealth é um **sistema desktop** desenvolvido com arquitetura de microsserviços, onde cada módulo possui:

- **Frontend Desktop**: Interface JavaFX executada localmente
- **Backend REST**: Servidor Spring Boot independente
- **Banco de Dados**: Tecnologia específica para cada domínio (persistência poliglota)

**Arquitetura Simplificada**:

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   Frontend      │     │   Frontend      │     │   Frontend      │
│   Cadastro      │     │  Agendamento    │     │   Estoque       │
│   (JavaFX)      │     │   (JavaFX)      │     │   (JavaFX)      │
└────────┬────────┘     └────────┬────────┘     └────────┬────────┘
         │ HTTP REST            │ HTTP REST            │ HTTP REST
         ▼                      ▼                      ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   Backend       │     │   Backend       │     │   Backend       │
│   Cadastro      │     │  Agendamento    │     │   Estoque       │
│ (Spring Boot)   │     │ (Spring Boot)   │     │ (Spring Boot)   │
│   Porta 8081    │     │   Porta 8082    │     │   Porta 8083    │
└────────┬────────┘     └────────┬────────┘     └────────┬────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  PostgreSQL +   │     │   MongoDB +     │     │  Cassandra +    │
│     Redis       │     │     Redis       │     │     Redis       │
│                 │     │                 │     │                 │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

> **⚠️ NOTA - Redução de Escopo:** Cassandra foi removido do módulo de Cadastro (Discrepância 1.2). O módulo usa apenas PostgreSQL + Redis.

### 4.2 Resumo das Capacidades

| Capacidade | Benefício para o Usuário | Módulo Responsável |
|------------|--------------------------|--------------------|
| Cadastro centralizado de pessoas | Dados únicos e consistentes | Cadastro |
| Agendamento inteligente | Evita conflitos e otimiza agenda | Agendamento |
| Controle de estoque em tempo real | Reduz perdas e garante disponibilidade | Estoque |
| Integração entre módulos | Visão completa do negócio | Todos |

### 4.3 Suposições e Dependências

**Suposições**:
- Clínica possui infraestrutura mínima (computadores com Java 17+)
- Conexão de rede local entre computadores
- Usuários receberão treinamento básico antes do uso

**Dependências Tecnológicas**:
- Java 17 ou superior
- Maven 3.9.x
- Docker (para bancos de dados)
- Sistema operacional: Windows, Linux ou macOS

---

## 5. Recursos do Produto

### 5.1 Módulo de Cadastro (✅ Implementado)

**Funcionalidades**:

1. **UC01 - Autenticação de Usuário**
   - Login com usuário e senha
   - Controle de perfis de acesso
   - Sessão de usuário ativa

2. **UC02 - Cadastrar Paciente**
   - Dados pessoais completos (nome, CPF, RG, data de nascimento)
   - Endereço e contatos
   - Convênio associado (opcional)
   - Validação de CPF único

3. **UC03 - Cadastrar Médico**
   - Dados pessoais e profissionais
   - CRM e especialidade
   - Horário de atendimento

4. **UC04 - Cadastrar Usuário do Sistema**
   - Criação de usuários para acesso ao sistema
   - Definição de perfis (ADMINISTRADOR, SECRETARIA, MEDICO, FARMACEUTICO, FINANCEIRO, TESOURARIA)
   - Associação com pessoa (médico ou funcionário)

5. **UC05 - Cadastrar Convênio Médico**
   - Nome, CNPJ e tipo do convênio
   - Tabela de preços (planejado para versão futura)

**Tecnologias**:
- Backend: Spring Boot 3.5.6, Java 17
- Banco de Dados Principal: PostgreSQL 16
- Comunicação entre Módulos: Redis 7 (Pub/Sub)
- Frontend: JavaFX 17

> **📝 Nota (Discrepância 1.3):** Redis implementado apenas para Pub/Sub, não para cache.

### 5.2 Módulo de Agendamento (✅ Implementado)

**Funcionalidades**:

1. **UC06 - Agendar Consulta**
   - Seleção de paciente e médico
   - Data e horário
   - Tipo de consulta (primeira, retorno, rotina)
   - Modalidade (presencial ou remota)
   - Validação de disponibilidade

2. **UC07 - Agendar Exame**
   - Similar à consulta, com campos específicos para exames
   - Preparação necessária (ex: jejum)

3. **UC08 - Agendar Procedimento**
   - Procedimentos médicos que podem requerer itens do estoque
   - Associação com materiais necessários (planejado)

4. **UC09 - Gerenciar Bloqueios de Agenda**
   - Médico ou secretária pode bloquear horários
   - Motivo do bloqueio (férias, reunião, indisponibilidade)
   - Período de bloqueio

**Funcionalidades Planejadas (❌ Não Implementadas por Redução de Escopo)**:
- ❌ Encaixes de emergência com permissão especial
- ❌ Lembretes automáticos por SMS/Email
- ❌ Confirmação de presença via aplicativo
- ❌ Integração com calendário do médico

**Tecnologias**:
- Backend: Spring Boot 3.5.6, Java 17
- Banco de Dados: MongoDB 6.0
- Cache: Redis 7
- Frontend: JavaFX 17

### 5.3 Módulo de Estoque (✅ Implementado)

**Funcionalidades**:

1. **UC10 - Cadastrar Medicamento**
   - Nome, descrição, dosagem
   - Tarja (vermelha, preta, livre)
   - Campos específicos para medicamentos controlados

2. **UC11 - Cadastrar Alimento**
   - Nome, descrição
   - Informações nutricionais
   - Alergênicos

3. **UC12 - Cadastrar Material Hospitalar**
   - Nome, descrição
   - Tipo de material
   - Descarte especial (se aplicável)

4. **UC13 - Cadastrar Fornecedor**
   - Razão social, CNPJ
   - Contatos e endereço
   - Histórico de fornecimento

5. **UC14 - Gerenciar Localizações de Estoque**
   - Setores e locais de armazenamento
   - Controle de distribuição por localização

6. **UC15 - Gerenciar Pedidos**
   - Criação de pedidos de compra
   - Associação com fornecedor
   - Status do pedido (pendente, aprovado, recebido)

7. **UC16 - Visualizar Itens Consolidados**
   - Visão consolidada de todos os itens em estoque
   - Busca e filtros
   - Quantidade total disponível

**Funcionalidades Planejadas (❌ Não Implementadas por Redução de Escopo)**:
- ❌ Entrada de Nota Fiscal (registro automático de lotes)
- ❌ Baixa automática de estoque ao dispensar medicamento
- ❌ Alertas automáticos de estoque mínimo
- ❌ Alertas de validade próxima
- ❌ Relatórios de movimentação
- ❌ Inventário periódico

**Tecnologias**:
- Backend: Spring Boot 3.5.6, Java 17
- Banco de Dados: Cassandra 5 (para movimentações e histórico)
- Cache: Redis 7
- Frontend: JavaFX 17

> **📝 Nota:** O módulo de Estoque mantém Cassandra pois foi implementado com este banco.

### 5.4 Módulo de Gestão Financeira (❌ NÃO IMPLEMENTADO)

> **⚠️ ESCOPO REDUZIDO**: Este módulo estava planejado na concepção inicial do projeto, mas **não foi implementado** devido a restrições de prazo do projeto acadêmico. A decisão foi priorizar a entrega completa e funcional dos três módulos principais.

**Funcionalidades Planejadas** (para versão futura):
- Contas a Receber (faturamento de consultas/exames)
- Contas a Pagar (despesas e fornecedores)
- Conciliação bancária
- Repasse médico
- Gestão de glosas de convênios
- Relatórios financeiros

---

### 6.1 Restrições Tecnológicas

- **Plataforma**: Sistema desktop (JavaFX 17)
- **Conectividade**: Requer rede local para acesso aos backends
- **Infraestrutura**: Docker necessário para bancos de dados
- **Java**: Versão 17 ou superior obrigatória

### 6.2 Restrições de Conformidade

- **Medicamentos Controlados**: Seguir regulamentações de controle (campo prescrição, tarja)
- **LGPD**: Dados pessoais devem ser protegidos (planejado para versão futura)

### 6.3 Restrições de Integração

- **Dados**: Migração de dados históricos necessária antes da implantação
- **Cadastro Prévio**: Pacientes e médicos devem estar cadastrados antes de agendar
- **Bloqueios**: Agendamentos devem respeitar bloqueios de agenda registrados

---

## 7. Requisitos de Qualidade

### 7.1 Usabilidade

- Interface intuitiva para usuários com conhecimento básico de computação
- Tempo médio para realizar operações simples (cadastro, agendamento): < 60 segundos
- Feedback visual claro para operações (sucesso, erro, validação)

### 7.2 Confiabilidade

- Sistema deve estar disponível 99% do tempo durante horário comercial
- Dados críticos (cadastros, agendamentos, estoque) devem ter backup diário

### 7.3 Desempenho

- Tempo de resposta para consultas simples: < 2 segundos
- Suporte a pelo menos 10 usuários simultâneos por módulo
- Banco de dados deve suportar crescimento de 10x em 2 anos

### 7.4 Segurança

- Autenticação obrigatória (login/senha)
- Controle de acesso por perfil (ADMINISTRADOR, SECRETARIA, MEDICO, FARMACEUTICO, FINANCEIRO, TESOURARIA)
- Dados sensíveis protegidos no banco de dados

---

## 8. Modelo de Inovação

### 8.1 Persistência Poliglota

O SimpleHealth adota **persistência poliglota**, uma abordagem onde cada módulo utiliza o banco de dados mais adequado às suas necessidades específicas:

| Módulo | Banco Principal | Justificativa |
|--------|----------------|---------------|
| **Cadastro** | PostgreSQL 16 | Consistência ACID para dados mestres, queries relacionais complexas |
| **Agendamento** | MongoDB 6.0 | Flexibilidade de schema, documentos com estruturas variadas |
| **Estoque** | Cassandra 5 | Alta disponibilidade, write-heavy workload, time-series |
| **Comunicação** | Redis 7 | Pub/Sub entre módulos (event-driven) |

> **⚠️ Notas de Discrepâncias:**
> - **1.2:** Auditoria com Cassandra foi removida do Cadastro
> - **1.3:** Redis no Cadastro é usado apenas para Pub/Sub, não cache

### 8.2 Arquitetura de Microsserviços

Cada módulo funciona de forma **independente**:

- **Escalabilidade**: Módulos podem escalar separadamente conforme demanda
- **Tecnologia**: Cada módulo usa a stack mais adequada
- **Resiliência**: Falha em um módulo não afeta os demais
- **Desenvolvimento**: Times podem trabalhar em paralelo

### 8.3 Integração via APIs REST

- Comunicação entre frontends e backends via HTTP/REST (JSON)
- APIs documentadas com Swagger/OpenAPI
- Cada backend expõe suas próprias APIs independentes

### 8.4 Frontend Desktop Moderno

- **JavaFX 17**: Interface desktop moderna e responsiva
- **Padrão MVC**: Separação clara entre View (FXML), Controller e Model
- **Padrões de Projeto**: Template Method, Observer, Singleton aplicados

---

## 9. Escopo Implementado vs Planejado

### 9.1 MVP Entregue (✅ Implementado)

**Módulos Completos**:
1. ✅ Módulo de Cadastro (5 casos de uso)
2. ✅ Módulo de Agendamento (4 casos de uso)
3. ✅ Módulo de Estoque (7 casos de uso)

**Total**: 16 casos de uso implementados e funcionais

### 9.2 Funcionalidades Reduzidas (❌ Não Implementadas)

Por decisão de **redução de escopo** para viabilizar entrega no prazo acadêmico:

**Módulo Financeiro Completo**:
- ❌ Contas a Receber
- ❌ Contas a Pagar
- ❌ Conciliação Bancária
- ❌ Repasse Médico
- ❌ Gestão de Glosas

**Integrações Avançadas**:
- ❌ Lembretes automáticos (SMS/Email)
- ❌ Portaldo Paciente (web/mobile)
- ❌ Integração com laboratórios externos
- ❌ Emissão de Nota Fiscal Eletrônica

**Relatórios Gerenciais**:
- ❌ Dashboards executivos
- ❌ Relatórios financeiros consolidados
- ❌ Business Intelligence

### 9.3 Roadmap Futuro

**Fase 2** (3-6 meses):
- Módulo Financeiro completo
- Portal do Paciente (web)
- Notificações automáticas
- Relatórios gerenciais

**Fase 3** (6-12 meses):
- Prontuário Eletrônico
- Integração com laboratórios
- App mobile para pacientes
- BI e Analytics

---

## 10. Conclusão

O SimpleHealth MVP representa um **sistema funcional e completo** para os três módulos principais: Cadastro, Agendamento e Estoque. A decisão de reduzir o escopo (removendo o módulo financeiro) foi estratégica para:

1. **Garantir qualidade**: Entregar módulos completos e testados
2. **Cumprir prazo**: Viabilizar entrega dentro do cronograma acadêmico
3. **Demonstrar competência técnica**: Aplicar arquitetura de microsserviços, persistência poliglota e padrões de projeto de forma consistente

O sistema está **pronto para uso** em ambiente de clínica/hospital de pequeno porte e serve como base sólida para evolução futura.

---

**Versão**: 1.0  
**Última Atualização**: Dezembro de 2025  
**Equipe**: Grupo 4 - SimpleHealth
