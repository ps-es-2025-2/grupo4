package com.simplehealth.estoque.domain.entity;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class Pedido {

  private Long idPedido;
  private Date dataPedido;
  private String status;
  private List<Item> itens;
  private Fornecedor fornecedor;
}
