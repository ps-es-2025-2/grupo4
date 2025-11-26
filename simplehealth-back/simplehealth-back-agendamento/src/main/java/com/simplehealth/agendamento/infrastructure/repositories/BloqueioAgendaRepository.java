package com.simplehealth.agendamento.infrastructure.repositories;

import com.simplehealth.agendamento.domain.entity.BloqueioAgenda;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BloqueioAgendaRepository extends Neo4jRepository<BloqueioAgenda, String> {

  @Query("MATCH (b:BloqueioAgenda) " +
      "WHERE b.medicoCrm = $medicoCrm " +
      "AND b.ativo = true " +
      "AND b.dataInicio <= $dataHoraFim " +
      "AND b.dataFim >= $dataHoraInicio " +
      "RETURN b")
  List<BloqueioAgenda> findBloqueiosConflitantes(
      @Param("medicoCrm") String medicoCrm,
      @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
      @Param("dataHoraFim") LocalDateTime dataHoraFim
  );

  @Query("MATCH (b:BloqueioAgenda) " +
      "WHERE b.medicoCrm = $medicoCrm " +
      "AND b.ativo = true " +
      "AND b.dataInicio >= $dataInicio " +
      "AND b.dataFim <= $dataFim " +
      "RETURN b " +
      "ORDER BY b.dataInicio")
  List<BloqueioAgenda> findByMedicoAndPeriodo(
      @Param("medicoCrm") String medicoCrm,
      @Param("dataInicio") LocalDateTime dataInicio,
      @Param("dataFim") LocalDateTime dataFim
  );

  @Query("MATCH (b:BloqueioAgenda) " +
      "WHERE b.medicoCrm = $medicoCrm " +
      "AND b.ativo = true " +
      "RETURN b " +
      "ORDER BY b.dataInicio")
  List<BloqueioAgenda> findAllByMedicoAtivos(@Param("medicoCrm") String medicoCrm);

  @Query("MATCH (c:Consulta) " +
      "WHERE c.medicoCrm = $medicoCrm " +
      "AND c.status = 'ATIVO' " +
      "AND c.dataHoraInicio >= $dataInicio " +
      "AND c.dataHoraFim <= $dataFim " +
      "RETURN count(c) > 0")
  boolean existemAgendamentosAtivosNoPeriodo(
      @Param("medicoCrm") String medicoCrm,
      @Param("dataInicio") LocalDateTime dataInicio,
      @Param("dataFim") LocalDateTime dataFim
  );
}
