package com.simplehealth.agendamento.infrastructure.repositories;

import com.simplehealth.agendamento.domain.entity.Agendamento;
import com.simplehealth.agendamento.domain.entity.Consulta;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends MongoRepository<Consulta, String> {

  List<Consulta> findByPacienteCpfOrderByDataHoraInicioDesc(String pacienteCpf);

  List<Consulta> findByMedicoCrmAndDataHoraInicioGreaterThanEqualAndDataHoraFimLessThanEqualAndStatus(
      String medicoCrm, LocalDateTime dataInicio, LocalDateTime dataFim, StatusAgendamentoEnum status
  );

  List<Consulta> findByPacienteCpfAndStatusOrderByDataHoraInicioDesc(
      String pacienteCpf, StatusAgendamentoEnum status
  );

  List<Consulta> findByMedicoCrmAndDataHoraInicioLessThanEqualAndDataHoraFimGreaterThanEqualAndStatus(
      String medicoCrm, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, StatusAgendamentoEnum status
  );
}

