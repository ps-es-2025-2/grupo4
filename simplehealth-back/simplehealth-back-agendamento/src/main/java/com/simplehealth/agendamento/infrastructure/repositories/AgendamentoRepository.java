package com.simplehealth.agendamento.infrastructure.repositories;

import com.simplehealth.agendamento.domain.entity.Agendamento;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository extends MongoRepository<Agendamento, String> {

  List<Agendamento> findByPacienteCpfOrderByDataHoraInicioDesc(String pacienteCpf);

  List<Agendamento> findByMedicoCrmAndDataHoraInicioGreaterThanEqualAndDataHoraInicioLessThanOrderByDataHoraInicio(
      String medicoCrm, LocalDateTime dataInicio, LocalDateTime dataFim
  );

  List<Agendamento> findByMedicoCrmAndDataHoraInicioLessThanEqualAndDataHoraFimGreaterThanEqualAndStatus(
      String medicoCrm, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, StatusAgendamentoEnum status
  );
}
