package com.simplehealth.agendamento.infrastructure.repositories;

import com.simplehealth.agendamento.domain.entity.Procedimento;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcedimentoRepository extends Neo4jRepository<Procedimento, String> {

  @Query("MATCH (p:Procedimento) " +
      "WHERE p.medicoCrm = $medicoCrm " +
      "AND p.dataHoraInicio >= $dataInicio " +
      "AND p.dataHoraFim <= $dataFim " +
      "AND p.status = 'ATIVO' " +
      "RETURN p")
  List<Procedimento> findByMedicoAndPeriodo(
      @Param("medicoCrm") String medicoCrm,
      @Param("dataInicio") LocalDateTime dataInicio,
      @Param("dataFim") LocalDateTime dataFim
  );

  @Query("MATCH (p:Procedimento) " +
      "WHERE p.medicoCrm = $medicoCrm " +
      "AND p.dataHoraInicio <= $dataHoraFim " +
      "AND p.dataHoraFim >= $dataHoraInicio " +
      "AND p.status = 'ATIVO' " +
      "RETURN p")
  List<Procedimento> findConflitosAgenda(
      @Param("medicoCrm") String medicoCrm,
      @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
      @Param("dataHoraFim") LocalDateTime dataHoraFim
  );
}
