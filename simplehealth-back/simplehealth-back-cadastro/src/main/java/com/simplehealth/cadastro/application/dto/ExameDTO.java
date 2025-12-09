package com.simplehealth.cadastro.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ExameDTO extends AgendamentoDTO {
  private String nomeExame;
  private Boolean requerPreparo;
  private String instrucoesPreparo;
}
