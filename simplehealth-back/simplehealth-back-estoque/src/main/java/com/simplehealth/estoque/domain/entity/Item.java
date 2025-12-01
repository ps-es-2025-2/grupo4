package com.simplehealth.estoque.domain.entity;

import java.util.Date;
import lombok.Data;

@Data
public class Item {

  private Long idItem;
  private String nome;
  private Integer quantidadeTotal;
  private Date validade;
  private Long idEstoque;
}
