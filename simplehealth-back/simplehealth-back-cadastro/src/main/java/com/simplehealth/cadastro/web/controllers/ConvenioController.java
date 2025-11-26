package com.simplehealth.cadastro.web.controllers;

import com.simplehealth.cadastro.application.dto.ConvenioDTO;
import com.simplehealth.cadastro.application.mapper.EntityDtoMapper;
import com.simplehealth.cadastro.application.service.ConvenioService;
import jakarta.validation.Valid;
import java.util.List;
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
public class ConvenioController {

  private final ConvenioService convenioService;

  public ConvenioController(ConvenioService convenioService) {
    this.convenioService = convenioService;
  }

  @PostMapping
  public ResponseEntity<ConvenioDTO> create(@Valid @RequestBody ConvenioDTO dto) {
    var created = convenioService.create(EntityDtoMapper.toEntity(dto));
    return ResponseEntity.status(201).body(EntityDtoMapper.toDto(created));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ConvenioDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(EntityDtoMapper.toDto(convenioService.findById(id)));
  }

  @GetMapping
  public ResponseEntity<List<ConvenioDTO>> list() {
    var convenios = convenioService.findAll();
    var dtos = convenios.stream().map(EntityDtoMapper::toDto).toList();
    return ResponseEntity.ok(dtos);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ConvenioDTO> update(@PathVariable Long id, @Valid @RequestBody ConvenioDTO dto) {
    return ResponseEntity.ok(EntityDtoMapper.toDto(convenioService.update(id, EntityDtoMapper.toEntity(dto))));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    convenioService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
