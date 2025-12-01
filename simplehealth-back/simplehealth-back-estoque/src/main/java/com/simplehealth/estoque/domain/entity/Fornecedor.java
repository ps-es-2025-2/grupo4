package com.simplehealth.estoque.domain.entity;


import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("fornecedor")
@Data
public class Fornecedor {

  @PrimaryKey
  private Long idFornecedor;

  @Column
  private String cnpj;

  public Fornecedor() {
    if (this.idFornecedor == null) {
      this.idFornecedor = System.currentTimeMillis();
    }
  }
}
