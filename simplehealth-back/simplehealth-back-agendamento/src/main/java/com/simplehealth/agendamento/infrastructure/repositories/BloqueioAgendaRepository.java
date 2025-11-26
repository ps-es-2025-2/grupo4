package com.simplehealth.agendamento.infrastructure.repositories;

import com.simplehealth.agendamento.domain.entity.BloqueioAgenda;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloqueioAgendaRepository extends MongoRepository<BloqueioAgenda, String> {

  List<BloqueioAgenda> findByMedicoCrmAndAtivoTrueAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
      String medicoCrm, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim
  );

  List<BloqueioAgenda> findByMedicoCrmAndAtivoTrueAndDataInicioGreaterThanEqualAndDataFimLessThanEqualOrderByDataInicio(
      String medicoCrm, LocalDateTime dataInicio, LocalDateTime dataFim
  );

  List<BloqueioAgenda> findByMedicoCrmAndAtivoTrueOrderByDataInicio(String medicoCrm);

  boolean existsByMedicoCrmAndAtivoTrueAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
      String medicoCrm, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim
  );
}
