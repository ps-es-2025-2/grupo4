# 

**SimpleHealth \-** Sistema de Gerenciamento Hospitalar

**Documento de Visão**

**Versão 1.0**

# 

| Histórico de Revisões |  |  |  |
| :---: | :---: | ----- | ----- |
| **Versão** | **Data** | **Descrição** | **Autor** |
| 1.0 | 07/09/2025 | Criação da versão inicial do documento para o Sistema de Gerenciamento Hospitalar. | Daired Almeida Cruz |
| 1.1 | 08/09/2025 | Revisão para adequação com capacidade do time técnico e da infra do stakeholder | Filipe Paço |
| 1.2 | 09/09/2025 | Revisão Geral | Caio Barbosa |

# 

**Sumário**

[1\. Introdução	4](#introdução)

[2\. Identificação do Projeto	4](#identificação-do-projeto)

[3\. Problema	4](#problema)

[4\. Usuários	5](#usuários)

[5\. Visão Geral do Produto	7](#visão-geral-do-produto)

[6\. Características do Produto	8](#características-do-produto)

[7\. Interface com Outros Sistemas	9](#interface-com-outros-sistemas)

[8\. Restrições Impostas	10](#restrições-impostas)

[9\. Precedência e Prioridade	10](#precedência-e-prioridade)

[10\. Requisitos de Documentação	10](#requisitos-de-documentação)

[11\. Anexos	11](#anexos)

[12\. Referências	11](#heading=h.bvdyv35jprvy)

[13\. Aprovações	12](#heading=h.w6pzvwo7cddv)

	**Documento de Visão**	

1. # **Introdução**

Este documento fornece uma visão geral do projeto do Sistema de Gerenciamento Hospitalar (SGH). Sua finalidade é descrever o problema a ser resolvido, o público-alvo, as funcionalidades e as restrições do sistema, servindo como um guia estratégico para as equipes de desenvolvimento, gestão e para os stakeholders do projeto.

2. # **Identificação do Projeto**

| Projeto | SGH – Sistema de Gerenciamento Hospitalar |
| :---- | :---- |
| **Requisitante** | Hospital |
| **Gerente de Projetos** | Daired Almeida Cruz |

3. # **Problema**

Esta seção detalha o problema que o SGH visa solucionar, identificando os envolvidos e o escopo geral.

1. ## **Resumo do Negócio**

A gestão de hospitais e clínicas envolve uma alta complexidade de processos, desde o atendimento inicial ao paciente até a gestão de faturamento, estoque e prontuários. A ausência de um sistema integrado resulta em fragmentação da informação, processos manuais propensos a erros, dificuldades na comunicação entre departamentos e, consequentemente, em uma experiência menos eficiente para pacientes e para a equipe de saúde.

2. ## **Problemas**

| O problema de | Falta de integração entre os dados clínicos, administrativos e financeiros, resultando em retrabalho, inconsistência de informações e dificuldades na tomada de decisão gerencial. |
| :---- | :---- |
| **Afeta** | Pacientes, médicos, enfermeiros, recepcionistas, administradores hospitalares e faturistas. |
| **Cujo impacto é** | Atrasos no atendimento, erros em prontuários e prescrições, glosas em faturamentos de convênios, gestão de estoque ineficiente e dificuldade para gerar relatórios de desempenho e indicadores de saúde. |
| **Benefícios de uma solução seriam** | Centralização das informações do paciente em um Prontuário Eletrônico único, otimização do agendamento de consultas e procedimentos, redução de erros operacionais, agilização do processo de faturamento e acesso a dados consolidados para gestão estratégica. |

4. #  **Usuários**

   1. ## **Resumo dos Usuários**

| Nome | Descrição | Responsabilidades |
| ----- | ----- | ----- |
| Recepcionista | Ponto de contato inicial do paciente com o hospital. Responsável pelo cadastro, agendamento e direcionamento. | Cadastrar e atualizar dados demográficos dos pacientes. Agendar, confirmar e cancelar consultas e exames. Realizar o check-in e check-out de pacientes.  Gerenciar o fluxo de atendimento na recepção. |
| Corpo Clínico (Médicos, Enfermeiros) | Profissionais responsáveis pelo cuidado direto ao paciente, desde o diagnóstico até o tratamento e acompanhamento. | • Acessar e atualizar o Prontuário Eletrônico do Paciente (PEP). Realizar prescrições de medicamentos e exames. Registrar evoluções clínicas e planos de tratamento. Consultar históricos e resultados de exames. |
| Administrador Hospitalar | Responsável pela gestão estratégica e operacional da instituição. | Monitorar indicadores de desempenho (taxa de ocupação, faturamento, custos). Gerar relatórios financeiros, clínicos e operacionais. Gerenciar permissões de acesso dos usuários ao sistema. |
| Faturista | Responsável pela gestão das contas dos pacientes e pelo faturamento junto aos convênios e planos de saúde. | Lançar procedimentos realizados para a conta do paciente. Gerar e enviar arquivos de faturamento para convênios (padrão TISS/TUSS). Controlar e tratar glosas. |

   2. ## **Ambiente do Usuário**

O sistema será uma plataforma web responsiva, acessível por meio de navegadores em desktops e dispositivos móveis. Inicialmente, não há restrições de hardware ou software específicas além da necessidade de acesso à internet. O sistema deverá interagir com aplicativos externos para processamento de agendamentos e logística. 

3. ## **Necessidades dos Interessados**

| Crítico | Requisitos essenciais ou o fracasso em sua implementação significa que o sistema não irá atender as necessidades do cliente. Imprescindível que seja atendido pelo sistema, condição fundamental para o sucesso do projeto.  |
| :---: | :---- |
| Importante | Requisitos importantes para a eficácia ou eficiência do sistema. Sua não implementação afeta a satisfação do usuário e/ou o valor agregado do produto. Afeta a satisfação do usuário significativamente, mas o não atendimento não determina o fracasso do projeto. |
| Útil | Requisitos úteis, porém menos críticos, sendo usados menos frequentemente. Não possui muito significado para a satisfação do usuário e pode deixar de ser atendida. |

| Necessidade | Prioridade | Preocupações | Solução Atual | Soluções Propostas |
| ----- | ----- | ----- | ----- | ----- |
| O **Corpo Clínico** necessita de **acesso rápido e unificado ao histórico completo do paciente**. | Crítico | Perda de tempo buscando informações em prontuários de papel ou sistemas diferentes. Risco de decisões clínicas baseadas em informações desatualizadas. | Prontuários físicos. Múltiplas planilhas e sistemas legados sem integração. | • Prontuário Eletrônico do Paciente (PEP) centralizado, com todo o histórico clínico, exames e prescrições.   |
| A **Administração** necessita de **dados consolidados para tomar decisões estratégicas**. | Crítico  | Dificuldade para obter uma visão geral da saúde financeira e operacional do hospital. Demora na geração de relatórios. | • Extração manual de dados de diversas fontes e compilação em planilhas.  | • Módulo de Business Intelligence (BI) com dashboards e relatórios customizáveis em tempo real. |
| A **Recepção** necessita de um **sistema ágil para gerenciar o fluxo de pacientes**. | Importante, porém não prioritário na entrega inicial.  | Filas na recepção. Erros de agendamento e cadastro duplicado de pacientes. | • Agendas em papel ou sistemas simples de agendamento. | • Módulo de agendamento integrado, com busca inteligente de pacientes e gestão de filas de atendimento. |

5. # **Visão Geral do Produto**

Esta seção descreve o produto que solucionará o problema, indicando suas principais capacidades para atender às necessidades dos usuários. 

1. ## **Perspectiva do Produto**

O SGH será uma plataforma web modular e integrada, projetada para ser o sistema central de operações de um hospital ou clínica. Ele substituirá processos manuais e sistemas legados, integrando-se a sistemas externos essenciais, como os de laboratórios (LIS) e de diagnóstico por imagem (RIS/PACS).

2. ## **Resumo das Capacidades do Produto**

| Benefícios para o Cliente | Recursos do Sistema |
| ----- | ----- |
| Melhoria na segurança e qualidade do atendimento ao paciente.  | Prontuário Eletrônico do Paciente (PEP) com validações e alertas clínicos. Módulo de prescrição eletrônica com checagem de interações medicamentosas. |
| Aumento da eficiência operacional e redução de custos. | Agendamento inteligente de consultas e recursos (salas, equipamentos). Gestão de estoque de materiais e medicamentos. Automação do ciclo de faturamento e controle de glosas. |
| Gestão baseada em dados e visão estratégica. | Geração de relatórios gerenciais e dashboards com indicadores de desempenho (KPIs) em tempo real. |

6. # **Características do Produto**

A seguir, são listadas as principais características que o sistema deverá fornecer. 

1. ## **O sistema deve gerenciar o cadastro completo de pacientes** 

Permitirá o registro de dados demográficos, contatos, informações de convênio e histórico de atendimentos, evitando duplicidade de registros.

2. ## **O sistema deve fornecer um Prontuário Eletrônico do Paciente (PEP)** 

Centralizará todo o histórico clínico, incluindo anamneses, prescrições e alergias.

3. ## **O sistema deve permitir o agendamento de consultas e procedimentos** 

Disponibilizará uma agenda (por profissional, por especialidade) com controle de disponibilidade e confirmação automática.

4. ## **O sistema deve gerenciar o estoque de insumos e medicamentos** 

Controlará a entrada, saída e validade de itens, com alertas de estoque baixo e integração com o módulo de prescrição.

7. # **Interface com Outros Sistemas**

O SGH (Sistema A) precisará interagir com os seguintes sistemas externos (Sistema B):

| Sistema | Descrição | Tipo da Integração |
| ----- | ----- | ----- |
| LIS (Laboratory Information System) | Receber os resultados de exames laboratoriais e anexá-los automaticamente ao prontuário do paciente correspondente no SGH. | Tipo 4 |
| RIS/PACS (Radiology Information System) | Recuperar laudos e imagens de exames de diagnóstico por imagem para visualização dentro do prontuário do paciente no SGH. | Tipo 1 |
| Sistema da Farmácia | Enviar prescrições eletrônicas para o sistema da farmácia e consultar a disponibilidade de medicamentos. | Tipo 3 |

**Sistema A**: Sistema objeto desse documento de escopo preliminar.  
**Sistema B**: Sistema a se integrar com o sistema A  
Tipo 1: Sistema A consulta dados/informações no sistema B;  
Tipo 2 : Sistema A atualiza dados/informações no sistema B;  
Tipo 3 : Sistema A gera dados/informações para processamento no sistema B;  
Tipo 4 : Sistema A processa dados/informações gerado por B.

8. # **Restrições Impostas**

   1. ## **Tecnologia:** 

O sistema deve ser desenvolvido em plataforma web para ser acessível a partir de qualquer dispositivo com navegador, dentro da rede do hospital.

2. ## **Sistemas:**

Deve prever futura compatibilidade com os padrões de interoperabilidade em saúde, como TISS/TUSS, para a comunicação com convênios.

3. ##  **Ambiente:** 

O sistema deve cumprir rigorosamente os requisitos da ***Lei Geral de Proteção de Dados (LGPD)***, garantindo a segurança e a privacidade das informações dos pacientes.

9. # **Precedência e Prioridade**

As características do sistema serão desenvolvidas com a seguinte ordem de prioridade:

* **Crítico:** Módulo de Cadastro de Pacientes, Agendamento e Prontuário Eletrônico.  
* **Importante:**  Módulo de Relatórios Gerenciais e Dashboards (BI) e Gestão de Estoque.  
* **Útil:**, integrações com sistemas externos. Módulo de Faturamento 

10. # **Requisitos de Documentação**

    1. ## **Manual do Usuário**

Serão criados manuais detalhados por perfil de usuário (Recepção, Enfermagem, Médico, etc.), com tutoriais passo a passo das principais funcionalidades.

2. ## **Ajuda On-Line**

O sistema contará com uma base de conhecimento interna e tooltips de ajuda contextual para auxiliar o usuário durante a operação.

3. ## **Guias de Instalação, de Configuração e arquivo Leia-me**

Não se aplica para o usuário final, mas a documentação técnica de implantação do sistema será criada para a equipe de desenvolvimento.

11. # **Anexos**

**Diagrama global de Casos de Uso:**  
[https://docs.google.com/document/d/1fEQroDgipLvfAld\_bi73r9KCkizuUwwNvFLKwLfbXII/edit?tab=t.0](https://docs.google.com/document/d/1fEQroDgipLvfAld_bi73r9KCkizuUwwNvFLKwLfbXII/edit?tab=t.0)

**Especificação detalhada dos Casos de Uso:**  
[https://docs.google.com/document/d/1bvxIqCcdE19H17sVnR\_TSu8AAMFqRAbwIoxKplFqZJE/edit?tab=t.0](https://docs.google.com/document/d/1bvxIqCcdE19H17sVnR_TSu8AAMFqRAbwIoxKplFqZJE/edit?tab=t.0)