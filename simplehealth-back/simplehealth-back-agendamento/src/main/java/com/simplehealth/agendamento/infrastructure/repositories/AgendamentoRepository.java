package com.simplehealth.agendamento.infrastructure.repositories;

import com.simplehealth.agendamento.domain.entity.Agendamento;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository extends Neo4jRepository<Agendamento, String> {

  @Query("MATCH (a:Agendamento) " +
      "WHERE a.pacienteCpf = $pacienteCpf " +
      "RETURN a " +
      "ORDER BY a.dataHoraInicio DESC")
  List<Agendamento> findAllByPaciente(@Param("pacienteCpf") String pacienteCpf);

  @Query("MATCH (a:Agendamento) " +
      "WHERE a.medicoCrm = $medicoCrm " +
      "AND a.dataHoraInicio >= $dataInicio " +
      "AND a.dataHoraInicio < $dataFim " +
      "RETURN a " +
      "ORDER BY a.dataHoraInicio")
  List<Agendamento> findByMedicoAndData(
      @Param("medicoCrm") String medicoCrm,
      @Param("dataInicio") LocalDateTime dataInicio,
      @Param("dataFim") LocalDateTime dataFim
  );

  @Query("MATCH (a:Agendamento) " +
      "WHERE a.medicoCrm = $medicoCrm " +
      "AND a.dataHoraInicio <= $dataHoraFim " +
      "AND a.dataHoraFim >= $dataHoraInicio " +
      "AND a.status = 'ATIVO' " +
      "RETURN a")
  List<Agendamento> verificarConflitoHorario(
      @Param("medicoCrm") String medicoCrm,
      @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
      @Param("dataHoraFim") LocalDateTime dataHoraFim
  );
}
