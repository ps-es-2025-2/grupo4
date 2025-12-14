package com.simplehealth.estoque.domain.entity;

import java.util.Date;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

@Data
public abstract class Item {

  @PrimaryKey
  private UUID idItem = UUID.randomUUID();

  @Column("nome")
  private String nome;

  @Column("quantidade_total")
  private Integer quantidadeTotal;

  @Column("validade")
  private Date validade;

  @Column("estoque_id")
  private UUID estoqueId;
}
