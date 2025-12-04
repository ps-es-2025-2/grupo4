package com.simplehealth.estoque.domain.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("hospitalar")
@Data
public class Hospitalar extends Item {

  @Column("descartabilidade")
  private Boolean descartabilidade;

}
