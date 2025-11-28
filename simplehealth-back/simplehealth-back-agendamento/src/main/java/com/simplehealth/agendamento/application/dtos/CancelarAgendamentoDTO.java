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
public class CancelarAgendamentoDTO {

  private String id;
  private String motivo;
  private String usuarioLogin;
  private LocalDateTime dataHoraCancelamento;
}