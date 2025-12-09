package br.com.simplehealth.cadastro.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Modelo de dados para Histórico do Paciente.
 * Contém todos os dados cadastrais e relacionados ao paciente.
 */
public class HistoricoPaciente {

    @JsonProperty("dadosCadastrais")
    private Paciente dadosCadastrais;

    @JsonProperty("pessoa")
    private Pessoa pessoa;

    @JsonProperty("consultas")
    private List<Consulta> consultas;

    @JsonProperty("exames")
    private List<Exame> exames;

    @JsonProperty("procedimentos")
    private List<Procedimento> procedimentos;

    @JsonProperty("itensBaixados")
    private List<ItemEstoque> itensBaixados;

    @JsonProperty("pagamentos")
    private List<Pagamento> pagamentos;

    public HistoricoPaciente() {
    }

    // Getters e Setters
    public Paciente getDadosCadastrais() {
        return dadosCadastrais;
    }

    public void setDadosCadastrais(Paciente dadosCadastrais) {
        this.dadosCadastrais = dadosCadastrais;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    public List<Exame> getExames() {
        return exames;
    }

    public void setExames(List<Exame> exames) {
        this.exames = exames;
    }

    public List<Procedimento> getProcedimentos() {
        return procedimentos;
    }

    public void setProcedimentos(List<Procedimento> procedimentos) {
        this.procedimentos = procedimentos;
    }

    public List<ItemEstoque> getItensBaixados() {
        return itensBaixados;
    }

    public void setItensBaixados(List<ItemEstoque> itensBaixados) {
        this.itensBaixados = itensBaixados;
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }

    // Compatibilidade com código antigo - retorna agendamentos vazios
    @Deprecated
    public List<Agendamento> getAgendamentos() {
        return java.util.Collections.emptyList();
    }
}
