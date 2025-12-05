package com.simplehealth.estoque.application.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaixaInsumoDTO {

  private UUID itemId;
  private Integer quantidadeNecessaria;
  private String destinoConsumo;
  private String lote;
}
