# Diagrama Global de Casos de Uso — SGH

Este documento apresenta uma visão consolidada dos **casos de uso** do **Sistema de Gestão Hospitalar (SGH)**, estruturados em módulos principais.

Ele fornece uma **visão unificada** das funcionalidades relacionadas a:

- Acesso e Segurança  
- Atendimento e Recepção  
- Clínico (PEP – Prontuário Eletrônico)  
- Faturamento  
- Administração  

Todos os módulos possuem como pré-requisito o **login obrigatório**, garantindo que apenas usuários autenticados interajam com o sistema.

---

## Diagrama Global
![Diagrama Global](https://github.com/ps-es-2025-2/grupo4/blob/develop/DiagramaGlobalCasosDeUso.png)

---

## Módulo de Acesso e Segurança

Este módulo concentra o **fluxo de autenticação** e funcionalidades de segurança do usuário.

- Todos os perfis (**Recepcionista, Corpo Clínico, Faturista, Administrador**) são **generalizações do ator "Usuário do Sistema"**.

### Funcionalidades
- **Realizar Login**: autenticação para acessar o sistema.  
- **Recuperar Senha**: recuperação de acesso em caso de esquecimento.  
- **Trocar Senha**: alteração periódica da senha para maior segurança.  

![Módulo de Acesso e Segurança](https://github.com/ps-es-2025-2/grupo4/blob/develop/DiagramaSeguranca.png)

---

## Módulo de Atendimento e Recepção

Este módulo cobre as atividades relacionadas ao **cadastro, agendamento e recepção de pacientes**.

### Funcionalidades
- **Gerenciar Cadastro de Pacientes**: criação e atualização de informações.  
- **Agendar Consulta/Procedimento**: marcação de atendimentos, dependente do cadastro prévio.  
- **Registrar Check-in de Paciente**: confirmação da presença no atendimento.  
- **Emitir Relatórios de Atendimentos**: geração de estatísticas e listas de consultas realizadas.

Todos os casos de uso exigem que o usuário esteja autenticado.

![Módulo de Atendimento e Recepção](https://github.com/ps-es-2025-2/grupo4/blob/develop/DiagramaAtendimento.png)

---

## Módulo Clínico (PEP – Prontuário Eletrônico)

Este módulo descreve as interações da **equipe clínica** com o prontuário eletrônico do paciente, incluindo integrações externas.

### Funcionalidades
- **Acessar Prontuário do Paciente**: acesso central às informações clínicas.  
- **Registrar Atendimento Clínico**: inclusão de procedimentos e observações.  
- **Registrar Evolução Clínica**: anotações contínuas do tratamento.  
- **Solicitar Exames**: requisições de exames laboratoriais ou de imagem.  
- **Realizar Prescrição Eletrônica**: emissão de receitas médicas e exames.  
- **Visualizar Resultados de Exames**: consulta de laudos laboratoriais e radiológicos.

### Integrações Externas
- **LIS**: Laboratório.  
- **RIS/PACS**: Radiologia e imagens.

Todos os casos dependem de **login** e, quando aplicável, do **acesso ao prontuário**.

![Módulo Clínico](https://github.com/ps-es-2025-2/grupo4/blob/develop/DiagramaClinico.png)

---

## Módulo de Faturamento

Este módulo cobre os processos financeiros e de controle administrativo.

### Funcionalidades
- **Gerenciar Contas de Pacientes**: controle de cobranças individuais.  
- **Gerar Faturamento para Convênios**: processamento de pagamentos e repasses.  
- **Gerenciar Convênios/Planos de Saúde**: cadastro e manutenção de convênios.  
- **Emitir Relatórios Financeiros**: conferência e controle do faturamento.

Todos os casos de uso exigem **login** e, quando necessário, o **gerenciamento prévio das contas dos pacientes**.

![Módulo de Faturamento](https://github.com/ps-es-2025-2/grupo4/blob/develop/DiagramaFaturamento.png)
