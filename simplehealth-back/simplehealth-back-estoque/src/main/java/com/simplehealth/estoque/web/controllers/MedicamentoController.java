package com.simplehealth.estoque.web.controllers;

import com.simplehealth.estoque.application.service.MedicamentoService;
import com.simplehealth.estoque.domain.entity.Medicamento;
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
@RequestMapping("/medicamentos")
@RequiredArgsConstructor
public class MedicamentoController {

  private final MedicamentoService medicamentoService;

  @PostMapping
  public ResponseEntity<Medicamento> salvarMedicamento(@RequestBody Medicamento medicamento) {
    if (medicamento.getIdItem() == null) {
      medicamento.setIdItem(UUID.randomUUID());
    }
    Medicamento savedMedicamento = medicamentoService.salvar(medicamento);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedMedicamento);
  }

  @GetMapping
  public ResponseEntity<List<Medicamento>> listarTodos() {
    List<Medicamento> medicamentos = medicamentoService.listarTodos();
    return ResponseEntity.ok(medicamentos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Medicamento> buscarPorId(@PathVariable UUID id) {
    try {
      Medicamento medicamento = medicamentoService.buscarPorId(id);
      return ResponseEntity.ok(medicamento);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Medicamento> atualizarMedicamento(
      @PathVariable UUID id,
      @RequestBody Medicamento medicamento) {
    try {
      medicamentoService.buscarPorId(id);
      medicamento.setIdItem(id);
      Medicamento updatedMedicamento = medicamentoService.salvar(medicamento);
      return ResponseEntity.ok(updatedMedicamento);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletarMedicamento(@PathVariable UUID id) {
    medicamentoService.deletar(id);
  }
}
