package br.com.simplehealth.agendamento.model.enums;

/**
 * Enum que representa os tipos de consulta disponíveis no sistema.
 * Alinhado com o backend (TipoConsultaEnum).
 */
public enum TipoConsultaEnum {
    PRIMEIRA("Primeira Consulta"),
    RETORNO("Retorno"),
    ROTINA("Consulta de Rotina");

    private final String descricao;

    TipoConsultaEnum(String descricao) {
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
    public static TipoConsultaEnum fromString(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        try {
            return TipoConsultaEnum.valueOf(valor.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
