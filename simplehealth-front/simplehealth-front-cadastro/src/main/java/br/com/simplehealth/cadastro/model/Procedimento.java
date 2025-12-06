package br.com.simplehealth.cadastro.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Modelo de dados para Procedimento.
 */
public class Procedimento {

    @JsonProperty("id")
    private String id;

    @JsonProperty("dataHoraInicio")
    private LocalDateTime dataHoraInicio;

    @JsonProperty("dataHoraFim")
    private LocalDateTime dataHoraFim;

    @JsonProperty("isEncaixe")
    private Boolean isEncaixe;

    @JsonProperty("modalidade")
    private String modalidade;

    @JsonProperty("motivoEncaixe")
    private String motivoEncaixe;

    @JsonProperty("observacoes")
    private String observacoes;

    @JsonProperty("status")
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

    @JsonProperty("descricaoProcedimento")
    private String descricaoProcedimento;

    @JsonProperty("salaEquipamentoNecessario")
    private String salaEquipamentoNecessario;

    @JsonProperty("nivelRisco")
    private String nivelRisco;

    public Procedimento() {
    }

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

    public String getDescricaoProcedimento() {
        return descricaoProcedimento;
    }

    public void setDescricaoProcedimento(String descricaoProcedimento) {
        this.descricaoProcedimento = descricaoProcedimento;
    }

    public String getSalaEquipamentoNecessario() {
        return salaEquipamentoNecessario;
    }

    public void setSalaEquipamentoNecessario(String salaEquipamentoNecessario) {
        this.salaEquipamentoNecessario = salaEquipamentoNecessario;
    }

    public String getNivelRisco() {
        return nivelRisco;
    }

    public void setNivelRisco(String nivelRisco) {
        this.nivelRisco = nivelRisco;
    }
}
