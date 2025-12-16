# Sumário das Correções de Alinhamento - SimpleHealth

**Projeto**: SimpleHealth  
**Equipe**: Grupo 4  
**Data**: Dezembro 2025 - Janeiro 2025  
**Documento Base**: [relatorio-alinhamento-grupo4.md.pdf](./relatorio-alinhamento-grupo4.md.pdf)

---

## Índice

- [1. Introdução](#1-introdução)
- [2. Discrepâncias da Série 1.x](#2-discrepâncias-da-série-1x)
- [3. Discrepâncias da Série 2.x](#3-discrepâncias-da-série-2x)
- [4. Discrepâncias da Série 3.x - Integrações entre Microserviços](#4-discrepâncias-da-série-3x---integrações-entre-microserviços)
- [5. Discrepâncias da Série 4.x - Relacionamentos UML](#5-discrepâncias-da-série-4x---relacionamentos-uml)
- [6. Discrepâncias da Série 5.x - Casos de Uso com Redução de Escopo](#6-discrepâncias-da-série-5x---casos-de-uso-com-redução-de-escopo)
- [7. Estatísticas Gerais](#7-estatísticas-gerais)

---

## 1. Introdução

Este diretório contém todas as correções de alinhamento entre documentação e implementação do projeto SimpleHealth, identificadas através do **Relatório de Alinhamento** gerado pela equipe de qualidade.

**Objetivo**: Sincronizar a documentação técnica (diagramas UML, casos de uso, BPM, arquitetura) com a implementação real no backend Java/Spring Boot.

**Metodologia**: Para cada discrepância identificada, foi realizada:
1. Verificação da implementação backend via grep/análise de código
2. Identificação do gap (documentação incorreta vs código correto)
3. Correção da documentação mantendo o código como fonte autoritativa
4. Documentação detalhada da correção em arquivo markdown específico

---

## 2. Discrepâncias da Série 1.x

**Escopo**: Primeira rodada de correções (anteriores ao escopo atual)

| ID | Título | Tipo | Status |
|----|--------|------|--------|
| 1.1 | [Discrepância 1.1](./CORRECAO_DISCREPANCIA_1.1.md) | Documentação | ✅ Corrigido |
| 1.2 | [Discrepância 1.2](./CORRECAO_DISCREPANCIA_1.2.md) | Documentação | ✅ Corrigido |
| 1.3 | [Discrepância 1.3](./CORRECAO_DISCREPANCIA_1.3.md) | Documentação | ✅ Corrigido |
| 1.4 | [Discrepância 1.4](./CORRECAO_DISCREPANCIA_1.4.md) | Documentação | ✅ Corrigido |
| 1.5 | [Discrepância 1.5](./CORRECAO_DISCREPANCIA_1.5.md) | Documentação | ✅ Corrigido |
| 1.6 | [Módulo Estoque: Entidade Estoque Subimplementada](./CORRECAO_DISCREPANCIA_1.6.md) | Documentação | ✅ Corrigido |
| 1.7 | [Discrepância 1.7](./CORRECAO_DISCREPANCIA_1.7.md) | Documentação | ✅ Corrigido |
| 1.8 | [Módulo Estoque: Relacionamento Pedido ↔ Fornecedor Simplificado](./CORRECAO_DISCREPANCIA_1.8.md) | Documentação | ✅ Corrigido |

### 1.6. Módulo Estoque: Entidade Estoque Subimplementada
- **Arquivo**: [CORRECAO_DISCREPANCIA_1.6.md](./CORRECAO_DISCREPANCIA_1.6.md)
- **Resumo**: Entidade `Estoque` estava com atributos incompletos (apenas `local`). Faltavam: `nome`, `localizacao`, `setor`. Solução: Adicionados atributos faltantes mantendo separação de responsabilidades (Estoque = local físico; Item = produto com quantidade/validade). Relacionamento `Item → Estoque` (Many-to-One) implementado via FK.
- **Tipo**: Documentação (código corrigido)
- **Impacto**: 3 arquivos (Classes de Análise, Casos de Uso UC05, Modelagem de Dados)
- **Status**: ✅ Corrigido

### 1.8. Módulo Estoque: Relacionamento Pedido ↔ Fornecedor Simplificado
- **Arquivo**: [CORRECAO_DISCREPANCIA_1.8.md](./CORRECAO_DISCREPANCIA_1.8.md)
- **Resumo**: Entidade `Fornecedor` estava extremamente simplificada (apenas `cnpj`). Faltavam: `nome`, `telefone`, `email`, `endereco`. Problema: UC06 (Processar Entrada de NF) mencionava "seleciona o Fornecedor", mas UI exibiria apenas UUID. Solução: Adicionados todos os atributos essenciais. Relacionamento FK `Pedido → Fornecedor` já estava correto.
- **Tipo**: Documentação (código corrigido)
- **Impacto**: 4 arquivos (Classes de Análise, UC06, UC07 novo, Modelagem)
- **Status**: ✅ Corrigido

---

## 3. Discrepâncias da Série 2.x

**Escopo**: Segunda rodada de correções

| ID | Título | Tipo | Status |
|----|--------|------|--------|
| 2.1 | [Discrepância 2.1](./CORRECAO_DISCREPANCIA_2.1.md) | Documentação | ✅ Corrigido |

---

## 4. Discrepâncias da Série 3.x - Integrações entre Microserviços

**Escopo**: Correções de arquitetura de microserviços, padrão de integração via Redis Pub/Sub

### 3.1. Discrepância 3.1
- **Arquivo**: [CORRECAO_DISCREPANCIA_3.1.md](./CORRECAO_DISCREPANCIA_3.1.md)
- **Resumo**: (Conteúdo da série 3.1)
- **Tipo**: Documentação
- **Status**: ✅ Corrigido

### 3.2. Discrepância 3.2
- **Arquivo**: [CORRECAO_DISCREPANCIA_3.2.md](./CORRECAO_DISCREPANCIA_3.2.md)
- **Resumo**: (Conteúdo da série 3.2)
- **Tipo**: Documentação
- **Status**: ✅ Corrigido

### 3.3. Integração AgendamentoService → CadastroService
- **Arquivo**: [CORRECAO_DISCREPANCIA_3.3.md](./CORRECAO_DISCREPANCIA_3.3.md)
- **Resumo**: Documentação indicava dependências diretas entre `AgendamentoService` e `CadastroService`/`EstoqueService`, mas implementação usa Redis Pub/Sub e armazenamento de strings identificadoras (pacienteCpf, medicoCrm) sem dependências diretas entre módulos.
- **Tipo**: Documentação (código correto)
- **Impacto**: 3 arquivos corrigidos (3.4 Classes de Análise, 3.9 Modelagem de Interações, 3.3 Casos de Uso)
- **Status**: ✅ Corrigido

### 3.4. Integração AgendamentoService → EstoqueService (Baixa em Insumos)
- **Arquivo**: [CORRECAO_DISCREPANCIA_3.4.md](./CORRECAO_DISCREPANCIA_3.4.md)
- **Resumo**: Documentação mostrava `AgendamentoService` com atributo `estoqueService` e método `darBaixaEmMateriais()`, mas UC05 (Dar Baixa em Insumos) é independente no módulo Estoque. UC08 (Consultar Histórico) retorna apenas dados de agendamentos, sem integração com insumos.
- **Tipo**: Redução de Escopo
- **Impacto**: 3 arquivos corrigidos, RN-HIST.1 marcada como [REDUÇÃO DE ESCOPO]
- **Status**: ✅ Corrigido

---

## 5. Discrepâncias da Série 4.x - Relacionamentos UML

**Escopo**: Correções de diagramas de classes, cardinalidades, relacionamentos Many-to-Many/Many-to-One

### 4.1. Paciente ↔ Convênio (Cardinalidade Errada)
- **Arquivo**: [CORRECAO_DISCREPANCIA_4.1.md](./CORRECAO_DISCREPANCIA_4.1.md)
- **Resumo**: Documentação mostrava `Paciente "1" -- "0..*" Convenio`, mas implementação tem `@ManyToOne` (Many-to-One) com FK nullable: **correto é `"0..*" -- "0..1"`** (muitos pacientes para zero ou um convênio).
- **Tipo**: Documentação (cardinalidade invertida)
- **Impacto**: 2 ocorrências corrigidas em 3.4 Classes de Análise
- **Status**: ✅ Corrigido

### 4.2. Médico ↔ Convênio (Relacionamento Faltante)
- **Arquivo**: [CORRECAO_DISCREPANCIA_4.2.md](./CORRECAO_DISCREPANCIA_4.2.md)
- **Resumo**: Documentação mostrava relacionamento Many-to-Many entre `Medico` e `Convenio` (médicos credenciados em convênios), mas implementação **não possui** este relacionamento (nem `@ManyToMany`, nem collections). Validação é manual pela secretária.
- **Tipo**: Redução de Escopo
- **Impacto**: Relacionamento removido de 2 ocorrências em 3.4 Classes de Análise
- **Status**: ✅ Corrigido

### 4.3. Usuario ↔ PerfilUsuario (Enum Incompleto)
- **Arquivo**: [CORRECAO_DISCREPANCIA_4.3.md](./CORRECAO_DISCREPANCIA_4.3.md)
- **Resumo**: Documentação mostrava enum `EPerfilUsuario` com apenas 3 valores (MEDICO, SECRETARIA, GESTOR), mas backend tem **5 valores** (incluindo FINANCEIRO, TESOURARIA). Enum foi completado em todos os arquivos de documentação.
- **Tipo**: Documentação (enum incompleto)
- **Impacto**: 4 arquivos corrigidos (3.4, 3.7, 3.1 - 2 ocorrências em cada)
- **Status**: ✅ Corrigido

---

## 6. Discrepâncias da Série 5.x - Casos de Uso com Redução de Escopo

**Escopo**: UCs com implementação parcial ou ausente (integrações Redis Pub/Sub, persistência de movimentações)

### 5.1. UC07: Gerar Alerta de Estoque Crítico
- **Arquivo**: [CORRECAO_DISCREPANCIA_5.1.md](./CORRECAO_DISCREPANCIA_5.1.md) (63KB)
- **Resumo**: UC07 **não está implementado** (0% funcional). Publisher existe em Cadastro (`EstoqueAlertaPublisher.solicitarAlertasEstoqueCritico()`), mas **não há subscriber** no módulo Estoque. Sempre retorna lista vazia após timeout de 5 segundos.
- **Tipo**: Redução de Escopo - Não Implementado
- **Impacto**: 6 arquivos marcados com `[NÃO IMPLEMENTADO]` + cor cinza no diagrama
- **Funcionalidade**: 🔴 **0% funcional** (sem subscriber)
- **Status**: ✅ Documentado

### 5.2. UC08: Consultar Histórico do Paciente
- **Arquivo**: [CORRECAO_DISCREPANCIA_5.2.md](./CORRECAO_DISCREPANCIA_5.2.md)
- **Resumo**: UC08 **parcialmente implementado** (60% funcional). Publisher existe em Cadastro, publica 5 requests via Redis:
  - ✅ **3 canais funcionam**: `historico.consulta.request`, `historico.exame.request`, `historico.procedimento.request` (AgendamentoSubscriber responde)
  - ❌ **2 canais não funcionam**: `historico.estoque.request` (sem subscriber no Estoque), `historico.pagamento.request` (Módulo Financeiro não existe)
- **Tipo**: Redução de Escopo - Implementação Parcial
- **Impacto**: 4 arquivos marcados com `[IMPLEMENTAÇÃO PARCIAL]` + cor amarela no diagrama
- **Funcionalidade**: 🟡 **60% funcional** (3 de 5 integrações funcionam)
- **Status**: ✅ Documentado

### 5.3. UC10: Controlar Validade de Itens
- **Arquivo**: [CORRECAO_DISCREPANCIA_5.3.md](./CORRECAO_DISCREPANCIA_5.3.md) (21KB)
- **Resumo**: UC10 **parcialmente implementado** (50% funcional). `ControlarValidadeUseCase` existe com endpoint REST `/validade`, busca itens por validade funciona, descarte zera quantidade. **Porém**:
  - ❌ **Movimentações não persistidas**: método `registrarMovimentacaoDescarte()` apenas imprime logs no console, não salva em banco de dados
  - ❌ **Sem integração financeira**: Módulo Financeiro não existe (código de custo validado mas não registrado)
  - ❌ **Sem alertas automáticos**: Sem Redis Pub/Sub para alertas de validade próxima
- **Tipo**: Redução de Escopo - Implementação Parcial
- **Impacto**: 4 arquivos marcados com `[IMPLEMENTAÇÃO PARCIAL]` + cor amarela no diagrama
- **Funcionalidade**: 🟡 **50% funcional** (consulta funciona, persistência não)
- **Status**: ✅ Documentado

---

## 7. Estatísticas Gerais

### Correções por Tipo

| Tipo | Quantidade | Descrição |
|------|------------|-----------|
| **Documentação (código correto)** | 8 | Discrepâncias 1.x (1.1-1.8), 2.x, 3.3, 4.1, 4.3 |
| **Redução de Escopo** | 4 | Discrepâncias 3.4, 4.2, 5.1, 5.2, 5.3 |
| **Total** | 19 | Todas as discrepâncias identificadas |

### Arquivos de Documentação Modificados

| Arquivo | Discrepâncias Corrigidas |
|---------|--------------------------|
| **3.1. Documento de Visão do Projeto** | 4.3 (enum PerfilUsuario) |
| **3.2. Diagrama Global de Casos de Uso** | 5.1 (UC07), 5.2 (UC08), 5.3 (UC10) |
| **3.3. Descrição detalhada de cada Caso de Uso** | 1.6 (UC05), 1.8 (UC06, UC07), 3.3, 3.4, 5.1 (6 alterações), 5.2 (2 alterações), 5.3 (2 alterações) |
| **3.4. Classes de Análise** | 1.6 (Estoque), 1.8 (Fornecedor), 3.3 (3 alterações), 3.4, 4.1 (2 alterações), 4.2 (2 alterações), 4.3 (2 alterações) |
| **3.5. Diagramas de Processos de Negócio (BPM)** | 5.1, 5.2, 5.3 |
| **3.6. Arquitetura do Sistema** | 5.1 (Redis Pub/Sub notes) |
| **3.7. Modelagem de Classes de Projeto** | 1.6 (Estoque), 1.8 (Fornecedor), 4.3 (enum) |
| **3.9. Modelagem de Interações** | 3.3, 3.4, 5.1, 5.2, 5.3 |
| **3.10. Modelagem de Estados** | 5.1 (2 alterações DTE) |

### Padrões Observados

**Integrações Redis Pub/Sub**:
- UC07 (Alerta de Estoque): Publisher existe ✅, Subscriber não existe ❌ → **0% funcional**
- UC08 (Histórico Paciente): Publisher existe ✅, Subscriber parcial ⚠️ → **60% funcional**
- UC10 (Controle Validade): UseCase existe ✅, sem Redis Pub/Sub ❌ → **50% funcional**

**Relacionamentos UML**:
- Paciente ↔ Convênio: Cardinalidade lida de trás para frente (Many-to-One vs One-to-Many)
- Médico ↔ Convênio: Feature de credenciamento não implementada (planejada mas não desenvolvida)

**Microserviços**:
- Zero imports diretos entre módulos (isolamento arquitetural correto)
- Redis Pub/Sub para comunicação assíncrona (pattern correto, implementação parcial)
- Database per Service (PostgreSQL/Cadastro, MongoDB/Agendamento, Cassandra/Estoque)

---

## 8. Próximos Passos (Opcional)

### Implementações Sugeridas (Prioridade)

**🔴 Alta Prioridade**:
1. **UC10**: Persistência de movimentações de descarte (4-8 horas) - compliance regulatório
2. **UC08**: Subscriber para `historico.estoque.request` (9-13 horas) - completar integração

**🟡 Média Prioridade**:
3. **UC07**: Subscriber para alertas de estoque crítico (20-30 horas) - feature completa
4. **UC10**: Redis Publisher/Subscriber para alertas de validade (10-18 horas) - automação

**🟢 Baixa Prioridade**:
5. **Módulo Financeiro**: Implementação completa (40-60 horas) - feature adicional
6. **Médico-Convênio**: Relacionamento Many-to-Many para credenciamento (15-25 horas) - melhoria

---

**Versão**: 1.0  
**Última Atualização**: Janeiro 2025  
**Equipe**: Grupo 4 - SimpleHealth  
**Documento Base**: [relatorio-alinhamento-grupo4.md.pdf](./relatorio-alinhamento-grupo4.md.pdf)
