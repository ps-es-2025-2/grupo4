package com.simplehealth.cadastro.web.controllers;

import com.simplehealth.cadastro.application.dto.HistoricoPacienteDTO;
import com.simplehealth.cadastro.application.dto.PacienteDTO;
import com.simplehealth.cadastro.application.usecases.AtualizarPacienteUseCase;
import com.simplehealth.cadastro.application.usecases.BuscarPacienteUseCase;
import com.simplehealth.cadastro.application.usecases.CadastrarNovoPacienteUseCase;
import com.simplehealth.cadastro.application.usecases.ConsultarHistoricoPacienteUseCase;
import com.simplehealth.cadastro.application.usecases.DeletarPacienteUseCase;
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
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

  private final CadastrarNovoPacienteUseCase cadastrarNovoPacienteUseCase;
  private final BuscarPacienteUseCase buscarPacienteUseCase;
  private final AtualizarPacienteUseCase atualizarPacienteUseCase;
  private final DeletarPacienteUseCase deletarPacienteUseCase;
  private final ConsultarHistoricoPacienteUseCase consultarHistoricoPacienteUseCase;

  @PostMapping
  public ResponseEntity<PacienteDTO> create(@Valid @RequestBody PacienteDTO dto) throws Exception {
    PacienteDTO created = cadastrarNovoPacienteUseCase.execute(dto);
    return ResponseEntity.status(201).body(created);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PacienteDTO> findById(@PathVariable Long id) {
    PacienteDTO dto = buscarPacienteUseCase.buscarPorId(id);
    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<List<PacienteDTO>> findAll() {
    List<PacienteDTO> pacientes = buscarPacienteUseCase.listarTodos();
    return ResponseEntity.ok(pacientes);
  }

  @GetMapping("/historico/{cpf}")
  public ResponseEntity<HistoricoPacienteDTO> consultarHistorico(@PathVariable String cpf) {
    HistoricoPacienteDTO historico = consultarHistoricoPacienteUseCase.execute(cpf);
    return ResponseEntity.ok(historico);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PacienteDTO> update(@PathVariable Long id, @Valid @RequestBody PacienteDTO dto)
      throws Exception {
    PacienteDTO updated = atualizarPacienteUseCase.execute(id, dto);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    deletarPacienteUseCase.execute(id);
    return ResponseEntity.noContent().build();
  }
}
