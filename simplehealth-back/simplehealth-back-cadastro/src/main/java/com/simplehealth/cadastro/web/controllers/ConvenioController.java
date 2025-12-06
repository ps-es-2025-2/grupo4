package com.simplehealth.cadastro.web.controllers;

import com.simplehealth.cadastro.application.dto.ConvenioDTO;
import com.simplehealth.cadastro.application.usecases.GerenciarConvenioUseCase;
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
@RequestMapping("/api/cadastro/convenios")
@RequiredArgsConstructor
public class ConvenioController {

  private final GerenciarConvenioUseCase gerenciarConvenioUseCase;

  @PostMapping
  public ResponseEntity<ConvenioDTO> create(@Valid @RequestBody ConvenioDTO dto) {
    ConvenioDTO created = gerenciarConvenioUseCase.criar(dto);
    return ResponseEntity.status(201).body(created);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ConvenioDTO> getById(@PathVariable Long id) {
    ConvenioDTO convenio = gerenciarConvenioUseCase.buscarPorId(id);
    return ResponseEntity.ok(convenio);
  }

  @GetMapping
  public ResponseEntity<List<ConvenioDTO>> list() {
    List<ConvenioDTO> convenios = gerenciarConvenioUseCase.listarTodos();
    return ResponseEntity.ok(convenios);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ConvenioDTO> update(@PathVariable Long id, @Valid @RequestBody ConvenioDTO dto) {
    ConvenioDTO updated = gerenciarConvenioUseCase.atualizar(id, dto);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    gerenciarConvenioUseCase.deletar(id);
    return ResponseEntity.noContent().build();
  }
}
