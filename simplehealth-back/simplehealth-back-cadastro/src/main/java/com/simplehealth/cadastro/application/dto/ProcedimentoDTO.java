package com.simplehealth.cadastro.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProcedimentoDTO extends AgendamentoDTO {

  private String descricaoProcedimento;
  private String salaEquipamentoNecessario;
  private String nivelRisco;
}
