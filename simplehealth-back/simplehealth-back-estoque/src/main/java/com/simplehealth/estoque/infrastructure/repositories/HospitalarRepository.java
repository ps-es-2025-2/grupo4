package com.simplehealth.estoque.infrastructure.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.estoque.domain.entity.Hospitalar;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
@RequiredArgsConstructor
public class HospitalarRepository {

  private final RestTemplate restTemplate;
  private final ObjectMapper mapper;

  private final String baseUrl = "http://localhost:3323/api/v1/kv";
  private final String prefix = "hospitalar:";

  public Hospitalar save(Hospitalar h) {
    if (h.getIdItem() == null) {
      h.setIdItem(System.currentTimeMillis());
    }

    try {
      String key = prefix + h.getIdItem();
      String value = mapper.writeValueAsString(h);
      restTemplate.postForObject(baseUrl + "/set?key=" + key, value, String.class);
      return h;
    } catch (Exception e) {
      throw new RuntimeException("Erro ao salvar item hospitalar", e);
    }
  }

  public Hospitalar findById(Long id) {
    try {
      String key = prefix + id;
      String json = restTemplate.getForObject(baseUrl + "/get?key=" + key, String.class);
      if (json == null) {
        return null;
      }
      return mapper.readValue(json, Hospitalar.class);
    } catch (Exception e) {
      return null;
    }
  }

  public List<Hospitalar> findAll() {
    try {
      String scanJson = restTemplate.getForObject(baseUrl + "/scan?prefix=" + prefix, String.class);
      if (scanJson == null) {
        return new ArrayList<>();
      }
      return mapper.readValue(scanJson, new TypeReference<List<Hospitalar>>() {});
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  public void deleteById(Long id) {
    try {
      String key = prefix + id;
      restTemplate.delete(baseUrl + "/delete?key=" + key);
    } catch (Exception ignored) {}
  }
}
