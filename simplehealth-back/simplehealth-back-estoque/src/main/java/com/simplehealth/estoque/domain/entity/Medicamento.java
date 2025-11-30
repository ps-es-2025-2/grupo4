package com.simplehealth.estoque.domain.entity;

import lombok.Data;

@Data
public class Medicamento extends Item {

  private String presciricao;
  private String targa;
}
