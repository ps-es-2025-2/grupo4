package com.simplehealth.estoque.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ImmudbClient {

  private final RestTemplate restTemplate;
  private final ObjectMapper mapper;

  @Value("${immudb.url}")
  private String baseUrl;

  @Value("${immudb.user}")
  private String user;

  @Value("${immudb.pass}")
  private String pass;

  private String token;

  @PostConstruct
  public void login() {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      Map<String, String> body = Map.of(
          "user", Base64.getEncoder().encodeToString(user.getBytes(StandardCharsets.UTF_8)),
          "password", Base64.getEncoder().encodeToString(pass.getBytes(StandardCharsets.UTF_8))
      );

      HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

      var res = restTemplate.postForEntity(baseUrl + "/login", request, Map.class);

      if (res.getStatusCode().is2xxSuccessful() && res.getBody() != null) {
        token = (String) res.getBody().get("token");
        System.out.println("Login realizado com sucesso. Token: " + token);
      } else {
        throw new RuntimeException("Erro no login Immugw: " + res.getStatusCode());
      }
    } catch (Exception e) {
      throw new RuntimeException("Falha ao logar no Immugw", e);
    }
  }

  private <T> ResponseEntity<T> executeWithToken(String path, Object body, Class<T> responseType, HttpMethod method) {
    try {
      if (token == null) login();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<Object> request = new HttpEntity<>(body, headers);

      ResponseEntity<T> response = restTemplate.exchange(baseUrl + path + "?token=" + token, method, request, responseType);
      return response;

    } catch (HttpClientErrorException | HttpServerErrorException e) {
      System.err.println("Erro na chamada para " + path);
      System.err.println("Status: " + e.getStatusCode());
      System.err.println("Resposta do servidor: " + e.getResponseBodyAsString());
      throw e;
    } catch (Exception e) {
      throw new RuntimeException("Erro na chamada para " + path, e);
    }
  }


  public void set(String key, String value) {
    Map<String, String> body = Map.of(
        "key", Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8)),
        "value", Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8))
    );
    executeWithToken("/set", body, Void.class, HttpMethod.PUT);
    System.out.println("Chave '" + key + "' salva com sucesso.");
  }


  public String get(String key) {
    try {
      String url = baseUrl + "/get?token=" + token + "&key=" +
          Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8));

      ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

      if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
        String encodedValue = (String) response.getBody().get("value");
        if (encodedValue != null) {
          String value = new String(Base64.getDecoder().decode(encodedValue), StandardCharsets.UTF_8);
          System.out.println("Chave '" + key + "' recuperada com sucesso: " + value);
          return value;
        }
      }
    } catch (HttpClientErrorException | HttpServerErrorException e) {
      System.err.println("Erro ao buscar chave '" + key + "'");
      System.err.println("Status: " + e.getStatusCode());
      System.err.println("Resposta: " + e.getResponseBodyAsString());
      throw e;
    }
    return null;
  }

  public String scan(String prefix) {
    try {
      String url = baseUrl + "/scan?token=" + token + "&prefix=" +
          Base64.getEncoder().encodeToString(prefix.getBytes(StandardCharsets.UTF_8));

      ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

      if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
        String result = mapper.writeValueAsString(response.getBody());
        System.out.println("Scan realizado com prefixo '" + prefix + "': " + result);
        return result;
      }
    } catch (HttpClientErrorException | HttpServerErrorException e) {
      System.err.println("Erro no scan com prefixo '" + prefix + "'");
      System.err.println("Status: " + e.getStatusCode());
      System.err.println("Resposta: " + e.getResponseBodyAsString());
      throw e;
    } catch (Exception e) {
      throw new RuntimeException("Erro ao processar resultado do scan", e);
    }
    return null;
  }

  public void delete(String key) {
    try {
      String url = baseUrl + "/delete?token=" + token + "&key=" +
          Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8));

      restTemplate.delete(url);
      System.out.println("Chave '" + key + "' deletada com sucesso.");
    } catch (HttpClientErrorException | HttpServerErrorException e) {
      System.err.println("Erro ao deletar chave '" + key + "'");
      System.err.println("Status: " + e.getStatusCode());
      System.err.println("Resposta: " + e.getResponseBodyAsString());
      throw e;
    }
  }

}
