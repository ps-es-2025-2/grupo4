package com.simplehealth.estoque.application.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EntradaItensInput {

  private String nfNumero;
  private Long fornecedorId;
  private Long pedidoId;
  private List<ItemEntradaDTO> itens;

}
