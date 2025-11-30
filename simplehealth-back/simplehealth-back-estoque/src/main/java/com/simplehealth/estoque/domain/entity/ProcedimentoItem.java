package com.simplehealth.estoque.domain.entity;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProcedimentoItem {

  private Long id;
  private BigDecimal quantidadeNecessaria;
  private Item item;
}
