package com.simplehealth.estoque.web.controllers;

import com.simplehealth.estoque.application.service.HospitalarService;
import com.simplehealth.estoque.domain.entity.Hospitalar;
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
@RequestMapping("/hospitalares")
@RequiredArgsConstructor
public class HospitalarController {

  private final HospitalarService hospitalarService;

  @PostMapping
  public ResponseEntity<Hospitalar> salvarHospitalar(@RequestBody Hospitalar hospitalar) {
    if (hospitalar.getIdItem() == null) {
      hospitalar.setIdItem(UUID.randomUUID());
    }
    Hospitalar savedHospitalar = hospitalarService.salvar(hospitalar);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedHospitalar);
  }

  @GetMapping
  public ResponseEntity<List<Hospitalar>> listarTodos() {
    List<Hospitalar> hospitalares = hospitalarService.listarTodos();
    return ResponseEntity.ok(hospitalares);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Hospitalar> buscarPorId(@PathVariable UUID id) {
    try {
      Hospitalar hospitalar = hospitalarService.buscarPorId(id);
      return ResponseEntity.ok(hospitalar);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Hospitalar> atualizarHospitalar(
      @PathVariable UUID id,
      @RequestBody Hospitalar hospitalar) {
    try {
      hospitalarService.buscarPorId(id);
      hospitalar.setIdItem(id);
      Hospitalar updatedHospitalar = hospitalarService.salvar(hospitalar);
      return ResponseEntity.ok(updatedHospitalar);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletarHospitalar(@PathVariable UUID id) {
    hospitalarService.deletar(id);
  }
}
