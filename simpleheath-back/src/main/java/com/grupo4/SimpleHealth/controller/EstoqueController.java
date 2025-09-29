package com.grupo4.SimpleHealth.controller;

import com.grupo4.SimpleHealth.entity.Estoque;
import com.grupo4.SimpleHealth.service.EstoqueService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estoques")
@RequiredArgsConstructor
public class EstoqueController {

  private final EstoqueService estoqueService;

  @GetMapping
  public List<Estoque> listar() {
    return estoqueService.listar();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Estoque> buscarPorId(@PathVariable Long id) {
    return estoqueService.buscarPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Estoque salvar(@RequestBody Estoque estoque) {
    return estoqueService.salvar(estoque);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Estoque> atualizar(@PathVariable Long id, @RequestBody Estoque estoque) {
    try {
      return ResponseEntity.ok(estoqueService.atualizar(id, estoque));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable Long id) {
    estoqueService.deletar(id);
    return ResponseEntity.noContent().build();
  }
}
