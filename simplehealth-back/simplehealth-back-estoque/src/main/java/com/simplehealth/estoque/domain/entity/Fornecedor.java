package com.simplehealth.estoque.domain.entity;

import java.util.UUID;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("fornecedor")
@Data
public class Fornecedor {

  @PrimaryKey
  private UUID idFornecedor = UUID.randomUUID();

  @Column
  private String cnpj;

  @Column
  private String nome;

  @Column
  private String telefone;

  @Column
  private String email;

  @Column
  private String endereco;

}
