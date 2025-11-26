package com.simplehealth.cadastro.domain.events;

import com.simplehealth.cadastro.application.dto.ProcedimentoDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoProcedimentoResponseEvent {

  private String correlationId;
  private List<ProcedimentoDTO> procedimentos;
}
