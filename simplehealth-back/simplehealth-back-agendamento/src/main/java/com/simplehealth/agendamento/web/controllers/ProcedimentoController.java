package com.simplehealth.agendamento.web.controllers;

import com.simplehealth.agendamento.application.dtos.AgendarProcedimentoDTO;
import com.simplehealth.agendamento.application.dtos.AtualizarProcedimentoDTO;
import com.simplehealth.agendamento.application.dtos.CancelarAgendamentoDTO;
import com.simplehealth.agendamento.application.dtos.FinalizarServicoDTO;
import com.simplehealth.agendamento.application.dtos.IniciarServicoDTO;
import com.simplehealth.agendamento.application.dtos.ProcedimentoResponseDTO;
import com.simplehealth.agendamento.application.usecases.AgendarProcedimentoUseCase;
import com.simplehealth.agendamento.application.usecases.AtualizarProcedimentoUseCase;
import com.simplehealth.agendamento.application.usecases.BuscarProcedimentoPorIdUseCase;
import com.simplehealth.agendamento.application.usecases.CancelarProcedimentoUseCase;
import com.simplehealth.agendamento.application.usecases.DeletarProcedimentoUseCase;
import com.simplehealth.agendamento.application.usecases.FinalizarProcedimentoUseCase;
import com.simplehealth.agendamento.application.usecases.IniciarProcedimentoUseCase;
import com.simplehealth.agendamento.application.usecases.ListarProcedimentosUseCase;
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
@RequestMapping("/procedimentos")
public class ProcedimentoController {

  private final AgendarProcedimentoUseCase agendarProcedimentoUseCase;
  private final BuscarProcedimentoPorIdUseCase buscarProcedimentoPorIdUseCase;
  private final ListarProcedimentosUseCase listarProcedimentosUseCase;
  private final AtualizarProcedimentoUseCase atualizarProcedimentoUseCase;
  private final CancelarProcedimentoUseCase cancelarProcedimentoUseCase;
  private final DeletarProcedimentoUseCase deletarProcedimentoUseCase;
  private final IniciarProcedimentoUseCase iniciarProcedimentoUseCase;
  private final FinalizarProcedimentoUseCase finalizarProcedimentoUseCase;

  public ProcedimentoController(
      AgendarProcedimentoUseCase agendarProcedimentoUseCase,
      BuscarProcedimentoPorIdUseCase buscarProcedimentoPorIdUseCase,
      ListarProcedimentosUseCase listarProcedimentosUseCase,
      AtualizarProcedimentoUseCase atualizarProcedimentoUseCase,
      CancelarProcedimentoUseCase cancelarProcedimentoUseCase,
      DeletarProcedimentoUseCase deletarProcedimentoUseCase,
      IniciarProcedimentoUseCase iniciarProcedimentoUseCase,
      FinalizarProcedimentoUseCase finalizarProcedimentoUseCase) {
    this.agendarProcedimentoUseCase = agendarProcedimentoUseCase;
    this.buscarProcedimentoPorIdUseCase = buscarProcedimentoPorIdUseCase;
    this.listarProcedimentosUseCase = listarProcedimentosUseCase;
    this.atualizarProcedimentoUseCase = atualizarProcedimentoUseCase;
    this.cancelarProcedimentoUseCase = cancelarProcedimentoUseCase;
    this.deletarProcedimentoUseCase = deletarProcedimentoUseCase;
    this.iniciarProcedimentoUseCase = iniciarProcedimentoUseCase;
    this.finalizarProcedimentoUseCase = finalizarProcedimentoUseCase;
  }

  @PostMapping
  public ResponseEntity<ProcedimentoResponseDTO> agendar(@RequestBody AgendarProcedimentoDTO dto) throws Exception {
    return ResponseEntity.ok(agendarProcedimentoUseCase.execute(dto));
  }

  @GetMapping
  public ResponseEntity<List<ProcedimentoResponseDTO>> listarTodos() {
    return ResponseEntity.ok(listarProcedimentosUseCase.execute());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProcedimentoResponseDTO> buscarPorId(@PathVariable String id) {
    return ResponseEntity.ok(buscarProcedimentoPorIdUseCase.execute(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProcedimentoResponseDTO> atualizar(
      @PathVariable String id,
      @RequestBody AtualizarProcedimentoDTO dto) throws Exception {
    dto.setId(id);
    return ResponseEntity.ok(atualizarProcedimentoUseCase.execute(dto));
  }

  @PostMapping("/{id}/iniciar")
  public ResponseEntity<ProcedimentoResponseDTO> iniciarServico(
      @PathVariable String id,
      @RequestBody IniciarServicoDTO dto) {
    dto.setId(id);
    return ResponseEntity.ok(iniciarProcedimentoUseCase.execute(dto));
  }

  @PostMapping("/{id}/finalizar")
  public ResponseEntity<ProcedimentoResponseDTO> finalizarServico(
      @PathVariable String id,
      @RequestBody FinalizarServicoDTO dto) {
    dto.setId(id);
    return ResponseEntity.ok(finalizarProcedimentoUseCase.execute(dto));
  }

  @PostMapping("/cancelar")
  public ResponseEntity<ProcedimentoResponseDTO> cancelar(@RequestBody CancelarAgendamentoDTO dto) {
    return ResponseEntity.ok(cancelarProcedimentoUseCase.execute(dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable String id) {
    deletarProcedimentoUseCase.execute(id);
    return ResponseEntity.noContent().build();
  }
}