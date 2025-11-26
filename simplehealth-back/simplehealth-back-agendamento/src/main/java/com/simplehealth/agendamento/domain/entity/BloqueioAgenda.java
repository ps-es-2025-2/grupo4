package com.simplehealth.agendamento.domain.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bloqueio_agenda")
public class BloqueioAgenda {

  @Id
  private String id;

  @Field("data_inicio")
  private LocalDateTime dataInicio;

  @Field("data_fim")
  private LocalDateTime dataFim;

  private String motivo;

  @Field("antecedencia_minima")
  private Integer antecedenciaMinima;

  @Field("medico_crm")
  private String medicoCrm;

  @Field("usuario_criador_login")
  private String usuarioCriadorLogin;

  @Field("data_criacao")
  private LocalDateTime dataCriacao = LocalDateTime.now();

  private Boolean ativo = true;
}
