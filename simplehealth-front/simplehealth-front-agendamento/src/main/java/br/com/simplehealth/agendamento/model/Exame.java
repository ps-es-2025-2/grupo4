package br.com.simplehealth.agendamento.model;

import br.com.simplehealth.agendamento.model.enums.TipoConsultaEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representa um exame médico no sistema.
 * Estende Agendamento com informações específicas de exames.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Exame extends Agendamento {

    @JsonProperty("nomeExame")
    private String nomeExame;

    @JsonProperty("requerPreparo")
    private Boolean requerPreparo;

    @JsonProperty("instrucoesPreparo")
    private String instrucoesPreparo;

    // Campos necessários para compatibilidade com o backend (AgendarConsultaDTO)
    // Estes campos são enviados automaticamente ao backend mas não aparecem no formulário
    @JsonProperty("especialidade")
    private String especialidade = "EXAME"; // Valor padrão para identificar como exame
    
    @JsonProperty("tipoConsulta")
    private TipoConsultaEnum tipoConsulta = TipoConsultaEnum.ROTINA; // Valor padrão

    // Construtores
    public Exame() {
        super();
    }

    // Getters e Setters
    public String getNomeExame() {
        return nomeExame;
    }

    public void setNomeExame(String nomeExame) {
        this.nomeExame = nomeExame;
    }

    public Boolean getRequerPreparo() {
        return requerPreparo;
    }

    public void setRequerPreparo(Boolean requerPreparo) {
        this.requerPreparo = requerPreparo;
    }

    public String getInstrucoesPreparo() {
        return instrucoesPreparo;
    }

    public void setInstrucoesPreparo(String instrucoesPreparo) {
        this.instrucoesPreparo = instrucoesPreparo;
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

    @Override
    public String toString() {
        return "Exame{" +
                "id='" + getId() + '\'' +
                ", nomeExame='" + nomeExame + '\'' +
                ", requerPreparo=" + requerPreparo +
                ", pacienteCpf='" + getPacienteCpf() + '\'' +
                ", medicoCrm='" + getMedicoCrm() + '\'' +
                ", dataHoraInicioPrevista=" + getDataHoraInicioPrevista() +
                ", status=" + getStatus() +
                '}';
    }
}
