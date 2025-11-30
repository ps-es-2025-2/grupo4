package br.com.simplehealth.cadastro.service;

import br.com.simplehealth.cadastro.config.AppConfig;
import br.com.simplehealth.cadastro.model.Convenio;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço para operações relacionadas a Convênios.
 */
public class ConvenioService {

    private static final Logger logger = LoggerFactory.getLogger(ConvenioService.class);
    private final ObjectMapper objectMapper = AppConfig.getObjectMapper();

    /**
     * Lista todos os convênios.
     */
    public List<Convenio> listarTodos() throws IOException, org.apache.hc.core5.http.ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(AppConfig.CONVENIOS_ENDPOINT);
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, new TypeReference<List<Convenio>>() {});
            }
        } catch (Exception e) {
            logger.error("Erro ao listar convênios", e);
            return new ArrayList<>();
        }
    }

    /**
     * Busca um convênio por ID.
     */
    public Convenio buscarPorId(Long id) throws IOException, org.apache.hc.core5.http.ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(AppConfig.CONVENIOS_ENDPOINT + "/" + id);
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Convenio.class);
            }
        }
    }

    /**
     * Cria um novo convênio.
     */
    public Convenio criar(Convenio convenio) throws IOException, org.apache.hc.core5.http.ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(AppConfig.CONVENIOS_ENDPOINT);
            request.setHeader("Content-Type", "application/json");
            
            String json = objectMapper.writeValueAsString(convenio);
            request.setEntity(new StringEntity(json));
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Convenio.class);
            }
        }
    }

    /**
     * Atualiza um convênio existente.
     */
    public Convenio atualizar(Long id, Convenio convenio) throws IOException, org.apache.hc.core5.http.ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(AppConfig.CONVENIOS_ENDPOINT + "/" + id);
            request.setHeader("Content-Type", "application/json");
            
            String json = objectMapper.writeValueAsString(convenio);
            request.setEntity(new StringEntity(json));
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Convenio.class);
            }
        }
    }

    /**
     * Deleta um convênio.
     */
    public void deletar(Long id) throws IOException, org.apache.hc.core5.http.ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(AppConfig.CONVENIOS_ENDPOINT + "/" + id);
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                logger.debug("Convênio deletado com sucesso. Status: {}", response.getCode());
            }
        }
    }
}
