package br.com.simplehealth.agendamento.model.enums;

/**
 * Enum que representa os possíveis status de um agendamento.
 * Alinhado com o backend (StatusAgendamentoEnum).
 */
public enum StatusAgendamentoEnum {
    ATIVO("Ativo"),
    INICIADO("Iniciado"),
    FINALIZADO("Finalizado"),
    CANCELADO("Cancelado"),
    NAO_COMPARECEU("Não Compareceu");

    private final String descricao;

    StatusAgendamentoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return this.name();
    }

    /**
     * Converte string para enum, lidando com valores null ou inválidos
     */
    public static StatusAgendamentoEnum fromString(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        try {
            return StatusAgendamentoEnum.valueOf(valor.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
