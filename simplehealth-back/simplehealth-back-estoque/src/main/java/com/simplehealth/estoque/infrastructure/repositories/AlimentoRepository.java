package com.simplehealth.estoque.infrastructure.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.estoque.application.config.ImmudbClient;
import com.simplehealth.estoque.domain.entity.Alimento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AlimentoRepository {

  private final ImmudbClient immudbClient;
  private final ObjectMapper mapper;

  private final String prefix = "alimento:";

  public Alimento save(Alimento alimento) {
    if (alimento.getIdItem() == null) {
      alimento.setIdItem(System.currentTimeMillis());
    }

    try {
      String key = prefix + alimento.getIdItem();
      String value = mapper.writeValueAsString(alimento);

      immudbClient.set(key, value);

      return alimento;
    } catch (Exception e) {
      throw new RuntimeException("Erro ao salvar alimento", e);
    }
  }

  public Alimento findById(Long id) {
    try {
      String key = prefix + id;

      String json = immudbClient.get(key);

      if (json == null) {
        return null;
      }

      return mapper.readValue(json, Alimento.class);
    } catch (Exception e) {
      return null;
    }
  }
}