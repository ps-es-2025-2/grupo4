package com.simplehealth.agendamento.application.dtos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BloqueioAgendaDTO {

  private LocalDateTime dataInicio;
  private LocalDateTime dataFim;
  private String motivo;
  private Integer antecedenciaMinima;
  private String medicoCrm;
  private String usuarioCriadorLogin;
}
