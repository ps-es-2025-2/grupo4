package com.simplehealth.estoque.application.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EntradaItensDTO {

  private String nfNumero;
  private UUID fornecedorId;
  private UUID pedidoId;
  private List<ItemDTO> itens;

}
