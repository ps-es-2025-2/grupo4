package com.simplehealth.agendamento.application.dtos;

import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import com.simplehealth.agendamento.domain.enums.TipoConsultaEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AgendarConsultaDTO {

  private String pacienteCpf;
  private String medicoCrm;
  private LocalDateTime dataHoraInicioPrevista;
  private LocalDateTime dataHoraFimPrevista;
  private TipoConsultaEnum tipoConsulta;
  private String especialidade;
  private String convenioNome;
  private ModalidadeEnum modalidade;
  private Boolean isEncaixe;
  private String motivoEncaixe;
  private String observacoes;
  private String usuarioCriadorLogin;
}