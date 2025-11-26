package com.simplehealth.agendamento.application.dtos;

import java.time.LocalDateTime;

public class BloqueioAgendaDTO {

  private LocalDateTime dataInicio;
  private LocalDateTime dataFim;
  private String motivo;
  private Integer antecedenciaMinima;
  private String medicoCrm;
  private String usuarioCriadorLogin;

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
}
