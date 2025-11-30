package com.simplehealth.estoque.infrastructure.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.estoque.application.config.ImmudbClient;
import com.simplehealth.estoque.domain.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstoqueRepositoryImmudb {

  private final ImmudbClient client;

  public void salvarItem(Item item) throws JsonProcessingException {
    String key = "item:" + item.getIdItem();
    String value = new ObjectMapper().writeValueAsString(item);
    client.set(key, value);
  }

  public Item buscarItem(Long id) throws JsonProcessingException {
    String value = client.get("item:" + id);
    return new ObjectMapper().readValue(value, Item.class);
  }
}
