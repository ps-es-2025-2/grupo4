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
public class MedicoDTO {

  private Long id;

  @NotBlank
  private String nomeCompleto;

  @NotBlank
  private String crm;

  private String especialidade;
  private String telefone;
  private String email;
}
