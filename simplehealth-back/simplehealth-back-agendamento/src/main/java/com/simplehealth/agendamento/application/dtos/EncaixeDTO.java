package com.simplehealth.agendamento.application.dtos;

import java.time.LocalDateTime;

public class EncaixeDTO {

  private String pacienteCpf;
  private String medicoCrm;
  private LocalDateTime dataHoraInicio;
  private LocalDateTime dataHoraFim;
  private String motivoEncaixe;
  private String observacoes;
  private String usuarioCriadorLogin;

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

  public String getUsuarioCriadorLogin() {
    return usuarioCriadorLogin;
  }

  public void setUsuarioCriadorLogin(String usuarioCriadorLogin) {
    this.usuarioCriadorLogin = usuarioCriadorLogin;
  }
}
