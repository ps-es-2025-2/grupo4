**Especificação Detalhada dos Casos de Uso**

**Módulo de Autenticação e Segurança**  
Este módulo trata de como é conduzido o acesso dos atores ao sistema.

**UC01: Realizar Login**

NOME: Realizar Login

DESCRIÇÃO SUCINTA: Permite que os usuários (Recepcionista, Corpo Clínico, Faturista) acessem o sistema de forma segura através da validação de credenciais.

ATORES: Recepcionista, Médico, Enfermeiro, Faturista.

PRÉ-CONDIÇÕES:

* O usuário deve possuir um cadastro prévio no sistema.

PÓS-CONDIÇÕES:

* O usuário é autenticado e redirecionado para o menu principal do sistema, de acordo com seu perfil de acesso.  
* O sistema registra o login para fins de auditoria (RN0).

FLUXO BÁSICO:

1. O usuário acessa a tela de login.  
2. O sistema exibe os campos para "Usuário" e "Senha".  
3. O usuário insere suas credenciais.  
4. O usuário clica no botão "Entrar".  
5. O sistema valida as credenciais.  
6. Se as credenciais forem válidas, o sistema redireciona o usuário para a tela inicial correspondente ao seu perfil.  
7. O caso de uso é encerrado.

FLUXOS ALTERNATIVOS:

(A1) Alternativa ao Passo 5 \- Credenciais Inválidas:

1. O sistema exibe a mensagem "Usuário ou senha inválidos. Tente novamente."  
2. O caso de uso retorna ao Passo 2\.

(A2) Alternativa ao Passo 2 \- Esqueci minha senha:

1. O usuário clica na opção "Esqueci minha senha".  
2. O sistema solicita o e-mail ou nome de usuário cadastrado.  
3. O usuário informa o dado solicitado.  
4. O sistema envia um link de redefinição de senha para o e-mail cadastrado do usuário (RN1.1).  
5. O caso de uso é encerrado.

FLUXOS DE EXCEÇÃO:

(E1) Usuário bloqueado:

1. Se, após N tentativas de login falhas, o usuário for bloqueado (RN0.1).  
2. O sistema exibe a mensagem "Sua conta está bloqueada. Entre em contato com o administrador do sistema."  
3. O caso de uso é encerrado.

REGRAS DE NEGÓCIO:

(RN1) O sistema deve bloquear o acesso de um usuário após um número configurável de tentativas de login falhas.  
(RN1.1) O sistema deve enviar um link único e temporário para redefinição de senha para o e-mail cadastrado do usuário.

**Módulo de Atendimento e Recepção**  
Este módulo concentra as funcionalidades da linha de frente do hospital, gerenciando o primeiro contato e o fluxo de pacientes.

**UC02: Gerenciar Cadastro de Pacientes**  
NOME: Gerenciar Cadastro de Pacientes   
DESCRIÇÃO SUCINTA: Permite o registro, a consulta e a atualização das informações demográficas e de convênio dos pacientes.   
ATORES: Recepcionista.   
PRÉ-CONDIÇÕES:

O Recepcionista deve ter executado o caso de uso "Realizar Login". 

PÓS-CONDIÇÕES:

Os dados do paciente são criados ou atualizados no sistema.

Um número de identificação único é gerado para novos pacientes.

FLUXO BÁSICO: 

O Recepcionista seleciona a opção "Gestão de Pacientes".

O sistema exibe a tela de busca de pacientes.

O Recepcionista busca o paciente por nome, CPF ou número de identificação.

Se o paciente não for encontrado, o Recepcionista seleciona "Novo Cadastro" (ver Fluxo Alternativo A1).

Se o paciente for encontrado, o sistema exibe os dados cadastrais.

O Recepcionista atualiza as informações necessárias (endereço, telefone, convênio, etc.).

O Recepcionista salva as alterações.

O sistema valida os dados conforme a regra RN1 e persiste as informações.

O caso de uso é encerrado. 

FLUXOS ALTERNATIVOS: 

(A1) Alternativa ao Passo 4 \- Cadastrar novo paciente: 

O sistema exibe o formulário de cadastro de novo paciente.

O Recepcionista preenche todos os campos obrigatórios.

O sistema retoma ao Passo 7 do fluxo básico. 

REGRAS DE NEGÓCIO: 

(RN2) O CPF do paciente deve ser único na base de dados para evitar duplicidade de cadastros.

**UC03: Agendar Consulta/Procedimento**  
NOME: Agendar Consulta/Procedimento  
DESCRIÇÃO SUCINTA: Realiza o agendamento de consultas e procedimentos, verificando a disponibilidade de médicos e recursos (salas, equipamentos).

ATORES: Recepcionista.  
PRÉ-CONDIÇÕES:

O Recepcionista deve ter executado o caso de uso "Realizar Login". 

O paciente já deve estar cadastrado no sistema (execução prévia do UC01).

PÓS-CONDIÇÕES:

Um novo agendamento é registrado no sistema.

A agenda do médico e do recurso (sala/equipamento) é bloqueada para o horário selecionado.

FLUXO BÁSICO:

O Recepcionista seleciona a opção "Agendamento".

O sistema exibe a tela de busca de paciente para selecioná-lo

O Recepcionista informa a especialidade, o médico ou o procedimento desejado.

O sistema exibe as datas e horários disponíveis.

O Recepcionista seleciona um horário.

O Recepcionista confirma o agendamento.

O sistema registra o agendamento e atualiza as agendas envolvidas.

O sistema emite um comprovante de agendamento.

O caso de uso é encerrado.

FLUXOS ALTERNATIVOS:

(A1) Alternativa ao Passo 4 \- Sem horários disponíveis:

O sistema exibe a mensagem "Não há horários disponíveis para os critérios informados".

O caso de uso retorna ao Passo 3 para uma nova busca.

**Módulo Clínico (PEP \- Prontuário Eletrônico)**  
Este módulo é o coração da operação de saúde, onde todas as informações clínicas do paciente são registradas e consultadas.

**UC04: Acessar Prontuário do Paciente**  
NOME: Acessar Prontuário do Paciente  
DESCRIÇÃO SUCINTA: Permite que a equipe clínica visualize de forma segura e unificada todo o histórico de saúde do paciente.  
ATORES: Corpo Clínico (Médicos, Enfermeiros).  
PRÉ-CONDIÇÕES:

O membro do Corpo Clínico deve ter executado o caso de uso "Realizar Login". 

PÓS-CONDIÇÕES:

As informações do paciente são exibidas para o profissional de saúde.

FLUXO BÁSICO:

O profissional de saúde seleciona a opção "Buscar Paciente".

O sistema exibe a tela de busca.

O profissional busca o paciente por nome, CPF ou número de identificação.

O sistema localiza o paciente e exibe um resumo clínico.

O profissional seleciona o prontuário para visualizar os detalhes (evoluções, prescrições, exames).

O sistema registra o acesso ao prontuário para fins de auditoria (RN2).

O caso de uso é encerrado.

FLUXOS DE EXCEÇÃO:

(E1) Paciente não encontrado:

O sistema exibe a mensagem "Paciente não encontrado".

O caso de uso é encerrado.

REGRAS DE NEGÓCIO:

(RN3) Todo acesso ao prontuário eletrônico deve ser registrado (log) com usuário, data e hora para garantir a rastreabilidade e segurança da informação.

**UC05: Realizar Prescrição Eletrônica**  
NOME: Realizar Prescrição Eletrônica  
DESCRIÇÃO SUCINTA: Permite que médicos criem e gerenciem prescrições de medicamentos de forma digital. Este caso de uso inclui "Acessar Prontuário do Paciente".  
ATORES: Médico.  
PRÉ-CONDIÇÕES:

O Médico deve ter executado o caso de uso "Realizar Login". 

PÓS-CONDIÇÕES:

A prescrição é salva no prontuário do paciente.

Se integrado, um pedido é enviado ao sistema da farmácia.

FLUXO BÁSICO:

Dentro do atendimento, o Médico seleciona a opção "Nova Prescrição".

O sistema exibe a interface de prescrição, mostrando alertas de alergias do paciente (RN3).

O Médico busca o medicamento pelo nome ou princípio ativo.

O Médico seleciona o medicamento e informa a dose, via de administração e frequência.

O Médico adiciona quantos medicamentos forem necessários à receita.

O sistema verifica possíveis interações medicamentosas entre os itens prescritos (RN4).

O Médico finaliza e assina digitalmente a prescrição.

O sistema salva a prescrição no PEP.

O caso de uso é encerrado.

REGRAS DE NEGÓCIO:

(RN4) O sistema deve exibir de forma proeminente as alergias medicamentosas registradas para o paciente.

(RN5) O sistema deve consultar uma base de dados de interações medicamentosas e alertar o médico sobre riscos potenciais.

**Módulo de Faturamento (Escopo estendido: Opcional, condicionado a disponibilidade de tempo)**  
Este módulo lida com os aspectos financeiros do atendimento, desde a conta do paciente até a cobrança dos convênios.

**UC06: Gerar Faturamento para Convênios**  
NOME: Gerar Faturamento para Convênios  
DESCRIÇÃO SUCINTA: Consolida todas as despesas dos pacientes de um determinado convênio e gera o arquivo de faturamento no padrão TISS/TUSS para envio.  
ATORES: Faturista.  
PRÉ-CONDIÇÕES:

O Faturista deve ter executado o caso de uso "Realizar Login". 

As contas dos pacientes devem ter sido fechadas e revisadas.

PÓS-CONDIÇÕES:

Um arquivo XML no padrão TISS/TUSS é gerado.

O status das contas faturadas é alterado para "Enviado ao Convênio".

FLUXO BÁSICO:

O Faturista seleciona a opção "Faturamento de Convênios".

O Faturista seleciona o convênio e o período de competência.

O sistema busca todas as contas de pacientes elegíveis para faturamento no período.

O sistema exibe um resumo dos valores e guias a serem faturadas.

O Faturista confirma a geração do lote de faturamento.

O sistema valida os dados das guias conforme as regras do padrão TISS/TUSS (RN5).

O sistema gera o arquivo XML.

O caso de uso é encerrado.

FLUXOS DE EXCEÇÃO:

(E1) Erro na validação das guias:

O sistema exibe um relatório de inconsistências (ex: código de procedimento inválido, falta de autorização).

O faturamento não é concluído.

O Faturista deve corrigir as inconsistências antes de tentar novamente.

O caso de uso é encerrado.

REGRAS de NEGÓCIO:

(RN6) Todos os procedimentos e materiais cobrados devem estar de acordo com a tabela de preços e regras contratuais negociadas com cada convênio.