package com.grupo4.SimpleHealth.controller;

import com.grupo4.SimpleHealth.entity.Pedido;
import com.grupo4.SimpleHealth.service.PedidoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

  @Autowired
  private final PedidoService pedidoService;

  @GetMapping
  public List<Pedido> listar() {
    return pedidoService.listar();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
    return pedidoService.buscarPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Pedido salvar(@RequestBody Pedido pedido) {
    return pedidoService.salvar(pedido);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Pedido> atualizar(@PathVariable Long id, @RequestBody Pedido pedido) {
    try {
      return ResponseEntity.ok(pedidoService.atualizar(id, pedido));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable Long id) {
    pedidoService.deletar(id);
    return ResponseEntity.noContent().build();
  }
}
