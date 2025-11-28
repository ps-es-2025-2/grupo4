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

  private LocalDateTime dataHoraInicio;
  private LocalDateTime dataHoraFim;
  private ModalidadeEnum modalidade;
  private String especialidade;
  private TipoConsultaEnum tipoConsulta;
  private String pacienteCpf;
  private String medicoCrm;
  private String convenioNome;
  private String usuarioCriadorLogin;
  private String observacoes;
}
