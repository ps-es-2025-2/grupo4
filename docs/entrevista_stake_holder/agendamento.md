# SimpleHealth – Perguntas ao Stakeholder (Módulo de Agendamento)

---

## I. Detalhamento da Agenda e Disponibilidade (Perspectiva Médica)

1. **Qual é a regra básica de definição de horários de atendimento (dias da semana e horários de início/fim)? Além disso, como os médicos informam ou solicitam bloqueios ou exceções na agenda (ex: faltas, cirurgias, eventos) para a secretária, e com qual antecedência mínima esses bloqueios devem ser registrados no sistema?**  
   **R1:** “Geralmente a regra dos horários são dias fixos na semana, tipo de segunda a sexta das 8 às 12. Já no caso dos bloqueios depende muito porque para casos normais a gente pede pra notificarem uns 2 dias antes para dar tempo de remarcar ou encontrar outro médico, mas quando o médico precisa ir pra um atendimento de emergência ou sofre um acidente e não tem como comparecer, a secretária precisa registrar na hora.”

2. **Qual é a duração padrão de cada tipo de consulta (ex: Primeira consulta, Retorno, Procedimento específico)? O sistema precisa permitir que cada médico personalize esses tempos, ou a duração é fixa para todos?**  
   **R2:** “A primeira consulta costuma ser de uns 40 minutos até 1 hora, já o retorno por volta de meia hora. Agora cirurgia varia muito, pode ser de 1 hora até mais de 5. Então é bom que o sistema deixe ajustar esse tempo pra organizar cada uma de um jeito diferente.”

---

## II. Processos de Agendamento e Ações (Perspectiva Atendente/Médica)

3. **Quais são os critérios essenciais de busca e filtro ao agendar uma consulta? O atendente precisa buscar por Médico, Especialidade, Data/Período, Tipo de Convênio ou uma combinação desses?**  
   **R3:** “Geralmente são esses filtros mesmo, aí quando precisa ser com um médico específico a gente não usa a especialidade. Especialidade é pra quando pode ser qualquer médico dessa área, tipo se precisar atender uma fratura de fêmur qualquer ortopedista serve, agora pra reconstrução de ligamento cruzado precisa ser um médico especialista em cirurgia de joelho.”

4. **Como o sistema deve lidar com os status de agendamento? Quais são todos os status possíveis (ex: Agendado, Confirmado, Cancelado, Remarcado, Falta/No-show)? É necessário um processo de Confirmação Automática com o paciente (ex: SMS, WhatsApp, E-mail) e, em caso afirmativo, com que antecedência (ex: 48h ou 24h antes)?**  
   **R4:** “Os mais comuns são esses que você citou mesmo e o Atendido quando já aconteceu o atendimento. A confirmação automática pro paciente facilita bastante a vida porque não tem que ficar ligando pra cada paciente, normalmente 1 dia antes do atendimento via WhatsApp e SMS já que a maioria das pessoas não olha o email. Tem lugares que perguntam 2 dias antes, mas o ideal seria 1 mesmo.”

5. **Qual é o processo para remarcar ou cancelar uma consulta? O sistema deve manter um histórico de todas as mudanças (data/hora antiga, nova data/hora, motivo da alteração/cancelamento e quem fez a mudança)?**  
   **R5:** “Pra cancelar precisa marcar as datas e hora antigas e novas e o motivo para mudança. Registrar o usuário que fez não costuma ser tão necessário, mas ajudaria se quisessem fazer auditoria depois.”

---

## III. Gestão de Pacientes e Convênios (Perspectiva Atendente)

6. **Que informações do paciente são obrigatórias para realizar um agendamento (ex: Nome completo, Data de Nasc., Telefone, Convênio)? O sistema deve ser capaz de verificar se o paciente já está cadastrado ou deve sempre criar um novo registro?**  
   **R6:** “As informações mínimas são o nome completo, a data de nascimento, o cpf, telefone e convenio se tiver. Aí o sistema precisa ver pelo cpf se o paciente já tá cadastrado.”

7. **Como é feita a gestão de Convênios/Planos de Saúde? O módulo de agendamento precisa permitir a associação de um convênio específico à consulta e, idealmente, bloquear o agendamento se o médico não atender aquele plano?**  
   **R7:** “Os convênios precisam estar cadastrados no sistema e associado aos médicos que atendem ele. Quando o médico não atende o convênio então não deveria nem ser criado o agendamento. Aí caso o médico vá parar de atender um convênio ele precisa avisar antes pra não deixar agendar mais com antecedência.”

---

## IV. Casos Específicos e Segurança (Perspectiva Geral)

8. **O módulo de agendamento deve prever a possibilidade de "Encaixes" ou "Prioridades" em horários já cheios? Em que circunstâncias isso é permitido, quem tem a permissão para realizar um encaixe, e o sistema deve emitir algum tipo de alerta visual (ex: sobrecarga ou tempo de espera reduzido)?**  
   **R8:** “Tem que ter os encaixes, principalmente pra caso de urgência e pacientes graves. Aí geralmente é a secretária responsável ou o gestor que podem colocar o encaixe. Em questão de alerta visual talvez só colocar de uma cor diferente tipo amarelo pra mostrar que foi encaixe e não agendado antes.”

9. **Existe a necessidade de agendar Teleconsultas (Consultas Online) via este módulo? Se sim, o sistema precisará gerar ou integrar-se com uma plataforma para gerar o link da videoconferência automaticamente e enviá-lo ao paciente e médico?**  
   **R9:** “Depende muito do hospital e do médico pra ter teleconsulta. Um médico geral até faz sentido, mas um ortopedista já não consegue avaliar tão bem a distância. Se for pra ter eu acho que poderia só usar o Google Meet ou alguma dessas que todo mundo tá acostumado a usar mesmo.”

10. **Quais relatórios sobre agendamentos são considerados cruciais para a gestão da clínica/consultório? Especifique exemplos (ex: Relatório de Taxa de Falta/No-show por médico, Relatório de Ocupação da Agenda, Relatório de Consultas Agendadas por Convênio).**  
    **R10:** “Os que a gente mais usa é consulta agendada por convênio, o relatório de cancelamento, a produção médica que seria o número de consultas e quais consultas em um tempo e um comparativo de atendimentos mensal, semestral, anual.”

---