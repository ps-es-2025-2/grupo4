package br.com.simplehealth.agendamento.model.enums;

/**
 * Enum que representa as ações possíveis para um bloqueio de agenda.
 */
public enum AcaoBloqueioEnum {
    ALTERAR_DADOS("Alterar Dados"),
    DESATIVAR("Desativar Bloqueio");

    private final String descricao;

    AcaoBloqueioEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
