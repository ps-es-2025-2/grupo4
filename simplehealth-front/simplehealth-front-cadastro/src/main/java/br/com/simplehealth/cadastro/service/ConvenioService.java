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
                int statusCode = response.getCode();
                String jsonResponse = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API: {} - {}", statusCode, jsonResponse);
                
                if (statusCode == 201 || statusCode == 200) {
                    return objectMapper.readValue(jsonResponse, Convenio.class);
                } else {
                    throw new IOException("Erro ao criar convênio. Status: " + statusCode + " - " + jsonResponse);
                }
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
                int statusCode = response.getCode();
                String jsonResponse = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API: {} - {}", statusCode, jsonResponse);
                
                if (statusCode == 200) {
                    return objectMapper.readValue(jsonResponse, Convenio.class);
                } else {
                    throw new IOException("Erro ao atualizar convênio. Status: " + statusCode + " - " + jsonResponse);
                }
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
                int statusCode = response.getCode();
                logger.debug("Convênio deletado. Status: {}", statusCode);
                
                if (statusCode != 204 && statusCode != 200) {
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    throw new IOException("Erro ao deletar convênio. Status: " + statusCode + " - " + jsonResponse);
                }
            }
        }
    }
}
