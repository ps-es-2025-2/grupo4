package br.com.simplehealth.cadastro.service;

import br.com.simplehealth.cadastro.config.AppConfig;
import br.com.simplehealth.cadastro.model.Medico;
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
 * Serviço para operações relacionadas a Médicos.
 */
public class MedicoService {

    private static final Logger logger = LoggerFactory.getLogger(MedicoService.class);
    private final ObjectMapper objectMapper = AppConfig.getObjectMapper();

    /**
     * Lista todos os médicos.
     */
    public List<Medico> listarTodos() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(AppConfig.MEDICOS_ENDPOINT);
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, new TypeReference<List<Medico>>() {});
            }
        } catch (Exception e) {
            logger.error("Erro ao listar médicos", e);
            return new ArrayList<>();
        }
    }

    /**
     * Busca um médico por ID.
     */
    public Medico buscarPorId(Long id) throws IOException, org.apache.hc.core5.http.ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(AppConfig.MEDICOS_ENDPOINT + "/" + id);
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Medico.class);
            }
        }
    }

    /**
     * Cria um novo médico.
     */
    public Medico criar(Medico medico) throws IOException, org.apache.hc.core5.http.ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(AppConfig.MEDICOS_ENDPOINT);
            request.setHeader("Content-Type", "application/json");
            
            String json = objectMapper.writeValueAsString(medico);
            request.setEntity(new StringEntity(json, java.nio.charset.StandardCharsets.UTF_8));
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getCode();
                String jsonResponse = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API: {} - {}", statusCode, jsonResponse);
                
                if (statusCode == 201 || statusCode == 200) {
                    return objectMapper.readValue(jsonResponse, Medico.class);
                } else {
                    throw new IOException("Erro ao criar médico. Status: " + statusCode + " - " + jsonResponse);
                }
            }
        }
    }

    /**
     * Atualiza um médico existente.
     */
    public Medico atualizar(Long id, Medico medico) throws IOException, org.apache.hc.core5.http.ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(AppConfig.MEDICOS_ENDPOINT + "/" + id);
            request.setHeader("Content-Type", "application/json");
            
            String json = objectMapper.writeValueAsString(medico);
            request.setEntity(new StringEntity(json, java.nio.charset.StandardCharsets.UTF_8));
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getCode();
                String jsonResponse = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API: {} - {}", statusCode, jsonResponse);
                
                if (statusCode == 200) {
                    return objectMapper.readValue(jsonResponse, Medico.class);
                } else {
                    throw new IOException("Erro ao atualizar médico. Status: " + statusCode + " - " + jsonResponse);
                }
            }
        }
    }

    /**
     * Deleta um médico.
     */
    public void deletar(Long id) throws IOException, org.apache.hc.core5.http.ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(AppConfig.MEDICOS_ENDPOINT + "/" + id);
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getCode();
                logger.debug("Médico deletado. Status: {}", statusCode);
                
                if (statusCode != 204 && statusCode != 200) {
                    try {
                        String jsonResponse = EntityUtils.toString(response.getEntity());
                        throw new IOException("Erro ao deletar médico. Status: " + statusCode + " - " + jsonResponse);
                    } catch (org.apache.hc.core5.http.ParseException e) {
                        throw new IOException("Erro ao deletar médico. Status: " + statusCode, e);
                    }
                }
            }
        }
    }
}
