package br.com.simplehealth.estoque.service;

import br.com.simplehealth.estoque.config.AppConfig;
import br.com.simplehealth.estoque.model.Estoque;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EstoqueService {
    
    private static final Logger logger = LoggerFactory.getLogger(EstoqueService.class);
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    
    public EstoqueService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.baseUrl = AppConfig.API_BASE_URL + AppConfig.ENDPOINT_CONTROLE;
    }
    
    public List<Estoque> listar() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl);
            logger.info("Buscando estoques em: {}", baseUrl);
            
            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                logger.info("Status: {}, Response: {}", status, responseBody.substring(0, Math.min(200, responseBody.length())));
                
                if (status == 200) {
                    return objectMapper.readValue(responseBody, 
                        new TypeReference<List<Estoque>>() {});
                }
                logger.error("Erro ao listar estoques. Status: {}", status);
                return new ArrayList<>();
            });
        } catch (Exception e) {
            logger.error("Erro ao conectar com API de estoques", e);
            throw new IOException("Erro ao listar estoques: " + e.getMessage(), e);
        }
    }
    
    public Estoque buscarPorId(UUID id) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl + "/" + id);
            
            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                if (status == 200) {
                    return objectMapper.readValue(responseBody, Estoque.class);
                }
                return null;
            });
        }
    }
    
    public Estoque salvar(Estoque estoque) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl);
            String json = objectMapper.writeValueAsString(estoque);
            
            request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
            request.setHeader("Content-Type", "application/json");
            
            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                if (status == 200) {
                    return objectMapper.readValue(responseBody, Estoque.class);
                }
                return null;
            });
        }
    }
    
    public Estoque atualizar(UUID id, Estoque estoque) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(baseUrl + "/" + id);
            String json = objectMapper.writeValueAsString(estoque);
            
            request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
            request.setHeader("Content-Type", "application/json");
            
            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                if (status == 200) {
                    return objectMapper.readValue(responseBody, Estoque.class);
                }
                return null;
            });
        }
    }
    
    public boolean deletar(UUID id) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(baseUrl + "/" + id);
            
            return httpClient.execute(request, response -> {
                return response.getCode() == 204;
            });
        }
    }
}
