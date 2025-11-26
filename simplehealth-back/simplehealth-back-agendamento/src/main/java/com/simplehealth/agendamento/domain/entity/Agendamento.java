package com.simplehealth.agendamento.domain.entity;

import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import com.simplehealth.agendamento.domain.enums.StatusAgendamentoEnum;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("Agendamento")
public abstract class Agendamento {

  @Id
  private String id;

  @Property("dataHoraInicio")
  private LocalDateTime dataHoraInicio;

  @Property("dataHoraFim")
  private LocalDateTime dataHoraFim;

  @Property("isEncaixe")
  private Boolean isEncaixe;

  @Property("modalidade")
  private ModalidadeEnum modalidade;

  @Property("motivoEncaixe")
  private String motivoEncaixe;

  @Property("observacoes")
  private String observacoes;

  @Property("status")
  private StatusAgendamentoEnum status;

  @Property("motivoCancelamento")
  private String motivoCancelamento;

  @Property("dataCancelamento")
  private LocalDateTime dataCancelamento;

  //Relacionamentos com entidades de cadastro (IDs externos - PostgreSQL)
  @Property("pacienteCpf")
  private String pacienteCpf;

  @Property("medicoCrm")
  private String medicoCrm;

  @Property("convenioNome")
  private String convenioNome;

  @Property("usuarioCriadorLogin")
  private String usuarioCriadorLogin;

  @Property("usuarioCanceladorLogin")
  private String usuarioCanceladorLogin;

  public Agendamento() {
    this.id = UUID.randomUUID().toString();
    this.isEncaixe = false;
    this.status = StatusAgendamentoEnum.ATIVO;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LocalDateTime getDataHoraInicio() {
    return dataHoraInicio;
  }

  public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
    this.dataHoraInicio = dataHoraInicio;
  }

  public LocalDateTime getDataHoraFim() {
    return dataHoraFim;
  }

  public void setDataHoraFim(LocalDateTime dataHoraFim) {
    this.dataHoraFim = dataHoraFim;
  }

  public Boolean getIsEncaixe() {
    return isEncaixe;
  }

  public void setIsEncaixe(Boolean isEncaixe) {
    this.isEncaixe = isEncaixe;
  }

  public ModalidadeEnum getModalidade() {
    return modalidade;
  }

  public void setModalidade(ModalidadeEnum modalidade) {
    this.modalidade = modalidade;
  }

  public String getMotivoEncaixe() {
    return motivoEncaixe;
  }

  public void setMotivoEncaixe(String motivoEncaixe) {
    this.motivoEncaixe = motivoEncaixe;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public StatusAgendamentoEnum getStatus() {
    return status;
  }

  public void setStatus(StatusAgendamentoEnum status) {
    this.status = status;
  }

  public String getMotivoCancelamento() {
    return motivoCancelamento;
  }

  public void setMotivoCancelamento(String motivoCancelamento) {
    this.motivoCancelamento = motivoCancelamento;
  }

  public LocalDateTime getDataCancelamento() {
    return dataCancelamento;
  }

  public void setDataCancelamento(LocalDateTime dataCancelamento) {
    this.dataCancelamento = dataCancelamento;
  }

  public String getPacienteCpf() {
    return pacienteCpf;
  }

  public void setPacienteCpf(String pacienteCpf) {
    this.pacienteCpf = pacienteCpf;
  }

  public String getMedicoCrm() {
    return medicoCrm;
  }

  public void setMedicoCrm(String medicoCrm) {
    this.medicoCrm = medicoCrm;
  }

  public String getConvenioNome() {
    return convenioNome;
  }

  public void setConvenioNome(String convenioNome) {
    this.convenioNome = convenioNome;
  }

  public String getUsuarioCriadorLogin() {
    return usuarioCriadorLogin;
  }

  public void setUsuarioCriadorLogin(String usuarioCriadorLogin) {
    this.usuarioCriadorLogin = usuarioCriadorLogin;
  }

  public String getUsuarioCanceladorLogin() {
    return usuarioCanceladorLogin;
  }

  public void setUsuarioCanceladorLogin(String usuarioCanceladorLogin) {
    this.usuarioCanceladorLogin = usuarioCanceladorLogin;
  }
}

