package com.simplehealth.cadastro.web.controllers;

import com.simplehealth.cadastro.application.dto.UsuarioDTO;
import com.simplehealth.cadastro.application.usecases.GerenciarUsuarioUseCase;
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
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

  private final GerenciarUsuarioUseCase gerenciarUsuarioUseCase;

  @PostMapping
  public ResponseEntity<UsuarioDTO> create(@Valid @RequestBody UsuarioDTO dto) {
    UsuarioDTO created = gerenciarUsuarioUseCase.criar(dto);
    return ResponseEntity.status(201).body(created);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UsuarioDTO> getById(@PathVariable Long id) {
    UsuarioDTO usuario = gerenciarUsuarioUseCase.buscarPorId(id);
    return ResponseEntity.ok(usuario);
  }

  @GetMapping
  public ResponseEntity<List<UsuarioDTO>> list() {
    List<UsuarioDTO> usuarios = gerenciarUsuarioUseCase.listarTodos();
    return ResponseEntity.ok(usuarios);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UsuarioDTO> update(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
    UsuarioDTO updated = gerenciarUsuarioUseCase.atualizar(id, dto);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    gerenciarUsuarioUseCase.deletar(id);
    return ResponseEntity.noContent().build();
  }
}
