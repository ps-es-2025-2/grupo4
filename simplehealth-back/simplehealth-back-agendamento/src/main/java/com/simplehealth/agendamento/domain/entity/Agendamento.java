package com.simplehealth.agendamento.domain.entity;

import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "agendamento")
public abstract class Agendamento {

  @Id
  private String id;

  @Field("data_hora_inicio")
  private LocalDateTime dataHoraInicio;

  @Field("data_hora_fim")
  private LocalDateTime dataHoraFim;

  @Field("is_encaixe")
  private Boolean isEncaixe = false;

  private ModalidadeEnum modalidade;

  @Field("motivo_encaixe")
  private String motivoEncaixe;

  private String observacoes;

  private StatusAgendamentoEnum status = StatusAgendamentoEnum.ATIVO;

  @Field("motivo_cancelamento")
  private String motivoCancelamento;

  @Field("data_cancelamento")
  private LocalDateTime dataCancelamento;

  @Field("paciente_cpf")
  private String pacienteCpf;

  @Field("medico_crm")
  private String medicoCrm;

  @Field("convenio_nome")
  private String convenioNome;

  @Field("usuario_criador_login")
  private String usuarioCriadorLogin;

  @Field("usuario_cancelador_login")
  private String usuarioCanceladorLogin;
}
