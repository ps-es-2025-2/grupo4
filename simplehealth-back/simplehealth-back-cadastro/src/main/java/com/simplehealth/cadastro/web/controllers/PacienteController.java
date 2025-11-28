package com.simplehealth.cadastro.web.controllers;

import com.simplehealth.cadastro.application.dto.HistoricoPacienteDTO;
import com.simplehealth.cadastro.application.dto.PacienteDTO;
import com.simplehealth.cadastro.application.service.PacienteService;
import com.simplehealth.cadastro.application.usecases.CadastrarNovoPacienteUseCase;
import com.simplehealth.cadastro.application.usecases.ConsultarHistoricoPacienteUseCase;
import com.simplehealth.cadastro.domain.entity.Paciente;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
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
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

  private final PacienteService pacienteService;
  private final CadastrarNovoPacienteUseCase cadastrarNovoPacienteUseCase;
  private final ConsultarHistoricoPacienteUseCase consultarHistoricoPacienteUseCase;


  @PostMapping
  public ResponseEntity<PacienteDTO> create(@Valid @RequestBody PacienteDTO dto) throws Exception {
    PacienteDTO created = cadastrarNovoPacienteUseCase.execute(dto);
    return ResponseEntity.status(201).body(created);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PacienteDTO> findById(@PathVariable Long id) {
    Paciente paciente = pacienteService.findById(id);
    PacienteDTO dto = new PacienteDTO(
        paciente.getId(),
        paciente.getNomeCompleto(),
        paciente.getDataNascimento(),
        paciente.getCpf(),
        paciente.getTelefone(),
        paciente.getEmail()
    );
    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<List<PacienteDTO>> findAll() {
    List<PacienteDTO> pacientes = pacienteService.findAll()
        .stream()
        .map(p -> new PacienteDTO(
            p.getId(),
            p.getNomeCompleto(),
            p.getDataNascimento(),
            p.getCpf(),
            p.getTelefone(),
            p.getEmail()
        ))
        .collect(Collectors.toList());
    return ResponseEntity.ok(pacientes);
  }

  @GetMapping("/historico/{cpf}")
  public ResponseEntity<HistoricoPacienteDTO> consultarHistorico(@PathVariable String cpf) {
    HistoricoPacienteDTO historico = consultarHistoricoPacienteUseCase.execute(cpf);
    return ResponseEntity.ok(historico);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PacienteDTO> update(@PathVariable Long id, @Valid @RequestBody PacienteDTO dto) {
    Paciente paciente = new Paciente();
    paciente.setNomeCompleto(dto.getNomeCompleto());
    paciente.setCpf(dto.getCpf());
    paciente.setDataNascimento(dto.getDataNascimento());
    paciente.setTelefone(dto.getTelefone());
    paciente.setEmail(dto.getEmail());

    Paciente updated = pacienteService.update(id, paciente);

    return ResponseEntity.ok(new PacienteDTO(
        updated.getId(),
        updated.getNomeCompleto(),
        updated.getDataNascimento(),
        updated.getCpf(),
        updated.getTelefone(),
        updated.getEmail()
    ));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    pacienteService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
