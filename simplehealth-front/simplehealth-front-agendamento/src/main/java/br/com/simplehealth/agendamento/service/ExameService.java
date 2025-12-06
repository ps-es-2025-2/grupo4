package br.com.simplehealth.agendamento.service;

import br.com.simplehealth.agendamento.config.AppConfig;
import br.com.simplehealth.agendamento.model.Exame;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
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
 * Serviço para gerenciar operações de Exames via API REST.
 * 
 * IMPORTANTE: Este serviço está preparado para integração com a API de agendamentos,
 * porém a API backend atual (open_api_agendamento.json) NÃO possui endpoints específicos
 * para Exames. A API atual suporta apenas:
 * - POST /agendamentos (Consultas)
 * - POST /agendamentos/cancelar
 * - POST /encaixe
 * - POST /bloqueio-agenda
 * 
 * Para que este serviço funcione completamente, o backend precisa implementar:
 * - GET /agendamentos/exames (listar exames)
 * - GET /agendamentos/exames/{id} (buscar exame por ID)
 * - POST /agendamentos/exames (criar exame)
 * - PUT /agendamentos/exames/{id} (atualizar exame)
 * - DELETE /agendamentos/exames/{id} (deletar exame)
 * 
 * Até lá, os métodos retornarão listas vazias ou lançarão exceções apropriadas.
 */
public class ExameService {

    private static final Logger logger = LoggerFactory.getLogger(ExameService.class);
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public ExameService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.baseUrl = AppConfig.AGENDAMENTOS_ENDPOINT;
    }

    /**
     * Lista todos os exames
     */
    public List<Exame> listarTodos() throws IOException {
        logger.info("Listando todos os exames");
        // Por enquanto retorna lista vazia - depende da API ter endpoint específico
        return new ArrayList<>();
    }

    /**
     * Busca um exame por ID
     */
    public Exame buscarPorId(String id) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Buscando exame por ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl + "/" + id);
            request.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", json);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(json, Exame.class);
                }
            }
        }

        return null;
    }

    /**
     * Cria um novo exame
     */
    public Exame criar(Exame exame) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Criando novo exame: {}", exame);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = objectMapper.writeValueAsString(exame);
            request.setEntity(new StringEntity(json));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200 || response.getCode() == 201) {
                    return objectMapper.readValue(responseJson, Exame.class);
                } else {
                    throw new IOException("Erro ao criar exame. Código: " + response.getCode());
                }
            }
        }
    }

    /**
     * Atualiza um exame existente
     */
    public Exame atualizar(String id, Exame exame) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Atualizando exame ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(baseUrl + "/" + id);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = objectMapper.writeValueAsString(exame);
            request.setEntity(new StringEntity(json));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(responseJson, Exame.class);
                } else {
                    throw new IOException("Erro ao atualizar exame. Código: " + response.getCode());
                }
            }
        }
    }

    /**
     * Deleta um exame
     */
    public void deletar(String id) throws IOException {
        logger.info("Deletando exame ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(baseUrl + "/" + id);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                logger.debug("Response code: {}", response.getCode());

                if (response.getCode() != 200 && response.getCode() != 204) {
                    throw new IOException("Erro ao deletar exame. Código: " + response.getCode());
                }
            }
        }
    }

    /**
     * Cancela um exame
     */
    public Exame cancelar(String id, String motivo, String usuarioCancelador) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Cancelando exame ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl + "/cancelar");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = String.format("{\"id\":\"%s\",\"motivo\":\"%s\",\"usuarioCancelador\":\"%s\"}", 
                id, motivo, usuarioCancelador);
            request.setEntity(new StringEntity(json));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(responseJson, Exame.class);
                } else {
                    throw new IOException("Erro ao cancelar exame. Código: " + response.getCode());
                }
            }
        }
    }
}
