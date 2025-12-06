package br.com.simplehealth.estoque.service;

import br.com.simplehealth.estoque.config.AppConfig;
import br.com.simplehealth.estoque.model.Medicamento;
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

/**
 * @deprecated O backend não possui endpoint específico para Medicamentos.
 * Os itens do tipo Medicamento são gerenciados através do endpoint /controle/entrada
 * usando EntradaItensDTO com tipo=MEDICAMENTO.
 * Este service permanece apenas para compatibilidade com controllers legados.
 */
@Deprecated
public class MedicamentoService {
    
    private static final Logger logger = LoggerFactory.getLogger(MedicamentoService.class);
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    
    public MedicamentoService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        // NOTA: Backend não possui endpoint específico para Medicamentos
        // Os itens são gerenciados através do endpoint /controle
        this.baseUrl = AppConfig.API_BASE_URL + "/medicamentos"; // Endpoint não implementado
    }
    
    public List<Medicamento> listar() throws IOException {
        logger.debug("Listando medicamentos...");
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl);
            
            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                logger.debug("Status: {}, Response: {}", status, responseBody);
                
                if (status == 200) {
                    return objectMapper.readValue(responseBody, 
                        new TypeReference<List<Medicamento>>() {});
                }
                return new ArrayList<>();
            });
        }
    }
    
    public Medicamento buscarPorId(UUID id) throws IOException {
        logger.debug("Buscando medicamento por ID: {}", id);
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl + "/" + id);
            
            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                logger.debug("Status: {}, Response: {}", status, responseBody);
                
                if (status == 200) {
                    return objectMapper.readValue(responseBody, Medicamento.class);
                }
                return null;
            });
        }
    }
    
    public Medicamento salvar(Medicamento medicamento) throws IOException {
        logger.debug("Salvando medicamento: {}", medicamento);
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl);
            String json = objectMapper.writeValueAsString(medicamento);
            
            request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
            request.setHeader("Content-Type", "application/json");
            
            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                logger.debug("Status: {}, Response: {}", status, responseBody);
                
                if (status == 200) {
                    return objectMapper.readValue(responseBody, Medicamento.class);
                }
                return null;
            });
        }
    }
    
    public Medicamento atualizar(UUID id, Medicamento medicamento) throws IOException {
        logger.debug("Atualizando medicamento ID: {}", id);
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(baseUrl + "/" + id);
            String json = objectMapper.writeValueAsString(medicamento);
            
            request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
            request.setHeader("Content-Type", "application/json");
            
            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                logger.debug("Status: {}, Response: {}", status, responseBody);
                
                if (status == 200) {
                    return objectMapper.readValue(responseBody, Medicamento.class);
                }
                return null;
            });
        }
    }
    
    public boolean deletar(UUID id) throws IOException {
        logger.debug("Deletando medicamento ID: {}", id);
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(baseUrl + "/" + id);
            
            return httpClient.execute(request, response -> {
                int status = response.getCode();
                logger.debug("Status da deleção: {}", status);
                return status == 200;
            });
        }
    }
}
