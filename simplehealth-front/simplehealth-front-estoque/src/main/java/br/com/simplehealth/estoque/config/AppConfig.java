package br.com.simplehealth.estoque.config;

/**
 * Configurações centralizadas da aplicação
 */
public class AppConfig {
    
    // URL base da API de estoque
    public static final String API_BASE_URL = "http://localhost:8083/estoque";
    
    // Endpoints específicos
    public static final String ENDPOINT_MEDICAMENTOS = "/medicamentos";
    public static final String ENDPOINT_ALIMENTOS = "/alimentos";
    public static final String ENDPOINT_HOSPITALARES = "/hospitalares";
    public static final String ENDPOINT_FORNECEDORES = "/fornecedores";
    public static final String ENDPOINT_ESTOQUES = "/estoques";
    public static final String ENDPOINT_PEDIDOS = "/pedidos";
    public static final String ENDPOINT_ITENS = "/itens";
    
    // Timeouts
    public static final int CONNECTION_TIMEOUT = 30000; // 30 segundos
    public static final int REQUEST_TIMEOUT = 30000;
    
    private AppConfig() {
        // Utility class
    }
}
