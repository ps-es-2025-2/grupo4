package com.grupo4.SimpleHealth.controller;

import com.grupo4.SimpleHealth.entity.Hospitalar;
import com.grupo4.SimpleHealth.service.HospitalarService;
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
@RequestMapping("/hospitalares")
@RequiredArgsConstructor
public class HospitalarController {

  private final HospitalarService hospitalarService;

  @GetMapping
  public List<Hospitalar> listar() {
    return hospitalarService.listar();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Hospitalar> buscarPorId(@PathVariable Long id) {
    return hospitalarService.buscarPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Hospitalar salvar(@RequestBody Hospitalar hospitalar) {
    return hospitalarService.salvar(hospitalar);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Hospitalar> atualizar(@PathVariable Long id, @RequestBody Hospitalar hospitalar) {
    try {
      return ResponseEntity.ok(hospitalarService.atualizar(id, hospitalar));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable Long id) {
    hospitalarService.deletar(id);
    return ResponseEntity.noContent().build();
  }
}
