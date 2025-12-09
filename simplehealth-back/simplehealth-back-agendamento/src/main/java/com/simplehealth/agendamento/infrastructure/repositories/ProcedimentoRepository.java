package com.simplehealth.agendamento.infrastructure.repositories;

import com.simplehealth.agendamento.domain.entity.Procedimento;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcedimentoRepository extends MongoRepository<Procedimento, String> {

  List<Procedimento> findByMedicoCrmAndDataHoraInicioPrevistaGreaterThanEqualAndDataHoraFimPrevistaLessThanEqualAndStatus(
      String medicoCrm, LocalDateTime dataInicio, LocalDateTime dataFim, StatusAgendamentoEnum status
  );

  List<Procedimento> findByMedicoCrmAndDataHoraInicioPrevistaLessThanEqualAndDataHoraFimPrevistaGreaterThanEqualAndStatus(
      String medicoCrm, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, StatusAgendamentoEnum status
  );

  List<Procedimento> findByPacienteCpfOrderByDataHoraInicioPrevistaDesc(String cpf);
}
