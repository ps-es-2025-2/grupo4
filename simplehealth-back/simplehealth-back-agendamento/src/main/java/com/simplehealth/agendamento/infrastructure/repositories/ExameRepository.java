package com.simplehealth.agendamento.infrastructure.repositories;

import com.simplehealth.agendamento.domain.entity.Exame;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExameRepository extends MongoRepository<Exame, String> {

  List<Exame> findByMedicoCrmAndDataHoraInicioGreaterThanEqualAndDataHoraFimLessThanEqualAndStatus(
      String medicoCrm, LocalDateTime dataInicio, LocalDateTime dataFim, StatusAgendamentoEnum status
  );

  List<Exame> findByMedicoCrmAndDataHoraInicioLessThanEqualAndDataHoraFimGreaterThanEqualAndStatus(
      String medicoCrm, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, StatusAgendamentoEnum status
  );
}
