package com.simplehealth.agendamento.application.dtos;

import java.time.LocalDateTime;

public class CancelarAgendamentoDTO {

  private String id;
  private String motivo;
  private String usuarioLogin;
  private LocalDateTime dataHoraCancelamento;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMotivo() {
    return motivo;
  }

  public void setMotivo(String motivo) {
    this.motivo = motivo;
  }

  public String getUsuarioLogin() {
    return usuarioLogin;
  }

  public void setUsuarioLogin(String usuarioLogin) {
    this.usuarioLogin = usuarioLogin;
  }

  public LocalDateTime getDataHoraCancelamento() {
    return dataHoraCancelamento;
  }

  public void setDataHoraCancelamento(LocalDateTime dataHoraCancelamento) {
    this.dataHoraCancelamento = dataHoraCancelamento;
  }
}