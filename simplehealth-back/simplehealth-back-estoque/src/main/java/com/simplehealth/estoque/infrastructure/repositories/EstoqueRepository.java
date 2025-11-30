package com.simplehealth.estoque.infrastructure.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.estoque.domain.entity.Estoque;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EstoqueRepository {

  private final RestTemplate restTemplate;
  private final ObjectMapper mapper;

  private final String baseUrl = "http://localhost:3323/api/v1/kv";
  private final String prefix = "estoque:";

  public Estoque save(Estoque estoque) {
    try {
      if (estoque.getIdEstoque() == null) {
        estoque.setIdEstoque(System.currentTimeMillis());
      }

      String key = prefix + estoque.getIdEstoque();
      String json = mapper.writeValueAsString(estoque);

      restTemplate.postForObject(baseUrl + "/set?key=" + key, json, String.class);
      return estoque;

    } catch (Exception e) {
      throw new RuntimeException("Erro ao salvar estoque", e);
    }
  }

  public List<Estoque> findAll() {
    try {
      String scanJson =
          restTemplate.getForObject(baseUrl + "/scan?prefix=" + prefix, String.class);

      if (scanJson == null) return new ArrayList<>();

      return mapper.readValue(scanJson, new TypeReference<List<Estoque>>() {});

    } catch (Exception e) {
      return new ArrayList<>();
    }
  }
}
