package br.com.simplehealth.agendamento.model.enums;

/**
 * Enum que representa as modalidades de atendimento disponíveis.
 * Alinhado com o backend (ModalidadeEnum).
 */
public enum ModalidadeEnum {
    PRESENCIAL("Presencial"),
    REMOTA("Remota/Telemedicina");

    private final String descricao;

    ModalidadeEnum(String descricao) {
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
    public static ModalidadeEnum fromString(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        try {
            return ModalidadeEnum.valueOf(valor.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
