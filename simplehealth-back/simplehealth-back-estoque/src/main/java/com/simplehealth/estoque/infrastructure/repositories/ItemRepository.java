package com.simplehealth.estoque.infrastructure.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.estoque.domain.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

  private final RestTemplate restTemplate;
  private final ObjectMapper mapper;

  private final String baseUrl = "http://localhost:3323/api/v1/kv";
  private final String prefix = "item:";

  public Item save(Item item) {
    try {
      if (item.getIdItem() == null) {
        item.setIdItem(System.currentTimeMillis());
      }

      String key = prefix + item.getIdItem();
      String json = mapper.writeValueAsString(item);

      restTemplate.postForObject(baseUrl + "/set?key=" + key, json, String.class);
      return item;

    } catch (Exception e) {
      throw new RuntimeException("Erro ao salvar item", e);
    }
  }

  public Item findById(Long id) {
    try {
      String key = prefix + id;
      String json = restTemplate.getForObject(baseUrl + "/get?key=" + key, String.class);

      if (json == null) return null;

      return mapper.readValue(json, Item.class);

    } catch (Exception e) {
      return null;
    }
  }

  public List<Item> findAll() {
    try {
      String scanJson =
          restTemplate.getForObject(baseUrl + "/scan?prefix=" + prefix, String.class);

      if (scanJson == null) return new ArrayList<>();

      return mapper.readValue(scanJson, new TypeReference<List<Item>>() {});

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
