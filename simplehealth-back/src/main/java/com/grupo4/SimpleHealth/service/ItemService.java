package com.grupo4.SimpleHealth.service;

import com.grupo4.SimpleHealth.entity.Item;
import com.grupo4.SimpleHealth.repository.ItemRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;

  public List<Item> listar() {
    return itemRepository.findAll();
  }

  public Optional<Item> buscarPorId(Long id) {
    return itemRepository.findById(id);
  }

  public void deletar(Long id) {
    itemRepository.deleteById(id);
  }
}
