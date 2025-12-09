package com.simplehealth.agendamento.application.dtos;

import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendarExameDTO {

  private String pacienteCpf;
  private String medicoCrm;
  private LocalDateTime dataHoraInicioPrevista;
  private LocalDateTime dataHoraFimPrevista;
  private String nomeExame;
  private Boolean requerPreparo;
  private String instrucoesPreparo;
  private String convenioNome;
  private ModalidadeEnum modalidade;
  private String observacoes;
  private String usuarioCriadorLogin;
}