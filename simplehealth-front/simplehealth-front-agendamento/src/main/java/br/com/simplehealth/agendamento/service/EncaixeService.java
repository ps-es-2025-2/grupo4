package br.com.simplehealth.agendamento.service;

import br.com.simplehealth.agendamento.config.AppConfig;
import br.com.simplehealth.agendamento.model.Consulta;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Serviço para gerenciar operações de Encaixe via API REST.
 * Encaixe é um tipo especial de consulta agendada fora do horário normal.
 */
public class EncaixeService {

    private static final Logger logger = LoggerFactory.getLogger(EncaixeService.class);
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public EncaixeService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        this.baseUrl = AppConfig.ENCAIXE_ENDPOINT;
    }

    /**
     * Solicita um encaixe (consulta fora do horário normal)
     * Endpoint: POST /encaixe
     * Body: EncaixeDTO conforme especificação OpenAPI
     */
    public Consulta solicitarEncaixe(Consulta consulta) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Solicitando encaixe para paciente: {}", consulta.getPacienteCpf());

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(baseUrl);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            // Construir DTO de encaixe conforme especificação da API
            Map<String, Object> encaixeDTO = new HashMap<>();
            encaixeDTO.put("pacienteCpf", consulta.getPacienteCpf());
            encaixeDTO.put("medicoCrm", consulta.getMedicoCrm());
            encaixeDTO.put("dataHoraInicioPrevista", consulta.getDataHoraInicioPrevista());
            encaixeDTO.put("dataHoraFimPrevista", consulta.getDataHoraFimPrevista());
            encaixeDTO.put("motivoEncaixe", consulta.getMotivoEncaixe());
            encaixeDTO.put("observacoes", consulta.getObservacoes());
            encaixeDTO.put("usuarioCriadorLogin", consulta.getUsuarioCriadorLogin());
            // Enums devem ser enviados como strings (name())
            encaixeDTO.put("tipoConsulta", consulta.getTipoConsulta() != null ? consulta.getTipoConsulta().name() : null);
            encaixeDTO.put("especialidade", consulta.getEspecialidade());
            encaixeDTO.put("convenioNome", consulta.getConvenioNome());
            encaixeDTO.put("modalidade", consulta.getModalidade() != null ? consulta.getModalidade().name() : null);

            String json = objectMapper.writeValueAsString(encaixeDTO);
            request.setEntity(new StringEntity(json));
            logger.debug("Request body: {}", json);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Response: {}", responseJson);

                if (response.getCode() == 200 || response.getCode() == 201) {
                    return objectMapper.readValue(responseJson, Consulta.class);
                } else {
                    throw new IOException("Erro ao solicitar encaixe. Código: " + response.getCode() + 
                                        ", Response: " + responseJson);
                }
            }
        }
    }
}
