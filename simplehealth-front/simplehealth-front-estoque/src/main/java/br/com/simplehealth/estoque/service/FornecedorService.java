package br.com.simplehealth.estoque.service;

import br.com.simplehealth.estoque.config.AppConfig;
import br.com.simplehealth.estoque.model.Fornecedor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class FornecedorService {
    
    private static final Logger logger = LoggerFactory.getLogger(FornecedorService.class);
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    
    public FornecedorService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.baseUrl = AppConfig.API_BASE_URL + AppConfig.ENDPOINT_FORNECEDORES;
    }
    
    public List<Fornecedor> listar() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl);
            
            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                if (status == 200) {
                    return objectMapper.readValue(responseBody, 
                        new TypeReference<List<Fornecedor>>() {});
                }
                return new ArrayList<>();
            });
        }
    }
    
    public Fornecedor buscarPorId(UUID id) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl + "/" + id);
            
            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                if (status == 200) {
                    return objectMapper.readValue(responseBody, Fornecedor.class);
                }
                return null;
            });
        }
    }
    
    public Fornecedor salvar(Fornecedor fornecedor) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl);
            String json = objectMapper.writeValueAsString(fornecedor);
            
            request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
            request.setHeader("Content-Type", "application/json");
            
            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                if (status == 200) {
                    return objectMapper.readValue(responseBody, Fornecedor.class);
                }
                return null;
            });
        }
    }
    
    public Fornecedor atualizar(UUID id, Fornecedor fornecedor) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(baseUrl + "/" + id);
            String json = objectMapper.writeValueAsString(fornecedor);
            
            request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
            request.setHeader("Content-Type", "application/json");
            
            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                if (status == 200) {
                    return objectMapper.readValue(responseBody, Fornecedor.class);
                }
                return null;
            });
        }
    }
    
    public boolean deletar(UUID id) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(baseUrl + "/" + id);
            
            return httpClient.execute(request, response -> {
                return response.getCode() == 200;
            });
        }
    }
}
