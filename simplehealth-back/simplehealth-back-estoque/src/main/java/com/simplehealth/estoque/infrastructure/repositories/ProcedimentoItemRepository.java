package com.simplehealth.estoque.infrastructure.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.estoque.domain.entity.ProcedimentoItem;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
@RequiredArgsConstructor
public class ProcedimentoItemRepository {

  private final RestTemplate restTemplate;
  private final ObjectMapper mapper;

  private final String baseUrl = "http://localhost:3323/api/v1/kv";
  private final String prefix = "procedimentoItem:";

  public ProcedimentoItem save(ProcedimentoItem p) {
    if (p.getId() == null) {
      p.setId(System.currentTimeMillis());
    }

    try {
      String key = prefix + p.getId();
      String value = mapper.writeValueAsString(p);
      restTemplate.postForObject(baseUrl + "/set?key=" + key, value, String.class);
      return p;
    } catch (Exception e) {
      throw new RuntimeException("Erro ao salvar ProcedimentoItem", e);
    }
  }

  public ProcedimentoItem findById(Long id) {
    try {
      String key = prefix + id;
      String json = restTemplate.getForObject(baseUrl + "/get?key=" + key, String.class);
      if (json == null) {
        return null;
      }
      return mapper.readValue(json, ProcedimentoItem.class);
    } catch (Exception e) {
      return null;
    }
  }

  public List<ProcedimentoItem> findAll() {
    try {
      String scanJson = restTemplate.getForObject(baseUrl + "/scan?prefix=" + prefix, String.class);
      if (scanJson == null) {
        return new ArrayList<>();
      }
      return mapper.readValue(scanJson, new TypeReference<List<ProcedimentoItem>>() {});
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
