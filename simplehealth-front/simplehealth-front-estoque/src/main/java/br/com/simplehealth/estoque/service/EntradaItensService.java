package br.com.simplehealth.estoque.service;

import br.com.simplehealth.estoque.config.AppConfig;
import br.com.simplehealth.estoque.model.Item;
import br.com.simplehealth.estoque.model.dto.EntradaItensDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.hc.client5.http.classic.methods.HttpPost;
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

/**
 * Service para UC06 - Entrada de Itens (Nota Fiscal)
 * Endpoint: POST /controle/entrada
 */
public class EntradaItensService {
    
    private static final Logger logger = LoggerFactory.getLogger(EntradaItensService.class);
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    
    public EntradaItensService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.baseUrl = AppConfig.API_BASE_URL + AppConfig.ENDPOINT_CONTROLE_ENTRADA;
    }
    
    /**
     * UC06: Registrar entrada de itens através de nota fiscal
     * 
     * @param dto EntradaItensDTO com informações da NF e itens
     * @return Lista de itens inseridos no estoque
     * @throws IOException Se houver erro na comunicação com o backend
     */
    public List<Item> entradaDeItens(EntradaItensDTO dto) throws IOException {
        logger.debug("Registrando entrada de itens: NF {}", dto.getNfNumero());
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl);
            String json = objectMapper.writeValueAsString(dto);
            
            request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
            request.setHeader("Content-Type", "application/json");
            
            return httpClient.execute(request, response -> {
                int status = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                logger.debug("Status: {}, Response: {}", status, responseBody);
                
                if (status == 200) {
                    return objectMapper.readValue(responseBody, 
                        new TypeReference<List<Item>>() {});
                }
                logger.error("Erro ao registrar entrada. Status: {}, Response: {}", status, responseBody);
                return new ArrayList<>();
            });
        }
    }
}
