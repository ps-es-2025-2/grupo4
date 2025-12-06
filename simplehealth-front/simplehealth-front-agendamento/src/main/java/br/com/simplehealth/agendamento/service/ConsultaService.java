package br.com.simplehealth.agendamento.service;

import br.com.simplehealth.agendamento.config.AppConfig;
import br.com.simplehealth.agendamento.model.Consulta;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.hc.client5.http.classic.methods.HttpPost;
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
     * NOTA: A API atual não possui endpoint GET para listar consultas.
     * Este método retorna lista vazia até que o backend implemente o endpoint.
     */
    public List<Consulta> listarTodos() throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Listando todas as consultas");
        logger.warn("API não possui endpoint GET /agendamentos - retornando lista vazia");
        return new ArrayList<>();
    }

    /**
     * Busca uma consulta por ID
     * NOTA: A API atual não possui endpoint GET /{id} para buscar consulta específica.
     * Este método retorna null até que o backend implemente o endpoint.
     */
    public Consulta buscarPorId(String id) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Buscando consulta por ID: {}", id);
        logger.warn("API não possui endpoint GET /agendamentos/{} - retornando null", id);
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
     * NOTA: A API atual não possui endpoint PUT para atualizar consultas.
     * Para modificar um agendamento, deve-se cancelar e criar um novo.
     */
    public Consulta atualizar(String id, Consulta consulta) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.warn("API não possui endpoint PUT /agendamentos/{} - operação não suportada", id);
        throw new UnsupportedOperationException("A API não suporta atualização de consultas. Cancele e crie um novo agendamento.");
    }

    /**
     * Deleta uma consulta
     * NOTA: A API atual não possui endpoint DELETE.
     * Use o método cancelar() em vez de deletar.
     */
    public void deletar(String id) throws IOException {
        logger.warn("API não possui endpoint DELETE /agendamentos/{} - use cancelar() ao invés", id);
        throw new UnsupportedOperationException("A API não suporta deleção de consultas. Use o método cancelar().");
    }

    /**
     * Cancela uma consulta conforme especificação da API
     * Endpoint: POST /agendamentos/cancelar
     * Body: CancelarAgendamentoDTO {id, motivo, usuarioLogin, dataHoraCancelamento}
     */
    public Consulta cancelar(String id, String motivo, String usuarioLogin) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Cancelando consulta ID: {}", id);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl + "/cancelar");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            // Criar JSON conforme CancelarAgendamentoDTO da API
            String json = String.format(
                "{\"id\":\"%s\",\"motivo\":\"%s\",\"usuarioLogin\":\"%s\",\"dataHoraCancelamento\":\"%s\"}", 
                id, 
                motivo, 
                usuarioLogin,
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
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
