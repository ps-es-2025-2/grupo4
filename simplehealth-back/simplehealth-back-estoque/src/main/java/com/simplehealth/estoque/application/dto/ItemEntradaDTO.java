package com.simplehealth.estoque.application.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ItemEntradaDTO {

  private Long itemId;
  private String nome;
  private int quantidade;
  private Date validade;

}
