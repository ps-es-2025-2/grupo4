package br.com.simplehealth.agendamento.service;

import br.com.simplehealth.agendamento.config.AppConfig;
import br.com.simplehealth.agendamento.model.Consulta;
import com.fasterxml.jackson.core.type.TypeReference;
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
 * Serviço para gerenciar operações de Consultas via API REST.
 */
public class ConsultaService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaService.class);
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public ConsultaService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.baseUrl = AppConfig.AGENDAMENTOS_ENDPOINT;
    }

    /**
     * Lista todas as consultas
     */
    public List<Consulta> listarTodos() throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Listando todas as consultas");
        List<Consulta> consultas = new ArrayList<>();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(baseUrl);
            request.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", json);

                if (response.getCode() == 200) {
                    // Filtra apenas consultas da lista de agendamentos
                    List<Object> agendamentos = objectMapper.readValue(json, new TypeReference<List<Object>>() {});
                    // TODO: Implementar filtragem por tipo quando API retornar tipo
                    // Por enquanto retorna todos como consulta
                }
            }
        }

        return consultas;
    }

    /**
     * Busca uma consulta por ID
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
     * Cria uma nova consulta
     */
    public Consulta criar(Consulta consulta) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Criando nova consulta: {}", consulta);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = objectMapper.writeValueAsString(consulta);
            request.setEntity(new StringEntity(json));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200 || response.getCode() == 201) {
                    return objectMapper.readValue(responseJson, Consulta.class);
                } else {
                    throw new IOException("Erro ao criar consulta. Código: " + response.getCode());
                }
            }
        }
    }

    /**
     * Atualiza uma consulta existente
     */
    public Consulta atualizar(String id, Consulta consulta) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Atualizando consulta ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(baseUrl + "/" + id);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            String json = objectMapper.writeValueAsString(consulta);
            request.setEntity(new StringEntity(json));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200) {
                    return objectMapper.readValue(responseJson, Consulta.class);
                } else {
                    throw new IOException("Erro ao atualizar consulta. Código: " + response.getCode());
                }
            }
        }
    }

    /**
     * Deleta uma consulta
     */
    public void deletar(String id) throws IOException {
        logger.info("Deletando consulta ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(baseUrl + "/" + id);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                logger.debug("Response code: {}", response.getCode());

                if (response.getCode() != 200 && response.getCode() != 204) {
                    throw new IOException("Erro ao deletar consulta. Código: " + response.getCode());
                }
            }
        }
    }

    /**
     * Cancela uma consulta
     */
    public Consulta cancelar(String id, String motivo, String usuarioCancelador) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Cancelando consulta ID: {}", id);

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
                    return objectMapper.readValue(responseJson, Consulta.class);
                } else {
                    throw new IOException("Erro ao cancelar consulta. Código: " + response.getCode());
                }
            }
        }
    }
}
