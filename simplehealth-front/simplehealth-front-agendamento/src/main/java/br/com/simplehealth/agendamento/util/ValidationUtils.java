package br.com.simplehealth.agendamento.util;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * Utilitário para validações de dados.
 */
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern TELEFONE_PATTERN = Pattern.compile(
        "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$"
    );

    private static final Pattern CRM_PATTERN = Pattern.compile(
        "^\\d{4,10}$" // Aceita de 4 a 10 dígitos
    );

    /**
     * Valida um CPF.
     * 
     * @param cpf CPF a ser validado (com ou sem formatação)
     * @return true se o CPF é válido, false caso contrário
     */
    public static boolean validarCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }

        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se tem exatamente 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Validação do primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int digito1 = 11 - (soma % 11);
        if (digito1 >= 10) {
            digito1 = 0;
        }

        if (Character.getNumericValue(cpf.charAt(9)) != digito1) {
            return false;
        }

        // Validação do segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int digito2 = 11 - (soma % 11);
        if (digito2 >= 10) {
            digito2 = 0;
        }

        return Character.getNumericValue(cpf.charAt(10)) == digito2;
    }

    /**
     * Valida formato do CPF (apenas numérico com 11 dígitos).
     * Não valida dígitos verificadores.
     * 
     * @param cpf CPF a ser validado
     * @return true se tem formato válido (11 dígitos numéricos), false caso contrário
     */
    public static boolean validarFormatoCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        String cpfNumeros = cpf.replaceAll("[^0-9]", "");
        return cpfNumeros.length() == 11;
    }

    /**
     * Valida um email.
     * 
     * @param email Email a ser validado
     * @return true se o email é válido, false caso contrário
     */
    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Valida um telefone.
     * 
     * @param telefone Telefone a ser validado
     * @return true se o telefone é válido, false caso contrário
     */
    public static boolean validarTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }
        return TELEFONE_PATTERN.matcher(telefone.trim()).matches();
    }

    /**
     * Valida um CRM.
     * Aceita de 4 a 10 caracteres numéricos.
     * 
     * @param crm CRM a ser validado
     * @return true se o CRM é válido, false caso contrário
     */
    public static boolean validarCRM(String crm) {
        if (crm == null || crm.trim().isEmpty()) {
            return false;
        }
        // Remove caracteres não numéricos
        String crmNumeros = crm.replaceAll("[^0-9]", "");
        return CRM_PATTERN.matcher(crmNumeros).matches();
    }

    /**
     * Limita string de CPF apenas a números com no máximo 11 dígitos.
     * 
     * @param cpf CPF a ser limitado
     * @return CPF contendo apenas números, com no máximo 11 dígitos
     */
    public static String limitarCPF(String cpf) {
        if (cpf == null) {
            return "";
        }
        String somenteNumeros = cpf.replaceAll("[^0-9]", "");
        return somenteNumeros.length() <= 11 ? somenteNumeros : somenteNumeros.substring(0, 11);
    }

    /**
     * Limita string de CRM apenas a números com no máximo 10 dígitos.
     * 
     * @param crm CRM a ser limitado
     * @return CRM contendo apenas números, com no máximo 10 dígitos
     */
    public static String limitarCRM(String crm) {
        if (crm == null) {
            return "";
        }
        String somenteNumeros = crm.replaceAll("[^0-9]", "");
        return somenteNumeros.length() <= 10 ? somenteNumeros : somenteNumeros.substring(0, 10);
    }

    /**
     * Valida se uma string não é nula ou vazia.
     * 
     * @param valor String a ser validada
     * @return true se a string não é nula e não está vazia, false caso contrário
     */
    public static boolean validarCampoObrigatorio(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    /**
     * Valida um formato de data/hora.
     * 
     * @param dataHora String com data/hora no formato yyyy-MM-dd HH:mm
     * @return true se o formato é válido, false caso contrário
     */
    public static boolean validarFormatoDataHora(String dataHora) {
        if (dataHora == null || dataHora.trim().isEmpty()) {
            return false;
        }
        // Formato esperado: yyyy-MM-dd HH:mm
        Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$");
        return pattern.matcher(dataHora.trim()).matches();
    }

    /**
     * Valida um formato de hora.
     * 
     * @param hora String com hora no formato HH:mm
     * @return true se o formato é válido, false caso contrário
     */
    public static boolean validarFormatoHora(String hora) {
        if (hora == null || hora.trim().isEmpty()) {
            return false;
        }
        // Formato esperado: HH:mm
        Pattern pattern = Pattern.compile("^([01]\\d|2[0-3]):([0-5]\\d)$");
        return pattern.matcher(hora.trim()).matches();
    }

    /**
     * Valida se a data/hora de início é anterior à data/hora de fim.
     * 
     * @param dataHoraInicio Data/hora de início
     * @param dataHoraFim Data/hora de fim
     * @return true se início é anterior ao fim, false caso contrário
     */
    public static boolean validarPeriodo(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        if (dataHoraInicio == null || dataHoraFim == null) {
            return false;
        }
        return dataHoraInicio.isBefore(dataHoraFim);
    }

    /**
     * Valida se a data/hora está no futuro.
     * 
     * @param dataHora Data/hora a validar
     * @return true se está no futuro, false caso contrário
     */
    public static boolean validarDataFutura(LocalDateTime dataHora) {
        if (dataHora == null) {
            return false;
        }
        return dataHora.isAfter(LocalDateTime.now());
    }

    /**
     * Formata um CPF para o padrão xxx.xxx.xxx-xx
     * 
     * @param cpf CPF a ser formatado
     * @return CPF formatado
     */
    public static String formatarCPF(String cpf) {
        if (cpf == null) {
            return "";
        }
        String cpfNumeros = cpf.replaceAll("[^0-9]", "");
        if (cpfNumeros.length() != 11) {
            return cpf;
        }
        return cpfNumeros.substring(0, 3) + "." + 
               cpfNumeros.substring(3, 6) + "." + 
               cpfNumeros.substring(6, 9) + "-" + 
               cpfNumeros.substring(9, 11);
    }

    /**
     * Formata um telefone para o padrão (xx) xxxxx-xxxx ou (xx) xxxx-xxxx
     * 
     * @param telefone Telefone a ser formatado
     * @return Telefone formatado
     */
    public static String formatarTelefone(String telefone) {
        if (telefone == null) {
            return "";
        }
        String telefoneNumeros = telefone.replaceAll("[^0-9]", "");
        if (telefoneNumeros.length() == 11) {
            return "(" + telefoneNumeros.substring(0, 2) + ") " + 
                   telefoneNumeros.substring(2, 7) + "-" + 
                   telefoneNumeros.substring(7, 11);
        } else if (telefoneNumeros.length() == 10) {
            return "(" + telefoneNumeros.substring(0, 2) + ") " + 
                   telefoneNumeros.substring(2, 6) + "-" + 
                   telefoneNumeros.substring(6, 10);
        }
        return telefone;
    }
}
