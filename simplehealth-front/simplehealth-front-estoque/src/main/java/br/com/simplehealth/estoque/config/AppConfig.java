package br.com.simplehealth.estoque.config;

/**
 * Configurações centralizadas da aplicação
 */
public class AppConfig {
    
    // URL base da API de estoque
    public static final String API_BASE_URL = "http://localhost:8083/estoque";
    
    // Endpoints específicos (baseados na OpenAPI do backend)
    public static final String ENDPOINT_CONTROLE = "/controle";
    public static final String ENDPOINT_FORNECEDORES = "/fornecedores";
    public static final String ENDPOINT_PEDIDOS = "/pedidos";
    public static final String ENDPOINT_ALIMENTOS = "/alimentos";
    public static final String ENDPOINT_MEDICAMENTOS = "/medicamentos";
    public static final String ENDPOINT_HOSPITALARES = "/hospitalares";
    public static final String ENDPOINT_ITENS = "/itens";
    
    // Endpoints dos casos de uso
    public static final String ENDPOINT_CONTROLE_VALIDADE = "/controle/validade";
    public static final String ENDPOINT_CONTROLE_BAIXA = "/controle/baixa";
    public static final String ENDPOINT_CONTROLE_ENTRADA = "/controle/entrada";
    
    // Timeouts
    public static final int CONNECTION_TIMEOUT = 30000; // 30 segundos
    public static final int REQUEST_TIMEOUT = 30000;
    
    private AppConfig() {
        // Utility class
    }
}
