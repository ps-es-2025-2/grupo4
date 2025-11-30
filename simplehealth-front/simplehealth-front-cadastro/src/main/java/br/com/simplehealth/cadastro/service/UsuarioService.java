package br.com.simplehealth.cadastro.service;

import br.com.simplehealth.cadastro.model.Usuario;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço para comunicação com a API de Usuários.
 */
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private static final String BASE_URL = "http://localhost:8081/usuarios";
    private final ObjectMapper objectMapper;

    public UsuarioService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Busca todos os usuários.
     */
    public List<Usuario> buscarTodos() throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Buscando todos os usuários");
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL);
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API: {}", json);
                
                return objectMapper.readValue(json, new TypeReference<List<Usuario>>() {});
            }
        } catch (IOException e) {
            logger.error("Erro ao buscar usuários", e);
            throw e;
        }
    }

    /**
     * Busca usuário por ID.
     */
    public Usuario buscarPorId(Long id) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Buscando usuário com ID: {}", id);
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/" + id);
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                logger.debug("Resposta da API: {}", json);
                
                return objectMapper.readValue(json, Usuario.class);
            }
        } catch (IOException e) {
            logger.error("Erro ao buscar usuário por ID", e);
            throw e;
        }
    }

    /**
     * Cria novo usuário.
     */
    public Usuario criar(Usuario usuario) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Criando novo usuário: {}", usuario.getLogin());
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(BASE_URL);
            String json = objectMapper.writeValueAsString(usuario);
            request.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Usuário criado: {}", responseJson);
                
                return objectMapper.readValue(responseJson, Usuario.class);
            }
        } catch (IOException e) {
            logger.error("Erro ao criar usuário", e);
            throw e;
        }
    }

    /**
     * Atualiza usuário existente.
     */
    public Usuario atualizar(Long id, Usuario usuario) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Atualizando usuário ID: {}", id);
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(BASE_URL + "/" + id);
            String json = objectMapper.writeValueAsString(usuario);
            request.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                logger.debug("Usuário atualizado: {}", responseJson);
                
                return objectMapper.readValue(responseJson, Usuario.class);
            }
        } catch (IOException e) {
            logger.error("Erro ao atualizar usuário", e);
            throw e;
        }
    }

    /**
     * Deleta usuário.
     */
    public void deletar(Long id) throws IOException, org.apache.hc.core5.http.ParseException {
        logger.info("Deletando usuário ID: {}", id);
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(BASE_URL + "/" + id);
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                logger.debug("Usuário deletado com sucesso");
            }
        } catch (IOException e) {
            logger.error("Erro ao deletar usuário", e);
            throw e;
        }
    }
}
