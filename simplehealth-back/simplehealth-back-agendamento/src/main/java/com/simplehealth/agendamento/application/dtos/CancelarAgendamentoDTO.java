package com.simplehealth.agendamento.application.dtos;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CancelarAgendamentoDTO {

  private Long id;
  private String motivo;
  private String usuarioLogin;
  private LocalDateTime dataHoraCancelamento;
}