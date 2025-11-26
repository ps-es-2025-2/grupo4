package com.simplehealth.cadastro.application.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AgendamentoDTO {

  private String id;
  private LocalDateTime dataHoraInicio;
  private LocalDateTime dataHoraFim;
  private Boolean isEncaixe;
  private String modalidade;
  private String motivoEncaixe;
  private String observacoes;
  private String status;
  private String motivoCancelamento;
  private LocalDateTime dataCancelamento;
  private String pacienteCpf;
  private String medicoCrm;
  private String convenioNome;
  private String usuarioCriadorLogin;
  private String usuarioCanceladorLogin;
}
