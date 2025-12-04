package com.simplehealth.estoque.domain.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("pedido")
@Data
public class Pedido {

  @PrimaryKey
  private UUID idPedido = UUID.randomUUID();

  @Column("data_pedido")
  private Date dataPedido;

  @Column("status")
  private String status;

  @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.BIGINT)
  @Column("item_ids")
  private List<UUID> itemIds;

  @Column("fornecedor_id")
  private UUID fornecedorId;

}
