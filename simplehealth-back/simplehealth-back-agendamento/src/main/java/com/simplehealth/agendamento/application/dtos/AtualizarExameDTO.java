package com.simplehealth.agendamento.application.dtos;

import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarExameDTO {

  private String id;
  private LocalDateTime dataHoraInicio;
  private LocalDateTime dataHoraFim;
  private String nomeExame;
  private Boolean requerPreparo;
  private String instrucoesPreparo;
  private ModalidadeEnum modalidade;
  private String observacoes;
  private String convenioNome;
  private LocalDateTime dataHoraAgendamento;
  private LocalDateTime dataHoraInicioPrevista;
  private LocalDateTime dataHoraFimPrevista;
  private LocalDateTime dataHoraInicioExecucao;
  private LocalDateTime dataHoraFimExecucao;
  private Boolean isEncaixe;
  private String motivoEncaixe;
  private StatusAgendamentoEnum status;
  private String motivoCancelamento;
  private LocalDateTime dataCancelamento;
  private String pacienteCpf;
  private String medicoCrm;
  private String usuarioCriadorLogin;
  private String usuarioCanceladorLogin;
}

