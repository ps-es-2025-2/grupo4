package com.simplehealth.cadastro.domain.events;

import com.simplehealth.cadastro.application.dto.ExameDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoExameResponseEvent {
  private String correlationId;
  private List<ExameDTO> exames;
}
