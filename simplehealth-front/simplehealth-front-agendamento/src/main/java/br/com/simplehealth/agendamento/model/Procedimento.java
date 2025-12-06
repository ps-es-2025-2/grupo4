package br.com.simplehealth.agendamento.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representa um procedimento médico no sistema.
 * Estende Agendamento com informações específicas de procedimentos.
 */
public class Procedimento extends Agendamento {

    @JsonProperty("descricaoProcedimento")
    private String descricaoProcedimento;

    @JsonProperty("salaEquipamentoNecessario")
    private String salaEquipamentoNecessario;

    @JsonProperty("nivelRisco")
    private String nivelRisco;

    // Construtores
    public Procedimento() {
        super();
    }

    // Getters e Setters
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

    @Override
    public String toString() {
        return "Procedimento{" +
                "id='" + getId() + '\'' +
                ", descricaoProcedimento='" + descricaoProcedimento + '\'' +
                ", nivelRisco='" + nivelRisco + '\'' +
                ", pacienteCpf='" + getPacienteCpf() + '\'' +
                ", medicoCrm='" + getMedicoCrm() + '\'' +
                ", dataHoraInicio=" + getDataHoraInicio() +
                '}';
    }
}
