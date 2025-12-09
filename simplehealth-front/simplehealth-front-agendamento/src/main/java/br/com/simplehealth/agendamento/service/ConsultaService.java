package br.com.simplehealth.agendamento.service;

import br.com.simplehealth.agendamento.config.AppConfig;
import br.com.simplehealth.agendamento.model.Consulta;
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
 * Serviço para gerenciar operações de Consultas via API REST.
 * Baseado na API: /consultas
 */
public class ConsultaService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaService.class);
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public ConsultaService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        this.baseUrl = AppConfig.AGENDAMENTOS_ENDPOINT + "/consultas";
    }

    /**
     * Lista todas as consultas
     * GET /consultas
     */
    public List<Consulta> listarTodos() throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Listando todas as consultas");
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl);
            request.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", json);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(json, new TypeReference<List<Consulta>>() {});
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * Busca uma consulta por ID
     * GET /consultas/{id}
     */
    public Consulta buscarPorId(String id) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Buscando consulta por ID: {}", id);
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl + "/" + id);
            request.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", json);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(json, Consulta.class);
                }
            }
        }
        return null;
    }

    /**
     * Busca consultas por CPF do paciente
     * GET /consultas/paciente/{cpf}
     */
    public List<Consulta> buscarPorPaciente(String cpf) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Buscando consultas do paciente CPF: {}", cpf);
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl + "/paciente/" + cpf);
            request.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", json);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(json, new TypeReference<List<Consulta>>() {});
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * Cria uma nova consulta (agendar)
     * POST /consultas
     * Body: AgendarConsultaDTO
     */
    public Consulta criar(Consulta consulta) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Criando nova consulta: {}", consulta);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = objectMapper.writeValueAsString(consulta);
            request.setEntity(new StringEntity(json, java.nio.charset.StandardCharsets.UTF_8));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200 || response.getCode() == 201) {
                    return objectMapper.readValue(responseJson, Consulta.class);
                } else {
                    throw new IOException("Erro ao criar consulta. Código: " + response.getCode() + " - " + responseJson);
                }
            }
        }
    }

    /**
     * Atualiza uma consulta existente
     * PUT /consultas/{id}
     * Body: AtualizarAgendamentoDTO
     */
    public Consulta atualizar(String id, Consulta consulta) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Atualizando consulta ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(baseUrl + "/" + id);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = objectMapper.writeValueAsString(consulta);
            request.setEntity(new StringEntity(json, java.nio.charset.StandardCharsets.UTF_8));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(responseJson, Consulta.class);
                } else {
                    throw new IOException("Erro ao atualizar consulta. Código: " + response.getCode() + " - " + responseJson);
                }
            }
        }
    }

    /**
     * Deleta uma consulta
     * DELETE /consultas/{id}
     */
    public void deletar(String id) throws IOException {
        logger.info("Deletando consulta ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(baseUrl + "/" + id);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                logger.debug("Response code: {}", response.getCode());

                if (response.getCode() != 200 && response.getCode() != 204) {
                    String responseJson = EntityUtils.toString(response.getEntity());
                    throw new IOException("Erro ao deletar consulta. Código: " + response.getCode() + " - " + responseJson);
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                throw new IOException("Erro ao processar resposta: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Cancela uma consulta
     * POST /consultas/cancelar
     * Body: CancelarAgendamentoDTO {id, motivo, usuarioLogin}
     */
    public Consulta cancelar(String id, String motivo, String usuarioLogin) throws IOException {
        logger.info("Cancelando consulta ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl + "/cancelar");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = String.format(
                "{\"id\":\"%s\",\"motivo\":\"%s\",\"usuarioLogin\":\"%s\"}", 
                id, motivo, usuarioLogin
            );
            request.setEntity(new StringEntity(json, java.nio.charset.StandardCharsets.UTF_8));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(responseJson, Consulta.class);
                } else {
                    throw new IOException("Erro ao cancelar consulta. Código: " + response.getCode() + " - " + responseJson);
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                throw new IOException("Erro ao processar resposta: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Inicia uma consulta
     * POST /consultas/{id}/iniciar
     * Body: IniciarServicoDTO {id, usuarioLogin}
     */
    public Consulta iniciar(String id, String usuarioLogin) throws IOException {
        logger.info("Iniciando consulta ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl + "/" + id + "/iniciar");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = String.format("{\"id\":\"%s\",\"usuarioLogin\":\"%s\"}", id, usuarioLogin);
            request.setEntity(new StringEntity(json, java.nio.charset.StandardCharsets.UTF_8));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(responseJson, Consulta.class);
                } else {
                    throw new IOException("Erro ao iniciar consulta. Código: " + response.getCode() + " - " + responseJson);
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                throw new IOException("Erro ao processar resposta: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Finaliza uma consulta
     * POST /consultas/{id}/finalizar
     * Body: FinalizarServicoDTO {id, usuarioLogin, observacoes}
     */
    public Consulta finalizar(String id, String usuarioLogin, String observacoes) throws IOException {
        logger.info("Finalizando consulta ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl + "/" + id + "/finalizar");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = String.format(
                "{\"id\":\"%s\",\"usuarioLogin\":\"%s\",\"observacoes\":\"%s\"}", 
                id, usuarioLogin, observacoes != null ? observacoes : ""
            );
            request.setEntity(new StringEntity(json, java.nio.charset.StandardCharsets.UTF_8));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(responseJson, Consulta.class);
                } else {
                    throw new IOException("Erro ao finalizar consulta. Código: " + response.getCode() + " - " + responseJson);
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                throw new IOException("Erro ao processar resposta: " + e.getMessage(), e);
            }
        }
    }
}
