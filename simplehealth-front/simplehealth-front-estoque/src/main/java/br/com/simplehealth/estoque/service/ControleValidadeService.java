package br.com.simplehealth.estoque.service;

import br.com.simplehealth.estoque.config.AppConfig;
import br.com.simplehealth.estoque.model.Item;
import br.com.simplehealth.estoque.model.dto.ControleValidadeDTO;
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
 * Service para UC10 - Controle de Validade de Itens
 * Endpoint: POST /controle/validade
 */
public class ControleValidadeService {
    
    private static final Logger logger = LoggerFactory.getLogger(ControleValidadeService.class);
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    
    public ControleValidadeService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.baseUrl = AppConfig.API_BASE_URL + AppConfig.ENDPOINT_CONTROLE_VALIDADE;
    }
    
    /**
     * UC10: Verificar validade de itens no estoque
     * 
     * @param dto ControleValidadeDTO com parâmetros de busca
     * @return Lista de itens que atendem aos critérios de validade
     * @throws IOException Se houver erro na comunicação com o backend
     */
    public List<Item> verificarValidade(ControleValidadeDTO dto) throws IOException {
        logger.debug("Verificando validade com parâmetros: {}", dto);
        
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
                logger.error("Erro ao verificar validade. Status: {}, Response: {}", status, responseBody);
                return new ArrayList<>();
            });
        }
    }
}
