package com.simplehealth.cadastro.web.controllers;

import static com.simplehealth.cadastro.application.mapper.EntityDtoMapper.toDto;
import static com.simplehealth.cadastro.application.mapper.EntityDtoMapper.toEntity;

import com.simplehealth.cadastro.application.dto.MedicoDTO;
import com.simplehealth.cadastro.application.mapper.EntityDtoMapper;
import com.simplehealth.cadastro.application.service.MedicoService;
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
@RequestMapping("/api/cadastro/medicos")
public class MedicoController {

  private final MedicoService medicoService;

  public MedicoController(MedicoService medicoService) {
    this.medicoService = medicoService;
  }

  @PostMapping
  public ResponseEntity<MedicoDTO> create(@Valid @RequestBody MedicoDTO dto) {
    var created = medicoService.create(toEntity(dto));
    return ResponseEntity.status(201).body(toDto(created));
  }

  @GetMapping("/{id}")
  public ResponseEntity<MedicoDTO> getById(@PathVariable Long id) {
    var medico = medicoService.findById(id);
    return ResponseEntity.ok(toDto(medico));
  }

  @GetMapping
  public ResponseEntity<List<MedicoDTO>> list() {
    var medicos = medicoService.findAll();
    var dtos = medicos.stream().map(EntityDtoMapper::toDto).toList();
    return ResponseEntity.ok(dtos);
  }

  @PutMapping("/{id}")
  public ResponseEntity<MedicoDTO> update(@PathVariable Long id,
      @Valid @RequestBody MedicoDTO dto) {
    var updated = medicoService.update(id, toEntity(dto));
    return ResponseEntity.ok(toDto(updated));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    medicoService.delete(id);
    return ResponseEntity.noContent().build();
  }
}