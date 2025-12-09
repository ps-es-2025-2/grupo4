package com.simplehealth.agendamento.domain.events;

import com.simplehealth.agendamento.application.dtos.ExameResponseDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoExameResponseEvent {

  private String correlationId;
  private List<ExameResponseDTO> exames;
}
