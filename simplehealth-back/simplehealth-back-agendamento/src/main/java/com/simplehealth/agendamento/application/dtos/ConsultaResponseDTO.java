package com.simplehealth.agendamento.application.dtos;

import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import com.simplehealth.agendamento.domain.enums.TipoConsultaEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaResponseDTO {

  private String id;
  private LocalDateTime dataHoraInicio;
  private LocalDateTime dataHoraFim;
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

  private String especialidade;
  private TipoConsultaEnum tipoConsulta;
}
