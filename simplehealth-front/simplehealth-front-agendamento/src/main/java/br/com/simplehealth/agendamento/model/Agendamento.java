package br.com.simplehealth.agendamento.model;

import br.com.simplehealth.agendamento.model.enums.ModalidadeEnum;
import br.com.simplehealth.agendamento.model.enums.StatusAgendamentoEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Classe base abstrata para todos os tipos de agendamento.
 * Contém os atributos comuns compartilhados por Consulta, Exame e Procedimento.
 * Alinhado com a especificação da API (ConsultaResponseDTO, ExameResponseDTO, ProcedimentoResponseDTO).
 */
public abstract class Agendamento {

    private String id;

    // Data/hora em que o agendamento foi criado no sistema
    @JsonProperty("dataHoraAgendamento")
    private LocalDateTime dataHoraAgendamento;

    // Data/hora prevista para início do atendimento (planejamento)
    @JsonProperty("dataHoraInicioPrevista")
    private LocalDateTime dataHoraInicioPrevista;

    // Data/hora prevista para fim do atendimento (planejamento)
    @JsonProperty("dataHoraFimPrevista")
    private LocalDateTime dataHoraFimPrevista;

    // Data/hora real de início do atendimento (execução)
    @JsonProperty("dataHoraInicioExecucao")
    private LocalDateTime dataHoraInicioExecucao;

    // Data/hora real de fim do atendimento (execução)
    @JsonProperty("dataHoraFimExecucao")
    private LocalDateTime dataHoraFimExecucao;

    @JsonProperty("isEncaixe")
    private Boolean isEncaixe = false;

    private ModalidadeEnum modalidade; // PRESENCIAL, REMOTA

    @JsonProperty("motivoEncaixe")
    private String motivoEncaixe;

    private String observacoes;

    private StatusAgendamentoEnum status; // ATIVO, CANCELADO, REALIZADO, NAO_COMPARECEU

    @JsonProperty("motivoCancelamento")
    private String motivoCancelamento;

    @JsonProperty("dataCancelamento")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
