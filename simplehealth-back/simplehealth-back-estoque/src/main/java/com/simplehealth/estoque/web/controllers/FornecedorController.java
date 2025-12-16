package com.simplehealth.estoque.web.controllers;

import com.simplehealth.estoque.application.service.FornecedorService;
import com.simplehealth.estoque.domain.entity.Fornecedor;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {

  private final FornecedorService fornecedorService;

  @PostMapping
  public ResponseEntity<Fornecedor> salvarFornecedor(@RequestBody Fornecedor fornecedor) {
    if (fornecedor.getIdFornecedor() == null) {
      fornecedor.setIdFornecedor(UUID.randomUUID());
    }
    Fornecedor savedFornecedor = fornecedorService.salvar(fornecedor);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedFornecedor);
  }

  @GetMapping
  public ResponseEntity<List<Fornecedor>> listarTodos() {
    List<Fornecedor> fornecedores = fornecedorService.listarTodos();
    return ResponseEntity.ok(fornecedores);
  }

  @GetMapping("/buscar")
  public ResponseEntity<List<Fornecedor>> buscarPorNome(
      @RequestParam String nome) {
    List<Fornecedor> fornecedores = fornecedorService.buscarPorNome(nome);
    return ResponseEntity.ok(fornecedores);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Fornecedor> buscarPorId(@PathVariable UUID id) {
    try {
      Fornecedor fornecedor = fornecedorService.buscarPorId(id);
      return ResponseEntity.ok(fornecedor);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Fornecedor> atualizarFornecedor(
      @PathVariable UUID id,
      @RequestBody Fornecedor fornecedor) {
    try {
      fornecedorService.buscarPorId(id);
      fornecedor.setIdFornecedor(id);
      Fornecedor updatedFornecedor = fornecedorService.salvar(fornecedor);
      return ResponseEntity.ok(updatedFornecedor);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletarFornecedor(@PathVariable UUID id) {
    fornecedorService.deletar(id);
  }
}