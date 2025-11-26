package com.simplehealth.agendamento.application.dtos;

import com.simplehealth.agendamento.domain.enums.ModalidadeEnum;
import com.simplehealth.agendamento.domain.enums.TipoConsultaEnum;
import java.time.LocalDateTime;

public class AgendarConsultaDTO {

  private LocalDateTime dataHoraInicio;
  private LocalDateTime dataHoraFim;
  private ModalidadeEnum modalidade;
  private String especialidade;
  private TipoConsultaEnum tipoConsulta;
  private String pacienteCpf;
  private String medicoCrm;
  private String convenioNome;
  private String usuarioCriadorLogin;
  private String observacoes;

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

  public ModalidadeEnum getModalidade() {
    return modalidade;
  }

  public void setModalidade(ModalidadeEnum modalidade) {
    this.modalidade = modalidade;
  }

  public String getEspecialidade() {
    return especialidade;
  }

  public void setEspecialidade(String especialidade) {
    this.especialidade = especialidade;
  }

  public TipoConsultaEnum getTipoConsulta() {
    return tipoConsulta;
  }

  public void setTipoConsulta(TipoConsultaEnum tipoConsulta) {
    this.tipoConsulta = tipoConsulta;
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

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }
}
