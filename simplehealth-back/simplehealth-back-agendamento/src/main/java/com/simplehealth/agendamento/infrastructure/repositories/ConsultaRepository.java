package com.simplehealth.agendamento.infrastructure.repositories;

import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends MongoRepository<Consulta, String> {

  List<Consulta> findByPacienteCpfOrderByDataHoraInicioPrevistaDesc(String pacienteCpf);

  List<Consulta> findByMedicoCrmAndDataHoraInicioPrevistaGreaterThanEqualAndDataHoraFimPrevistaLessThanEqualAndStatus(
      String medicoCrm, LocalDateTime dataInicio, LocalDateTime dataFim, StatusAgendamentoEnum status
  );

  List<Consulta> findByPacienteCpfAndStatusOrderByDataHoraInicioPrevistaDesc(
      String pacienteCpf, StatusAgendamentoEnum status
  );

  List<Consulta> findByMedicoCrmAndDataHoraInicioPrevistaLessThanEqualAndDataHoraFimPrevistaGreaterThanEqualAndStatus(
      String medicoCrm, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, StatusAgendamentoEnum status
  );
}

