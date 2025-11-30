package br.com.simplehealth.estoque.util;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * Classe utilitária para validações de campos do sistema de estoque.
 * Centraliza todas as regras de validação para reutilização.
 */
public class ValidationUtils {

    // Padrões de validação
    private static final Pattern CNPJ_PATTERN = Pattern.compile("\\d{14}");
    private static final Pattern CNPJ_FORMATTED_PATTERN = Pattern.compile("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern TELEFONE_PATTERN = Pattern.compile("\\d{10,11}");
    private static final Pattern TELEFONE_FORMATTED_PATTERN = Pattern.compile("\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4}");
    
    /**
     * Valida se um campo não está vazio ou nulo.
     */
    public static boolean validarCampoObrigatorio(String valor, String nomeCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Valida CNPJ (14 dígitos numéricos).
     * Aceita com ou sem formatação.
     */
    public static boolean validarCNPJ(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            return false;
        }
        
        String cnpjLimpo = cnpj.replaceAll("[./-]", "");
        
        if (!CNPJ_PATTERN.matcher(cnpjLimpo).matches()) {
            return false;
        }
        
        // Validação matemática dos dígitos verificadores
        if (cnpjLimpo.matches("(\\d)\\1{13}")) {
            return false; // CNPJ com todos os dígitos iguais é inválido
        }
        
        try {
            // Primeiro dígito verificador
            int soma = 0;
            int[] peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            for (int i = 0; i < 12; i++) {
                soma += Character.getNumericValue(cnpjLimpo.charAt(i)) * peso1[i];
            }
            int digito1 = (soma % 11 < 2) ? 0 : 11 - (soma % 11);
            
            if (Character.getNumericValue(cnpjLimpo.charAt(12)) != digito1) {
                return false;
            }
            
            // Segundo dígito verificador
            soma = 0;
            int[] peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            for (int i = 0; i < 13; i++) {
                soma += Character.getNumericValue(cnpjLimpo.charAt(i)) * peso2[i];
            }
            int digito2 = (soma % 11 < 2) ? 0 : 11 - (soma % 11);
            
            return Character.getNumericValue(cnpjLimpo.charAt(13)) == digito2;
            
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida formato de email.
     */
    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Valida telefone (10 ou 11 dígitos com DDD).
     * Aceita com ou sem formatação.
     */
    public static boolean validarTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }
        
        String telefoneLimpo = telefone.replaceAll("[()\\s-]", "");
        return TELEFONE_PATTERN.matcher(telefoneLimpo).matches();
    }

    /**
     * Valida quantidade (deve ser positiva).
     */
    public static boolean validarQuantidade(String quantidade) {
        if (quantidade == null || quantidade.trim().isEmpty()) {
            return false;
        }
        
        try {
            int qtd = Integer.parseInt(quantidade);
            return qtd >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Valida se a data de validade não está no passado.
     */
    public static boolean validarDataValidade(LocalDateTime validade) {
        if (validade == null) {
            return false;
        }
        return validade.isAfter(LocalDateTime.now());
    }

    /**
     * Valida formato de lote (alfanumérico, 3-20 caracteres).
     */
    public static boolean validarLote(String lote) {
        if (lote == null || lote.trim().isEmpty()) {
            return false;
        }
        String loteTrimmed = lote.trim();
        return loteTrimmed.length() >= 3 && loteTrimmed.length() <= 20 
               && loteTrimmed.matches("[A-Za-z0-9]+");
    }

    /**
     * Valida número de nota fiscal (alfanumérico, 6-44 caracteres).
     */
    public static boolean validarNotaFiscal(String nf) {
        if (nf == null || nf.trim().isEmpty()) {
            return false;
        }
        String nfTrimmed = nf.trim();
        return nfTrimmed.length() >= 6 && nfTrimmed.length() <= 44;
    }

    /**
     * Formata CNPJ para exibição (00.000.000/0000-00).
     */
    public static String formatarCNPJ(String cnpj) {
        if (cnpj == null || cnpj.isEmpty()) {
            return cnpj;
        }
        
        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
        
        if (cnpjLimpo.length() != 14) {
            return cnpj;
        }
        
        return String.format("%s.%s.%s/%s-%s",
            cnpjLimpo.substring(0, 2),
            cnpjLimpo.substring(2, 5),
            cnpjLimpo.substring(5, 8),
            cnpjLimpo.substring(8, 12),
            cnpjLimpo.substring(12, 14)
        );
    }

    /**
     * Formata telefone para exibição ((00) 0000-0000 ou (00) 00000-0000).
     */
    public static String formatarTelefone(String telefone) {
        if (telefone == null || telefone.isEmpty()) {
            return telefone;
        }
        
        String telefoneLimpo = telefone.replaceAll("[^0-9]", "");
        
        if (telefoneLimpo.length() == 10) {
            return String.format("(%s) %s-%s",
                telefoneLimpo.substring(0, 2),
                telefoneLimpo.substring(2, 6),
                telefoneLimpo.substring(6, 10)
            );
        } else if (telefoneLimpo.length() == 11) {
            return String.format("(%s) %s-%s",
                telefoneLimpo.substring(0, 2),
                telefoneLimpo.substring(2, 7),
                telefoneLimpo.substring(7, 11)
            );
        }
        
        return telefone;
    }

    /**
     * Mensagem de erro formatada para campo obrigatório.
     */
    public static String mensagemCampoObrigatorio(String nomeCampo) {
        return "O campo '" + nomeCampo + "' é obrigatório.";
    }

    /**
     * Mensagem de erro formatada para formato inválido.
     */
    public static String mensagemFormatoInvalido(String nomeCampo, String formatoEsperado) {
        return "O campo '" + nomeCampo + "' está em formato inválido. Formato esperado: " + formatoEsperado;
    }
}
