package com.grupo4.SimpleHealth.controller;

import com.grupo4.SimpleHealth.entity.Item;
import com.grupo4.SimpleHealth.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/itens")
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @GetMapping
  public List<Item> listar() {
    return itemService.listar();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Item> buscarPorId(@PathVariable Long id) {
    return itemService.buscarPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable Long id) {
    itemService.deletar(id);
    return ResponseEntity.noContent().build();
  }
}
