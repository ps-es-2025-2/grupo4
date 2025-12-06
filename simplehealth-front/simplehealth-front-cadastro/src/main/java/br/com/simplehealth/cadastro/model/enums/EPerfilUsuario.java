package br.com.simplehealth.cadastro.model.enums;

/**
 * Enumeração dos perfis de usuário do sistema.
 * Deve estar alinhado com o enum do backend.
 */
public enum EPerfilUsuario {
    MEDICO("Médico"),
    SECRETARIA("Secretaria"),
    GESTOR("Gestor"),
    FINANCEIRO("Financeiro"),
    TESOURARIA("Tesouraria");

    private final String descricao;

    EPerfilUsuario(String descricao) {
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
