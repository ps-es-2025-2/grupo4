package com.simplehealth.agendamento.application.dtos;

import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarProcedimentoDTO {

  private String id;
  private LocalDateTime dataHoraInicio;
  private LocalDateTime dataHoraFim;
  private String descricaoProcedimento;
  private String salaEquipamentoNecessario;
  private String nivelRisco;
  private ModalidadeEnum modalidade;
  private String observacoes;
  private String convenioNome;
}

