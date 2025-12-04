package com.simplehealth.estoque.application.dto;

import com.simplehealth.estoque.domain.enums.TipoItem;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ItemEntradaDTO {

  private UUID itemId;
  private String nome;
  private int quantidade;
  private Date validade;
  private TipoItem tipo;

}
