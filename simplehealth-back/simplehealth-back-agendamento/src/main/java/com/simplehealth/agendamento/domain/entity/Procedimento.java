package com.simplehealth.agendamento.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "procedimento")
public class Procedimento extends Agendamento {

  @Field("descricao_procedimento")
  private String descricaoProcedimento;

  @Field("sala_equipamento_necessario")
  private String salaEquipamentoNecessario;

  @Field("nivel_risco")
  private String nivelRisco;
}
