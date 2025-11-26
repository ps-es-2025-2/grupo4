package com.simplehealth.cadastro.domain.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoRequestEvent {

  private String correlationId;
  private String cpf;
}
