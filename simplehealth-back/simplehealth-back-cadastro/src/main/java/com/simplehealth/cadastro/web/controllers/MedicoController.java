package com.simplehealth.cadastro.web.controllers;

import com.simplehealth.cadastro.application.dto.MedicoDTO;
import com.simplehealth.cadastro.application.usecases.GerenciarMedicoUseCase;
import jakarta.validation.Valid;
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
@RequestMapping("/api/cadastro/medicos")
@RequiredArgsConstructor
public class MedicoController {

  private final GerenciarMedicoUseCase gerenciarMedicoUseCase;

  @PostMapping
  public ResponseEntity<MedicoDTO> create(@Valid @RequestBody MedicoDTO dto) {
    MedicoDTO created = gerenciarMedicoUseCase.criar(dto);
    return ResponseEntity.status(201).body(created);
  }

  @GetMapping("/{id}")
  public ResponseEntity<MedicoDTO> getById(@PathVariable Long id) {
    MedicoDTO medico = gerenciarMedicoUseCase.buscarPorId(id);
    return ResponseEntity.ok(medico);
  }

  @GetMapping
  public ResponseEntity<List<MedicoDTO>> list() {
    List<MedicoDTO> medicos = gerenciarMedicoUseCase.listarTodos();
    return ResponseEntity.ok(medicos);
  }

  @PutMapping("/{id}")
  public ResponseEntity<MedicoDTO> update(@PathVariable Long id, @Valid @RequestBody MedicoDTO dto) {
    MedicoDTO updated = gerenciarMedicoUseCase.atualizar(id, dto);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    gerenciarMedicoUseCase.deletar(id);
    return ResponseEntity.noContent().build();
  }
}
