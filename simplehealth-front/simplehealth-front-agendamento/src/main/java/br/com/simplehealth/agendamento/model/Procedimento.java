package br.com.simplehealth.agendamento.model;

import br.com.simplehealth.agendamento.model.enums.TipoConsultaEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representa um procedimento médico no sistema.
 * Estende Agendamento com informações específicas de procedimentos.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Procedimento extends Agendamento {

    @JsonProperty("descricaoProcedimento")
    private String descricaoProcedimento;

    @JsonProperty("salaEquipamentoNecessario")
    private String salaEquipamentoNecessario;

    @JsonProperty("nivelRisco")
    private String nivelRisco;

    // Campos necessários para compatibilidade com o backend (AgendarConsultaDTO)
    @JsonProperty("especialidade")
    private String especialidade = "PROCEDIMENTO"; // Valor padrão para identificar como procedimento
    
    @JsonProperty("tipoConsulta")
    private TipoConsultaEnum tipoConsulta = TipoConsultaEnum.ROTINA; // Valor padrão

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
