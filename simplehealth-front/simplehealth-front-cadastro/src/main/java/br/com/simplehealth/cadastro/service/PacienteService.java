package br.com.simplehealth.cadastro.service;

import br.com.simplehealth.cadastro.config.AppConfig;
import br.com.simplehealth.cadastro.model.HistoricoPaciente;
import br.com.simplehealth.cadastro.model.Paciente;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * Serviço para operações relacionadas a Pacientes.
 */
public class PacienteService {

    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);
    private final ObjectMapper objectMapper = AppConfig.getObjectMapper();

    /**
     * Lista todos os pacientes.
     */
    public List<Paciente> listarTodos() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(AppConfig.PACIENTES_ENDPOINT);
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, new TypeReference<List<Paciente>>() {});
            }
        } catch (Exception e) {
            logger.error("Erro ao listar pacientes", e);
            return new ArrayList<>();
        }
    }

    /**
     * Busca um paciente por ID.
     */
    public Paciente buscarPorId(Long id) throws IOException, org.apache.hc.core5.http.ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(AppConfig.PACIENTES_ENDPOINT + "/" + id);
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Paciente.class);
            }
        }
    }

    /**
     * Cria um novo paciente.
     */
    public Paciente criar(Paciente paciente) throws IOException, org.apache.hc.core5.http.ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(AppConfig.PACIENTES_ENDPOINT);
            request.setHeader("Content-Type", "application/json");
            
            String json = objectMapper.writeValueAsString(paciente);
            request.setEntity(new StringEntity(json));
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getCode();
                String jsonResponse = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API: {} - {}", statusCode, jsonResponse);
                
                if (statusCode == 201 || statusCode == 200) {
                    return objectMapper.readValue(jsonResponse, Paciente.class);
                } else {
                    throw new IOException("Erro ao criar paciente. Status: " + statusCode + " - " + jsonResponse);
                }
            }
        }
    }

    /**
     * Atualiza um paciente existente.
     */
    public Paciente atualizar(Long id, Paciente paciente) throws IOException, org.apache.hc.core5.http.ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(AppConfig.PACIENTES_ENDPOINT + "/" + id);
            request.setHeader("Content-Type", "application/json");
            
            String json = objectMapper.writeValueAsString(paciente);
            request.setEntity(new StringEntity(json));
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Paciente.class);
            }
        }
    }

    /**
     * Deleta um paciente.
     */
    public void deletar(Long id) throws IOException, org.apache.hc.core5.http.ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(AppConfig.PACIENTES_ENDPOINT + "/" + id);
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getCode();
                logger.debug("Paciente deletado. Status: {}", statusCode);
                
                if (statusCode != 204 && statusCode != 200) {
                    throw new IOException("Erro ao deletar paciente. Status: " + statusCode);
                }
            }
        }
    }

    /**
     * Consulta o histórico completo de um paciente pelo CPF.
     * Retorna dados cadastrais, agendamentos, procedimentos, itens baixados e pagamentos.
     */
    public HistoricoPaciente consultarHistorico(String cpf) throws IOException, org.apache.hc.core5.http.ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(AppConfig.HISTORICO_PACIENTE_ENDPOINT + "/" + cpf);
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API (Histórico): {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, HistoricoPaciente.class);
            }
        }
    }
}
