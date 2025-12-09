package com.simplehealth.estoque.web.controllers;

import com.simplehealth.estoque.application.service.PedidoService;
import com.simplehealth.estoque.domain.entity.Pedido;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

  private final PedidoService service;

  @PostMapping
  public ResponseEntity<Pedido> salvarPedido(@RequestBody Pedido pedido) {
    if (pedido.getIdPedido() == null) {
      pedido.setIdPedido(UUID.randomUUID());
    }
    Pedido savedPedido = service.salvar(pedido);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedPedido);
  }

  @GetMapping
  public ResponseEntity<List<Pedido>> listarTodos() {
    List<Pedido> pedidos = service.listarTodos();
    return ResponseEntity.ok(pedidos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Pedido> buscarPorId(@PathVariable UUID id) {
    try {
      Pedido pedido = service.buscarPorId(id);
      return ResponseEntity.ok(pedido);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Pedido> atualizarPedido(
      @PathVariable UUID id,
      @RequestBody Pedido pedido) {
    try {
      service.buscarPorId(id);
      pedido.setIdPedido(id);
      Pedido updatedPedido = service.salvar(pedido);
      return ResponseEntity.ok(updatedPedido);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletarPedido(@PathVariable UUID id) {
    service.deletar(id);
  }
}
