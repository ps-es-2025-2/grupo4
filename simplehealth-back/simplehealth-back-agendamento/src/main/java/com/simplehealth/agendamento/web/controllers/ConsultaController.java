package com.simplehealth.agendamento.web.controllers;

import com.simplehealth.agendamento.application.dtos.AgendarConsultaDTO;
import com.simplehealth.agendamento.application.dtos.AtualizarAgendamentoDTO;
import com.simplehealth.agendamento.application.dtos.CancelarAgendamentoDTO;
import com.simplehealth.agendamento.application.dtos.ConsultaResponseDTO;
import com.simplehealth.agendamento.application.dtos.FinalizarServicoDTO;
import com.simplehealth.agendamento.application.dtos.IniciarServicoDTO;
import com.simplehealth.agendamento.application.dtos.AgendamentoDTO;
import com.simplehealth.agendamento.application.usecases.AgendarConsultaUseCase;
import com.simplehealth.agendamento.application.usecases.AtualizarAgendamentoUseCase;
import com.simplehealth.agendamento.application.usecases.BuscarAgendamentoPorIdUseCase;
import com.simplehealth.agendamento.application.usecases.BuscarAgendamentosPorPacienteUseCase;
import com.simplehealth.agendamento.application.usecases.CancelarAgendamentoUseCase;
import com.simplehealth.agendamento.application.usecases.DeletarAgendamentoUseCase;
import com.simplehealth.agendamento.application.usecases.FinalizarConsultaUseCase;
import com.simplehealth.agendamento.application.usecases.IniciarConsultaUseCase;
import com.simplehealth.agendamento.application.usecases.ListarAgendamentosUseCase;
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
@RequestMapping("/consultas")
public class ConsultaController {

  private final AgendarConsultaUseCase agendarConsultaUseCase;
  private final CancelarAgendamentoUseCase cancelarAgendamentoUseCase;
  private final BuscarAgendamentoPorIdUseCase buscarAgendamentoPorIdUseCase;
  private final ListarAgendamentosUseCase listarAgendamentosUseCase;
  private final BuscarAgendamentosPorPacienteUseCase buscarAgendamentosPorPacienteUseCase;
  private final AtualizarAgendamentoUseCase atualizarAgendamentoUseCase;
  private final DeletarAgendamentoUseCase deletarAgendamentoUseCase;
  private final IniciarConsultaUseCase iniciarConsultaUseCase;
  private final FinalizarConsultaUseCase finalizarConsultaUseCase;

  public ConsultaController(
      AgendarConsultaUseCase agendarConsultaUseCase,
      CancelarAgendamentoUseCase cancelarAgendamentoUseCase,
      BuscarAgendamentoPorIdUseCase buscarAgendamentoPorIdUseCase,
      ListarAgendamentosUseCase listarAgendamentosUseCase,
      BuscarAgendamentosPorPacienteUseCase buscarAgendamentosPorPacienteUseCase,
      AtualizarAgendamentoUseCase atualizarAgendamentoUseCase,
      DeletarAgendamentoUseCase deletarAgendamentoUseCase,
      IniciarConsultaUseCase iniciarConsultaUseCase,
      FinalizarConsultaUseCase finalizarConsultaUseCase) {
    this.agendarConsultaUseCase = agendarConsultaUseCase;
    this.cancelarAgendamentoUseCase = cancelarAgendamentoUseCase;
    this.buscarAgendamentoPorIdUseCase = buscarAgendamentoPorIdUseCase;
    this.listarAgendamentosUseCase = listarAgendamentosUseCase;
    this.buscarAgendamentosPorPacienteUseCase = buscarAgendamentosPorPacienteUseCase;
    this.atualizarAgendamentoUseCase = atualizarAgendamentoUseCase;
    this.deletarAgendamentoUseCase = deletarAgendamentoUseCase;
    this.iniciarConsultaUseCase = iniciarConsultaUseCase;
    this.finalizarConsultaUseCase = finalizarConsultaUseCase;
  }

  @PostMapping
  public ResponseEntity<ConsultaResponseDTO> agendar(@RequestBody AgendarConsultaDTO dto) throws Exception {
    return ResponseEntity.ok(agendarConsultaUseCase.execute(dto));
  }

  @GetMapping
  public ResponseEntity<List<AgendamentoDTO>> listarTodos() {
    return ResponseEntity.ok(listarAgendamentosUseCase.execute());
  }

  @GetMapping("/{id}")
  public ResponseEntity<AgendamentoDTO> buscarPorId(@PathVariable String id) {
    return ResponseEntity.ok(buscarAgendamentoPorIdUseCase.execute(id));
  }

  @GetMapping("/paciente/{cpf}")
  public ResponseEntity<List<AgendamentoDTO>> buscarPorPaciente(@PathVariable String cpf) {
    return ResponseEntity.ok(buscarAgendamentosPorPacienteUseCase.execute(cpf));
  }

  @PutMapping("/{id}")
  public ResponseEntity<AgendamentoDTO> atualizar(
      @PathVariable String id,
      @RequestBody AtualizarAgendamentoDTO dto) throws Exception {
    dto.setId(id);
    return ResponseEntity.ok(atualizarAgendamentoUseCase.execute(dto));
  }

  @PostMapping("/{id}/iniciar")
  public ResponseEntity<ConsultaResponseDTO> iniciarServico(
      @PathVariable String id,
      @RequestBody IniciarServicoDTO dto) {
    dto.setId(id);
    return ResponseEntity.ok(iniciarConsultaUseCase.execute(dto));
  }

  @PostMapping("/{id}/finalizar")
  public ResponseEntity<ConsultaResponseDTO> finalizarServico(
      @PathVariable String id,
      @RequestBody FinalizarServicoDTO dto) {
    dto.setId(id);
    return ResponseEntity.ok(finalizarConsultaUseCase.execute(dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable String id) {
    deletarAgendamentoUseCase.execute(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/cancelar")
  public ResponseEntity<ConsultaResponseDTO> cancelar(@RequestBody CancelarAgendamentoDTO dto) throws Exception {
    return ResponseEntity.ok(cancelarAgendamentoUseCase.execute(dto));
  }
}