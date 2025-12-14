**Testes de alinhamento \- Avaliação de conformidade documentos**

  **CLASSES DE ANÁLISE / DIAGRAMA DE CLASSES** 

mapeamento resumido: 

\- não foi implementado integração entre módulo de agendamento e estoque, por questões tempo foi retirado do escopo, mas a ideia inicial era que o agendamento se comunicasse com o serviço de estoque para trazer os itens que seriam usados no atendimento.  
\- há a comunicação entre o módulo de cadastro e módulo de agendamento no endpoint de /pacientes/historico/{cpf} do módulo de cadastro

mapeamento detalhado das classes gerais:

\- não foi implementado integração entre módulo de agendamento e estoque, por questões tempo foi retirado do escopo, mas a ideia inicial era que o agendamento se comunicasse com o serviço de estoque para trazer os itens que seriam usados no atendimento.  
\-no diagrama não está representado TODAS as classes do projeto, até porque, durante o desenvolvimento houve a necessidade de criação de outras classes, mas está fornecendo uma boa base de como o projeto está funcionando.

mapeamento classes cadastro:

\- conforme os requisitos, o core está bem representado e de acordo com a implementação

mapeamento classes agendamento:

\- houve necessidades de alterações em algumas classes desse módulo durante a implementação, no entanto o core está bem representado

mapeamento classes estoque:

\- core bem representado e implementação de acordo

  **DOCUMENTO DE VISÃO DO PROJETO**

\- Introdução e contexto sobre o sistema claros  
\- Módulo de cadastro não descrito como um dos Atores  
\- Módulo de gestão financeira foi descartado do escopo e movido para o backlog

Avaliando conformidade Requisitos de alto nível 

\- RNF-01: Não implementado  
\- RNF-02: Implementado  
\- RNF-03: Parcialmente implementado  
\- RNF-04: Implementado     
\- RNF-05: Parcialmente implementado  
\- RNF-06: Não implementado  
\- RNF-07: Implementado  
\- RNF-08: Implementado  
\- RNF-09: Implementado  
\- RNF-10: Implementado  
\- RNF-11: Parcialmente implementado (faltou integração com módulo de estoque)  
\- RNF-12: Parcialmente implementado (não foi implementado o permissionamento)  
\- RNF-13 a \- RNF-18: Não implementados pois o Módulo de gestão financeira foi removido do escopo

Critérios de Sucesso \- Ainda não é possível avaliar, pois necessitaria ser implementado e observado por 90 dias     

Os módulos estão sendo persistidos com as tecnologias descritas

No diagrama de arquitetura Fisica de implementação representa o Servidor Middleware como um serviço a parte, no entanto não foi implementado dessa maneira, e sim rodando o redis em cada um dos serviços

**DESCRIÇÃO DETALHADA DE CASO DE USO**

**UC01: Cadastrar Novo Paciente**

- Não implementamos autenticação por questão de tempo  
- Convênio não está previamente cadastrado

**UC02: Agendar Consulta**

- Ouve algumas modificações em nomes de classes mas o resto está ok

**UC03: Solicitar Encaixe**

- Existe a funcionalidade mas não está segura por uma permissão

**UC04: Registrar Bloqueio de Agenda**

- Não há autenticação  
- Não há bloqueio recorrente

**5\. UC05: Dar Baixa em Insumos**

- Não foi contemplado pois não existe saldo e sistema de pagamento

**6\. UC06: Processar Entrada de NF/Itens**

- Não foi contemplado pois não existe saldo e sistema de pagamento

**7\. UC07: Gerar Alerta de Estoque Crítico**

- Não foi implementado

**8\. UC08: Consultar Histórico do Paciente**

- Não há permissionamento

**9\. UC09: Cancelar Agendamento**

- Fluxo alternativo A1 não contemplado  
- Fluxo e exceção E1 não contemplado  
- RN-AGENDA.3 não contemplado

**10\. UC10: Controlar Validade de Itens**

- Não implementado pois envolve o módulo de pagamento

**11\. UC11: Gerenciar Usuários e Perfis de Acesso**

- Não foi implementado questões de perfis segurança, etc, seria necessário um módulo de autenticação e não haveria tempo

**12\. UC12: Gerenciar Cadastros Base do Sistema**

- Falta validações nos campos 

**DIAGRAMA GLOBAL DE CASOS DE USO**

- Os casos de uso Dar Baixa em Insumos, Processar Entrada de NF/Itens, Controlar Validade de Itens e Gerar Alerta de Estoque Crítico não forem implementados

**DIAGRAMA GLOBAL DE CASOS DE USO**

Agendar consulta: Os dados são passados todos ao mesmo tempo ao invés de selecionar a data antes 

Solicitar encaixe: Não há permissão, apenas salva

Bloqueio de agenda: OK

Cancelar agendamento: OK

Dar baixa em insumos: removido do escopo

Processar entrada de NF: removido do escopo

Gerar Alerta Crítico: Não implementado

Controlar validade: removido do escopo

Cadastro de paciente: OK

Consultar histórico: OK

**ARQUITETURA DO SISTEMA \- LÓGICA E FÍSICA**

- Arquitetura lógica bem representada  
- No diagrama de arquitetura Fisica de implementação representa o Servidor Middleware como um serviço a parte, no entanto não foi implementado dessa maneira, e sim rodando o redis em cada um dos serviços

**MODELAGEM DE CLASSES DO PROJETO**

Diagrama de classes Módulo de agendamento: core bem representado porém houve algumas alterações na implementação

Diagrama de classes cadastro: bem representado e implementado de acordo

Diagrama de classes estoque: bem representado e implementado de acordo

**MODELAGEM DE INTERAÇÕES**

**UC01 \- Cadastrar Novo Paciente** 

- OK

**UC02 \- Agendar Consulta**

- Os dados são passados todos ao mesmo tempo ao invés de selecionar a data antes 

**UC03 \- Solicitar Encaixe**

- Não tem permissões

**UC04 \- Registrar Bloqueio de Agenda**

- OK

**UC05: Dar Baixa em Insumos**

- Não implementada

**UC06: Processar Entrada de NF/Itens**

- Não implementada

**UC07: Gerar Alerta de Estoque Crítico**

- Não implementada

**UC08: Consultar Histórico do Paciente**

- OK

**UC09: Cancelar Agendamento**

- OK

**UC10: Controlar Validade de Itens**

- Não implementado

**MODELAGEM DE ESTADOS**

**Ciclo de Vida do Agendamento \-** Modelagem antiga, inclui a maneira como foi pensado inicialmente o módulo, no entanto foi removido as etapas que envolvem pagamento, e removido notificações por limitação de tecnologia

**Ciclo de Vida do Item de Estoque \-** Modelagem antiga, inclui a maneira como foi pensado inicialmente o módulo, no entanto foi removido as etapas que envolvem pagamento, perfis e permissões

**Ciclo de Vida do Paciente \- OK**

**Ciclo de Vida do Pedido \-** Modelagem antiga, inclui a maneira como foi pensado inicialmente o módulo, no entanto foi removido as etapas que envolvem pagamento, perfis e permissões

**Ciclo de Vida do BlocoAgenda \- OK**

**BOAS PRÁTICAS E PADRÕES DE PROJETOS**

O projeto segue as práticas e padrões sugeridos no documento  
