package com.simplehealth.cadastro.domain.events;

import com.simplehealth.cadastro.application.dto.AgendamentoDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoAgendamentoResponseEvent {

  private String correlationId;
  private List<AgendamentoDTO> agendamentos;
}
