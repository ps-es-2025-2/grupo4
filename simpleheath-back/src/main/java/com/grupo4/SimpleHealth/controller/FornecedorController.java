package com.grupo4.SimpleHealth.controller;

import com.grupo4.SimpleHealth.entity.Fornecedor;
import com.grupo4.SimpleHealth.service.FornecedorService;
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
@RequestMapping("/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {

  private final FornecedorService fornecedorService;

  @GetMapping
  public List<Fornecedor> listar() {
    return fornecedorService.listar();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Fornecedor> buscarPorId(@PathVariable Long id) {
    return fornecedorService.buscarPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Fornecedor salvar(@RequestBody Fornecedor fornecedor) {
    return fornecedorService.salvar(fornecedor);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Fornecedor> atualizar(@PathVariable Long id, @RequestBody Fornecedor fornecedor) {
    try {
      return ResponseEntity.ok(fornecedorService.atualizar(id, fornecedor));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable Long id) {
    fornecedorService.deletar(id);
    return ResponseEntity.noContent().build();
  }
}
