package com.simplehealth.estoque.domain.entity;

import java.util.Date;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("medicamento")
@Data
public class Medicamento {

  @PrimaryKeyColumn(
      name = "id_item",
      type = PrimaryKeyType.PARTITIONED
  )
  private Long idItem;

  @Column("nome")
  private String nome;

  @Column("quantidade_total")
  private Integer quantidadeTotal;

  @Column("validade")
  private Date validade;

  @Column("id_estoque")
  private Long idEstoque;

  @Column("prescricao")
  private String presciricao;

  @Column("targa")
  private String targa;

  public Medicamento() {
    if (this.idItem == null) {
      this.idItem = System.currentTimeMillis();
    }
  }
}
