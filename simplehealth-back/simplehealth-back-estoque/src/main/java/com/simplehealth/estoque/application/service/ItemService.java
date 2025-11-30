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
    repository.save(item);
    return item;
  }

  public Item buscarPorId(Long id) {
    var item = repository.findById(id);
    if (item == null) {
      throw new IllegalArgumentException("Item não encontrado.");
    }
    return item;
  }

  public List<Item> listarTodos() {
    return repository.findAll();
  }

  public void deletar(Long id) {
    repository.deleteById(id);
  }
}
