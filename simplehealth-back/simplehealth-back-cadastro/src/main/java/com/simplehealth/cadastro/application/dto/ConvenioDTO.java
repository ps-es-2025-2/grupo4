package com.simplehealth.cadastro.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConvenioDTO {

  private Long id;

  @NotBlank
  private String nome;

  private String plano;
  private Boolean ativo;
}
