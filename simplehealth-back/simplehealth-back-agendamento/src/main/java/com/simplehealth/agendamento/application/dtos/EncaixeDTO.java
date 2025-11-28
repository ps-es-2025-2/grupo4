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
public class EncaixeDTO {

  private String pacienteCpf;
  private String medicoCrm;
  private LocalDateTime dataHoraInicio;
  private LocalDateTime dataHoraFim;
  private String motivoEncaixe;
  private String observacoes;
  private String usuarioCriadorLogin;
}
