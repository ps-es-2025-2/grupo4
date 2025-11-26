package com.simplehealth.agendamento.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("BloqueioAgenda")
public class BloqueioAgenda {

  @Id
  private String id;

  @Property("dataInicio")
  private LocalDateTime dataInicio;

  @Property("dataFim")
  private LocalDateTime dataFim;

  @Property("motivo")
  private String motivo;

  @Property("antecedenciaMinima")
  private Integer antecedenciaMinima;

  @Property("medicoCrm")
  private String medicoCrm;

  @Property("usuarioCriadorLogin")
  private String usuarioCriadorLogin;

  @Property("dataCriacao")
  private LocalDateTime dataCriacao;

  @Property("ativo")
  private Boolean ativo;

  public BloqueioAgenda() {
    this.id = UUID.randomUUID().toString();
    this.dataCriacao = LocalDateTime.now();
    this.ativo = true;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LocalDateTime getDataInicio() {
    return dataInicio;
  }

  public void setDataInicio(LocalDateTime dataInicio) {
    this.dataInicio = dataInicio;
  }

  public LocalDateTime getDataFim() {
    return dataFim;
  }

  public void setDataFim(LocalDateTime dataFim) {
    this.dataFim = dataFim;
  }

  public String getMotivo() {
    return motivo;
  }

  public void setMotivo(String motivo) {
    this.motivo = motivo;
  }

  public Integer getAntecedenciaMinima() {
    return antecedenciaMinima;
  }

  public void setAntecedenciaMinima(Integer antecedenciaMinima) {
    this.antecedenciaMinima = antecedenciaMinima;
  }

  public String getMedicoCrm() {
    return medicoCrm;
  }

  public void setMedicoCrm(String medicoCrm) {
    this.medicoCrm = medicoCrm;
  }

  public String getUsuarioCriadorLogin() {
    return usuarioCriadorLogin;
  }

  public void setUsuarioCriadorLogin(String usuarioCriadorLogin) {
    this.usuarioCriadorLogin = usuarioCriadorLogin;
  }

  public LocalDateTime getDataCriacao() {
    return dataCriacao;
  }

  public void setDataCriacao(LocalDateTime dataCriacao) {
    this.dataCriacao = dataCriacao;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
  }
}
