## **Especificação Detalhada dos Casos de Uso**

### **Módulo de Estoque e Rastreabilidade**

Este módulo trata do gerenciamento do inventário e do controle de
movimentação de itens.

#### **UC02: Registrar Entrada de Lote Controlado**

**NOME:** Registrar Entrada de Lote de Medicamento Controlado

**DESCRIÇÃO SUCINTA:** Permite que o Técnico ou Farmacêutico registre a
entrada de medicamentos controlados no estoque, capturando todos os
dados essenciais para garantir a rastreabilidade completa e conformidade
legal.

**ATORES:** Técnico/Farmacêutico.

**PRÉ-CONDIÇÕES:**

- O Técnico/Farmacêutico deve estar autenticado no sistema.

- O medicamento controlado deve estar cadastrado no sistema (com
  indicação de ser \"Controlado\").

- Deve existir uma Nota Fiscal (NF) de compra associada à entrada.

**PÓS-CONDIÇÕES:**

- Um novo registro de lote/entrada é criado no estoque com status
  \"Disponível\".

- Os dados de NF, fornecedor, lote, validade e quantidade são
  armazenados (Conformidade Legal).

- Um alerta de validade é agendado (RNF-06).

**FLUXO BÁSICO:**

1.  O Técnico/Farmacêutico acessa a funcionalidade \"Registro de Entrada
    de Lote\".

2.  O sistema exibe o formulário de entrada.

3.  O usuário informa o **Medicamento Controlado**.

4.  O usuário preenche os campos obrigatórios: **Número da NF**,
    **Fornecedor**, **Lote**, **Data de Validade** e **Quantidade**.

5.  O sistema valida a integridade dos dados (ex: NF e Fornecedor
    existem, Data de Validade é futura).

6.  O sistema registra a entrada do lote e atualiza o saldo do estoque.

7.  O sistema agenda a emissão de alerta de validade com base na data
    fornecida.

8.  O caso de uso é encerrado.

**FLUXOS ALTERNATIVOS:** **(A1) Alternativa ao Passo 4 -- NF já
registrada:**

- O sistema identifica que a NF já está registrada para outro fornecedor
  ou data.

- O sistema emite um alerta de duplicidade, mas permite a continuação se
  o usuário confirmar a ação (permitindo NFs com múltiplos lançamentos).

- O caso de uso prossegue no Passo 5.

**(A2) Alternativa ao Passo 5 -- Validade Iminente (RNF-06):**

- Se a **Data de Validade** estiver a menos de 90 dias da data atual.

- O sistema exibe um aviso de \"Validade Crítica\" e exige uma
  confirmação explícita para registrar o lote.

- O caso de uso prossegue no Passo 6.

**FLUXOS DE EXCEÇÃO:** **(E1) Dados Incompletos ou Inválidos:**

- Se o usuário tentar prosseguir sem preencher um campo obrigatório
  (Passo 4).

- O sistema exibe a mensagem \"Preencha todos os campos obrigatórios
  (NF, Fornecedor, Lote, Validade).\"

- O caso de uso retorna ao Passo 2.

**REGRAS DE NEGÓCIO:**

- **(RNF-06)** O sistema deve monitorar a validade dos lotes e emitir
  alertas se estiverem a um prazo configurável (ex: 90 dias) da
  expiração.

- **(RNF-11)** Medicamentos controlados são considerados itens críticos
  e exigem rastreabilidade completa (entrada e saída).

### **Módulo de Gestão Financeira (Integrado com Agendamento)**

Este módulo trata da gestão de custos, receitas e repasses.

#### **UC03: Repasse Médico Automático Baseado em Receita Realizada**

**NOME:** Repasse Médico Automático Baseado em Receita Realizada

**DESCRIÇÃO SUCINTA:** O sistema calcula e gera automaticamente as
Contas a Pagar de repasse para os médicos, baseando-se nos serviços que
foram **efetivamente recebidos** pelo hospital.

**ATORES:** Gestor Financeiro, Sistema.

**PRÉ-CONDIÇÕES:**

- As regras percentuais de repasse para cada médico/procedimento devem
  estar configuradas.

- As **ContasAReceber** de serviços realizados pelos médicos devem ter
  sido baixadas como pagas.

- O período de cálculo (ex: mensal) deve ter sido definido pelo Gestor
  Financeiro.

**PÓS-CONDIÇÕES:**

- São geradas automaticamente **ContasAPagar** para cada médico
  referente ao valor do repasse.

- Os registros de repasse são vinculados às ContasAReceber que lhes
  deram origem, garantindo transparência.

- O saldo de repasses pendentes é atualizado.

**FLUXO BÁSICO:**

1.  O **Gestor Financeiro** acessa a funcionalidade \"Cálculo e Geração
    de Repasse Médico\".

2.  O usuário informa o **Período de Referência** para o cálculo.

3.  O **Sistema** consulta todas as **ContasAReceber** baixadas como
    \"Recebidas\" no período, associadas a serviços de médicos.

4.  Para cada conta, o sistema identifica o Médico, o valor recebido e a
    regra de repasse aplicada.

5.  O sistema calcula o **Valor de Repasse** por médico.

6.  O sistema exibe a pré-visualização dos repasses calculados para
    aprovação.

7.  O Gestor Financeiro confirma a geração.

8.  O sistema cria as **ContasAPagar** de repasse, uma para cada médico,
    com o valor e a data de vencimento configurados.

9.  O caso de uso é encerrado.

**FLUXOS ALTERNATIVOS:** **(A1) Alternativa ao Passo 6 -- Ajuste Manual
(Exceção):**

- O Gestor Financeiro pode selecionar um ou mais repasses e realizar um
  ajuste manual (ex: incluir bônus, desconto) antes da confirmação.

- O sistema exige uma justificativa para o ajuste.

- O caso de uso prossegue no Passo 7, utilizando o valor ajustado.

**(A2) Alternativa ao Passo 3 -- Nenhum Recebimento no Período:**

- Se o sistema não encontrar ContasAReceber baixadas no período.

- O sistema exibe a mensagem \"Nenhuma receita realizada e baixada
  encontrada no período selecionado.\"

- O caso de uso é encerrado.

**FLUXOS DE EXCEÇÃO:** **(E1) Regra de Repasse Ausente:**

- Durante o Passo 4, o sistema identifica um serviço/médico sem regra de
  repasse configurada.

- O sistema interrompe o cálculo e lista os registros pendentes de
  regra.

- O sistema exibe a mensagem \"O cálculo não pode ser concluído.
  Configure a regra de repasse para \[Nome do Médico/Serviço\].\"

- O caso de uso é encerrado.

**REGRAS DE NEGÓCIO:**

- **(RN-Repasse.1)** O repasse só pode ser calculado sobre o valor
  líquido da **receita efetivamente baixada** no sistema (recebimento
  real).

- **(RN-Repasse.2)** A geração da ContaAPagar deve ser transparente,
  vinculando-se aos lançamentos de recebimento.
