package com.simplehealth.estoque.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("procedimento_item")
@Data
public class ProcedimentoItem {

  @PrimaryKey
  private UUID id = UUID.randomUUID();

  @Column("quantidade_necessaria")
  private BigDecimal quantidadeNecessaria;

  @Column("item_id")
  private UUID itemId;

}

