package com.simplehealth.estoque.web.controllers;

import com.simplehealth.estoque.application.service.ItemService;
import com.simplehealth.estoque.domain.entity.Item;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/itens")
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @GetMapping
  public ResponseEntity<List<Item>> listarTodos() {
    List<Item> itens = itemService.listarTodos();
    return ResponseEntity.ok(itens);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Item> buscarPorId(@PathVariable UUID id) {
    try {
      Item item = itemService.buscarPorId(id);
      return ResponseEntity.ok(item);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletarItem(@PathVariable UUID id) {
    itemService.deletar(id);
  }
}
