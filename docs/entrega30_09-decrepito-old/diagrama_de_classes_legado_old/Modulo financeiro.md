![image1.png](images/image1.png)

\@startuml

\' Configurações e Estilos

skinparam class {

BackgroundColor White

ArrowColor Black

BorderColor Black

}

\' Linhas retas (Ortogonais) - PlantUML irá arredondar se necessário
para evitar colisões.

\' REMOVIDO/COMENTADO: skinparam linetype ortho

\' Removendo a configuração de package, pois eles foram removidos.

\' skinparam package {

\' BorderColor Black

\' BackgroundColor LightGray/White

\' }

\' \-\-- Classes de Cadastros/Módulos Externos \-\--

class Agendamento {

\+ id

}

class Medico {

\+ id: int

\+ nome: string

}

class Usuario {

\+ id: int

\+ nome: string

}

class NivelAcesso {

\+ nome: (Gestor, Financeiro, Tesouraria, Atendente)

}

Usuario \"1\" \-- \"1\" NivelAcesso : possui

\' \-\-- Classes de Faturamento (Receita e Despesa) \-\--

abstract class EntidadeFinanceira {

\+ id: int

\+ dataCompetencia: date

\+ valor: decimal \' Valor base (bruto/total)

\+ status: (Pendente, Aprovado/Recebido/Pago, Glosado)

}

class ContaAReceber extends EntidadeFinanceira {

\+ valorLiquido: decimal \' Valor - taxaAdmin

\+ tipoReceita: (Consulta, Procedimento, Outros)

\+ taxaAdmin: decimal

}

class ContaAPagar extends EntidadeFinanceira {

\+ dataVencimento: date

\+ ehRecorrente: boolean

\+ dataAutorizacao: date

}

class LancamentoConvenio {

\+ id: int

\+ valorGuia: decimal

\+ valorRepassado: decimal

\+ valorGlosado: decimal

}

class FormaPagamento {

\+ nome: (Dinheiro, Pix, Credito, Debito)

\+ taxaPercentual: decimal

}

class CategoriaDespesa {

\+ nome: (Aluguel, Salários, RepasseMedico, Outros)

}

class Comprovante {

\+ id: int

\+ urlAnexo: string

}

\' Relacionamentos Internos (Faturamento)

ContaAReceber \"1\" \-- \"1\" FormaPagamento : via

ContaAReceber \"1\" \-- \"0..1\" LancamentoConvenio : seConvenio

ContaAPagar \"1\" \-- \"1\" CategoriaDespesa : pertence

ContaAPagar \"1\" \-- \"0..\*\" Comprovante : possui

\' Repasse Médico

ContaAPagar \"0..\*\" \-- \"1\" Medico : repassePara

\' \-\-- Classes de Controle de Caixa e Bancos \-\--

abstract class Lancamento {

\+ id: int

\+ data: datetime

\+ valor: decimal

\+ tipo: (Entrada, Saida, Transferencia)

}

class LancamentoCaixa extends Lancamento {

\+ conciliado: boolean

}

class Transferencia extends Lancamento {

}

class ContaFinanceira {

\+ id: int

\+ nome: (CaixaFisico, BancoInstitucional, BancoConvenios)

\+ tipo: (Caixa, Banco)

\+ saldoAtual: decimal

}

\' Relacionamentos Internos (Caixa e Bancos)

ContaFinanceira \"1\" \-- \"\*\" LancamentoCaixa : registra

ContaFinanceira \"1\" \-- \"1\" Transferencia : origem

ContaFinanceira \"1\" \-- \"1\" Transferencia : destino

\' \-\-- Relacionamentos Inter-Pacotes (Agora inter-classes) \-\--

Agendamento \"0..\*\" \-- \"1\" ContaAReceber : gera

Agendamento \"0..\*\" \-- \"0..\*\" ContaAPagar : geradaPor (ex: custo
associado)

ContaAPagar \"0..\*\" \-- \"1\" Usuario : aprovador

ContaAPagar \"0..\*\" \-- \"1\" Usuario : baixador

\' Baixa/Recebimento gera o LancamentoCaixa

EntidadeFinanceira \"1\" \-- \"0..1\" LancamentoCaixa : geraMovimentacao

\@enduml
