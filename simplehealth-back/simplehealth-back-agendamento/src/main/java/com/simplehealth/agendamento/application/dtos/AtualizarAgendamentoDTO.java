package com.simplehealth.agendamento.application.dtos;

import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import com.simplehealth.agendamento.domain.enums.TipoConsultaEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarAgendamentoDTO {

  private String id;
  private LocalDateTime dataHoraInicio;
  private LocalDateTime dataHoraFim;
  private ModalidadeEnum modalidade;
  private String observacoes;
  private String convenioNome;
  private String usuarioAlteradorLogin;
  
  // Campos específicos de Consulta
  private String especialidade;
  private TipoConsultaEnum tipoConsulta;
  
  // Campos de encaixe
  private Boolean isEncaixe;
  private String motivoEncaixe;
}

