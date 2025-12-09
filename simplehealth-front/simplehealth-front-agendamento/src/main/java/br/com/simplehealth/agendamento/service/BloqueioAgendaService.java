package br.com.simplehealth.agendamento.service;

import br.com.simplehealth.agendamento.config.AppConfig;
import br.com.simplehealth.agendamento.model.BloqueioAgenda;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
 * Serviço para gerenciar operações de Bloqueios de Agenda via API REST.
 * Baseado na API: /bloqueio-agenda
 */
public class BloqueioAgendaService {

    private static final Logger logger = LoggerFactory.getLogger(BloqueioAgendaService.class);
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public BloqueioAgendaService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.baseUrl = AppConfig.BLOQUEIO_AGENDA_ENDPOINT;
    }

    /**
     * Lista todos os bloqueios de agenda
     * GET /bloqueio-agenda
     */
    public List<BloqueioAgenda> listarTodos() throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Listando todos os bloqueios de agenda");
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl);
            request.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", json);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(json, new TypeReference<List<BloqueioAgenda>>() {});
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * Busca um bloqueio de agenda por ID
     * GET /bloqueio-agenda/{id}
     */
    public BloqueioAgenda buscarPorId(String id) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Buscando bloqueio de agenda por ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl + "/" + id);
            request.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", json);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(json, BloqueioAgenda.class);
                }
            }
        }
        return null;
    }

    /**
     * Busca bloqueios de agenda por CRM do médico
     * GET /bloqueio-agenda/medico/{medicoCrm}
     */
    public List<BloqueioAgenda> buscarPorMedico(String medicoCrm) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Buscando bloqueios de agenda do médico CRM: {}", medicoCrm);
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl + "/medico/" + medicoCrm);
            request.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", json);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(json, new TypeReference<List<BloqueioAgenda>>() {});
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * Cria um novo bloqueio de agenda
     * POST /bloqueio-agenda
     * Body: BloqueioAgendaDTO
     */
    public BloqueioAgenda criar(BloqueioAgenda bloqueio) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Criando novo bloqueio de agenda: {}", bloqueio);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = objectMapper.writeValueAsString(bloqueio);
            request.setEntity(new StringEntity(json));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200 || response.getCode() == 201) {
                    return objectMapper.readValue(responseJson, BloqueioAgenda.class);
                } else {
                    throw new IOException("Erro ao criar bloqueio. Código: " + response.getCode() + " - " + responseJson);
                }
            }
        }
    }

    /**
     * Atualiza um bloqueio de agenda existente
     * PUT /bloqueio-agenda/{id}
     * Body: AtualizarBloqueioAgendaDTO
     */
    public BloqueioAgenda atualizar(String id, BloqueioAgenda bloqueio) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Atualizando bloqueio de agenda ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(baseUrl + "/" + id);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = objectMapper.writeValueAsString(bloqueio);
            request.setEntity(new StringEntity(json));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(responseJson, BloqueioAgenda.class);
                } else {
                    throw new IOException("Erro ao atualizar bloqueio. Código: " + response.getCode() + " - " + responseJson);
                }
            }
        }
    }

    /**
     * Deleta um bloqueio de agenda
     * DELETE /bloqueio-agenda/{id}
     */
    public void deletar(String id) throws IOException {
        logger.info("Deletando bloqueio de agenda ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(baseUrl + "/" + id);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                logger.debug("Response code: {}", response.getCode());

                if (response.getCode() != 200 && response.getCode() != 204) {
                    String responseJson = EntityUtils.toString(response.getEntity());
                    throw new IOException("Erro ao deletar bloqueio. Código: " + response.getCode() + " - " + responseJson);
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                throw new IOException("Erro ao processar resposta: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Desativa um bloqueio de agenda
     * PATCH /bloqueio-agenda/{id}/desativar
     */
    public BloqueioAgenda desativar(String id) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Desativando bloqueio de agenda ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPatch request = new HttpPatch(baseUrl + "/" + id + "/desativar");
            request.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(responseJson, BloqueioAgenda.class);
                } else {
                    throw new IOException("Erro ao desativar bloqueio. Código: " + response.getCode() + " - " + responseJson);
                }
            }
        }
    }
}
