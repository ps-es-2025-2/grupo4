package com.simplehealth.agendamento.infrastructure.repositories;

import com.simplehealth.agendamento.domain.entity.Agendamento;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository extends MongoRepository<Agendamento, String> {

  List<Agendamento> findByPacienteCpfOrderByDataHoraInicioPrevistaDesc(String pacienteCpf);

  List<Agendamento> findByMedicoCrmAndDataHoraInicioPrevistaGreaterThanEqualAndDataHoraInicioPrevistaLessThanOrderByDataHoraInicioPrevista(
      String medicoCrm, LocalDateTime dataInicio, LocalDateTime dataFim
  );

  List<Agendamento> findByMedicoCrmAndDataHoraInicioPrevistaLessThanEqualAndDataHoraFimPrevistaGreaterThanEqualAndStatus(
      String medicoCrm, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, StatusAgendamentoEnum status
  );
}
