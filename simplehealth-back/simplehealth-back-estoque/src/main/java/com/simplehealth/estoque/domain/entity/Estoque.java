package com.simplehealth.estoque.domain.entity;


import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("estoque")
@Data
public class Estoque {

  @PrimaryKey
  private Long idEstoque;

  @Column
  private String local;

  public Estoque() {
    if (this.idEstoque == null) {
      this.idEstoque = System.currentTimeMillis();
    }
  }
}
