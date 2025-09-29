package br.com.simplehealth.armazenamento.service;

import br.com.simplehealth.armazenamento.config.AppConfig;
import br.com.simplehealth.armazenamento.model.Alimento;
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
 * Serviço para consumir APIs relacionadas a alimentos.
 * 
 * @version 1.0
 */
public class AlimentoService {
    
    private static final Logger logger = LoggerFactory.getLogger(AlimentoService.class);
    private static final String BASE_URL = AppConfig.ALIMENTOS_API_URL;
    
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AlimentoService() {
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        // Configurar para não falhar em propriedades desconhecidas
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Desabilitar escrita de datas como timestamps
        this.objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Busca todos os alimentos.
     */
    public List<Alimento> buscarTodos() throws IOException {
        HttpGet request = new HttpGet(BASE_URL);
        request.setHeader("Content-Type", "application/json");
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, new TypeReference<List<Alimento>>() {});
            });
        }
    }

    /**
     * Busca um alimento por ID.
     */
    public Alimento buscarPorId(Long id) throws IOException {
        HttpGet request = new HttpGet(BASE_URL + "/" + id);
        request.setHeader("Content-Type", "application/json");
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                if (response.getCode() == 404) {
                    return null;
                }
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Alimento.class);
            });
        }
    }

    /**
     * Cria um novo alimento.
     */
    public Alimento criar(Alimento alimento) throws IOException {
        HttpPost request = new HttpPost(BASE_URL);
        request.setHeader("Content-Type", "application/json");
        
        String json = objectMapper.writeValueAsString(alimento);
        request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Alimento.class);
            });
        }
    }

    /**
     * Atualiza um alimento existente.
     */
    public Alimento atualizar(Long id, Alimento alimento) throws IOException {
        HttpPut request = new HttpPut(BASE_URL + "/" + id);
        request.setHeader("Content-Type", "application/json");
        
        String json = objectMapper.writeValueAsString(alimento);
        request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Alimento.class);
            });
        }
    }

    /**
     * Deleta um alimento.
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