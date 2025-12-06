package com.simplehealth.agendamento.application.dtos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloqueioAgendaResponseDTO {

  private String id;
  private LocalDateTime dataInicio;
  private LocalDateTime dataFim;
  private String motivo;
  private Integer antecedenciaMinima;
  private String medicoCrm;
  private String usuarioCriadorLogin;
  private LocalDateTime dataCriacao;
  private Boolean ativo;
}
