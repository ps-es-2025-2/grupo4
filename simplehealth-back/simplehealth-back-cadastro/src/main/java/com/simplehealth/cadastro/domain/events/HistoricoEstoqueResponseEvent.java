package com.simplehealth.cadastro.domain.events;

import com.simplehealth.cadastro.application.dto.ItemEstoqueDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoEstoqueResponseEvent {

  private String correlationId;
  private List<ItemEstoqueDTO> itens;
}
