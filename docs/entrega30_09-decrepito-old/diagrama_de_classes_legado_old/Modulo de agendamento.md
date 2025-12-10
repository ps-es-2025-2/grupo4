![image1.png](images/image1.png)

\@startuml

title SimpleHealth - Módulo de Agendamento

skinparam linetype ortho

\'
\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\--

\' Entidades Necessárias (Módulo de Cadastro)

\'
\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\--

\' Paciente (Cadastro)

class Paciente {

\+ cpf : String {unique}

\...

}

\' Médico (Cadastro)

class Medico {

\+ crm : String {unique}

\...

}

\' Convênio (Cadastro)

class Convenio {

\+ nome : String

\...

}

\' Usuário (Cadastro/Auditoria)

class Usuario {

\+ login : String

\...

}

\'
\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\--

\' Entidade Externa (Módulo de Estoque)

\'
\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\--

\' Item (Referenciado - Módulo de Estoque)

class Item {

\+ nome : String

\' Sem outros atributos, conforme solicitado.

}

\'
\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\--

\' Agendamento e Tipos (Herança)

\'
\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\--

class Agendamento {

\+ id : Integer

\+ dataHoraInicio : DateTime

\+ dataHoraFim : DateTime

\+ isEncaixe : Boolean

\' O tipo passa a ser implícito pela subclasse (e.g., Consulta,
Procedimento)

\+ modalidade: Enum\[presencial,remota\]

\+ motivoEncaixe : String \[Opcional\]

\+ observacoes : String \[Opcional\]

}

\' Herança (Generalização/Especialização)

Agendamento \<\|\-- Consulta

Agendamento \<\|\-- Procedimento

Agendamento \<\|\-- Exame

\' Tipo Específico: Consulta

class Consulta {

\+ especialidade : String

\+ tipoConsulta : Enum\[primeira, retorno, rotina\]

}

\' Tipo Específico: Procedimento

class Procedimento {

\+ descricaoProcedimento : String

\+ salaEquipamentoNecessario : String \[Opcional\]

\+ nivelRisco: String

}

\' Tipo Específico: Exame

class Exame {

\+ nomeExame : String

\+ requerPreparo : Boolean

\+ instrucoesPreparo : String \[Opcional\]

}

\' Classe de Associação para Itens (N:N entre Procedimento e Item)

class ProcedimentoItem {

\+ quantidadeNecessaria : Decimal

\+ observacoesUso : String \[Opcional\]

}

\'
\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\--

\' Gestão de Agenda e Bloqueios

\'
\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\--

class BloqueioAgenda {

\+ dataInicio : DateTime

\+ dataFim : DateTime

\+ motivo : String

\+ antecedenciaMinima : Integer \[dias\]

}

\'
\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\--

\' Relações de Agendamento/Agenda

\'
\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\--

\' Bloqueios e Médico (Gerenciamento de Agenda)

Medico \"1\" o\-- \"0..\*\" BloqueioAgenda : registra

\' Agendamento Básico (Relações comuns a todos os tipos)

Agendamento \"1\" \-- \"1\" Medico : agenda_para

Agendamento \"1\" \-- \"1\" Paciente : paciente_em

Agendamento \"1\" \-- \"0..1\" Convenio : cobre_por

\' Auditoria/Criação do Agendamento (Usuário responsável)

Agendamento \"0..\*\" \<\-- \"1\" Usuario : criado_por

\' Relação Específica de Procedimento com Estoque (N:N)

Procedimento \"1\" o\-- \"0..\*\" ProcedimentoItem : requer

Item \"1\" o\-- \"0..\*\" ProcedimentoItem : é_componente_de

\@enduml
