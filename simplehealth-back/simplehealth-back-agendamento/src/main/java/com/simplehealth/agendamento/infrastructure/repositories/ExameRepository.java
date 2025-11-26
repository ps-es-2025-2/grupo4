package com.simplehealth.agendamento.infrastructure.repositories;

import com.simplehealth.agendamento.domain.entity.Exame;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExameRepository extends Neo4jRepository<Exame, String> {

  @Query("MATCH (e:Exame) " +
      "WHERE e.medicoCrm = $medicoCrm " +
      "AND e.dataHoraInicio >= $dataInicio " +
      "AND e.dataHoraFim <= $dataFim " +
      "AND e.status = 'ATIVO' " +
      "RETURN e")
  List<Exame> findByMedicoAndPeriodo(
      @Param("medicoCrm") String medicoCrm,
      @Param("dataInicio") LocalDateTime dataInicio,
      @Param("dataFim") LocalDateTime dataFim
  );

  @Query("MATCH (e:Exame) " +
      "WHERE e.medicoCrm = $medicoCrm " +
      "AND e.dataHoraInicio <= $dataHoraFim " +
      "AND e.dataHoraFim >= $dataHoraInicio " +
      "AND e.status = 'ATIVO' " +
      "RETURN e")
  List<Exame> findConflitosAgenda(
      @Param("medicoCrm") String medicoCrm,
      @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
      @Param("dataHoraFim") LocalDateTime dataHoraFim
  );
}
