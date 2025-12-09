package br.com.simplehealth.agendamento.service;

import br.com.simplehealth.agendamento.config.AppConfig;
import br.com.simplehealth.agendamento.model.Procedimento;
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
 * Serviço para gerenciar operações de Procedimentos via API REST.
 * Baseado na API: /procedimentos
 */
public class ProcedimentoService {

    private static final Logger logger = LoggerFactory.getLogger(ProcedimentoService.class);
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public ProcedimentoService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        this.baseUrl = AppConfig.AGENDAMENTOS_ENDPOINT + "/procedimentos";
    }

    /**
     * Lista todos os procedimentos
     * GET /procedimentos
     */
    public List<Procedimento> listarTodos() throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Listando todos os procedimentos");
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl);
            request.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", json);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(json, new TypeReference<List<Procedimento>>() {});
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * Busca um procedimento por ID
     * GET /procedimentos/{id}
     */
    public Procedimento buscarPorId(String id) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Buscando procedimento por ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl + "/" + id);
            request.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", json);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(json, Procedimento.class);
                }
            }
        }
        return null;
    }

    /**
     * Cria um novo procedimento (agendar)
     * POST /procedimentos
     * Body: AgendarProcedimentoDTO
     */
    public Procedimento criar(Procedimento procedimento) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Criando novo procedimento: {}", procedimento);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = objectMapper.writeValueAsString(procedimento);
            request.setEntity(new StringEntity(json));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200 || response.getCode() == 201) {
                    return objectMapper.readValue(responseJson, Procedimento.class);
                } else {
                    throw new IOException("Erro ao criar procedimento. Código: " + response.getCode() + " - " + responseJson);
                }
            }
        }
    }

    /**
     * Atualiza um procedimento existente
     * PUT /procedimentos/{id}
     * Body: AtualizarProcedimentoDTO
     */
    public Procedimento atualizar(String id, Procedimento procedimento) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Atualizando procedimento ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(baseUrl + "/" + id);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = objectMapper.writeValueAsString(procedimento);
            request.setEntity(new StringEntity(json));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(responseJson, Procedimento.class);
                } else {
                    throw new IOException("Erro ao atualizar procedimento. Código: " + response.getCode() + " - " + responseJson);
                }
            }
        }
    }

    /**
     * Deleta um procedimento
     * DELETE /procedimentos/{id}
     */
    public void deletar(String id) throws IOException {
        logger.info("Deletando procedimento ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(baseUrl + "/" + id);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                logger.debug("Response code: {}", response.getCode());

                if (response.getCode() != 200 && response.getCode() != 204) {
                    String responseJson = EntityUtils.toString(response.getEntity());
                    throw new IOException("Erro ao deletar procedimento. Código: " + response.getCode() + " - " + responseJson);
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                throw new IOException("Erro ao processar resposta: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Cancela um procedimento
     * POST /procedimentos/cancelar
     * Body: CancelarAgendamentoDTO {id, motivo, usuarioLogin}
     */
    public Procedimento cancelar(String id, String motivo, String usuarioLogin) throws IOException {
        logger.info("Cancelando procedimento ID: {}", id);

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
                    return objectMapper.readValue(responseJson, Procedimento.class);
                } else {
                    throw new IOException("Erro ao cancelar procedimento. Código: " + response.getCode() + " - " + responseJson);
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                throw new IOException("Erro ao processar resposta: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Inicia um procedimento
     * POST /procedimentos/{id}/iniciar
     * Body: IniciarServicoDTO {id, usuarioLogin}
     */
    public Procedimento iniciar(String id, String usuarioLogin) throws IOException {
        logger.info("Iniciando procedimento ID: {}", id);

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
                    return objectMapper.readValue(responseJson, Procedimento.class);
                } else {
                    throw new IOException("Erro ao iniciar procedimento. Código: " + response.getCode() + " - " + responseJson);
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                throw new IOException("Erro ao processar resposta: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Finaliza um procedimento
     * POST /procedimentos/{id}/finalizar
     * Body: FinalizarServicoDTO {id, usuarioLogin, observacoes}
     */
    public Procedimento finalizar(String id, String usuarioLogin, String observacoes) throws IOException {
        logger.info("Finalizando procedimento ID: {}", id);

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
                    return objectMapper.readValue(responseJson, Procedimento.class);
                } else {
                    throw new IOException("Erro ao finalizar procedimento. Código: " + response.getCode() + " - " + responseJson);
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                throw new IOException("Erro ao processar resposta: " + e.getMessage(), e);
            }
        }
    }
}
