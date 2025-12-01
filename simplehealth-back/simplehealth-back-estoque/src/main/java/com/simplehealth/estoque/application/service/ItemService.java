package com.simplehealth.estoque.application.service;

import com.simplehealth.estoque.domain.entity.Item;
import com.simplehealth.estoque.infrastructure.repositories.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository repository;

  public Item salvar(Item item) {
    return repository.save(item);
  }

  public Item buscarPorId(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Item não encontrado."));
  }

  public List<Item> listarTodos() {
    return repository.findAll();
  }

  public void deletar(Long id) {
    repository.deleteById(id);
  }
}
