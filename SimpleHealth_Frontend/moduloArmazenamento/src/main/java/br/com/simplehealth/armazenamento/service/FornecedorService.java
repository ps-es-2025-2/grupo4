package br.com.simplehealth.armazenamento.service;

import br.com.simplehealth.armazenamento.config.AppConfig;
import br.com.simplehealth.armazenamento.model.Fornecedor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Serviço para consumir APIs relacionadas a fornecedores.
 * 
 * @version 1.0
 */
public class FornecedorService {
    
    private static final Logger logger = LoggerFactory.getLogger(FornecedorService.class);
    private static final String BASE_URL = AppConfig.FORNECEDORES_API_URL;
    
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FornecedorService() {
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        // Configurar para não falhar em propriedades desconhecidas
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Desabilitar escrita de datas como timestamps
        this.objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Permitir parsing flexível de datas
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
    }

    /**
     * Busca todos os fornecedores.
     */
    public List<Fornecedor> buscarTodos() throws IOException {
        HttpGet request = new HttpGet(BASE_URL);
        request.setHeader("Content-Type", "application/json");
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, new TypeReference<List<Fornecedor>>() {});
            });
        }
    }

    /**
     * Busca um fornecedor por ID.
     */
    public Fornecedor buscarPorId(Long id) throws IOException {
        HttpGet request = new HttpGet(BASE_URL + "/" + id);
        request.setHeader("Content-Type", "application/json");
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                if (response.getCode() == 404) {
                    return null;
                }
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Fornecedor.class);
            });
        }
    }

    /**
     * Cria um novo fornecedor.
     */
    public Fornecedor criar(Fornecedor fornecedor) throws IOException {
        HttpPost request = new HttpPost(BASE_URL);
        request.setHeader("Content-Type", "application/json");
        
        String json = objectMapper.writeValueAsString(fornecedor);
        request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Fornecedor.class);
            });
        }
    }

    /**
     * Atualiza um fornecedor existente.
     */
    public Fornecedor atualizar(Long id, Fornecedor fornecedor) throws IOException {
        HttpPut request = new HttpPut(BASE_URL + "/" + id);
        request.setHeader("Content-Type", "application/json");
        
        String json = objectMapper.writeValueAsString(fornecedor);
        request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Fornecedor.class);
            });
        }
    }

    /**
     * Deleta um fornecedor.
     */
    public boolean deletar(Long id) throws IOException {
        HttpDelete request = new HttpDelete(BASE_URL + "/" + id);
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                logger.info("Status da deleção: {}", response.getCode());
                return response.getCode() == 200 || response.getCode() == 204;
            });
        }
    }



    public void close() throws IOException {
        if (httpClient != null) {
            httpClient.close();
        }
    }
}