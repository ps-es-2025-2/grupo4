package com.simplehealth.cadastro.application.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertaEstoqueDTO {

  private Long itemId;
  private String itemNome;
  private Integer quantidadeAtual;
  private Integer pontoReposicao;
  private String categoria;
  private LocalDate validadeMaisProxima;
}
