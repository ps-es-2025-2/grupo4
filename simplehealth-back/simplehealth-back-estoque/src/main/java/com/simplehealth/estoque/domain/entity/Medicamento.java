package com.simplehealth.estoque.domain.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("medicamento")
@Data
public class Medicamento extends Item {

  @Column("prescricao")
  private String prescricao;

  @Column("targa")
  private String targa;

}
