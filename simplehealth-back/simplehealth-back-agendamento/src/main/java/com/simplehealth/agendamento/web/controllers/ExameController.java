package com.simplehealth.agendamento.web.controllers;

import com.simplehealth.agendamento.application.dtos.AgendarExameDTO;
import com.simplehealth.agendamento.application.dtos.AtualizarExameDTO;
import com.simplehealth.agendamento.application.dtos.CancelarAgendamentoDTO;
import com.simplehealth.agendamento.application.dtos.ExameResponseDTO;
import com.simplehealth.agendamento.application.dtos.FinalizarServicoDTO;
import com.simplehealth.agendamento.application.dtos.IniciarServicoDTO;
import com.simplehealth.agendamento.application.usecases.AgendarExameUseCase;
import com.simplehealth.agendamento.application.usecases.AtualizarExameUseCase;
import com.simplehealth.agendamento.application.usecases.BuscarExamePorIdUseCase;
import com.simplehealth.agendamento.application.usecases.CancelarExameUseCase;
import com.simplehealth.agendamento.application.usecases.DeletarExameUseCase;
import com.simplehealth.agendamento.application.usecases.FinalizarExameUseCase;
import com.simplehealth.agendamento.application.usecases.IniciarExameUseCase;
import com.simplehealth.agendamento.application.usecases.ListarExamesUseCase;
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
@RequestMapping("/exames")
public class ExameController {

  private final AgendarExameUseCase agendarExameUseCase;
  private final BuscarExamePorIdUseCase buscarExamePorIdUseCase;
  private final ListarExamesUseCase listarExamesUseCase;
  private final AtualizarExameUseCase atualizarExameUseCase;
  private final CancelarExameUseCase cancelarExameUseCase;
  private final DeletarExameUseCase deletarExameUseCase;
  private final IniciarExameUseCase iniciarExameUseCase;
  private final FinalizarExameUseCase finalizarExameUseCase;

  public ExameController(
      AgendarExameUseCase agendarExameUseCase,
      BuscarExamePorIdUseCase buscarExamePorIdUseCase,
      ListarExamesUseCase listarExamesUseCase,
      AtualizarExameUseCase atualizarExameUseCase,
      CancelarExameUseCase cancelarExameUseCase,
      DeletarExameUseCase deletarExameUseCase,
      IniciarExameUseCase iniciarExameUseCase,
      FinalizarExameUseCase finalizarExameUseCase) {
    this.agendarExameUseCase = agendarExameUseCase;
    this.buscarExamePorIdUseCase = buscarExamePorIdUseCase;
    this.listarExamesUseCase = listarExamesUseCase;
    this.atualizarExameUseCase = atualizarExameUseCase;
    this.cancelarExameUseCase = cancelarExameUseCase;
    this.deletarExameUseCase = deletarExameUseCase;
    this.iniciarExameUseCase = iniciarExameUseCase;
    this.finalizarExameUseCase = finalizarExameUseCase;
  }

  @PostMapping
  public ResponseEntity<ExameResponseDTO> agendar(@RequestBody AgendarExameDTO dto) throws Exception {
    return ResponseEntity.ok(agendarExameUseCase.execute(dto));
  }

  @GetMapping
  public ResponseEntity<List<ExameResponseDTO>> listarTodos() {
    return ResponseEntity.ok(listarExamesUseCase.execute());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ExameResponseDTO> buscarPorId(@PathVariable String id) {
    return ResponseEntity.ok(buscarExamePorIdUseCase.execute(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ExameResponseDTO> atualizar(
      @PathVariable String id,
      @RequestBody AtualizarExameDTO dto) throws Exception {
    dto.setId(id);
    return ResponseEntity.ok(atualizarExameUseCase.execute(dto));
  }

  @PostMapping("/{id}/iniciar")
  public ResponseEntity<ExameResponseDTO> iniciarServico(
      @PathVariable String id,
      @RequestBody IniciarServicoDTO dto) {
    dto.setId(id);
    return ResponseEntity.ok(iniciarExameUseCase.execute(dto));
  }

  @PostMapping("/{id}/finalizar")
  public ResponseEntity<ExameResponseDTO> finalizarServico(
      @PathVariable String id,
      @RequestBody FinalizarServicoDTO dto) {
    dto.setId(id);
    return ResponseEntity.ok(finalizarExameUseCase.execute(dto));
  }

  @PostMapping("/cancelar")
  public ResponseEntity<ExameResponseDTO> cancelar(@RequestBody CancelarAgendamentoDTO dto) {
    return ResponseEntity.ok(cancelarExameUseCase.execute(dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable String id) {
    deletarExameUseCase.execute(id);
    return ResponseEntity.noContent().build();
  }
}