package br.com.simplehealth.agendamento.service;

import br.com.simplehealth.agendamento.config.AppConfig;
import br.com.simplehealth.agendamento.model.BloqueioAgenda;
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
 * Serviço para gerenciar operações de Bloqueios de Agenda via API REST.
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
     */
    public List<BloqueioAgenda> listarTodos() throws IOException {
        logger.info("Listando todos os bloqueios de agenda");
        // Por enquanto retorna lista vazia - depende da API ter endpoint de listagem
        return new ArrayList<>();
    }

    /**
     * Cria um novo bloqueio de agenda
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
                    throw new IOException("Erro ao criar bloqueio. Código: " + response.getCode());
                }
            }
        }
    }
}
