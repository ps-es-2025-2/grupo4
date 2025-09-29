package br.com.simplehealth.armazenamento.config;

/**
 * Classe de configuração para URLs das APIs e outras configurações da aplicação.
 * 
 * @version 1.0
 */
public class AppConfig {
    
    // URLs base das APIs
    public static final String API_BASE_URL = getApiBaseUrl();
    public static final String ITENS_API_URL = API_BASE_URL + "/itens";
    public static final String FORNECEDORES_API_URL = API_BASE_URL + "/fornecedores";
    public static final String PEDIDOS_API_URL = API_BASE_URL + "/pedidos";
    public static final String ESTOQUES_API_URL = API_BASE_URL + "/estoques";
    public static final String MEDICAMENTOS_API_URL = API_BASE_URL + "/medicamentos";
    public static final String HOSPITALARES_API_URL = API_BASE_URL + "/hospitalares";
    public static final String ALIMENTOS_API_URL = API_BASE_URL + "/alimentos";
    
    // Configurações de timeout
    public static final int CONNECTION_TIMEOUT = 5000; // 5 segundos
    public static final int READ_TIMEOUT = 10000; // 10 segundos
    
    // Configurações da aplicação
    public static final String APP_NAME = "SimpleHealth - Módulo de Armazenamento";
    public static final String APP_VERSION = "1.0.0";
    
    /**
     * Obtém a URL base da API baseada na variável de ambiente ou usa o padrão.
     * 
     * @return URL base da API
     */
    private static String getApiBaseUrl() {
        String baseUrl = System.getenv("SIMPLEHEALTH_API_URL");
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            baseUrl = System.getProperty("simplehealth.api.url", "http://localhost:8080");
        }
        return baseUrl;
    }
    
    /**
     * Verifica se está em modo de desenvolvimento.
     * 
     * @return true se estiver em desenvolvimento
     */
    public static boolean isDevMode() {
        String devMode = System.getProperty("simplehealth.dev.mode", "false");
        return "true".equalsIgnoreCase(devMode);
    }
    
    /**
     * Obtém o nível de log configurado.
     * 
     * @return nível de log
     */
    public static String getLogLevel() {
        return System.getProperty("simplehealth.log.level", "INFO");
    }
}