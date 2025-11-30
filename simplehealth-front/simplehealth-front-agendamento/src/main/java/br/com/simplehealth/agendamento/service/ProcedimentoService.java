package br.com.simplehealth.agendamento.service;

import br.com.simplehealth.agendamento.config.AppConfig;
import br.com.simplehealth.agendamento.model.Procedimento;
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
 * Serviço para gerenciar operações de Procedimentos via API REST.
 */
public class ProcedimentoService {

    private static final Logger logger = LoggerFactory.getLogger(ProcedimentoService.class);
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public ProcedimentoService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.baseUrl = AppConfig.AGENDAMENTOS_ENDPOINT;
    }

    /**
     * Lista todos os procedimentos
     */
    public List<Procedimento> listarTodos() throws IOException {
        logger.info("Listando todos os procedimentos");
        // Por enquanto retorna lista vazia - depende da API ter endpoint específico
        return new ArrayList<>();
    }

    /**
     * Busca um procedimento por ID
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
     * Cria um novo procedimento
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
                    throw new IOException("Erro ao criar procedimento. Código: " + response.getCode());
                }
            }
        }
    }

    /**
     * Atualiza um procedimento existente
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
                    throw new IOException("Erro ao atualizar procedimento. Código: " + response.getCode());
                }
            }
        }
    }

    /**
     * Deleta um procedimento
     */
    public void deletar(String id) throws IOException {
        logger.info("Deletando procedimento ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(baseUrl + "/" + id);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                logger.debug("Response code: {}", response.getCode());

                if (response.getCode() != 200 && response.getCode() != 204) {
                    throw new IOException("Erro ao deletar procedimento. Código: " + response.getCode());
                }
            }
        }
    }

    /**
     * Cancela um procedimento
     */
    public Procedimento cancelar(String id, String motivo, String usuarioCancelador) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Cancelando procedimento ID: {}", id);

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
                    return objectMapper.readValue(responseJson, Procedimento.class);
                } else {
                    throw new IOException("Erro ao cancelar procedimento. Código: " + response.getCode());
                }
            }
        }
    }
}
