package br.com.simplehealth.armazenamento.service;

import br.com.simplehealth.armazenamento.model.Item;
import br.com.simplehealth.armazenamento.model.Medicamento;
import br.com.simplehealth.armazenamento.model.Hospitalar;
import br.com.simplehealth.armazenamento.model.Alimento;
import br.com.simplehealth.armazenamento.config.AppConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Serviço para consumir APIs relacionadas a itens.
 * 
 * @version 1.0
 */
public class ItemService {
    
    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
    private static final String BASE_URL = AppConfig.ITENS_API_URL;
    
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ItemService() {
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Busca todos os itens.
     */
    public List<Item> buscarTodos() throws IOException {
        HttpGet request = new HttpGet(BASE_URL);
        request.setHeader("Content-Type", "application/json");
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, new TypeReference<List<Item>>() {});
            });
        }
    }

    /**
     * Busca um item por ID.
     */
    public Item buscarPorId(Long id) throws IOException {
        HttpGet request = new HttpGet(BASE_URL + "/" + id);
        request.setHeader("Content-Type", "application/json");
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                if (response.getCode() == 404) {
                    return null;
                }
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Item.class);
            });
        }
    }

    /**
     * Cria um novo item.
     */
    public Item criar(Item item) throws IOException {
        HttpPost request = new HttpPost(BASE_URL);
        request.setHeader("Content-Type", "application/json");
        
        String json = objectMapper.writeValueAsString(item);
        request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Item.class);
            });
        }
    }

    /**
     * Atualiza um item existente.
     */
    public Item atualizar(Long id, Item item) throws IOException {
        HttpPut request = new HttpPut(BASE_URL + "/" + id);
        request.setHeader("Content-Type", "application/json");
        
        String json = objectMapper.writeValueAsString(item);
        request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Item.class);
            });
        }
    }

    /**
     * Deleta um item.
     */
    public boolean deletar(Long id) throws IOException {
        HttpDelete request = new HttpDelete(BASE_URL + "/" + id);
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                logger.info("Status da deleção: {}", response.getCode());
                return response.getCode() == 200 || response.getCode() == 204;
            });
        }
    }

    /**
     * Busca itens por tipo.
     */
    public List<Item> buscarPorTipo(String tipo) throws IOException {
        HttpGet request = new HttpGet(BASE_URL + "/tipo/" + tipo);
        request.setHeader("Content-Type", "application/json");
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, new TypeReference<List<Item>>() {});
            });
        }
    }

    /**
     * Cria um medicamento.
     */
    public Medicamento criarMedicamento(Medicamento medicamento) throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "/medicamento");
        request.setHeader("Content-Type", "application/json");
        
        String json = objectMapper.writeValueAsString(medicamento);
        request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Medicamento.class);
            });
        }
    }

    /**
     * Cria um item hospitalar.
     */
    public Hospitalar criarHospitalar(Hospitalar hospitalar) throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "/hospitalar");
        request.setHeader("Content-Type", "application/json");
        
        String json = objectMapper.writeValueAsString(hospitalar);
        request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Hospitalar.class);
            });
        }
    }

    /**
     * Cria um alimento.
     */
    public Alimento criarAlimento(Alimento alimento) throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "/alimento");
        request.setHeader("Content-Type", "application/json");
        
        String json = objectMapper.writeValueAsString(alimento);
        request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(request, response -> {
                String jsonResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("Resposta da API: {}", jsonResponse);
                
                return objectMapper.readValue(jsonResponse, Alimento.class);
            });
        }
    }

    public void close() throws IOException {
        if (httpClient != null) {
            httpClient.close();
        }
    }
}