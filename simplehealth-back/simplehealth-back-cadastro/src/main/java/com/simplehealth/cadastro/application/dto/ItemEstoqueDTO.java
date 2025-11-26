package com.simplehealth.cadastro.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemEstoqueDTO {

  private String nome;
  private Integer quantidade;
  // placeholder, dps adiciono o resto
}
