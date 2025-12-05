package com.simplehealth.estoque.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ControleValidadeDTO {

  private Integer diasAntecedencia;
  private Boolean incluirVencidos;
  private Boolean descartarItens;
  private String codigoCusto;
}
