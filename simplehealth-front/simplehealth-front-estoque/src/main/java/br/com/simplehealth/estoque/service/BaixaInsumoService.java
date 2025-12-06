package br.com.simplehealth.estoque.service;

import br.com.simplehealth.estoque.config.AppConfig;
import br.com.simplehealth.estoque.model.dto.BaixaInsumoDTO;
import br.com.simplehealth.estoque.model.dto.BaixaInsumoResponse;
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

/**
 * Service para UC05 - Dar Baixa em Insumos
 * Endpoint: POST /controle/baixa
 */
public class BaixaInsumoService {
    
    private static final Logger logger = LoggerFactory.getLogger(BaixaInsumoService.class);
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    
    public BaixaInsumoService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.baseUrl = AppConfig.API_BASE_URL + AppConfig.ENDPOINT_CONTROLE_BAIXA;
    }
    
    /**
     * UC05: Dar baixa em insumo do estoque
     * 
     * @param dto BaixaInsumoDTO com informações da baixa
     * @return BaixaInsumoResponse com resultado da operação
     * @throws IOException Se houver erro na comunicação com o backend
     */
    public BaixaInsumoResponse darBaixa(BaixaInsumoDTO dto) throws IOException {
        logger.debug("Dando baixa em insumo: {}", dto);
        
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
                    return objectMapper.readValue(responseBody, BaixaInsumoResponse.class);
                }
                logger.error("Erro ao dar baixa. Status: {}, Response: {}", status, responseBody);
                return null;
            });
        }
    }
}
