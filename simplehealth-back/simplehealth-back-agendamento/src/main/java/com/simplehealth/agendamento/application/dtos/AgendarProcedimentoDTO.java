package com.simplehealth.agendamento.application.dtos;

import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendarProcedimentoDTO {

  private String pacienteCpf;
  private String medicoCrm;
  private LocalDateTime dataHoraInicioPrevista;
  private LocalDateTime dataHoraFimPrevista;
  private String descricaoProcedimento;
  private String salaEquipamentoNecessario;
  private String nivelRisco;
  private String convenioNome;
  private ModalidadeEnum modalidade;
  private String observacoes;
  private String usuarioCriadorLogin;
}