package com.simplehealth.agendamento.application.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExameResponseDTO {

  private String id;
  private LocalDateTime dataHoraAgendamento;
  private LocalDateTime dataHoraInicioPrevista;
  private LocalDateTime dataHoraFimPrevista;
  private LocalDateTime dataHoraInicioExecucao;
  private LocalDateTime dataHoraFimExecucao;
  private Boolean isEncaixe;
  private ModalidadeEnum modalidade;
  private String motivoEncaixe;
  private String observacoes;
  private StatusAgendamentoEnum status;
  private String motivoCancelamento;
  private LocalDateTime dataCancelamento;
  private String pacienteCpf;
  private String medicoCrm;
  private String convenioNome;
  private String usuarioCriadorLogin;
  private String usuarioCanceladorLogin;
  private String nomeExame;
  private Boolean requerPreparo;
  private String instrucoesPreparo;
}
