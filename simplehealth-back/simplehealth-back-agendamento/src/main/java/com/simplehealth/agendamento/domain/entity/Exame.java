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
@Document(collection = "exame")
public class Exame extends Agendamento {

  @Field("nome_exame")
  private String nomeExame;

  @Field("requer_preparo")
  private Boolean requerPreparo;

  @Field("instrucoes_preparo")
  private String instrucoesPreparo;
}
