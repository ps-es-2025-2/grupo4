package br.com.simplehealth.agendamento.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representa uma consulta médica no sistema.
 * Estende Agendamento com informações específicas de consultas.
 */
public class Consulta extends Agendamento {

    private String especialidade;

    @JsonProperty("tipoConsulta")
    private String tipoConsulta; // PRIMEIRA_VEZ, RETORNO

    // Construtores
    public Consulta() {
        super();
    }

    // Getters e Setters
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

    @Override
    public String toString() {
        return "Consulta{" +
                "id='" + getId() + '\'' +
                ", especialidade='" + especialidade + '\'' +
                ", tipoConsulta='" + tipoConsulta + '\'' +
                ", pacienteCpf='" + getPacienteCpf() + '\'' +
                ", medicoCrm='" + getMedicoCrm() + '\'' +
                ", dataHoraInicio=" + getDataHoraInicio() +
                '}';
    }
}
