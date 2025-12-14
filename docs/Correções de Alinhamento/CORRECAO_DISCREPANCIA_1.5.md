# Correção da Discrepância 1.5 - Atributos Extras de Rastreamento no Agendamento

**Data:** 14/12/2025  
**Responsável:** Equipe de Documentação  
**Status:** ✅ Concluída

---

## 1. Discrepância Identificada

### Problema
A documentação **NÃO mostrava os campos de rastreamento** na classe `Agendamento`, mas a implementação possui campos completos para rastreabilidade de entidades e ações.

### Campos Faltantes na Documentação

#### Rastreamento de Entidades (Foreign Keys para outros microsserviços):
- `pacienteCpf` - Referência ao paciente (Microsserviço Cadastro)
- `medicoCrm` - Referência ao médico (Microsserviço Cadastro)
- `convenioNome` - Referência ao convênio (Microsserviço Cadastro)

#### Rastreamento de Ações (Auditoria completa):
- `usuarioCriadorLogin` - Usuário que criou o agendamento
- `usuarioCanceladorLogin` - Usuário que cancelou o agendamento
- `usuarioIniciouServicoLogin` - Usuário que iniciou o atendimento
- `usuarioFinalizouServicoLogin` - Usuário que finalizou o atendamento

---

## 2. Análise da Situação

### Implementação Completa vs Documentação Incompleta

A implementação é **COMPLETA e SUPERIOR** ao que estava documentado, com rastreabilidade total.

#### ✅ Arquitetura de Microsserviços (Referências FK)

**Documentação:** Não mostrava como `Agendamento` se conecta com outras entidades

**Implementação:**
```java
@Field("paciente_cpf")
private String pacienteCpf;

@Field("medico_crm")
private String medicoCrm;

@Field("convenio_nome")
private String convenioNome;
```

**Benefícios:**
- ✅ **Desacoplamento:** Agendamento não depende diretamente de Cadastro
- ✅ **Comunicação assíncrona:** Usa Redis Pub/Sub para sincronização
- ✅ **Tolerância a falhas:** Se Cadastro cair, Agendamento continua funcionando
- ✅ **Performance:** Não precisa fazer chamadas HTTP para cada consulta
- ✅ **MongoDB:** Armazena referências como strings (desnormalizado para performance)

**Padrão Arquitetural:**
```
Microsserviço Cadastro (PostgreSQL)
    └─> Publica eventos Redis: "paciente.criado", "medico.atualizado"
         └─> Microsserviço Agendamento (MongoDB) escuta e armazena CPF/CRM
```

#### ✅ Auditoria Completa de Ações

**Documentação:** Não especificado

**Implementação:**
```java
@Field("usuario_criador_login")
private String usuarioCriadorLogin;

@Field("usuario_cancelador_login")
private String usuarioCanceladorLogin;

@Field("usuario_iniciou_servico_login")
private String usuarioIniciouServicoLogin;

@Field("usuario_finalizou_servico_login")
private String usuarioFinalizouServicoLogin;
```

**Benefícios:**
- ✅ **Rastreabilidade:** Sabe-se quem fez cada ação
- ✅ **Auditoria:** Conformidade com LGPD e boas práticas
- ✅ **Responsabilização:** Identificação de responsáveis por ações
- ✅ **Governança:** Controle de acesso e operações
- ✅ **Histórico:** Timeline completa do ciclo de vida

**Exemplo de Ciclo de Vida Rastreado:**
```
Agendamento #123456
├─ Criado por: secretaria.maria (10:30)
├─ Iniciado por: medico.silva (14:15)
├─ Finalizado por: medico.silva (14:50)
└─ Cancelado por: N/A

Agendamento #123457
├─ Criado por: secretaria.joao (09:00)
└─ Cancelado por: secretaria.joao (09:45) - Motivo: Paciente não compareceu
```

---

## 3. Decisão Técnica

### ✅ Decisão: Atualizar Documentação

**Ação:** Corrigir documentação para incluir **todos os campos de rastreamento** implementados.

**Justificativa:**
1. Implementação está **correta e completa**
2. Campos são **essenciais** para operação em microsserviços
3. Auditoria é **requisito não-funcional** crítico
4. Rastreabilidade melhora **qualidade e governança**

---

## 4. Arquivos Corrigidos

### 4.1. Classes de Análise - Diagrama de Classes (3 Ocorrências)

**Arquivo:** `docs/documentos-finais-definitivos/3.4. Classes de Análise/3.4. Classes de Análise_Diagrama de Classes.md`

#### Ocorrência 1: Diagrama Modular Simplificado (linha ~140)

**Alterações:**
- ✅ Adicionados 4 campos de rastreamento na visão simplificada
- ✅ Foco nos campos essenciais para integração entre módulos

**Antes:**
```plantuml
class Agendamento <<Entity>> {
+ id : Integer
+ dataHoraInicio : DateTime
+ motivoEncaixe : String [Opcional]
--
+ isEncaixe() : Boolean
}
```

**Depois:**
```plantuml
class Agendamento <<Entity>> {
+ id : Integer
+ dataHoraInicio : DateTime
+ motivoEncaixe : String [Opcional]
+ pacienteCpf : String
+ medicoCrm : String
+ convenioNome : String
+ usuarioCriadorLogin : String
--
+ isEncaixe() : Boolean
}
```

**Nota:** Visão simplificada mostra apenas campos essenciais para integração.

---

#### Ocorrência 2: Diagrama Completo (linha ~424)

**Alterações:**
- ✅ Adicionados 7 campos de rastreamento completos
- ✅ Todos os campos de FK para entidades
- ✅ Todos os campos de auditoria de ações

**Antes:**
```plantuml
class Agendamento {
+ id : Integer
+ dataHoraAgendamento : DateTime
+ dataHoraInicioPrevista : DateTime
+ dataHoraFimPrevista : DateTime
+ dataHoraInicioExecucao : DateTime
+ dataHoraFimExecucao : DateTime
+ isEncaixe : Boolean
+ modalidade : ModalidadeEnum
+ motivoEncaixe : String [Opcional]
+ observacoes : String
+ status : StatusAgendamentoEnum
+ motivoCancelamento : String
+ dataCancelamento : DateTime
}
```

**Depois:**
```plantuml
class Agendamento {
+ id : Integer
+ dataHoraAgendamento : DateTime
+ dataHoraInicioPrevista : DateTime
+ dataHoraFimPrevista : DateTime
+ dataHoraInicioExecucao : DateTime
+ dataHoraFimExecucao : DateTime
+ isEncaixe : Boolean
+ modalidade : ModalidadeEnum
+ motivoEncaixe : String [Opcional]
+ observacoes : String
+ status : StatusAgendamentoEnum
+ motivoCancelamento : String
+ dataCancelamento : DateTime
+ pacienteCpf : String
+ medicoCrm : String
+ convenioNome : String
+ usuarioCriadorLogin : String
+ usuarioCanceladorLogin : String
+ usuarioIniciouServicoLogin : String
+ usuarioFinalizouServicoLogin : String
}
```

---

#### Ocorrência 3: Diagrama do Módulo de Agendamento (linha ~908)

**Alterações:**
- ✅ Idêntico à Ocorrência 2
- ✅ Mantém consistência entre diagramas

**Resultado:** Total de **20 atributos** documentados (vs 13 anteriormente).

---

### 4.2. Nota Técnica Adicionada

**Arquivo:** `docs/documentos-finais-definitivos/3.4. Classes de Análise/3.4. Classes de Análise_Diagrama de Classes.md`

**Conteúdo da Nota:**
```markdown
> **⚠️ NOTA TÉCNICA - Discrepância 1.5 Resolvida:**
> 
> **Discrepância Identificada:** A documentação não mostrava os campos de rastreamento 
> (pacienteCpf, medicoCrm, convenioNome, usuarioCriadorLogin, usuarioCanceladorLogin, 
> usuarioIniciouServicoLogin, usuarioFinalizouServicoLogin) na classe `Agendamento`.
> 
> **Implementação Real (Completa):**
> - ✅ Rastreamento de entidades: pacienteCpf, medicoCrm, convenioNome 
>       (referências FK a outros microsserviços)
> - ✅ Rastreamento de ações: usuarioCriadorLogin, usuarioCanceladorLogin, 
>       usuarioIniciouServicoLogin, usuarioFinalizouServicoLogin
> - ✅ Auditoria completa do ciclo de vida do agendamento
> 
> **Decisão:** Documentação atualizada para incluir todos os campos de rastreamento implementados.
> 
> Data da correção: 14/12/2025
```

---

## 5. Validação

### 5.1. Código Backend Verificado

#### Implementação Completa:
```java
// Agendamento.java (MongoDB)
@Data
@Document(collection = "agendamento")
public abstract class Agendamento {
    @Id
    private String id;
    
    // ... outros campos (timestamps, controle, etc.) ...
    
    // RASTREAMENTO DE ENTIDADES (Foreign Keys)
    @Field("paciente_cpf")
    private String pacienteCpf;
    
    @Field("medico_crm")
    private String medicoCrm;
    
    @Field("convenio_nome")
    private String convenioNome;
    
    // RASTREAMENTO DE AÇÕES (Auditoria)
    @Field("usuario_criador_login")
    private String usuarioCriadorLogin;
    
    @Field("usuario_cancelador_login")
    private String usuarioCanceladorLogin;
    
    @Field("usuario_iniciou_servico_login")
    private String usuarioIniciouServicoLogin;
    
    @Field("usuario_finalizou_servico_login")
    private String usuarioFinalizouServicoLogin;
}
```

### 5.2. Documentação Alinhada
- ✅ Classes de Análise atualizadas (3 diagramas)
- ✅ Nota técnica adicionada
- ✅ Todos os campos de rastreamento documentados
- ✅ Documentação reflete implementação completa

### 5.3. Arquivo 3.7 Já Estava Correto

**Arquivo:** `docs/documentos-finais-definitivos/3.7_3.9_3.10_Modelagens/3.7. Modelagem de Classes de Projeto/3.7. Modelagem de Classes de Projeto.md`

**Verificação:**
- ✅ Já possuía todos os 7 campos de rastreamento
- ✅ Corrigido anteriormente na Discrepância 1.4
- ✅ Nenhuma alteração necessária

**Código UML (3.7 - já correto):**
```plantuml
abstract class Agendamento <<abstract>> { 
    - id: String {PK}
    ...
    -- 
    {field} Referências a outros microsserviços: 
    - pacienteCpf: String {FK} 
    - medicoCrm: String {FK} 
    - convenioNome: String {FK} 
    - usuarioCriadorLogin: String {FK} 
    - usuarioCanceladorLogin: String {FK}
    - usuarioIniciouServicoLogin: String {FK}
    - usuarioFinalizouServicoLogin: String {FK}
}
```

---

## 6. Comparação Detalhada

| Campo | Tipo | Propósito | Documentação Antes | Documentação Depois |
|-------|------|-----------|-------------------|---------------------|
| `pacienteCpf` | String (FK) | Referência ao Paciente | ❌ Ausente | ✅ Documentado |
| `medicoCrm` | String (FK) | Referência ao Médico | ❌ Ausente | ✅ Documentado |
| `convenioNome` | String (FK) | Referência ao Convênio | ❌ Ausente | ✅ Documentado |
| `usuarioCriadorLogin` | String | Quem criou | ❌ Ausente | ✅ Documentado |
| `usuarioCanceladorLogin` | String | Quem cancelou | ❌ Ausente | ✅ Documentado |
| `usuarioIniciouServicoLogin` | String | Quem iniciou | ❌ Ausente | ✅ Documentado |
| `usuarioFinalizouServicoLogin` | String | Quem finalizou | ❌ Ausente | ✅ Documentado |

**Legenda:**
- ❌ Ausente - Não estava na documentação
- ✅ Documentado - Agora incluído

---

## 7. Benefícios da Correção

### 7.1. Arquitetura de Microsserviços

#### Desacoplamento Adequado
```
Cadastro (PostgreSQL)          Agendamento (MongoDB)
├─ Paciente                    ├─ Consulta
│  └─ cpf (PK)                │  └─ pacienteCpf (String)
├─ Medico                      ├─ Exame
│  └─ crm (PK)                │  └─ medicoCrm (String)
└─ Convenio                    └─ Procedimento
   └─ nome (PK)                   └─ convenioNome (String)

Comunicação via Redis Pub/Sub
```

**Vantagens:**
- ✅ Cada módulo tem seu banco de dados
- ✅ Comunicação assíncrona
- ✅ Tolerância a falhas
- ✅ Performance (sem joins entre bases)

#### Sincronização via Eventos
```java
// Quando um paciente é criado no Cadastro:
historicoPublisher.publicar("paciente.criado", {
    cpf: "123.456.789-00",
    nome: "João Silva"
});

// Agendamento recebe e pode armazenar a referência
```

### 7.2. Auditoria e Governança

#### Timeline Completa
```
Agendamento #123456 - Consulta Cardiologia

10:30 - Criado por secretaria.maria
        └─ Paciente: João Silva (CPF 123.456.789-00)
        └─ Médico: Dr. Carlos (CRM 12345)
        └─ Convênio: Unimed

14:15 - Iniciado por medico.carlos
        └─ Atraso: 15 minutos

14:50 - Finalizado por medico.carlos
        └─ Duração: 35 minutos (5 min extra)

Auditoria completa: ✅
```

#### Conformidade LGPD
- ✅ Rastreamento de acesso a dados pessoais
- ✅ Identificação de responsáveis
- ✅ Histórico de operações

### 7.3. Operacionais

#### Relatórios Possíveis
- Agendamentos por médico
- Agendamentos por convênio
- Taxa de cancelamento por usuário
- Pontualidade por médico
- Tempo de atendimento por especialidade

#### Análises
- Quem mais cria agendamentos (secretários eficientes)
- Quem mais cancela (identificar padrões)
- Tempos médios de atendimento

---

## 8. Padrões de Projeto Relacionados

### 8.1. Saga Pattern (Eventos Distribuídos)

**Cenário:** Criação de Agendamento
```
1. Usuario cria agendamento via API Agendamento
2. Agendamento valida se pacienteCpf existe (cache local ou consulta)
3. Agendamento valida se medicoCrm existe
4. Agendamento cria registro no MongoDB
5. Agendamento publica evento "agendamento.criado" no Redis
6. Outros módulos podem reagir (ex: Estoque reserva materiais)
```

### 8.2. Event Sourcing (Auditoria)

**Rastreamento de Ações:**
```java
// Cada mudança de estado registra o usuário responsável
agendamento.setUsuarioCriadorLogin("secretaria.maria");
agendamento.setStatus(StatusAgendamentoEnum.ATIVO);

// Se cancelar:
agendamento.setUsuarioCanceladorLogin("secretaria.joao");
agendamento.setDataCancelamento(LocalDateTime.now());
agendamento.setStatus(StatusAgendamentoEnum.CANCELADO);

// Timeline reconstruível:
Evento 1: Criado por secretaria.maria às 10:30
Evento 2: Cancelado por secretaria.joao às 10:45
```

### 8.3. Database per Service (Microsserviços)

**Separação de Bancos:**
```
┌─────────────────────┐      ┌──────────────────────┐
│ Cadastro Service    │      │ Agendamento Service  │
├─────────────────────┤      ├──────────────────────┤
│ PostgreSQL 16       │      │ MongoDB 6.0          │
│ - Paciente (tabela) │ ───> │ - pacienteCpf (ref)  │
│ - Medico (tabela)   │ ───> │ - medicoCrm (ref)    │
│ - Convenio (tabela) │ ───> │ - convenioNome (ref) │
└─────────────────────┘      └──────────────────────┘
         │                            │
         └────────> Redis Pub/Sub <───┘
```

---

## 9. Impacto da Mudança

### Impacto na Documentação
- ✅ **Positivo:** Documentação agora completa
- ✅ **Clareza:** Rastreabilidade explícita
- ✅ **Arquitetura:** Padrões de microsserviços evidentes

### Impacto no Sistema
- ✅ **Nenhum:** Implementação já está correta
- ✅ **Benefício:** Sistema já possui auditoria completa
- ✅ **Conformidade:** LGPD e governança atendidos

### Funcionalidades Documentadas
- ✅ **Integração entre módulos:** FK para entidades
- ✅ **Auditoria:** Rastreamento de ações
- ✅ **Governança:** Responsabilização clara
- ✅ **Relatórios:** Dados para análises

---

## 10. Lições Aprendidas

### Documentação de Microsserviços
- ✅ **Explicitar referências FK** entre módulos
- ✅ **Documentar campos de auditoria** desde o início
- ✅ **Mostrar padrões de comunicação** (Pub/Sub)

### Rastreabilidade
- ✅ **Campos de usuário** são essenciais para governança
- ✅ **Auditoria completa** deve ser documentada
- ✅ **Timeline de ações** facilita troubleshooting

### Arquitetura
- ✅ **Database per Service** requer FKs como strings
- ✅ **Comunicação assíncrona** via eventos
- ✅ **Desacoplamento** entre microsserviços

---

## 11. Próximos Passos

### Recomendações
1. ✅ Documentação atualizada - **Concluído**
2. ⚠️ Considerar adicionar diagramas de sequência mostrando Pub/Sub - **Pendente**
3. ⚠️ Documentar eventos Redis publicados - **Pendente**
4. 💡 Criar guia de rastreabilidade e auditoria

### Oportunidades
- Dashboard de auditoria (quem fez o quê)
- Relatórios de responsabilização
- Alertas de ações suspeitas
- Análise de padrões de comportamento

---

## 12. Checklist de Validação

- [x] Código backend analisado (Agendamento.java)
- [x] Campos de rastreamento identificados (7 campos)
- [x] Classes de Análise atualizadas (3 diagramas)
- [x] Nota técnica adicionada no cabeçalho
- [x] Arquivo 3.7 verificado (já estava correto)
- [x] Benefícios documentados
- [x] Padrões arquiteturais explicados

---

## 13. Referências

### Arquivos Relacionados (Implementados)
- `simplehealth-back-agendamento/src/main/java/com/simplehealth/agendamento/domain/entity/Agendamento.java`
- `simplehealth-back-agendamento/src/main/java/com/simplehealth/agendamento/domain/entity/Consulta.java`
- `simplehealth-back-agendamento/src/main/java/com/simplehealth/agendamento/domain/entity/Exame.java`
- `simplehealth-back-agendamento/src/main/java/com/simplehealth/agendamento/domain/entity/Procedimento.java`

### Documentação Corrigida
- [Classes de Análise](./documentos-finais-definitivos/3.4.%20Classes%20de%20Análise/3.4.%20Classes%20de%20Análise_Diagrama%20de%20Classes.md)

### Documentação Já Correta (Discrepância 1.4)
- [Modelagem de Classes de Projeto](./documentos-finais-definitivos/3.7_3.9_3.10_Modelagens/3.7.%20Modelagem%20de%20Classes%20de%20Projeto/3.7.%20Modelagem%20de%20Classes%20de%20Projeto.md)

### Outras Discrepâncias
- [Discrepância 1.1 - Médico vs Usuario](./CORRECAO_DISCREPANCIA_1.1.md)
- [Discrepância 1.2 - EventoAuditoria com Cassandra](./CORRECAO_DISCREPANCIA_1.2.md)
- [Discrepância 1.3 - Redis para Cache](./CORRECAO_DISCREPANCIA_1.3.md)
- [Discrepância 1.4 - Atributos de Agendamento](./CORRECAO_DISCREPANCIA_1.4.md)

---

**Assinatura Digital:**  
Correção realizada e validada em 14/12/2025  
Documentação sincronizada com implementação completa do backend  
Rastreabilidade e auditoria agora totalmente documentadas
