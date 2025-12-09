package com.simplehealth.estoque.web.controllers;

import com.simplehealth.estoque.application.service.AlimentoService;
import com.simplehealth.estoque.domain.entity.Alimento;
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
@RequestMapping("/alimentos")
@RequiredArgsConstructor
public class AlimentoController {

  private final AlimentoService alimentoService;

  @PostMapping
  public ResponseEntity<Alimento> salvarAlimento(@RequestBody Alimento alimento) {
    if (alimento.getIdItem() == null) {
      alimento.setIdItem(UUID.randomUUID());
    }
    Alimento savedAlimento = alimentoService.salvar(alimento);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedAlimento);
  }

  @GetMapping
  public ResponseEntity<List<Alimento>> listarTodos() {
    List<Alimento> alimentos = alimentoService.listarTodos();
    return ResponseEntity.ok(alimentos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Alimento> buscarPorId(@PathVariable UUID id) {
    try {
      Alimento alimento = alimentoService.buscarPorId(id);
      return ResponseEntity.ok(alimento);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Alimento> atualizarAlimento(
      @PathVariable UUID id,
      @RequestBody Alimento alimento) {
    try {
      alimentoService.buscarPorId(id);
      alimento.setIdItem(id);
      Alimento updatedAlimento = alimentoService.salvar(alimento);
      return ResponseEntity.ok(updatedAlimento);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletarAlimento(@PathVariable UUID id) {
    alimentoService.deletar(id);
  }
}
