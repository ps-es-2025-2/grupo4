package br.com.simplehealth.agendamento.model.enums;

/**
 * Enumeração das ações disponíveis para agendamentos.
 * Cada ação corresponde a um endpoint específico da API.
 */
public enum AcaoAgendamentoEnum {
    
    ALTERAR_DADOS("Alterar Dados", "Atualizar informações do agendamento"),
    INICIAR("Iniciar Atendimento", "Marcar início do atendimento"),
    FINALIZAR("Finalizar Atendimento", "Marcar conclusão do atendimento"),
    CANCELAR("Cancelar", "Cancelar o agendamento");

    private final String label;
    private final String descricao;

    AcaoAgendamentoEnum(String label, String descricao) {
        this.label = label;
        this.descricao = descricao;
    }

    public String getLabel() {
        return label;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return label;
    }
}
