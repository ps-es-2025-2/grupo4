package br.com.simplehealth.agendamento.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Representa um bloqueio de agenda para um médico.
 * Impede que agendamentos sejam feitos em determinado período.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BloqueioAgenda {

    private String id;

    @JsonProperty("dataInicio")
    private LocalDateTime dataInicio;

    @JsonProperty("dataFim")
    private LocalDateTime dataFim;

    private String motivo;

    @JsonProperty("antecedenciaMinima")
    private Integer antecedenciaMinima;

    @JsonProperty("medicoCrm")
    private String medicoCrm;

    @JsonProperty("usuarioCriadorLogin")
    private String usuarioCriadorLogin;

    @JsonProperty("dataCriacao")
    private LocalDateTime dataCriacao;

    private Boolean ativo = true;

    // Construtores
    public BloqueioAgenda() {
    }

    // Getters e Setters
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

    @Override
    public String toString() {
        return "BloqueioAgenda{" +
                "id='" + id + '\'' +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", motivo='" + motivo + '\'' +
                ", medicoCrm='" + medicoCrm + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}
