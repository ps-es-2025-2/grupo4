package br.com.simplehealth.agendamento.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representa um exame médico no sistema.
 * Estende Agendamento com informações específicas de exames.
 */
public class Exame extends Agendamento {

    @JsonProperty("nomeExame")
    private String nomeExame;

    @JsonProperty("requerPreparo")
    private Boolean requerPreparo;

    @JsonProperty("instrucoesPreparo")
    private String instrucoesPreparo;

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

    @Override
    public String toString() {
        return "Exame{" +
                "id='" + getId() + '\'' +
                ", nomeExame='" + nomeExame + '\'' +
                ", requerPreparo=" + requerPreparo +
                ", pacienteCpf='" + getPacienteCpf() + '\'' +
                ", medicoCrm='" + getMedicoCrm() + '\'' +
                ", dataHoraInicio=" + getDataHoraInicio() +
                '}';
    }
}
