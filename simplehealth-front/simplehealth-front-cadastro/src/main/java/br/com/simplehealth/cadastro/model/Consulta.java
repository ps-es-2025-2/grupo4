package br.com.simplehealth.cadastro.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class Consulta {
    
    private String id;
    
    @JsonProperty("dataHoraAgendamento")
    private LocalDateTime dataHoraAgendamento;
    
    @JsonProperty("dataHoraInicioPrevista")
    private LocalDateTime dataHoraInicioPrevista;
    
    @JsonProperty("dataHoraFimPrevista")
    private LocalDateTime dataHoraFimPrevista;
    
    @JsonProperty("dataHoraInicioExecucao")
    private LocalDateTime dataHoraInicioExecucao;
    
    @JsonProperty("dataHoraFimExecucao")
    private LocalDateTime dataHoraFimExecucao;
    
    @JsonProperty("isEncaixe")
    private Boolean isEncaixe;
    
    private String modalidade;
    
    @JsonProperty("motivoEncaixe")
    private String motivoEncaixe;
    
    private String observacoes;
    private String status;
    
    @JsonProperty("motivoCancelamento")
    private String motivoCancelamento;
    
    @JsonProperty("dataCancelamento")
    private LocalDateTime dataCancelamento;
    
    @JsonProperty("pacienteCpf")
    private String pacienteCpf;
    
    @JsonProperty("medicoCrm")
    private String medicoCrm;
    
    @JsonProperty("convenioNome")
    private String convenioNome;
    
    @JsonProperty("usuarioCriadorLogin")
    private String usuarioCriadorLogin;
    
    @JsonProperty("usuarioCanceladorLogin")
    private String usuarioCanceladorLogin;
    
    private String especialidade;
    private String tipoConsulta;

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDataHoraAgendamento() {
        return dataHoraAgendamento;
    }

    public void setDataHoraAgendamento(LocalDateTime dataHoraAgendamento) {
        this.dataHoraAgendamento = dataHoraAgendamento;
    }

    public LocalDateTime getDataHoraInicioPrevista() {
        return dataHoraInicioPrevista;
    }

    public void setDataHoraInicioPrevista(LocalDateTime dataHoraInicioPrevista) {
        this.dataHoraInicioPrevista = dataHoraInicioPrevista;
    }

    public LocalDateTime getDataHoraFimPrevista() {
        return dataHoraFimPrevista;
    }

    public void setDataHoraFimPrevista(LocalDateTime dataHoraFimPrevista) {
        this.dataHoraFimPrevista = dataHoraFimPrevista;
    }

    public LocalDateTime getDataHoraInicioExecucao() {
        return dataHoraInicioExecucao;
    }

    public void setDataHoraInicioExecucao(LocalDateTime dataHoraInicioExecucao) {
        this.dataHoraInicioExecucao = dataHoraInicioExecucao;
    }

    public LocalDateTime getDataHoraFimExecucao() {
        return dataHoraFimExecucao;
    }

    public void setDataHoraFimExecucao(LocalDateTime dataHoraFimExecucao) {
        this.dataHoraFimExecucao = dataHoraFimExecucao;
    }

    public Boolean getIsEncaixe() {
        return isEncaixe;
    }

    public void setIsEncaixe(Boolean isEncaixe) {
        this.isEncaixe = isEncaixe;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }
    
    // Método auxiliar para compatibilidade
    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicioPrevista;
    }
}
