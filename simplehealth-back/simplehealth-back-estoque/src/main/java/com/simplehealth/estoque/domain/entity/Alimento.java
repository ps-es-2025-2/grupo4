package com.simplehealth.estoque.domain.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("alimento")
@Data
public class Alimento extends Item {

  @Column("alergenicos")
  private String alergenicos;

}
