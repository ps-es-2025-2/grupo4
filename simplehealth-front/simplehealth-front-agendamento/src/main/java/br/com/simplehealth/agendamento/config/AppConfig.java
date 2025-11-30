package br.com.simplehealth.agendamento.config;

/**
 * Configurações da aplicação de Agendamento.
 * Centraliza as URLs da API e outras configurações globais.
 */
public class AppConfig {

    /**
     * URL base da API de agendamento.
     * O backend está configurado na porta 8082 com context-path /agendamento
     */
    public static final String API_BASE_URL = "http://localhost:8082/agendamento";

    /**
     * Endpoint para agendamentos (consultas, exames, procedimentos)
     */
    public static final String AGENDAMENTOS_ENDPOINT = API_BASE_URL + "/agendamentos";

    /**
     * Endpoint para bloqueios de agenda
     */
    public static final String BLOQUEIO_AGENDA_ENDPOINT = API_BASE_URL + "/bloqueio-agenda";

    /**
     * Endpoint para encaixes
     */
    public static final String ENCAIXE_ENDPOINT = API_BASE_URL + "/encaixe";

    /**
     * Timeout de conexão em milissegundos
     */
    public static final int CONNECTION_TIMEOUT = 5000;

    /**
     * Timeout de resposta em milissegundos
     */
    public static final int RESPONSE_TIMEOUT = 10000;

    private AppConfig() {
        // Classe utilitária - construtor privado
    }
}
