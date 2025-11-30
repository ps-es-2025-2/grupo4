package br.com.simplehealth.agendamento.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Classe base abstrata para todos os tipos de agendamento.
 * Contém os atributos comuns compartilhados por Consulta, Exame e Procedimento.
 */
public abstract class Agendamento {

    private String id;

    @JsonProperty("data_hora_inicio")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataHoraInicio;

    @JsonProperty("data_hora_fim")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataHoraFim;

    @JsonProperty("is_encaixe")
    private Boolean isEncaixe = false;

    private String modalidade; // PRESENCIAL, ONLINE

    @JsonProperty("motivo_encaixe")
    private String motivoEncaixe;

    private String observacoes;

    private String status; // ATIVO, CANCELADO, REALIZADO

    @JsonProperty("motivo_cancelamento")
    private String motivoCancelamento;

    @JsonProperty("data_cancelamento")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataCancelamento;

    @JsonProperty("paciente_cpf")
    private String pacienteCpf;

    @JsonProperty("medico_crm")
    private String medicoCrm;

    @JsonProperty("convenio_nome")
    private String convenioNome;

    @JsonProperty("usuario_criador_login")
    private String usuarioCriadorLogin;

    @JsonProperty("usuario_cancelador_login")
    private String usuarioCanceladorLogin;

    // Getters e Setters
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
}
