package br.com.simplehealth.agendamento.service;

import br.com.simplehealth.agendamento.config.AppConfig;
import br.com.simplehealth.agendamento.model.Exame;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
 * Serviço para gerenciar operações de Exames via API REST.
 * Baseado na API: /exames
 */
public class ExameService {

    private static final Logger logger = LoggerFactory.getLogger(ExameService.class);
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public ExameService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        this.baseUrl = AppConfig.AGENDAMENTOS_ENDPOINT + "/exames";
    }

    /**
     * Lista todos os exames
     * GET /exames
     */
    public List<Exame> listarTodos() throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Listando todos os exames");
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl);
            request.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", json);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(json, new TypeReference<List<Exame>>() {});
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * Busca um exame por ID
     * GET /exames/{id}
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
     * Cria um novo exame (agendar)
     * POST /exames
     * Body: AgendarExameDTO
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
                    throw new IOException("Erro ao criar exame. Código: " + response.getCode() + " - " + responseJson);
                }
            }
        }
    }

    /**
     * Atualiza um exame existente
     * PUT /exames/{id}
     * Body: AtualizarExameDTO
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
                    throw new IOException("Erro ao atualizar exame. Código: " + response.getCode() + " - " + responseJson);
                }
            }
        }
    }

    /**
     * Deleta um exame
     * DELETE /exames/{id}
     */
    public void deletar(String id) throws IOException {
        logger.info("Deletando exame ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(baseUrl + "/" + id);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                logger.debug("Response code: {}", response.getCode());

                if (response.getCode() != 200 && response.getCode() != 204) {
                    String responseJson = EntityUtils.toString(response.getEntity());
                    throw new IOException("Erro ao deletar exame. Código: " + response.getCode() + " - " + responseJson);
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                throw new IOException("Erro ao processar resposta: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Cancela um exame
     * POST /exames/cancelar
     * Body: CancelarAgendamentoDTO {id, motivo, usuarioLogin}
     */
    public Exame cancelar(String id, String motivo, String usuarioLogin) throws IOException {
        logger.info("Cancelando exame ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl + "/cancelar");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = String.format(
                "{\"id\":\"%s\",\"motivo\":\"%s\",\"usuarioLogin\":\"%s\"}", 
                id, motivo, usuarioLogin
            );
            request.setEntity(new StringEntity(json));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(responseJson, Exame.class);
                } else {
                    throw new IOException("Erro ao cancelar exame. Código: " + response.getCode() + " - " + responseJson);
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                throw new IOException("Erro ao processar resposta: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Inicia um exame
     * POST /exames/{id}/iniciar
     * Body: IniciarServicoDTO {id, usuarioLogin}
     */
    public Exame iniciar(String id, String usuarioLogin) throws IOException {
        logger.info("Iniciando exame ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl + "/" + id + "/iniciar");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = String.format("{\"id\":\"%s\",\"usuarioLogin\":\"%s\"}", id, usuarioLogin);
            request.setEntity(new StringEntity(json));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(responseJson, Exame.class);
                } else {
                    throw new IOException("Erro ao iniciar exame. Código: " + response.getCode() + " - " + responseJson);
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                throw new IOException("Erro ao processar resposta: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Finaliza um exame
     * POST /exames/{id}/finalizar
     * Body: FinalizarServicoDTO {id, usuarioLogin, observacoes}
     */
    public Exame finalizar(String id, String usuarioLogin, String observacoes) throws IOException {
        logger.info("Finalizando exame ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl + "/" + id + "/finalizar");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = String.format(
                "{\"id\":\"%s\",\"usuarioLogin\":\"%s\",\"observacoes\":\"%s\"}", 
                id, usuarioLogin, observacoes != null ? observacoes : ""
            );
            request.setEntity(new StringEntity(json));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(responseJson, Exame.class);
                } else {
                    throw new IOException("Erro ao finalizar exame. Código: " + response.getCode() + " - " + responseJson);
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                throw new IOException("Erro ao processar resposta: " + e.getMessage(), e);
            }
        }
    }
}
