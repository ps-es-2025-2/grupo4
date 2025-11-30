package com.simplehealth.estoque.infrastructure.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplehealth.estoque.application.config.ImmudbClient;
import com.simplehealth.estoque.domain.entity.Fornecedor;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FornecedorRepository {

  private final ImmudbClient immudbClient;
  private final ObjectMapper mapper;

  private final String prefix = "fornecedor:";

  public Fornecedor save(Fornecedor fornecedor) {
    if (fornecedor.getIdFornecedor() == null) {
      fornecedor.setIdFornecedor(System.currentTimeMillis());
    }

    try {
      String key = prefix + fornecedor.getIdFornecedor();
      String value = mapper.writeValueAsString(fornecedor);
      immudbClient.set(key, value);
      return fornecedor;
    } catch (Exception e) {
      throw new RuntimeException("Erro ao salvar fornecedor", e);
    }
  }

  public Fornecedor findById(Long id) {
    try {
      String key = prefix + id;
      String json = immudbClient.get(key);
      if (json == null) {
        return null;
      }
      return mapper.readValue(json, Fornecedor.class);
    } catch (Exception e) {
      return null;
    }
  }

  public List<Fornecedor> findAll() {
    try {
      String jsonScan = immudbClient.scan(prefix);
      if (jsonScan == null || jsonScan.isBlank()) {
        return List.of();
      }

      Map<String, Object> scanResult = mapper.readValue(jsonScan, Map.class);
      List<Map<String, String>> items = (List<Map<String, String>>) scanResult.get("items");
      if (items == null) {
        return List.of();
      }

      List<Fornecedor> fornecedores = new ArrayList<>();
      for (Map<String, String> item : items) {
        String encodedValue = item.get("value");
        if (encodedValue != null) {
          String decodedJson = new String(Base64.getDecoder().decode(encodedValue), StandardCharsets.UTF_8);
          Fornecedor f = mapper.readValue(decodedJson, Fornecedor.class);
          fornecedores.add(f);
        }
      }
      return fornecedores;
    } catch (Exception e) {
      throw new RuntimeException("Erro ao listar fornecedores", e);
    }
  }

  public void deleteById(Long id) {
    try {
      String key = prefix + id;
      immudbClient.delete(key);
    } catch (Exception e) {
      throw new RuntimeException("Erro ao deletar fornecedor", e);
    }
  }
}
