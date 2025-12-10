![image1.png](images/image1.png)

\@startuml

title SimpleHealth - Módulo de Cadastro

skinparam linetype ortho

\' =======================================

\' 1. Entidades Base

\' =======================================

\' Classe Pessoa (Base)

class Pessoa {

\+ nomeCompleto : String

\+ telefone : String

\+ email : String

}

\' Classe Convenio

class Convenio {

\+ nome : String

\+ plano : String

\+ ativo : Boolean

}

\' =======================================

\' 2. Heranças e Especializações

\' =======================================

\' Paciente herda de Pessoa

class Paciente {

\+ dataNascimento : Date

\+ cpf : String {unique}

\--

\+ verificarExistencia()

}

\' Médico herda de Pessoa

class Medico {

\+ crm : String {unique}

\+ especialidade : String

}

\' Usuário herda de Pessoa (Novo)

class Usuario {

\+ login : String {unique}

\+ senha : String

\--

\+ temPermissaoEncaixe() : Boolean

}

\' =======================================

\' 3. Gestão de Perfis e Tipos

\' =======================================

\' Enum para Perfil de Usuário

enum PerfilUsuario {

MEDICO

SECRETARIA

GESTOR

}

\' =======================================

\' 4. Relacionamentos

\' =======================================

\' A. Herança (Generalização/Especialização)

Pessoa \<\|\-- Paciente

Usuario \<\|\-- Medico

Pessoa \<\|\-- Usuario

\' B. Associações

\' 1. Relacionamento Médico-Convênio (Convênios que o médico atende)

Medico \"1\" \-- \"0..\*\" Convenio : atende

\' 2. Relacionamento Paciente-Convênio (Convênios que o paciente possui)
\<\< Novo \>\>

\' Um Paciente pode ter 0 ou muitos convênios. Um Convênio pode ter 0 ou
muitos pacientes.

Paciente \"1\" \-- \"0..\*\" Convenio : possui plano

\' 3. Relacionamento Usuário-Perfil

Usuario \"1\" \-- \"1\" PerfilUsuario : tem

\@enduml

Obs: Adicionar módulo clínico como um submódulo do "Módulo de Cadastro"
para cumprir um requisito de ter um Prontuário Eletrônico do Paciente
(PEP).
