package com.simplehealth.cadastro.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoDTO {

  private String descricao;
  private Double valor;
  // placeholder, dps adiciono o resto
}
