package com.simplehealth.agendamento.domain.events;

import com.simplehealth.agendamento.application.dtos.AgendamentoDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoAgendamentoResponseEvent {

  private String correlationId;
  private List<AgendamentoDTO> agendamentos;
}