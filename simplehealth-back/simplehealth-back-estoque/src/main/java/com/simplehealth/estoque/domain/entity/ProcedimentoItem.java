package com.simplehealth.estoque.domain.entity;

import java.math.BigDecimal;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("procedimento_item")
@Data
public class ProcedimentoItem {

  @PrimaryKeyColumn(
      name = "id",
      type = PrimaryKeyType.PARTITIONED
  )
  private Long id;

  @Column("quantidade_necessaria")
  private BigDecimal quantidadeNecessaria;

  @Column("item_id")
  private Long itemId;

  public ProcedimentoItem() {
    if (this.id == null) {
      this.id = System.currentTimeMillis();
    }
  }
}

