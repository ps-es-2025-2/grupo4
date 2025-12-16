# **DOCUMENTO DE CASOS DE TESTE \- SISTEMA SIMPLE HEALTH**

## **0\. Estrutura do Caso de Teste (Template)**

| Campo | Descrição |
| :---- | :---- |
| **ID do Caso de Teste** | Código único (Ex: CT-Módulo-Sequencial) |
| **Módulo / Recurso** | Microserviço (Ex: Estoque) e Entidade (Ex: Pedido) |
| **Endpoint / Ação** | URL da API e/ou Interação do Front-end (Ex: POST /api/v1/pedidos) |
| **Prioridade** | Crítica, Alta, Média, Baixa |
| **Tipo de Teste** | Positivo (Caminho Feliz), Negativo, Fronteira, Integração |
| **Pré-Condições** | Dados necessários para executar o teste (Ex: Paciente e Médico cadastrados) |
| **Passos para Execução** | Sequência detalhada de ações. |
| **Resultado Esperado** | O que o sistema deve retornar/fazer (Status HTTP, Mensagem, Estado do DB) |

## **1\. Casos de Teste \- Módulo ESTOQUE**

O módulo Estoque gerencia Fornecedores, Itens (Medicamentos, Hospitalares, Alimentos) e Pedidos.

### **1.1. Gerenciamento de Fornecedores (/fornecedores)**

| ID | Endpoint / Ação | Prioridade | Tipo de Teste | Pré-Condições | Passos para Execução | Resultado Esperado |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| **CT-E-001** | POST /fornecedores | Alta | Positivo | N/A | Enviar um *payload* de Fornecedor com todos os campos válidos (CNPJ não duplicado). | Status: **201 Created**. O fornecedor é salvo no DB. |
| **CT-E-002** | POST /fornecedores | Crítica | Negativo | Fornecedor X cadastrado. | Tentar cadastrar novo fornecedor com o mesmo CNPJ do Fornecedor X. | Status: **400 Bad Request** ou **409 Conflict**. Mensagem de erro de duplicidade. |
| **CT-E-003** | DELETE /fornecedores/{id} | Crítica | Negativo | Fornecedor Y cadastrado e vinculado a um Pedido Z. | Tentar deletar o Fornecedor Y. | Status: **409 Conflict** (Regra de Negócio). Mensagem informando que o fornecedor está em uso. |
| **CT-E-004** | DELETE /fornecedores/{id} | Alta | Positivo | Fornecedor K cadastrado e **sem vínculos** com Pedidos. | Tentar deletar o Fornecedor K. | Status: **204 No Content**. O fornecedor é removido do DB. |

### 

### **1.2. Gerenciamento de Itens (Medicamentos \- /medicamentos)**

| ID | Endpoint / Ação | Prioridade | Tipo de Teste | Pré-Condições | Passos para Execução | Resultado Esperado |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| **CT-E-010** | POST /medicamentos | Alta | Negativo | N/A | Tentar cadastrar um Medicamento sem o campo obrigatório numeroLote. | Status: **400 Bad Request**. Mensagem de erro sobre campo obrigatório (Conforme Tabela de Risco). |
| **CT-E-011** | POST /medicamentos | Alta | Negativo | N/A | Tentar cadastrar um Medicamento sem o campo obrigatório dataValidade. | Status: **400 Bad Request**. Mensagem de erro sobre campo obrigatório (Conforme Tabela de Risco). |

### 

### 

### 

### 

### **1.3. Gerenciamento de Pedidos (/pedidos)**

| ID | Endpoint / Ação | Prioridade | Tipo de Teste | Pré-Condições | Passos para Execução | Resultado Esperado |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| **CT-E-020** | POST /pedidos | Crítica | Positivo | Fornecedor A e Item B cadastrados e disponíveis. | Criar um Pedido associado ao Fornecedor A e incluir 10 unidades do Item B. | Status: **201 Created**. O Pedido é salvo. O Estoque do Item B deve ser **aumentado** em 10 unidades. |
| **CT-E-021** | POST /pedidos | Crítica | Negativo | Item B cadastrado. | Tentar criar um Pedido associado a um Fornecedor\_ID inexistente. | Status: **400 Bad Request** ou **404 Not Found**. Mensagem informando que o fornecedor não existe (Conforme Regra de Negócio). |
| **CT-E-022** | POST /pedidos | Crítica | Negativo | Fornecedor A cadastrado. | Tentar criar um Pedido com um Item\_ID inexistente ou inválido. | Status: **400 Bad Request** ou **404 Not Found**. Mensagem informando que o item não existe (Conforme Tabela de Risco). |

### 

### 

### **1.4. Controle de Validade e Baixa de Estoque**

| ID | Módulo / Recurso | Prioridade | Tipo de Teste | Pré-Condições | Passos para Execução | Resultado Esperado |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| **CT-E-030** | POST /estoque/baixa | Crítica | Positivo | Estoque com 20 unidades do Item C. | Enviar solicitação de baixa de 5 unidades do Item C. | Status: **200 OK**. O Estoque do Item C no DB deve ser **diminuído** para 15 unidades. |
| **CT-E-031** | POST /estoque/baixa | Crítica | Negativo | Estoque com 5 unidades do Item C. | Enviar solicitação de baixa de 10 unidades do Item C. | Status: **400 Bad Request**. Mensagem informando Estoque Insuficiente. |
| **CT-E-032** | E2E / Alerta | Alta | Integração | Item D com estoque baixo e alertaCritico configurado. | Atingir o limite de estoque crítico através de uma baixa (CT-E-030). | O serviço de **Cadastro** deve receber e processar o evento de Alerta de Estoque Crítico (Comunicação entre microsserviços). |

## **2\. Casos de Teste \- Módulo CADASTRO**

O módulo Cadastro gerencia Pacientes, Médicos, Convênios e Usuários.

### **2.1. Gerenciamento de Pacientes (/pacientes)**

| ID | Endpoint / Ação | Prioridade | Tipo de Teste | Pré-Condições | Passos para Execução | Resultado Esperado |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| **CT-C-001** | POST /pacientes | Alta | Positivo | N/A | Cadastrar um novo Paciente com todos os campos válidos e statusAtivo: true. | Status: **201 Created**. Paciente registrado no DB. |
| **CT-C-002** | POST /pacientes | Crítica | Negativo | Paciente X cadastrado com CPF '123'. | Tentar cadastrar um novo Paciente com o mesmo CPF '123'. | Status: **409 Conflict**. Mensagem informando CPF já cadastrado. |
| **CT-C-003** | PUT /pacientes/{id} | Alta | Positivo | Paciente Y cadastrado. | Atualizar o Paciente Y para statusAtivo: false. | Status: **200 OK**. O campo statusAtivo no DB é alterado para false. |
| **CT-C-004** | DELETE /pacientes/{id} | Crítica | Negativo | Paciente Z cadastrado e vinculado a um Agendamento A (no módulo Agendamento). | Tentar deletar o Paciente Z. | Status: **409 Conflict**. Mensagem informando que o paciente possui vínculos. |

### 

### 

### 

### 

### 

### 

### 

### 

### 

### 

### **2.2. Gerenciamento de Médicos (/medicos)**

| ID | Endpoint / Ação | Prioridade | Tipo de Teste | Pré-Condições | Passos para Execução | Resultado Esperado |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| **CT-C-010** | POST /medicos | Alta | Negativo | N/A | Tentar cadastrar um Médico com o campo CRM vazio ou inválido. | Status: **400 Bad Request**. Mensagem de erro sobre o campo CRM obrigatório. |
| **CT-C-011** | POST /medicos | Crítica | Negativo | Médico M cadastrado com CRM '456'. | Tentar cadastrar um novo Médico com o mesmo CRM '456'. | Status: **409 Conflict**. Mensagem informando CRM já cadastrado. |

### 

### 

### 

### 

### 

### 

### 

### 

### **2.3. Consulta de Histórico (Integração)**

| ID | Endpoint / Ação | Prioridade | Tipo de Teste | Pré-Condições | Passos para Execução | Resultado Esperado |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| **CT-C-020** | GET /pacientes/{id}/historico | Crítica | Integração | Paciente H cadastrado, e possui Consultas/Exames/Pedidos (de outros módulos). | Solicitar o histórico completo do Paciente H. | Status: **200 OK**. A resposta deve consolidar e retornar os dados de **todos** os microserviços (Agendamento, Estoque, Financeiro, etc.) via comunicação Redis/Cache (Conforme arquitetura de eventos). |

## **3\. Casos de Teste \- Módulo AGENDAMENTO**

O módulo Agendamento gerencia Consultas, Exames, Procedimentos e Bloqueios de Agenda.

### **3.1. Gerenciamento de Consultas (/consultas)**

| ID | Endpoint / Ação | Prioridade | Tipo de Teste | Pré-Condições | Passos para Execução | Resultado Esperado |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| **CT-A-001** | POST /consultas | Crítica | Positivo | Paciente P (Ativo) e Médico M (Disponível) cadastrados. | Agendar uma consulta válida em um horário livre. | Status: **201 Created**. A consulta é salva com StatusAgendamento: AGENDADO. |
| **CT-A-002** | POST /consultas | Crítica | Negativo | Médico M (Disponível) cadastrado. | Tentar agendar uma consulta para um Paciente P que está com statusAtivo: false (no Cadastro). | Status: **400 Bad Request**. Mensagem informando que o paciente está inativo (Regra de Negócio \- RN-005). |
| **CT-A-003** | POST /consultas | Crítica | Negativo | Paciente P (Ativo) e Médico M (Com Bloqueio de Agenda B para o horário). | Tentar agendar uma consulta em um horário bloqueado. | Status: **409 Conflict**. Mensagem informando conflito com Bloqueio de Agenda. |
| **CT-A-004** | PUT /consultas/{id}/cancelar | Alta | Positivo | Consulta C agendada. | Cancelar a Consulta C. | Status: **200 OK**. O status da Consulta C é atualizado para StatusAgendamento: CANCELADO. |

### 

### 

### 

### 

### 

### 

### 

### 

### 

### 

### **3.2. Gerenciamento de Bloqueio de Agenda (/bloqueios-agenda)**

| ID | Endpoint / Ação | Prioridade | Tipo de Teste | Pré-Condições | Passos para Execução | Resultado Esperado |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| **CT-A-010** | POST /bloqueios-agenda | Alta | Positivo | Médico M cadastrado. | Criar um Bloqueio de Agenda para o Médico M no dia X, das 14h às 16h. | Status: **201 Created**. O bloqueio é salvo. |
| **CT-A-011** | POST /bloqueios-agenda | Alta | Negativo | Bloqueio B1 cadastrado (14h-16h). | Tentar criar um Bloqueio B2 que se sobrepõe ao B1 (Ex: 15h-17h). | Status: **409 Conflict**. Mensagem informando sobreposição de bloqueio. |
| **CT-A-012** | PUT /bloqueios-agenda/{id}/desativar | Média | Positivo | Bloqueio B1 cadastrado. | Desativar o Bloqueio B1. | Status: **200 OK**. O horário das 14h às 16h fica disponível para agendamento. |

### **3.3. Fluxo de Atendimento (Iniciar/Finalizar)**

| ID | Endpoint / Ação | Prioridade | Tipo de Teste | Pré-Condições | Passos para Execução | Resultado Esperado |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| **CT-A-020** | PUT /consultas/{id}/iniciar | Alta | Positivo | Consulta C com status AGENDADO. | Iniciar a Consulta C. | Status: **200 OK**. O status da Consulta C é atualizado para StatusAgendamento: EM\_ATENDIMENTO. |
| **CT-A-021** | PUT /consultas/{id}/finalizar | Alta | Positivo | Consulta C com status EM\_ATENDIMENTO. | Finalizar a Consulta C. | Status: **200 OK**. O status da Consulta C é atualizado para StatusAgendamento: FINALIZADO. O evento de histórico da consulta é enviado para o módulo Cadastro. |
| **CT-A-022** | PUT /consultas/{id}/iniciar | Média | Negativo | Consulta C com status CANCELADO. | Tentar iniciar a Consulta C. | Status: **400 Bad Request**. Mensagem informando que consultas canceladas não podem ser iniciadas. |

---

## **4\. Casos de Teste \- Integração e End-to-End (E2E)**

Estes casos simulam o fluxo completo do usuário no Front-end ou dependem da comunicação entre serviços.

| ID | Módulo / Recurso | Prioridade | Tipo de Teste | Pré-Condições | Passos para Execução | Resultado Esperado |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| **CT-INT-001** | E2E Agendamento-Cadastro | Crítica | E2E | Ambiente configurado (Cadastro e Agendamento UP). | 1\. Criar Paciente P no Front-end Cadastro. 2\. Acessar Front-end Agendamento. 3\. Agendar consulta com o Paciente P. | Sucesso na criação do Paciente. O Paciente P deve ser **listado corretamente** na tela de Agendamento. Consulta agendada com sucesso. |
| **CT-INT-002** | E2E Estoque-Agendamento | Alta | E2E | Estoque E com 10 unidades do Item X (Insumo para Procedimento). | 1\. Realizar um Procedimento que consuma 5 unidades do Item X (Baixa Automática). 2\. Consultar o estoque do Item X. | O status do Procedimento muda para **FINALIZADO**. O Estoque E deve ser **atualizado** para 5 unidades (10 \- 5\) no Back-end Estoque. |
| **CT-INT-003** | E2E Regra Crítica | Crítica | Integração | Paciente P agendado para o dia seguinte (Consulta C). | 1\. No Front-end Cadastro, alterar o status do Paciente P para **INATIVO**. 2\. No Front-end Agendamento, tentar **reagendar** a Consulta C. | A atualização para INATIVO é persistida. O reagendamento deve ser **bloqueado** (400 Bad Request), com a mensagem: "Paciente inativo para novos agendamentos". |
| **CT-INT-004** | E2E Front-end UX | Média | Usabilidade | API Estoque fora do ar (Simulação de erro 500). | Tentar acessar a tela de "Gerenciar Pedidos" no Front-end Estoque. | O Front-end deve exibir uma **mensagem de erro amigável** (Ex: "Não foi possível conectar ao serviço de Estoque"), sem quebrar o sistema (UI *crash*). |

