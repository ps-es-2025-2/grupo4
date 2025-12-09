package com.simplehealth.estoque.domain.entity;


import java.util.UUID;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("estoque")
@Data
public class Estoque {

  @PrimaryKey
  private UUID idEstoque = UUID.randomUUID();

  @Column
  private String local;

}
