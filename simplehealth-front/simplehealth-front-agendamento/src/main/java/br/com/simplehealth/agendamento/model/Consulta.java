package br.com.simplehealth.agendamento.model;

import br.com.simplehealth.agendamento.model.enums.TipoConsultaEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representa uma consulta médica no sistema.
 * Estende Agendamento com informações específicas de consultas.
 * Alinhado com a especificação da API (ConsultaResponseDTO).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Consulta extends Agendamento {

    @JsonProperty("especialidade")
    private String especialidade;

    @JsonProperty("tipoConsulta")
    private TipoConsultaEnum tipoConsulta; // PRIMEIRA, RETORNO, ROTINA

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

    public TipoConsultaEnum getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(TipoConsultaEnum tipoConsulta) {
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
