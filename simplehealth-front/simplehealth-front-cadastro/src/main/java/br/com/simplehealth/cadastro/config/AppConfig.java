package br.com.simplehealth.cadastro.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Configurações da aplicação.
 */
public class AppConfig {

    // URL base da API de cadastro
    public static final String API_BASE_URL = "http://localhost:8081/cadastro";
    
    // Endpoints
    public static final String PACIENTES_ENDPOINT = API_BASE_URL + "/pacientes";
    public static final String MEDICOS_ENDPOINT = API_BASE_URL + "/api/cadastro/medicos";
    public static final String CONVENIOS_ENDPOINT = API_BASE_URL + "/api/cadastro/convenios";
    public static final String USUARIOS_ENDPOINT = API_BASE_URL + "/api/cadastro/usuarios";

    // Timeout para requisições HTTP (em segundos)
    public static final int HTTP_TIMEOUT = 30;

    private static ObjectMapper objectMapper;

    /**
     * Retorna uma instância configurada do ObjectMapper para JSON.
     */
    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        }
        return objectMapper;
    }
}
