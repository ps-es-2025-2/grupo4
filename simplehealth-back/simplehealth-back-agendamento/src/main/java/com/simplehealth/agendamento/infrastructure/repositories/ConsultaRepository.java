package com.simplehealth.agendamento.infrastructure.repositories;

import com.simplehealth.agendamento.domain.entity.Consulta;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends Neo4jRepository<Consulta, String> {

  @Query("MATCH (c:Consulta) " +
      "WHERE c.medicoCrm = $medicoCrm " +
      "AND c.dataHoraInicio >= $dataInicio " +
      "AND c.dataHoraFim <= $dataFim " +
      "AND c.status = 'ATIVO' " +
      "RETURN c")
  List<Consulta> findByMedicoAndPeriodo(
      @Param("medicoCrm") String medicoCrm,
      @Param("dataInicio") LocalDateTime dataInicio,
      @Param("dataFim") LocalDateTime dataFim
  );

  @Query("MATCH (c:Consulta) " +
      "WHERE c.pacienteCpf = $pacienteCpf " +
      "AND c.status = $status " +
      "RETURN c " +
      "ORDER BY c.dataHoraInicio DESC")
  List<Consulta> findByPacienteAndStatus(
      @Param("pacienteCpf") String pacienteCpf,
      @Param("status") String status
  );

  @Query("MATCH (c:Consulta) " +
      "WHERE c.medicoCrm = $medicoCrm " +
      "AND c.dataHoraInicio <= $dataHoraFim " +
      "AND c.dataHoraFim >= $dataHoraInicio " +
      "AND c.status = 'ATIVO' " +
      "RETURN c")
  List<Consulta> findConflitosAgenda(
      @Param("medicoCrm") String medicoCrm,
      @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
      @Param("dataHoraFim") LocalDateTime dataHoraFim
  );
}

