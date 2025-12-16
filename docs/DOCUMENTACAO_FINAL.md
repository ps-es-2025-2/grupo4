# 📚 Documentação Final - SimpleHealth

> Sistema Integrado de Gestão Hospitalar

Este documento serve como guia de navegação para toda a documentação técnica do projeto SimpleHealth, organizada de forma didática para facilitar a compreensão do sistema.

---

## � Sumário

- [📂 Entrevistas com Stakeholders](#-entrevistas-com-stakeholders)
- [3.1. Documento de Visão do Projeto](#31-documento-de-visão-do-projeto)
- [3.2 e 3.3. Casos de Uso](#32-e-33-casos-de-uso)
- [3.4. Classes de Análise](#34-classes-de-análise)
- [3.5. Diagramas de Processos de Negócio (BPM)](#35-diagramas-de-processos-de-negócio-bpm)
- [3.6. Arquitetura do Sistema - Lógica e Física](#36-arquitetura-do-sistema---lógica-e-física)
- [3.7, 3.9 e 3.10. Modelagens Técnicas](#37-39-e-310-modelagens-técnicas)
- [3.8. Boas Práticas e Padrões de Projeto](#38-boas-práticas-e-padrões-de-projeto)
- [QA - Qualidade e Testes](#qa---qualidade-e-testes)
- [📝 Correções de Alinhamento](#-correções-de-alinhamento)
- [🧪 Testes de Requisitos nas Interfaces](#-testes-de-requisitos-nas-interfaces)
- [💾 Teste de Instalação](#-teste-de-instalação)
- [👥 Relatórios Individuais](#-relatórios-individuais)
- [🗂️ Ordem Sugerida de Leitura](#️-ordem-sugerida-de-leitura)
- [🚀 Executando o Sistema](#-executando-o-sistema)

---

## 📂 Entrevistas com Stakeholders

As entrevistas realizadas com stakeholders, contendo informações valiosas sobre requisitos, expectativas e processos do sistema, estão disponíveis na pasta [`entrevista_stake_holder`](./entrevista_stake_holder/).

### [3.1. Documento de Visão do Projeto](./documentos-finais-definitivos/3.1.%20Documento%20de%20Visão%20do%20Projeto/)

**O que é**: Documento inicial que apresenta o contexto, motivação e objetivos do projeto.

**Conteúdo**:
- Contexto do problema e escopo do sistema
- Motivação e justificativa do projeto
- Objetivos gerais e específicos
- Descrição geral do produto
- Funcionalidades de alto nível
- Atores principais do sistema
- Requisitos funcionais de alto nível
- Restrições, premissas e critérios de sucesso
- Modelo de inovação (Persistência Poliglota)

**Por que começar aqui**: Este documento estabelece a base conceitual do projeto, apresentando a visão geral antes de detalhar os aspectos técnicos.

---

### [3.2 e 3.3. Casos de Uso](./documentos-finais-definitivos/3.2_3.3_Casos%20de%20uso/)

**O que é**: Especificação funcional detalhada do sistema através de casos de uso.

**Conteúdo**:

- **3.2. Diagrama Global de Casos de Uso**: Visão geral de todos os casos de uso e suas relações
- **3.3. Descrição Detalhada**: Especificação completa de cada caso de uso com:
  - Atores envolvidos
  - Pré-condições e pós-condições
  - Fluxos principais e alternativos
  - Regras de negócio
  - Exceções e tratamentos de erro

**Por que ler**: Apresenta COMO o sistema será usado, descrevendo as interações entre usuários e sistema de forma estruturada.

---

### [3.4. Classes de Análise](./documentos-finais-definitivos/3.4.%20Classes%20de%20Análise/)

**O que é**: Modelagem conceitual das entidades de negócio do sistema.

**Conteúdo**:

- Diagrama de classes de análise
- Identificação das principais entidades do domínio
- Relacionamentos entre as classes
- Atributos essenciais de cada entidade
- Hierarquias e generalizações (ex: Pessoa → Paciente, Médico, Secretária)

**Por que ler**: Mostra QUAIS são as entidades do sistema e como elas se relacionam, fornecendo uma visão conceitual do modelo de dados.

---

### [3.5. Diagramas de Processos de Negócio (BPM)](./documentos-finais-definitivos/3.5.%20Diagramas%20de%20Processos%20de%20Negócio%20(BPM)/)

**O que é**: Modelagem dos processos de negócio usando notação BPMN 2.0.

**Conteúdo**:

- **Módulo de Agendamento**: Processos de agendamento de consultas, encaixes e bloqueios
- **Módulo de Estoque**: Processos de baixa, entrada de NF, alertas e controle de validade
- **Módulo de Cadastro**: Processos de registro e consulta de dados

**Por que ler**: Demonstra os FLUXOS DE TRABALHO do sistema, mostrando a sequência de atividades, decisões e responsabilidades em cada processo.

---

### [3.6. Arquitetura do Sistema - Lógica e Física](./documentos-finais-definitivos/3.6.%20Arquitetura%20do%20Sistema%20-%20Lógica%20e%20Física/)

**O que é**: Especificação completa da arquitetura do sistema.

**Conteúdo**:

- Arquitetura de microsserviços
- Visão lógica: Organização em camadas (Domain, Application, Infrastructure, Web)
- Visão física: Distribuição dos componentes, containers Docker, bancos de dados
- Tecnologias utilizadas (Spring Boot, JavaFX, PostgreSQL, MongoDB)
- Estratégia de persistência poliglota
- APIs REST e comunicação entre serviços
- Diagramas de componentes e implantação

**Por que ler**: Explica COMO o sistema está estruturado tecnicamente, desde a arquitetura macro até os detalhes de implementação.

---

### [3.7, 3.9 e 3.10. Modelagens Técnicas](./documentos-finais-definitivos/3.7_3.9_3.10_Modelagens/)

**O que é**: Modelagens detalhadas de design do sistema.

**Conteúdo**:

#### **3.7. Modelagem de Classes de Projeto**

- Diagrama de classes de implementação (com detalhes técnicos)
- Classes de cada camada (Domain, Application, Infrastructure)
- Métodos, atributos e tipos de dados
- Relacionamentos e dependências entre classes

#### **3.9. Modelagem de Interações (Diagramas de Sequência)**

- Fluxos de execução de casos de uso
- Interação entre objetos e camadas
- Chamadas de métodos e retornos
- Ciclo de vida das requisições

#### **3.10. Modelagem de Estados**

- Máquinas de estado das entidades principais
- Transições de estado válidas
- Eventos que disparam mudanças de estado
- Regras de negócio associadas aos estados

**Por que ler**: Fornece o DETALHAMENTO TÉCNICO necessário para implementação, mostrando como as classes interagem e como os estados são gerenciados.

---

### [3.8. Boas Práticas e Padrões de Projeto](./documentos-finais-definitivos/3.8%20Boas%20Práticas/)

**O que é**: Documentação das práticas de desenvolvimento e padrões de projeto aplicados.

**Conteúdo**:

- **Boas Práticas**:
  - Arquitetura de microsserviços
  - Clean Architecture e separação em camadas
  - Princípios SOLID
  - Boas práticas de código
  - Estratégias de integração e comunicação
  - Testabilidade

- **Design Patterns** (DESIGN_PATTERNS.md):
  - 10 padrões de projeto implementados com exemplos de código real:
    1. Template Method Pattern
    2. Strategy Pattern
    3. Repository Pattern
    4. DTO Pattern
    5. Singleton Pattern
    6. Factory Pattern
    7. Observer Pattern
    8. Dependency Injection
    9. Builder Pattern
    10. Adapter Pattern
  - Links para classes no código-fonte
  - Explicação do propósito e benefícios de cada padrão

**Por que ler**: Demonstra a QUALIDADE TÉCNICA do projeto, evidenciando a aplicação de boas práticas de engenharia de software e padrões consolidados da indústria.

---

### [QA - Qualidade e Testes](./qa/)

**O que é**: Documentação completa da estratégia de qualidade e testes do sistema SimpleHealth.

**Conteúdo**:

- **Plano de Teste** (`PLANO DE TESTE - SISTEMA SIMPLE HEALTH.md`):
  - Estratégia geral de testes
  - Escopo e objetivos dos testes
  - Tipos de teste aplicados (unitários, integração, sistema, aceitação)
  - Recursos e cronograma
  - Critérios de entrada e saída
  - Ambiente de teste

- **Casos de Teste** (`DOCUMENTO DE CASOS DE TESTE - SISTEMA SIMPLE HEALTH.md`):
  - Especificação detalhada dos cenários de teste
  - Casos de teste por módulo (Cadastro, Agendamento, Estoque)
  - Pré-condições, dados de entrada e resultados esperados
  - Priorização e rastreabilidade com requisitos

- **Relatório de Testes** (`RELATÓRIO DE TESTES - SISTEMA SIMPLE HEALTH.md`):
  - Resultados da execução dos testes
  - Métricas de qualidade e cobertura
  - Defeitos encontrados e status
  - Análise de riscos
  - Conclusões e recomendações

- **Avaliação de Conformidade** (`Avaliação de conformidade documentos.md`):
  - Verificação da conformidade da documentação
  - Checklist de completude
  - Análise de consistência entre documentos

- **Testes Automatizados** (arquivos JSON):
  - `automacaocadastro.json` - Scripts de automação do módulo de cadastro
  - `automacaoagendamento.json` - Scripts de automação do módulo de agendamento
  - `automacaoestoque.json` - Scripts de automação do módulo de estoque

**Por que ler**: Demonstra a GARANTIA DE QUALIDADE do sistema através de processos sistemáticos de verificação e validação, evidenciando a confiabilidade e robustez do software desenvolvido.

---

## 📝 Correções de Alinhamento

**O que é**: Documentação das correções de discrepâncias e alinhamentos realizados entre diferentes artefatos do projeto.

**Conteúdo**:

A pasta [`Correções de Alinhamento`](./Correções%20de%20Alinhamento/) contém documentos detalhados de correções aplicadas para garantir consistência entre os diversos artefatos do projeto:

- **Correções da Entrega 1** (Discrepâncias 1.1 a 1.8):
  - Alinhamento entre Documento de Visão e Casos de Uso
  - Ajustes em requisitos funcionais e não-funcionais
  - Correção de inconsistências em atores e funcionalidades
  - Padronização de nomenclaturas

- **Correção da Entrega 2** (Discrepância 2.1):
  - Alinhamento entre Classes de Análise e Casos de Uso

- **Correções da Entrega 3** (Discrepâncias 3.1 a 3.4):
  - Alinhamento entre Diagramas BPM e Casos de Uso
  - Correção de fluxos de processo
  - Ajustes em decisões e responsabilidades

- **Correções da Entrega 4** (Discrepâncias 4.1 a 4.3):
  - Alinhamento entre Arquitetura e demais artefatos
  - Correção de componentes e tecnologias

- **Correções da Entrega 5** (Discrepâncias 5.1 a 5.3):
  - Alinhamento entre Modelagens Detalhadas e Implementação
  - Correção de diagramas de sequência e estados

**Sumário**: O arquivo [`SUMARIO_CORRECAO_DISCREPANCIA.md`](./Correções%20de%20Alinhamento/SUMARIO_CORRECAO_DISCREPANCIA.md) apresenta uma visão consolidada de todas as correções realizadas.

**Por que ler**: Demonstra o processo de REFINAMENTO E MELHORIA CONTÍNUA da documentação, evidenciando a preocupação com a consistência e qualidade dos artefatos produzidos ao longo do projeto.

---

## 🧪 Testes de Requisitos nas Interfaces

**O que é**: Relatórios de testes funcionais executados diretamente nas interfaces gráficas de cada módulo do sistema.

**Conteúdo**:

A pasta [`Testes de requisitos nas interfaces`](./Testes%20de%20requisitos%20nas%20interfaces/) contém relatórios detalhados dos testes realizados:

- **Módulo de Cadastro** ([`RELATORIO_TESTE_MODULO_CADASTRO.md`](./Testes%20de%20requisitos%20nas%20interfaces/RELATORIO_TESTE_MODULO_CADASTRO.md)):
  - Testes de cadastro de pacientes, médicos, secretárias e convênios
  - Validações de campos obrigatórios
  - Testes de edição e exclusão
  - Evidências visuais em `imagens-cadastro/`

- **Módulo de Agendamento** ([`RELATORIO_TESTE_MODULO_AGENDAMENTO.md`](./Testes%20de%20requisitos%20nas%20interfaces/RELATORIO_TESTE_MODULO_AGENDAMENTO.md)):
  - Testes de agendamento de consultas, exames e procedimentos
  - Testes de bloqueio de agenda
  - Validações de regras de negócio
  - Evidências visuais em `imagens-agendamento/`

- **Módulo de Estoque** ([`RELATORIO_TESTE_MODULO_ESTOQUE.md`](./Testes%20de%20requisitos%20nas%20interfaces/RELATORIO_TESTE_MODULO_ESTOQUE.md)):
  - Testes de controle de medicamentos, alimentos e materiais hospitalares
  - Testes de entrada de notas fiscais e baixa de estoque
  - Validações de alertas e controle de validade
  - Evidências visuais em `imagens-estoque/`

**Por que ler**: Comprova a EXECUÇÃO PRÁTICA DOS TESTES nas interfaces reais do sistema, demonstrando que o software atende aos requisitos funcionais especificados através de evidências visuais e resultados documentados.

---

## 💾 Teste de Instalação

**O que é**: Documentação do processo de instalação e execução do sistema SimpleHealth.

**Conteúdo**:

A pasta [`Teste de Instalacao`](./Teste%20de%20Instalacao/) contém:

- **Relatório de Instalação** ([`relatorio_de_instalacao_e_execucao_do_projeto (1).md`](./Teste%20de%20Instalacao/relatorio_de_instalacao_e_execucao_do_projeto%20(1).md)):
  - Pré-requisitos de software e hardware
  - Passo a passo detalhado da instalação
  - Configuração do ambiente de desenvolvimento
  - Execução dos módulos backend e frontend
  - Verificação do funcionamento do sistema
  - Troubleshooting de problemas comuns
  - Capturas de tela do processo em `imagens/`

**Por que ler**: Fornece um GUIA PRÁTICO E COMPLETO para instalação e execução do sistema, permitindo que qualquer pessoa consiga configurar e executar o SimpleHealth seguindo instruções claras e ilustradas.

---

## 👥 Relatórios Individuais

**O que é**: Relatórios individuais de cada membro da equipe documentando suas contribuições ao longo do projeto.

**Conteúdo**:

A pasta [`relatorios-individuais`](./relatorios-individuais/) está organizada por entregas:

- **1-entrega**: Relatórios da primeira fase do projeto
- **2-entrega**: Relatórios da segunda fase do projeto
- **3-entrega**: Relatórios da terceira fase do projeto
- **4-entrega**: Relatórios da quarta fase do projeto

Cada relatório individual documenta:
- Atividades realizadas pelo membro da equipe
- Artefatos produzidos
- Contribuições para o projeto
- Desafios enfrentados e soluções aplicadas
- Aprendizados adquiridos

**Por que ler**: Demonstra a CONTRIBUIÇÃO INDIVIDUAL de cada membro da equipe e proporciona transparência sobre a divisão de trabalho e responsabilidades ao longo do desenvolvimento do projeto.

---

## 🗂️ Ordem Sugerida de Leitura

Para uma compreensão progressiva e didática do projeto, recomenda-se a seguinte ordem:

1. **3.1. Documento de Visão** - Entenda o contexto e objetivos
2. **3.2/3.3. Casos de Uso** - Conheça as funcionalidades e interações
3. **3.4. Classes de Análise** - Compreenda o modelo conceitual
4. **3.5. Processos de Negócio (BPM)** - Visualize os fluxos de trabalho
5. **3.6. Arquitetura** - Entenda a estrutura técnica do sistema
6. **3.7/3.9/3.10. Modelagens** - Aprofunde-se nos detalhes de implementação
7. **3.8. Boas Práticas e Padrões** - Avalie a qualidade técnica e padrões aplicados
8. **QA - Qualidade e Testes** - Compreenda a estratégia de garantia de qualidade
9. **Correções de Alinhamento** - Veja o processo de refinamento dos artefatos
10. **Testes de Requisitos nas Interfaces** - Confira os testes práticos realizados
11. **Teste de Instalação** - Aprenda a instalar e executar o sistema
12. **Relatórios Individuais** - Conheça as contribuições de cada membro da equipe

---

## 🚀 Executando o Sistema

Para instruções sobre como executar o sistema, consulte:

- `README.md` na raiz do projeto
- Scripts de inicialização: `start-all.sh`, `stop-all.sh`, `status.sh`
- Documentação individual de cada módulo nos respectivos diretórios

---

## 📝 Observações

- Todos os diagramas estão disponíveis em formato de imagem nas respectivas pastas
- Os documentos em Markdown podem ser visualizados diretamente no GitHub ou em qualquer editor que suporte Markdown
- A documentação segue os padrões acadêmicos de Engenharia de Software
- O projeto implementa persistência poliglota: PostgreSQL (Cadastro), MongoDB (Agendamento)

---

**Última atualização**: Dezembro de 2025  
**Equipe**: Grupo 4 - SimpleHealth
