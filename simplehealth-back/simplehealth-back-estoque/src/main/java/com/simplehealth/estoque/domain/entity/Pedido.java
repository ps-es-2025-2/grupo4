package com.simplehealth.estoque.domain.entity;

import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("pedido")
@Data
public class Pedido {

  @PrimaryKeyColumn(
      name = "id_pedido",
      type = PrimaryKeyType.PARTITIONED
  )
  private Long idPedido;

  @Column("data_pedido")
  private Date dataPedido;

  @Column("status")
  private String status;

  @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.BIGINT)
  @Column("item_ids")
  private List<Long> itemIds;

  @Column("fornecedor_id")
  private Long fornecedorId;

  public Pedido() {
    if (this.idPedido == null) {
      this.idPedido = System.currentTimeMillis();
    }
  }
}
