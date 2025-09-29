package com.grupo4.SimpleHealth.controller;

import com.grupo4.SimpleHealth.entity.Medicamento;
import com.grupo4.SimpleHealth.service.MedicamentoService;
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
@RequestMapping("/medicamentos")
@RequiredArgsConstructor
public class MedicamentoController {

  private final MedicamentoService medicamentoService;

  @GetMapping
  public List<Medicamento> listar() {
    return medicamentoService.listar();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Medicamento> buscarPorId(@PathVariable Long id) {
    return medicamentoService.buscarPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Medicamento salvar(@RequestBody Medicamento medicamento) {
    return medicamentoService.salvar(medicamento);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Medicamento> atualizar(@PathVariable Long id, @RequestBody Medicamento medicamento) {
    try {
      return ResponseEntity.ok(medicamentoService.atualizar(id, medicamento));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable Long id) {
    medicamentoService.deletar(id);
    return ResponseEntity.noContent().build();
  }
}
