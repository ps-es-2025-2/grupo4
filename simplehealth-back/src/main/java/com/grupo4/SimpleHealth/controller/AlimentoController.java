package com.grupo4.SimpleHealth.controller;

import com.grupo4.SimpleHealth.entity.Alimento;
import com.grupo4.SimpleHealth.service.AlimentoService;
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
@RequestMapping("/alimentos")
@RequiredArgsConstructor
public class AlimentoController {

  private final AlimentoService alimentoService;


  @GetMapping
  public List<Alimento> listar() {
    return alimentoService.listar();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Alimento> buscarPorId(@PathVariable Long id) {
    return alimentoService.buscarPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Alimento salvar(@RequestBody Alimento alimento) {
    return alimentoService.salvar(alimento);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Alimento> atualizar(@PathVariable Long id, @RequestBody Alimento alimento) {
    try {
      return ResponseEntity.ok(alimentoService.atualizar(id, alimento));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable Long id) {
    alimentoService.deletar(id);
    return ResponseEntity.noContent().build();
  }
}
